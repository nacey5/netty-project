package com.hzh.netty.message;

import lombok.Data;
import lombok.ToString;

/**
 * @author DAHUANG
 * @date 2022/5/21
 */
@Data
@ToString(callSuper = true)
public abstract class AbstractResponseMessage extends Message {
    private boolean success;
    private String reason;

    public AbstractResponseMessage() {
    }

    public AbstractResponseMessage(boolean success, String reason) {
        this.success = success;
        this.reason = reason;
    }
}
