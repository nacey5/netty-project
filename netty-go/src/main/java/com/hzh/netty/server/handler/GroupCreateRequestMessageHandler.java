package com.hzh.netty.server.handler;

import com.hzh.netty.message.GroupCreateRequestMessage;
import com.hzh.netty.message.GroupCreateResponseMessage;
import com.hzh.netty.server.session.Group;
import com.hzh.netty.server.session.GroupSession;
import com.hzh.netty.server.session.GroupSessionFactory;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

import java.util.List;
import java.util.Set;

/**
 * @author DAHUANG
 * @date 2022/5/21
 */
@ChannelHandler.Sharable
public class GroupCreateRequestMessageHandler extends SimpleChannelInboundHandler<GroupCreateRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupCreateRequestMessage msg) throws Exception {
        String groupName = msg.getGroupName();
        Set<String> members = msg.getMembers();
        GroupSession groupSession = GroupSessionFactory.getGroupSession();
        Group group = groupSession.createGroup(groupName, members);
        if (group==null) {
            //发送拉群消息
            List<Channel> channels = groupSession.getMembersChannel(groupName);
            for (Channel channel : channels) {
                channel.writeAndFlush(new GroupCreateResponseMessage(true,"您已被拉入"+groupName));
            }
            //发送成功消息
            ctx.writeAndFlush(new GroupCreateResponseMessage(true,groupName+"创建成功"));
        }else {
            ctx.writeAndFlush(new GroupCreateResponseMessage(false,groupName+"已经存在"));
        }

    }
}
