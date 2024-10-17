package com.magq.mq.client;

import com.magq.mq.config.Config;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

public class User {

    private String name;

    // 用户构造器
    public User(String name) {
        this.name = name;
    }

    // 订阅平台
    public void subscribe(String platform) {
        try (Socket socket = new Socket("localhost", Config.SERVICE_PORT)) {
            OutputStream outputStream = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(outputStream, true);
            // 获取消息的逻辑
            System.out.println(name + " is getting messages."); // Debug print
            // 发送订阅指令
            writer.println(Config.SUBSCRIBE);
            writer.println(this.name);
            writer.println(platform);

            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 获取消息
    public void getMessages() {
        try (Socket socket = new Socket("localhost", Config.SERVICE_PORT)) {
            OutputStream outputStream = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(outputStream, true);

            // 发送获取消息指令
            writer.println(Config.GET);
            writer.println(this.name);

            // 接收返回的消息
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String response;
            while ((response = reader.readLine()) != null) {
                System.out.println("Received message: " + response);
            }

            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
