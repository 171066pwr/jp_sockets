package com.mycompany.app.gui;

import com.mycompany.app.model.Environment;
import com.mycompany.app.model.RainGenerator;

import javax.swing.*;
import java.awt.*;

public class EnvironmentGui extends JFrame {
    private Environment environment;
    private RainGenerator rainGenerator;
    private Thread generatorThread;
    private CreatorPanel creatorPanel;
    private EnvironmentPanel environmentPanel;
    private JPanel cardPanel;
    private LogPanel logPanel;

    public EnvironmentGui(Environment environment) {
        super();
        this.environment = environment;
        createGUI();
    }

    public EnvironmentGui(Environment environment, RainGenerator rainGenerator) {
        super();
        this.environment = environment;
        this.rainGenerator = rainGenerator;
        new Thread(rainGenerator).start();
        createGUI();
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
                environmentPanel.setSelfInfo(environment.getRemoteInfo());
                CardLayout cl = (CardLayout)(cardPanel.getLayout());
                cl.show(cardPanel, "environment");
            });
            cardPanel.add(creatorPanel, "creator");
            cardLayout.show(cardPanel, "creator");
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
        this.environment = environment;
        this.rainGenerator = new RainGenerator(environment);
    }
}
