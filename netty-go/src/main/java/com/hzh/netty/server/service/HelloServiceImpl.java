package com.hzh.netty.server.service;

/**
 * @author DAHUANG
 * @date 2022/5/21
 */
public class HelloServiceImpl implements HelloService {
    @Override
    public String sayHello(String msg) {
        return "你好, " + msg;
    }
}