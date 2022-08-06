package com.hzh.netty.server.handler;

import com.hzh.netty.message.ChatRequestMessage;
import com.hzh.netty.message.ChatResponseMessage;
import com.hzh.netty.server.session.SessionFactory;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author DAHUANG
 * @date 2022/5/21
 */
@ChannelHandler.Sharable
public class ChatRequestMessageHandler extends SimpleChannelInboundHandler<ChatRequestMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, ChatRequestMessage msg) throws Exception {
        String to = msg.getTo();
        Channel channel = SessionFactory.getSession().getChannel(to);
        //在线
        if (channel!=null){
            channel.writeAndFlush(new ChatResponseMessage(msg.getFrom(),msg.getContent()));
        }
        //不在线
        else {
            ctx.writeAndFlush(new ChatResponseMessage(false,"对方用户不存在或者不在线"));
        }
    }
}
