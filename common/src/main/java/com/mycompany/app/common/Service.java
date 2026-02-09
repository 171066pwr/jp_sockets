package com.mycompany.app.common;

import lombok.Getter;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
public abstract class Service implements RemoteActor, SocketListener {
    protected final String name;
    protected final int port;
    protected final long delay;
    protected final ServerWrapper incoming;
    private final Set<RemoteInfo> remoteSet = new HashSet<>();
    private final PropertyChangeSupport property;

    protected Service(int port, String name, long delay) {
        incoming = new ServerWrapper(port, this);
        this.port = port;
        this.name = name;
        this.delay = delay;
        property = new PropertyChangeSupport(this);
        new Thread(incoming).start();
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.property.addPropertyChangeListener("remoteSet", listener);
    }

    protected boolean addRemote(RemoteInfo remote) {
        boolean result = remoteSet.add(remote);
        if (result) {
            property.firePropertyChange("remoteSet", null, remote);
        }
        return result;
    }

    public List<RemoteInfo> getRemotes() {
        return Collections.unmodifiableList(remoteSet.stream().toList());
    }
}
