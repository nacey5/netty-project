package com.hzh.netty.message;

import lombok.Data;
import lombok.ToString;

/**
 * @author DAHUANG
 * @date 2022/5/21
 */
@Data
@ToString(callSuper = true)
public class GroupQuitRequestMessage extends Message {
    private String groupName;

    private String username;

    public GroupQuitRequestMessage(String username, String groupName) {
        this.groupName = groupName;
        this.username = username;
    }

    @Override
    public int getMessageType() {
        return GroupQuitRequestMessage;
    }
}
