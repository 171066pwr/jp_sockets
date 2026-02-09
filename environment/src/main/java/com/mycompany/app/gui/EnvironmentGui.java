package com.mycompany.app.gui;

import com.mycompany.app.model.Environment;
import com.mycompany.app.model.RainGenerator;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class EnvironmentGui extends JFrame implements PropertyChangeListener {
    private Environment environment;
    private RainGenerator rainGenerator;
    private Thread generatorThread;
    private CreatorPanel creatorPanel;
    private EnvironmentPanel environmentPanel;
    private JPanel cardPanel;
    private LogPanel logPanel;

    public EnvironmentGui() {
        super();
        createGUI();
    }

    public EnvironmentGui(Environment environment, RainGenerator rainGenerator) {
        super();
        setEnvironment(environment, rainGenerator);
        createGUI();

        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(WindowEvent winEvt) {
                System.exit(0);
            }
        });
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if(evt.getPropertyName().equals("remoteSet")) {
            environmentPanel.setRemotesList(environment.getRemotes());
        }
    }

    private void createGUI() {
        BorderLayout borderLayout = new BorderLayout();
        this.setLayout(borderLayout);
        creatorPanel = new CreatorPanel();
        environmentPanel = new EnvironmentPanel();
        logPanel = new LogPanel();
        CardLayout cardLayout = new CardLayout();
        cardPanel = new JPanel(cardLayout);

        if(environment == null) {
            creatorPanel.addCreateListener(e -> {
                setEnvironment(creatorPanel.createEnvironment());
                CardLayout cl = (CardLayout)(cardPanel.getLayout());
                cl.show(cardPanel, "environment");
                updateEnvironmentPanel();
            });
            cardPanel.add(creatorPanel, "creator");
            cardLayout.show(cardPanel, "creator");
        } else {
            updateEnvironmentPanel();
        }
        cardPanel.add(environmentPanel, "environment");

        environmentPanel.addRainValueListener(e -> {
            if(rainGenerator == null) {
                environment.setCurrentRainfall(environmentPanel.getRainfall());
            } else {
                rainGenerator.setAverage(environmentPanel.getRainfall());
            }
        });

        environmentPanel.addGeneratorToggleListener(e -> {
            if(environmentPanel.isRainGenerator()) {
                rainGenerator = new RainGenerator(environment);
                rainGenerator.setAverage(environmentPanel.getRainfall());
                generatorThread = new Thread(rainGenerator);
                generatorThread.start();
            } else {
                if(generatorThread != null) {
                    rainGenerator.kill();
                }
                rainGenerator = null;
            }
        });

        environmentPanel.setGeneratorSelected(rainGenerator != null);
        this.add(cardPanel, BorderLayout.CENTER);
        this.add(logPanel, BorderLayout.SOUTH);
        Thread.setDefaultUncaughtExceptionHandler(logPanel);
    }

    private void setEnvironment(Environment environment) {
        setEnvironment(environment, new RainGenerator(environment));
    }

    private void setEnvironment(Environment environment, RainGenerator generator) {
        this.environment = environment;
        this.rainGenerator = generator;
        environment.addPropertyChangeListener(this);
        generatorThread = new Thread(rainGenerator);
        generatorThread.start();
    }

    private void updateEnvironmentPanel() {
        environmentPanel.setSelfInfo(environment.getRemoteInfo());
        environmentPanel.setRemotesList(environment.getRemotes());
    }
}
