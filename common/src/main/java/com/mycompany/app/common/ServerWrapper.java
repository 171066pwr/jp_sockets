package com.mycompany.app.common;

import lombok.Getter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ServerWrapper implements Runnable {
    private final int queueSize = 100;
    private final ServerSocket incomingSocket;
    private final SocketListener socketListener;
    @Getter
    private final int port;

    public ServerWrapper(int port, SocketListener listener) {
        this.port = port;
        this.socketListener = listener;
        try {
            incomingSocket = new ServerSocket(port, queueSize);
        } catch (IOException e) {
            throw new PortBindException(port);
        }
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try(Socket socket = accept();
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream(), true))
            {
                String info = in.readLine();
                out.println(socketListener.handleRequest(new Request(info)));
            } catch (Exception e) {
                socketListener.handleException(e);
            }
        }
    }

    protected Socket accept() throws IOException {
        return incomingSocket.accept();
    }

    static class PortBindException extends RuntimeException {
        public PortBindException(final int port) {
            super("Failed to bind on " + port + ": port is already in use or protected");
        }
    }
}
