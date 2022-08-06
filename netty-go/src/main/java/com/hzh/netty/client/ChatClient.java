package com.hzh.netty.client;

import com.hzh.netty.message.*;
import com.hzh.netty.protocol.MessageCodecSharable;
import com.hzh.netty.protocol.ProcotolFrameDecoder;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author DAHUANG
 * @date 2022/5/21
 */
@Slf4j
public class ChatClient {
    public static void main(String[] args) {
        NioEventLoopGroup group = new NioEventLoopGroup();
        LoggingHandler LOGGING_HANDLER = new LoggingHandler(LogLevel.DEBUG);
        MessageCodecSharable MESSAGE_CODEC = new MessageCodecSharable();
        CountDownLatch WAIT_FOR_LOGIN=new CountDownLatch(1);
        AtomicBoolean LOGIN=new AtomicBoolean(false);
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.group(group);
            bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS,1000);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new ProcotolFrameDecoder());
                    ch.pipeline().addLast(MESSAGE_CODEC);
                    ch.pipeline().addLast(LOGGING_HANDLER);


                    //暂时取消心跳发送包
                    /*//3s内如果向服务器写数据，会触发一个IdleState事件#WRITE_IDLE事件
                    ch.pipeline().addLast(new IdleStateHandler(0,3,0));
                    //ChannelDuplexHandler 可以同时作为入站处理器和出站处理器
                    ch.pipeline().addLast(new ChannelDuplexHandler(){
                        //用来触发特殊事件
                        @Override
                        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
                            IdleStateEvent event= (IdleStateEvent) evt;
                            //触发了读空闲事件
                            if (event.state()== IdleState.WRITER_IDLE) {
//                                log.debug("3s 没有写数据，发送心跳包");
                                ctx.writeAndFlush(new PingMessage());
                            }
                        }
                    });*/

                    //为客户端添加激活处理器
                    ch.pipeline().addLast("client-handler",new ChannelInboundHandlerAdapter(){

                        //接收相应信息
                        @Override
                        public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
                            log.debug("msg:{}",msg);
                            if ((msg instanceof LoginResponseMessage)) {
                                LoginResponseMessage responseMessage= (LoginResponseMessage) msg;
                                if (responseMessage.isSuccess()) {
                                    //如果登陆成功
                                    LOGIN.set(true);
                                }
                                //唤醒system-in线程
                                WAIT_FOR_LOGIN.countDown();
                            }
                        }

                        //在连接建立后触发active事件
                        @Override
                        public void channelActive(ChannelHandlerContext ctx) throws Exception {
                            //创立一个新的线程接收用户输入,负责向服务器发送各种消息
                            new Thread(()->{
                                Scanner scanner=new Scanner(System.in);
                                System.out.println("请输入用户名:");
                                String userName=scanner.nextLine();
                                System.out.println("请输入密码:");
                                String pwd=scanner.nextLine();
                                //构造消息对象
                                LoginRequestMessage message = new LoginRequestMessage(userName, pwd);
                                //发送消息
                                ctx.writeAndFlush(message);

                                System.out.println("等待后续操作。。。");
                                try {
                                    WAIT_FOR_LOGIN.await();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                if (!LOGIN.get()) {
                                    ctx.channel().close();
                                    return;
                                }
                                while (true){
                                    System.out.println("===================================");
                                    System.out.println("send [username] [content]");
                                    System.out.println("gsend [group name] [content]");
                                    System.out.println("gcreate [group name] [m1 m2 m3]");
                                    System.out.println("gmembers [group name]");
                                    System.out.println("gjoin [group name]");
                                    System.out.println("gquit [group name]");
                                    System.out.println("quit");
                                    System.out.println("===================================");
                                    //先阻塞住，不然会一直循环
                                    String command = scanner.nextLine();
                                    String[] s=command.split(" ");
                                    switch (s[0]){
                                        case "send":
                                            ctx.writeAndFlush(new ChatRequestMessage(userName,s[1],s[2]));
                                            break;
                                        case "gsend":
                                            ctx.writeAndFlush(new GroupChatRequestMessage(userName,s[1],s[2]));
                                            break;
                                        case "gcreate":
                                            Set<String> set=new HashSet<>(Arrays.asList(s[2].split(",")));
                                            set.add(userName);
                                            ctx.writeAndFlush(new GroupCreateRequestMessage(s[1],set));
                                            break;
                                        case "gmembers":
                                            ctx.writeAndFlush(new GroupMembersRequestMessage(s[1]));
                                            break;
                                        case "gjoin":
                                            ctx.writeAndFlush(new GroupJoinRequestMessage(userName,s[1]));
                                            break;
                                        case "gquit":
                                            ctx.writeAndFlush(new GroupQuitRequestMessage(userName,s[1]));
                                            break;
                                        case "quit":
                                            ctx.channel().close();
                                            return;
                                    }
                                }
                            },"system-in").start();
                        }
                    });
                }
            });
            Channel channel = bootstrap.connect("localhost", 8080).sync().channel();
            channel.closeFuture().sync();
        } catch (Exception e) {
            log.error("client error", e);
        } finally {
            group.shutdownGracefully();
        }
    }
}
