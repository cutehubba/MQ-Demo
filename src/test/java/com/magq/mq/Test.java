package com.magq.mq;

import com.magq.mq.client.Platform;
import com.magq.mq.client.User;
import com.magq.mq.server.BrokerServer;

public class Test {

    public static void main(String[] args) {
        // 启动 Broker 服务器
        new Thread(() -> {
            new BrokerServer().start();
        }).start();

        // 创建平台和用户
        Platform platform1 = new Platform("Platform1");
        Platform platform2 = new Platform("Platform2");

        User user1 = new User("User1");
        User user2 = new User("User2");

        // 用户订阅平台
        user1.subscribe("Platform1");
        user2.subscribe("Platform2");

        // 发布消息
        platform1.produce("Hello from Platform1!");
        platform2.produce("Hello from Platform2!");

        // 用户获取消息
        user1.getMessages();  // User1 should receive messages from Platform1
        user2.getMessages();  // User2 should receive messages from Platform2
    }
}
