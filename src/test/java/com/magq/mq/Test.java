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

        // 创建多个平台
        Platform platform1 = new Platform("平台1");
        Platform platform2 = new Platform("平台2");
        Platform platform3 = new Platform("平台3");

        // 创建多个用户
        User user1 = new User("用户1");
        User user2 = new User("用户2");
        User user3 = new User("用户3");
        User user4 = new User("用户4");  // 用户4

        // 用户订阅平台
        user1.subscribe("平台1");
        user2.subscribe("平台2");
        user3.subscribe("平台1");  // 用户3 也订阅 平台1
        user3.subscribe("平台3");  // 用户3 还订阅 平台3
        user4.subscribe("平台4");  // 用户4 试图订阅一个不存在的平台

        // 发布消息
        platform1.produce("来自平台1的消息1！");
        platform1.produce("来自平台1的消息2！");
        platform2.produce("来自平台2的消息1！");
        platform3.produce("来自平台3的消息1！");
        platform1.produce("来自平台1的消息3！");

        // 用户获取消息
        System.out.println("用户1的消息：");
        user1.getMessages();  // 用户1 应该收到 平台1 的消息

        System.out.println("用户2的消息：");
        user2.getMessages();  // 用户2 应该收到 平台2 的消息

        System.out.println("用户3的消息：");
        user3.getMessages();  // 用户3 应该收到 平台1 和 平台3 的消息

        System.out.println("用户4的消息（未订阅任何有效平台）：");
        user4.getMessages();  // 用户4 应该没有消息

        // 模拟用户4尝试发布到不存在的平台
        System.out.println("尝试向未订阅的平台发送消息：");
        String nonExistentPlatformName = "平台4";  // 假设这个平台不存在
        if (!user4.getSubscribedPlatforms().contains(nonExistentPlatformName)) {
            System.out.println("错误：用户4未订阅 " + nonExistentPlatformName + "，无法发送消息。");
        }

        // 检查用户的订阅状态
        System.out.println("用户3的订阅状态：");
        user3.checkSubscriptions();  // 输出 用户3 订阅的平台
    }
}
