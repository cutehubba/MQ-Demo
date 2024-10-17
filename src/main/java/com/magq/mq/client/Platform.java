package com.magq.mq.client;

import com.magq.mq.config.Config;

import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class Platform {

    private String name;

    // 平台构造器
    public Platform(String name) {
        this.name = name;
    }

    // 发布消息
    public void produce(String message) {
        try (Socket socket = new Socket("localhost", Config.SERVICE_PORT)) {
            OutputStream outputStream = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(outputStream, true);
            System.out.println("Producing message: " + message); // Debug print
            // 连接到 Broker 发送消息的逻辑
            // 发送发布消息指令和消息内容
            writer.println(Config.PUBLISH);
            writer.println(this.name);
            writer.println(message);

            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
