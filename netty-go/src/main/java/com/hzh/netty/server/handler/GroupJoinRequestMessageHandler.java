package com.hzh.netty.server.handler;

import com.hzh.netty.message.GroupJoinRequestMessage;
import com.hzh.netty.message.GroupJoinResponseMessage;
import com.hzh.netty.server.session.Group;
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
public class GroupJoinRequestMessageHandler extends SimpleChannelInboundHandler<GroupJoinRequestMessage> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupJoinRequestMessage msg) throws Exception {
        String groupName = msg.getGroupName();
        String username = msg.getUsername();
        GroupSession groupSession = GroupSessionFactory.getGroupSession();
        Group group = groupSession.joinMember(groupName, username);
        if (group!=null){//加群成功
            List<Channel> membersChannel = groupSession.getMembersChannel(groupName);
            for (Channel channel : membersChannel) {
                channel.writeAndFlush(new GroupJoinResponseMessage(true,username+"已加入群聊"+groupName));
            }
            ctx.writeAndFlush(new GroupJoinResponseMessage(true,"加入群聊"+groupName+"成功"));
        }else{//加群失败，群不存在
            ctx.writeAndFlush(new GroupJoinResponseMessage(false,groupName+"不存在"));
        }
    }
}
