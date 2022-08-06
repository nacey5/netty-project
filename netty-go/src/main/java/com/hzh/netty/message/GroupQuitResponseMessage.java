package com.hzh.netty.message;

import lombok.Data;
import lombok.ToString;

/**
 * @author DAHUANG
 * @date 2022/5/21
 */
@Data
@ToString(callSuper = true)
public class GroupQuitResponseMessage extends AbstractResponseMessage {
    public GroupQuitResponseMessage(boolean success, String reason) {
        super(success, reason);
    }

    @Override
    public int getMessageType() {
        return GroupQuitResponseMessage;
    }
}
