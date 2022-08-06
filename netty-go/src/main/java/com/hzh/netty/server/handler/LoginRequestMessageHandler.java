package com.hzh.netty.server.handler;

import com.hzh.netty.message.LoginRequestMessage;
import com.hzh.netty.message.LoginResponseMessage;
import com.hzh.netty.server.service.UserServiceFactory;
import com.hzh.netty.server.session.SessionFactory;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;

/**
 * @author DAHUANG
 * @date 2022/5/21
 */
@ChannelHandler.Sharable
public class LoginRequestMessageHandler extends SimpleChannelInboundHandler<LoginRequestMessage> {
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, LoginRequestMessage msg) throws Exception {
        String userName = msg.getUsername();
        String password = msg.getPassword();
        boolean login = UserServiceFactory.getUserService().login(userName, password);
        LoginResponseMessage message = null;
        if (login) {//登陆成功
            SessionFactory.getSession().bind(ctx.channel(), userName);
            message = new LoginResponseMessage(true, "登陆成功");
        } else {//登陆失败
            message = new LoginResponseMessage(false, "netty聊天登陆失败，用户名或密码不正确");
        }
        ctx.writeAndFlush(message);
    }
}
