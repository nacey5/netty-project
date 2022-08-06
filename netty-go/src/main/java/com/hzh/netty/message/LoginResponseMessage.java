package com.hzh.netty.message;

import lombok.Data;
import lombok.ToString;

/**
 * @author DAHUANG
 * @date 2022/5/21
 */
@Data
@ToString(callSuper = true)
public class LoginResponseMessage extends AbstractResponseMessage {

    public LoginResponseMessage(boolean success, String reason) {
        super(success, reason);
    }

    @Override
    public int getMessageType() {
        return LoginResponseMessage;
    }
}
