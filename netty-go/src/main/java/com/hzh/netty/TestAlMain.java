package com.hzh.netty;

import com.hzh.netty.config.Config;
import com.hzh.netty.message.LoginRequestMessage;
import com.hzh.netty.message.Message;
import com.hzh.netty.protocol.MessageCodecSharable;
import com.hzh.netty.protocol.Serializer;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.handler.logging.LoggingHandler;

/**
 * @author DAHUANG
 * @date 2022/7/25
 */
public class TestAlMain {
    public static void main(String[] args) {
        MessageCodecSharable CODEC = new MessageCodecSharable();
        LoggingHandler LOGGING = new LoggingHandler();
        EmbeddedChannel channel = new EmbeddedChannel(LOGGING, CODEC, LOGGING);
        LoginRequestMessage message = new LoginRequestMessage("zhangsan", "!23");
//        channel.writeOutbound(message);
        ByteBuf buf = messageToBytes(message);
        channel.writeInbound(buf);
    }

    public static ByteBuf messageToBytes(Message msg){
        int algorithm = Config.getSerializerAlgorithm().ordinal();
        ByteBuf out = ByteBufAllocator.DEFAULT.buffer();
        out.writeBytes(new byte[]{1,2,3,4});
        out.writeByte(1);
        out.writeByte(algorithm);
        out.writeByte(msg.getMessageType());
        out.writeInt(msg.getSequenceId());
        out.writeByte(0xff);
        byte[] bytes = Serializer.Algorithm.values()[algorithm].serialize(msg);
        out.writeInt(bytes.length);
        out.writeBytes(bytes);
        return out;
    }
}
