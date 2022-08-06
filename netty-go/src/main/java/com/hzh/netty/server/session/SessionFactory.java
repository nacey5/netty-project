package com.hzh.netty.server.session;

/**
 * @author DAHUANG
 * @date 2022/5/21
 */
public abstract class SessionFactory {

    private static Session session = new SessionMemoryImpl();

    public static Session getSession() {
        return session;
    }
}
