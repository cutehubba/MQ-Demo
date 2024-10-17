package com.magq.mq.server;

public class LoadMq {

    public static void main(String[] args) {
        BrokerServer brokerServer = new BrokerServer();
        brokerServer.start();
    }
}
