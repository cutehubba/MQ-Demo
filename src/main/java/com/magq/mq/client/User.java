package com.magq.mq.client;

import com.magq.mq.config.Config;
import lombok.Getter;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class User {

    private String name;
    @Getter
    private List<String> subscribedPlatforms = new ArrayList<>();

    // 用户构造器
    public User(String name) {
        this.name = name;
    }

    // 订阅平台
    public void subscribe(String platform) {
        try (Socket socket = new Socket("localhost", Config.SERVICE_PORT)) {
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            // 发送订阅指令
            writer.println(Config.SUBSCRIBE);
            writer.println(this.name);
            writer.println(platform);

            // 记录订阅的平台
            subscribedPlatforms.add(platform);
            System.out.println(name + " 已成功订阅平台: " + platform);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 获取消息
    public void getMessages() {
        try (Socket socket = new Socket("localhost", Config.SERVICE_PORT)) {
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true);
            // 发送获取消息指令
            writer.println(Config.GET);
            writer.println(this.name);

            // 接收返回的消息
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            String response;
            System.out.println(name + " 正在接收消息:");
            while ((response = reader.readLine()) != null) {
                System.out.println("收到消息: " + response);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 检查订阅的平台
    public void checkSubscriptions() {
        if (subscribedPlatforms.isEmpty()) {
            System.out.println(name + " 没有订阅任何平台。");
        } else {
            System.out.println(name + " 的订阅平台： " + subscribedPlatforms);
        }
    }
}
