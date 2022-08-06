package com.hzh.netty.message;

import lombok.Data;
import lombok.ToString;

import java.util.Set;

/**
 * @author DAHUANG
 * @date 2022/5/21
 */
@Data
@ToString(callSuper = true)
public class GroupMembersResponseMessage extends Message {

    private Set<String> members;

    public GroupMembersResponseMessage(Set<String> members) {
        this.members = members;
    }

    @Override
    public int getMessageType() {
        return GroupMembersResponseMessage;
    }
}
