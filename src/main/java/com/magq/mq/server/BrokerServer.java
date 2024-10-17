package com.magq.mq.server;

import com.magq.mq.config.Config;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.atomic.AtomicInteger;

public class BrokerServer {

    private Broker broker = new Broker();
    private AtomicInteger messagesPublished = new AtomicInteger(0); // 记录已发布消息的计数
    private long startTime = System.currentTimeMillis(); // 记录服务器启动时间

    public void start() {
        try (ServerSocket serverSocket = new ServerSocket(Config.SERVICE_PORT)) {
            System.out.println("Broker server started on port " + Config.SERVICE_PORT);
            while (true) {
                // 等待客户端连接
                Socket clientSocket = serverSocket.accept();
                // 为每个连接创建一个新的线程
                new Thread(() -> handleClient(clientSocket)).start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleClient(Socket clientSocket) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true)) {

            String command = reader.readLine();  // 读取操作指令
            String user = reader.readLine();     // 读取用户名称

            if (command.equals(Config.SUBSCRIBE)) {
                String platform = reader.readLine();  // 读取订阅的平台
                broker.subscribe(user, platform);
                writer.println("Subscribed to platform: " + platform);
            } else if (command.equals(Config.PUBLISH)) {
                String platform = reader.readLine();  // 读取发布的平台
                String message = reader.readLine();    // 读取消息内容
                broker.publish(platform, message);
                messagesPublished.incrementAndGet(); // 更新已发布消息计数
                writer.println("Message published to platform: " + platform);
            } else if (command.equals(Config.GET)) {
                // 获取用户的消息
                for (String msg : broker.getMessages(user)) {
                    writer.println(msg);
                }
            }

            // 每次获取消息时都打印当前的吞吐率
            printThroughput();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void printThroughput() {
        long currentTime = System.currentTimeMillis();
        long elapsedTime = currentTime - startTime; // 计算已运行时间（毫秒）
        if (elapsedTime > 0) {
            double throughput = (messagesPublished.get() / (elapsedTime / 1000.0)); // 计算吞吐率
            System.out.printf("Current throughput: %.2f messages/second\n", throughput);
        }
    }

    public static void main(String[] args) {
        new BrokerServer().start();  // 启动 Broker 服务器
    }
}
