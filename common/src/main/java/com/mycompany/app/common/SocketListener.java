package com.mycompany.app.common;

public interface SocketListener {
    Response handleRequest(Request request);

    void handleException(Exception e);
}
