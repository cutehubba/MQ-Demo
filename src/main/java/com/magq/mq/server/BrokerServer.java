package com.magq.mq.server;

import com.magq.mq.config.Config;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class BrokerServer {

    private Broker broker = new Broker();

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
                writer.println("Message published to platform: " + platform);
            } else if (command.equals(Config.GET)) {
                // 获取用户的消息
                for (String msg : broker.getMessages(user)) {
                    writer.println(msg);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new BrokerServer().start();  // 启动 Broker 服务器
    }
}
