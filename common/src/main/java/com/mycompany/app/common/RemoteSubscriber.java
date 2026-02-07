package com.mycompany.app.common;

public interface RemoteSubscriber {
    Response subscribeToRemote(String host, int port);
}
