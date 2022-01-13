package com.hxx.nettydemo.handles;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.util.Date;

/*
1.A ChannelHandler has two life cycle listener methods: handlerAdded() and handlerRemoved(). You can perform an arbitrary (de)initialization task as long as it does not block for a long time.
2.First, all received data should be cumulated into buf.
3.And then, the handler must check if buf has enough data, 4 bytes in this example, and proceed to the actual business logic. Otherwise,
Netty will call the channelRead() method again when more data arrives, and eventually all 4 bytes will be cumulated.
*/
/**
 * @Author: huoxuxu
 * @Description:
 * @Date: 2022-01-11 17:46:13
 **/
public class TimeClientWithStreamHandler extends ChannelInboundHandlerAdapter {
    private ByteBuf buf;

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        buf = ctx.alloc().buffer(4); // (1)
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) {
        buf.release(); // (1)
        buf = null;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        ByteBuf m = (ByteBuf) msg;
        buf.writeBytes(m); // (2)
        m.release();

        if (buf.readableBytes() >= 4) { // (3)
            long currentTimeMillis = (buf.readUnsignedInt() - 2208988800L) * 1000L;
            System.out.println(new Date(currentTimeMillis));
            ctx.close();
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}