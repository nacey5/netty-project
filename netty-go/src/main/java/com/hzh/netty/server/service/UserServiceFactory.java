package com.hzh.netty.server.service;


/**
 * @author DAHUANG
 * @date 2022/5/21
 */
public abstract class UserServiceFactory {

    private static UserService userService = new UserServiceMemoryImpl();

    public static UserService getUserService() {
        return userService;
    }
}
