package com.hzh.netty.config;
import com.hzh.netty.protocol.Serializer;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 配置类，进行配置的读取【以便扩展】
 * @author DAHUANG
 * @date 2022/5/21
 */
public abstract class Config {
    static Properties properties;
    static {
        try (InputStream in = Config.class.getResourceAsStream("/application.properties")) {
            properties = new Properties();
            properties.load(in);
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
    }
    public static int getServerPort() {
        String value = properties.getProperty("server.port");
        if(value == null) {
            return 8080;
        } else {
            return Integer.parseInt(value);
        }
    }
    public static Serializer.Algorithm getSerializerAlgorithm() {
        String value = properties.getProperty("serializer.algorithm");
        if(value == null|| "Java".equals(value)) {
            return Serializer.Algorithm.Java;
        } else {
            return Serializer.Algorithm.valueOf(value);
        }
    }
}