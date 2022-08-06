package com.hzh.netty.server.handler;

import com.hzh.netty.message.PingMessage;
import com.hzh.netty.message.PongMessage;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author DAHUANG
 * @date 2022/5/21
 */
@ChannelHandler.Sharable
public class HeartbeatDetectionHandler extends SimpleChannelInboundHandler<PingMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, PingMessage msg) throws Exception {
        ctx.writeAndFlush(new PongMessage());
    }
}
