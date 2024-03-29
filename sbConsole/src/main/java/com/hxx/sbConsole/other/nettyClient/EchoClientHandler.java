package com.hxx.sbConsole.other.nettyClient;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-01-12 15:28:53
 **/
@ChannelHandler.Sharable
public class EchoClientHandler extends SimpleChannelInboundHandler<ByteBuf> {

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        // 当一个连接建立时被调用，发送一条消息
        ctx.writeAndFlush(Unpooled.copiedBuffer("Netty rocks!", CharsetUtil.UTF_8));
    }

//    @Override
//    protected void messageReceived(ChannelHandlerContext ctx, ByteBuf msg) {
//        // 记录已接收消息的转储
//        System.out.println("Client received: " + msg.toString(CharsetUtil.UTF_8));
//    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        // 发生异常时，记录错误并关闭 Channel
        cause.printStackTrace();
        ctx.close();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, ByteBuf msg) throws Exception {
        // 记录已接收消息的转储
        System.out.println("Client received: " + msg.toString(CharsetUtil.UTF_8));
    }
}
