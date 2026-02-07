package com.mycompany.app.common;

import lombok.Getter;

import java.util.HashSet;
import java.util.Set;

public abstract class Service implements RemoteActor, SocketListener {
    @Getter
    protected final String name;
    @Getter
    protected final int port;
    protected final ServerWrapper incoming;
    protected final Set<RemoteInfo> remoteSet = new HashSet<>();

    protected Service(final String name, final int port) {
        incoming = new ServerWrapper(port, this);
        this.port = port;
        this.name = name;
        new Thread(incoming).start();
    }
}
