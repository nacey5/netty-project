package com.hzh.netty.server.handler;

import com.hzh.netty.message.GroupQuitRequestMessage;
import com.hzh.netty.message.GroupQuitResponseMessage;
import com.hzh.netty.server.session.Group;
import com.hzh.netty.server.session.GroupSessionFactory;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author DAHUANG
 * @date 2022/5/21
 */
@ChannelHandler.Sharable
public class GroupQuitRequestMessageHandler extends SimpleChannelInboundHandler<GroupQuitRequestMessage> {


    @Override
    protected void channelRead0(ChannelHandlerContext ctx, GroupQuitRequestMessage msg) throws Exception {
        String groupName = msg.getGroupName();
        String username = msg.getUsername();
        Group group = GroupSessionFactory.getGroupSession().removeMember(groupName, username);
        if (group==null) {
            ctx.writeAndFlush(new GroupQuitResponseMessage(false,"群已不存在"));
        }else {
            ctx.writeAndFlush(new GroupQuitResponseMessage(true,"退出群成功"));
        }
    }
}
