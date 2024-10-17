package com.magq.mq.server;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Broker {

    // 存储订阅信息
    private Map<String, List<String>> subscriptions = new HashMap<>();

    // 存储消息
    private Map<String, List<String>> messages = new HashMap<>();

    // 订阅平台
    public void subscribe(String user, String platform) {
        subscriptions.putIfAbsent(user, new LinkedList<>());
        subscriptions.get(user).add(platform);
    }

    // 发布消息
    public void publish(String platform, String message) {
        messages.putIfAbsent(platform, new LinkedList<>());
        messages.get(platform).add(message);
    }

    // 获取消息
    public List<String> getMessages(String user) {
        List<String> result = new LinkedList<>();
        if (subscriptions.containsKey(user)) {
            for (String platform : subscriptions.get(user)) {
                if (messages.containsKey(platform)) {
                    result.addAll(messages.get(platform));
                }
            }
        }
        return result;
    }
}
