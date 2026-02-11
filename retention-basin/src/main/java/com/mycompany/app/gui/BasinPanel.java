package com.mycompany.app.gui;

import com.mycompany.app.common.RemoteInfo;
import com.mycompany.app.common.gui.ServicePanel;
import com.mycompany.app.model.RetentionBasin;

import javax.swing.*;
import java.awt.event.ActionListener;

public class BasinPanel extends ServicePanel<RetentionBasin> {
 private ControlCenterPanel controlCenterPanel;

    BasinPanel() {
        super();
        controlCenterPanel = new ControlCenterPanel();
        add(controlCenterPanel);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    }

    BasinPanel(RetentionBasin retentionBasin) {
        super();
        controlCenterPanel = new ControlCenterPanel(retentionBasin.getControlCenter());
        add(controlCenterPanel);
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    }

    @Override
    public String getName() {
        return "Retention Basin";
    }

    public void addSubscribeToCCListener(ActionListener l) {
        controlCenterPanel.addSubscribeToCCListener(l);
    }

    public void setControlCenter(RemoteInfo cc) {
        controlCenterPanel.setControlCenter(cc);
    }

    RemoteInfo createControlCenter() {
        return controlCenterPanel.create();
    }
}
