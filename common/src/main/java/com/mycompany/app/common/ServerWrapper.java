package com.mycompany.app.common;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public abstract class ServerWrapper implements Runnable {
    private final int queueSize = 100;
    private final ServerSocket incomingSoket;
    private final int port;

    public ServerWrapper(final int port) {
        this.port = port;
        try {
            incomingSoket = new ServerSocket(port, queueSize);
        } catch (IOException e) {
            throw new PortBindException(port);
        }
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Socket socket = accept();
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String info = in.readLine();
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                out.println(handleRequest(info));
                socket.close();
            } catch (Exception e) {
                handleException(e);
            }
        }
    }

    protected abstract Object handleRequest(String info);

    protected abstract void handleException(Exception e);

    protected Socket accept() throws IOException {
        return incomingSoket.accept();
    }

    static class PortBindException extends RuntimeException {
        public PortBindException(final int port) {
            super("Failed to bind on " + port + ": port is already in use or protected");
        }
    }
}
