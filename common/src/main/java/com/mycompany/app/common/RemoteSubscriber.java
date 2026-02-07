package com.mycompany.app.common;

public interface RemoteSubscriber {
    default Response subscribeToRemote(String host, int port) {
        return subscribeToRemote(new RemoteInfo(host, port, "", 0));
    }

    default Response subscribeWithDelay(RemoteInfo remote, long delay) {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return subscribeToRemote(remote);
    }

    Response subscribeToRemote(RemoteInfo remoteInfo);
}
