package com.hxx.nettydemo.serviceDemo.case1;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-01-12 15:46:52
 **/
public class NettyClientDemo {

    public static void main(String[] args) {
        // 创建事件循环线程组(客户端的线程组只有一个)
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            // Netty 客户端启动对象
            Bootstrap b = new Bootstrap();
            // 设置启动参数
            b.group(group)
                    // 设置通道类型
                    .channel(NioSocketChannel.class)
                    // 设置启动执行器(负责启动事件的业务执行,ClientInitializer 为自定义的类)
                    .handler(new ClientInitializer());

            // 连接服务器端并同步通道
            Channel ch = b.connect("127.0.0.1", 8007)
                    .sync()
                    .channel();

            // 发送消息
            ChannelFuture lastWriteFuture = null;
            // 给服务器端发送 10 条消息
            for (int i = 0; i < 10; i++) {
                // 发送给服务器消息
                lastWriteFuture = ch.writeAndFlush("Hi,Java.\n");
            }
            // 在关闭通道之前，同步刷新所有的消息
            if (lastWriteFuture != null) {
                lastWriteFuture.sync();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 释放资源
            group.shutdownGracefully();
        }
    }

    /**
     * 客户端通道初始化类
     */
    static class ClientInitializer extends ChannelInitializer<SocketChannel> {
        // 字符串编码器和解码器
        private static final StringDecoder DECODER = new StringDecoder();
        private static final StringEncoder ENCODER = new StringEncoder();
        // 客户端连接成功之后业务处理
        private static final ClientHandler CLIENT_HANDLER = new ClientHandler();

        /**
         * 初始化客户端通道
         */
        @Override
        public void initChannel(SocketChannel ch) {
            ChannelPipeline pipeline = ch.pipeline();
            // 设置结尾分隔符
            pipeline.addLast(new DelimiterBasedFrameDecoder(1024, Delimiters.lineDelimiter()));
            // 设置(字符串)编码器和解码器
            pipeline.addLast(DECODER);
            pipeline.addLast(ENCODER);
            // 客户端连接成功之后的业务处理
            pipeline.addLast(CLIENT_HANDLER);
        }
    }

    /**
     * 客户端连接成功之后的业务处理
     */
    static class ClientHandler extends SimpleChannelInboundHandler<String> {

        /**
         * 读取到服务器端的消息
         */
        @Override
        protected void channelRead0(ChannelHandlerContext ctx, String msg) {
            System.err.println("接到服务器的消息:" + msg);
        }

        /**
         * 异常处理，打印异常并关闭通道
         */
        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
            cause.printStackTrace();
            ctx.close();
        }
    }
}