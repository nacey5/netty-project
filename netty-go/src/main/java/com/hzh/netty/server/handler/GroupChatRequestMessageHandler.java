package com.hzh.netty.server.handler;

import com.hzh.netty.message.GroupChatRequestMessage;
import com.hzh.netty.message.GroupChatResponseMessage;
import com.hzh.netty.server.session.GroupSession;
import com.hzh.netty.server.session.GroupSessionFactory;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.List;

/**
 * @author DAHUANG
 * @date 2022/5/21
 */
@ChannelHandler.Sharable
public class GroupChatRequestMessageHandler extends SimpleChannelInboundHandler<GroupChatRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupChatRequestMessage msg) throws Exception {
        String from = msg.getFrom();
        String content = msg.getContent();
        String groupName = msg.getGroupName();
        GroupSession groupSession = GroupSessionFactory.getGroupSession();
        List<Channel> membersChannel = groupSession.getMembersChannel(groupName);
        for (Channel channel : membersChannel) {
            channel.writeAndFlush(new GroupChatResponseMessage(from,content));
        }
    }
}
