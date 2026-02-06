package com.mycompany.app.common;

import lombok.extern.log4j.Log4j2;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.*;

@Log4j2
public class OutgoingProxy extends ServerWrapper {
    private final List<SocketProxyListener> listeners = new ArrayList<>();
    //remotes that WE query about data
    private final Set<RemoteInfo> remoteInfo = new HashSet<>();
    private final int remoteTimeout;

    public OutgoingProxy(int port, int timeout) {
        super(port);
        this.remoteTimeout = timeout;
    }

    @Override
    protected Object handleRequest(String info) {
        String[] tokens = MessageSplitter.split(info);
        try {
            RemoteInfo remote = new RemoteInfo(tokens[0], Integer.parseInt(tokens[1]), tokens[2], Long.parseLong(tokens[3]));
            if (remoteInfo.add(remote)) {
                notifyListeners(remote);
                return ResponseCode.YES;
            }
            return ResponseCode.NO;
        } catch (Exception e) {
            return ResponseCode.ERROR;
        }
    }

    @Override
    protected void handleException(Exception e) {
        log.error("Failed to register remote: " + e.getMessage());
    }

    public void addListener(SocketProxyListener listener) {
        listeners.add(listener);
    }

    private void notifyListeners(RemoteInfo remote) {
        listeners.forEach(l -> l.acceptSource(remote));
    }

    public Map<RemoteInfo, String> updateRemotes(int method, String data) {
        Map<RemoteInfo, String> result = new HashMap<>();
        for (RemoteInfo info : remoteInfo) {
            String response = updateRemote(method, data, info);
            result.put(info, response);
        }
        return result;
    }

    public String updateRemote(int method, String data, RemoteInfo info) {
        String response;
        try(Socket socket = new Socket(info.host(), info.port())) {
            socket.setSoTimeout(remoteTimeout);
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out.println(new RequestData(method, data));
            response = in.readLine();
        } catch (IOException e) {
            log.error(e);
            response = MessageSplitter.combine(ResponseCode.ERROR, e.getMessage());
        }
        return response;
    }
}
