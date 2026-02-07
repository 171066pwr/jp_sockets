package com.mycompany.app.common;

import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

public abstract class Service implements RemoteActor, SocketListener {
    @Getter
    protected final String name;
    @Getter
    protected final int port;
    @Getter
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
