package com.magq.mq.server;

public class LoadMq {

    public static void main(String[] args) {
        // 启动 Broker 服务器
        Thread brokerThread = new Thread(() -> {
            BrokerServer brokerServer = new BrokerServer();
            brokerServer.start();
        });

        brokerThread.start();

        // 等待 Broker 服务器启动完成
        try {
            Thread.sleep(1000); // 等待 1 秒钟确保 Broker 启动
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Broker server has started.");
    }
}
