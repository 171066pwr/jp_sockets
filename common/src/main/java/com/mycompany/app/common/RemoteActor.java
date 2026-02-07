package com.mycompany.app.common;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;

public interface RemoteActor {
    String getName();
    int getPort();
    long getDelay();

    default String getHost() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            return "localhost";
        }
    }

    default RemoteInfo getRemoteInfo() {
        return new RemoteInfo(getHost(), getPort(), getName(), getDelay());
    }

    default Response updateRemote(String host, int port, Enum method, String data) {
        return updateRemote(host, port, method.ordinal(), data);
    }

    default Response updateRemote(String host, int port, int code, String data) {
        Response response;
        try(Socket socket = new Socket(host, port);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream())))
        {
            out.println(new Request(code, getName(), data));
            response = new Response(in.readLine());
        } catch (IOException e) {
            response = new Response(ResponseCode.ERROR, e.getMessage());
        }
        return response;
    }
}
