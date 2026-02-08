package com.mycompany.app.common;

import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

@Getter
public abstract class Service implements RemoteActor, SocketListener {
    protected final String name;
    protected final int port;
    protected final long delay;
    protected final ServerWrapper incoming;
    protected final Set<RemoteInfo> remoteSet = new HashSet<>();

    protected Service(int port, String name, long delay) {
        incoming = new ServerWrapper(port, this);
        this.port = port;
        this.name = name;
        this.delay = delay;
        new Thread(incoming).start();
    }
}
