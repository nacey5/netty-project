package com.hzh.netty.message;

/**
 * @author DAHUANG
 * @date 2022/5/21
 */
public class PongMessage extends Message {
    @Override
    public int getMessageType() {
        return PongMessage;
    }
}
