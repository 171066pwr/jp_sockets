package com.mycompany.app.common.gui;

import com.mycompany.app.common.Service;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public abstract class ServiceFrame <T extends Service> extends JFrame implements PropertyChangeListener {
    protected T service;
    protected LogPanel logPanel;
    private JPanel cardPanel;
    protected CreatorPanel<T> creatorPanel;
    protected ServicePanel<T> servicePanel;

    protected ServiceFrame(CreatorPanel<T> creatorPanel, ServicePanel<T> servicePanel) {
        this(null, creatorPanel, servicePanel);
    }

    protected ServiceFrame(T service, CreatorPanel<T> creatorPanel, ServicePanel<T> servicePanel) {
        super();
        this.service = service;
        this.creatorPanel = creatorPanel;
        this.servicePanel = servicePanel;

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(WindowEvent winEvt) {
                System.exit(0);
            }
        });
    }

    protected void createGUI() {
        this.setLayout(new GridLayout());
        logPanel = new LogPanel();
        CardLayout cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        if(service == null) {
            creatorPanel.addCreateListener(e -> {
                setService(creatorPanel.create());
                CardLayout cl = (CardLayout)(cardPanel.getLayout());
                cl.show(cardPanel, "service");
                updateServicePanel();
                createServiceListeners();
            });
            cardPanel.add(creatorPanel, "creator");
            cardLayout.show(cardPanel, "creator");
        } else {
            updateServicePanel();
            createServiceListeners();
        }
        cardPanel.add(servicePanel, "service");

        this.add(cardPanel);
        this.add(logPanel);
        Thread.setDefaultUncaughtExceptionHandler(logPanel);
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if(evt.getPropertyName().equals("remoteSet")) {
            servicePanel.setRemotesList(service.getRemotes());
        }
    }

    protected abstract void setService(T service);

    protected abstract void createServiceListeners();

    protected void updateServicePanel() {
        servicePanel.setSelfInfo(service.getSelfRemoteInfo());
        servicePanel.setRemotesList(service.getRemotes());
    }
}
