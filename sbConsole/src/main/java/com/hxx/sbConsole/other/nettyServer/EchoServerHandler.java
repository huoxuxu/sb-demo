package com.hxx.sbConsole.other.nettyServer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.CharsetUtil;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2023-01-12 15:25:34
 **/
@ChannelHandler.Sharable // 标识一个 ChannelHandler 可以被多个 Channel 安全的共享
public class EchoServerHandler extends ChannelHandlerAdapter {


//    @Override
//    public void channelRead(ChannelHandlerContext ctx, Object msg) {
//        ByteBuf in = (ByteBuf) msg;
//        System.out.println("Server receiver: " + in.toString(CharsetUtil.UTF_8));
//        // 将接收到的消息写给发送者
//        ctx.write(in);
//    }
//
//    @Override
//    public void channelReadComplete(ChannelHandlerContext ctx) {
//        // 将剩余的消息全部冲刷到远程结点，并关闭 CHannel
//        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)
//                .addListener(ChannelFutureListener.CLOSE);
//    }

//    @Override
//    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
//        cause.printStackTrace();
//        ctx.close();
//    }
}
