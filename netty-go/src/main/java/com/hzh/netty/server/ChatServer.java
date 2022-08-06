package com.hzh.netty.server;

import com.hzh.netty.protocol.MessageCodecSharable;
import com.hzh.netty.protocol.ProcotolFrameDecoder;
import com.hzh.netty.server.handler.*;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

/**
 * @author DAHUANG
 * @date 2022/5/21
 */
@Slf4j
public class ChatServer {
    public static void main(String[] args) {
        NioEventLoopGroup boss = new NioEventLoopGroup();
        NioEventLoopGroup worker = new NioEventLoopGroup();
        LoggingHandler LOGGING_HANDLER = new LoggingHandler(LogLevel.DEBUG);
        MessageCodecSharable MESSAGE_CODEC = new MessageCodecSharable();
        LoginRequestMessageHandler LOGIN_HANDLER = new LoginRequestMessageHandler();
        ChatRequestMessageHandler CHAT_HANDLER=new ChatRequestMessageHandler();
        GroupCreateRequestMessageHandler GROUP_CREATE_HANDLER = new GroupCreateRequestMessageHandler();
        GroupJoinRequestMessageHandler GROUP_JOIN_HANDLER = new GroupJoinRequestMessageHandler();
        GroupQuitRequestMessageHandler GROUP_QUIT_HANDLER = new GroupQuitRequestMessageHandler();
        GroupMembersRequestMessageHandler GROUP_MEMBERS_HANDLER = new GroupMembersRequestMessageHandler();
        GroupChatRequestMessageHandler GROUP_CHAT_HANDLER = new GroupChatRequestMessageHandler();
        QuitHandler QUIT_HANDLER = new QuitHandler();
//        HeartbeatDetectionHandler HEART_HANDLER=new HeartbeatDetectionHandler();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.channel(NioServerSocketChannel.class);
            serverBootstrap.group(boss, worker);
            serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new ProcotolFrameDecoder());
                    ch.pipeline().addLast(LOGGING_HANDLER);
                    ch.pipeline().addLast(MESSAGE_CODEC);
//                    ch.pipeline().addLast(HEART_HANDLER);
                    //心跳检测暂时关掉
                   /* //5s内如果没有收到channel的数据，会触发一个IdleState事件#READER_IDLE事件
                    ch.pipeline().addLast(new IdleStateHandler(5,0,0));
                    //ChannelDuplexHandler 可以同时作为入站处理器和出站处理器
                    ch.pipeline().addLast(new ChannelDuplexHandler(){
                        //用来触发特殊事件
                        @Override
                        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
                            IdleStateEvent event= (IdleStateEvent) evt;
                            //触发了读空闲事件
                            if (event.state()== IdleState.READER_IDLE) {
                                log.debug("已经5s没有收到数据了");
                            }
                        }
                    });*/
                    ch.pipeline().addLast(LOGIN_HANDLER);
                    ch.pipeline().addLast(CHAT_HANDLER);
                    ch.pipeline().addLast(GROUP_CREATE_HANDLER);
                    ch.pipeline().addLast(GROUP_CHAT_HANDLER);
                    ch.pipeline().addLast(GROUP_JOIN_HANDLER);
                    ch.pipeline().addLast(GROUP_QUIT_HANDLER);
                    ch.pipeline().addLast(GROUP_MEMBERS_HANDLER);
                    ch.pipeline().addLast(QUIT_HANDLER);
                }
            });
            Channel channel = serverBootstrap.bind(8080).sync().channel();
            channel.closeFuture().sync();
        } catch (InterruptedException e) {
            log.error("server error", e);
        } finally {
            boss.shutdownGracefully();
            worker.shutdownGracefully();
        }
    }

}
