package com.mycompany.app.gui;

import com.mycompany.app.common.RemoteInfo;
import com.mycompany.app.common.Response;
import com.mycompany.app.common.gui.ServiceFrame;
import com.mycompany.app.model.RetentionBasin;
import lombok.extern.log4j.Log4j2;

import java.beans.PropertyChangeEvent;
import java.util.List;

@Log4j2
public class BasinFrame extends ServiceFrame<RetentionBasin> {
    BasinPanel basinPanel;

    public BasinFrame() {
        this(null, new BasinCreatorPanel(), new BasinPanel());
    }

    public BasinFrame(RetentionBasin retentionBasin) {
        this(retentionBasin, new BasinCreatorPanel(), new BasinPanel(retentionBasin));
    }

    protected BasinFrame(RetentionBasin retentionBasin, BasinCreatorPanel creatorPanel, BasinPanel basinPanel) {
        super(retentionBasin, creatorPanel, basinPanel);
        this.basinPanel = basinPanel;
        setService(retentionBasin);
        createGUI();
    }

    @Override
    protected void createGUI() {
        super.createGUI();
        if(service != null && service.getControlCenter() != null) {
            basinPanel.setControlCenter(service.getControlCenter());
        }
    }

    private void registerRemotes(List<RemoteInfo> remotes) {
        if(!remotes.isEmpty()) {
            service.setControlCenter(remotes.getFirst());
        }
        remotes.stream().skip(1)
                .forEach(r -> {
                    Response response = service.subscribeToRemote(r);
                    log.info("Registering remote {}: {}", r, response );
                });
    }

    private void registerControlCenter(RemoteInfo remote) {
        service.setControlCenter(remote);
    }

    private void registerRemote(RemoteInfo remote) {
        Response response = service.subscribeToRemote(remote);
        log.info("Registering remote {}: {}", remote, response );
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        super.propertyChange(evt);
        if(evt.getPropertyName().equals("controlCenter")) {
            basinPanel.setControlCenter(service.getControlCenter());
        }
    }

    @Override
    protected void createServiceListeners() {
        service.addPropertyChangeListener(this);
        basinPanel.addSubscribeToCCListener(e -> {
            service.setControlCenter(basinPanel.createControlCenter());
            //PropertyChangeEvent for some reason won't work so have to do this ugly thing:
            if(service.getControlCenter() != null) {
                basinPanel.setControlCenter(service.getControlCenter());
            }
        });
    }

    @Override
    protected void setService(RetentionBasin service) {
        this.service = service;
    }
}
