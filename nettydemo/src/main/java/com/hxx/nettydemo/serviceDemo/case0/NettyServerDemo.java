package com.hxx.nettydemo.serviceDemo.case0;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-01-12 15:45:02
 **/
public class NettyServerDemo {
    // 定义服务器的端口号
    static final int PORT = 8007;

    public static void main(String[] args) {
        // 创建一个线程组,用来负责接收客户端连接
        EventLoopGroup bossGroup = new NioEventLoopGroup();
        // 创建另一个线程组,用来负责 I/O 的读写
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            // 创建一个 Server 实例(可理解为 Netty 的入门类)
            ServerBootstrap b = new ServerBootstrap();
            // 将两个线程池设置到 Server 实例
            b.group(bossGroup, workerGroup)
                    // 设置 Netty 通道的类型为 NioServerSocket(非阻塞 I/O Socket 服务器)
                    .channel(NioServerSocketChannel.class)
                    // 设置建立连接之后的执行器(ServerInitializer 是我创建的一个自定义类)
                    .childHandler(new ServerInitializer());
            // 绑定端口并且进行同步
            ChannelFuture future = b.bind(PORT)
                    .sync();
            // 对关闭通道进行监听
            future.channel()
                    .closeFuture()
                    .sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            // 资源关闭
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    /**
     * 服务端通道初始化
     */
    static class ServerInitializer extends ChannelInitializer<SocketChannel> {
        // 字符串编码器和解码器
        private static final StringDecoder DECODER = new StringDecoder();
        private static final StringEncoder ENCODER = new StringEncoder();
        // 服务器端连接之后的执行器(自定义的类)
        private static final ServerHandler SERVER_HANDLER = new ServerHandler();

        /**
         * 初始化通道的具体执行方法
         */
        @Override
        public void initChannel(SocketChannel ch) {
            // 通道 Channel 设置
            ChannelPipeline pipeline = ch.pipeline();
            // 设置(字符串)编码器和解码器
            pipeline.addLast(DECODER);
            pipeline.addLast(ENCODER);
            // 服务器端连接之后的执行器,接收到消息之后的业务处理
            pipeline.addLast(SERVER_HANDLER);
        }
    }

    /**
     * 服务器端接收到消息之后的业务处理类
     */
    static class ServerHandler extends SimpleChannelInboundHandler<String> {

        /**
         * 读取到客户端的消息
         */
        @Override
        public void channelRead0(ChannelHandlerContext ctx, String request) {
            if (!request.isEmpty()) {
                System.out.println("接到客户端的消息:" + request);
            }
        }

        /**
         * 数据读取完毕
         */
        @Override
        public void channelReadComplete(ChannelHandlerContext ctx) {
            ctx.flush();
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
