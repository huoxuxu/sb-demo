package com.hxx.nettydemo.handles;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-01-11 17:25:37
 **/
public class DiscardServerHandler extends ChannelInboundHandlerAdapter {// (1)
    // Writing a Discard Server
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) { // (2)
        // Discard the received data silently.
        ((ByteBuf) msg).release(); // (3)
    }

    // Looking into the Received Data
//    @Override
//    public void channelRead(ChannelHandlerContext ctx, Object msg) {
//        ByteBuf in = (ByteBuf) msg;
//        try {
//            while (in.isReadable()) { // (1)
//                System.out.print((char) in.readByte());
//                System.out.flush();
//            }
//        } finally {
//            ReferenceCountUtil.release(msg); // (2)
//        }
//    }

    // Writing an Echo Server
//    @Override
//    public void channelRead(ChannelHandlerContext ctx, Object msg) {
//        ctx.write(msg); // (1)
//        ctx.flush(); // (2)
//    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { // (4)
        // Close the connection when an exception is raised.
        cause.printStackTrace();
        ctx.close();
    }

}
