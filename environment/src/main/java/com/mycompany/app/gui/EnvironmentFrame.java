package com.mycompany.app.gui;

import com.mycompany.app.common.gui.ServiceFrame;
import com.mycompany.app.model.Environment;
import com.mycompany.app.model.RainGenerator;

public class EnvironmentFrame extends ServiceFrame<Environment> {
    private RainGenerator rainGenerator;
    private Thread generatorThread;
    EnvironmentPanel environmentPanel;

    public EnvironmentFrame() {
        this(null, null, new EnvCreatorPanel(), new EnvironmentPanel());
    }

    public EnvironmentFrame(Environment environment, RainGenerator rainGenerator) {
        this(environment, rainGenerator, new EnvCreatorPanel(), new EnvironmentPanel());
    }

    private EnvironmentFrame(Environment environment, RainGenerator rainGenerator, EnvCreatorPanel creatorPanel, EnvironmentPanel environmentPanel) {
        super(environment, creatorPanel, environmentPanel);
        this.environmentPanel = environmentPanel;
        this.rainGenerator = rainGenerator;
        setService(environment);
        createGUI();
    }

    @Override
    protected void createServiceListeners() {
        service.addPropertyChangeListener(this);
        environmentPanel.addRainValueListener(e -> {
            if(rainGenerator == null) {
                service.setCurrentRainfall(environmentPanel.getRainfall());
            } else {
                rainGenerator.setAverage(environmentPanel.getRainfall());
            }
        });
        environmentPanel.addGeneratorToggleListener(e -> {
            if(environmentPanel.isRainGenerator()) {
                rainGenerator = new RainGenerator(service);
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
    }

    @Override
    protected void setService(Environment service) {
        this.service = service;
        if(rainGenerator != null) {
            generatorThread = new Thread(rainGenerator);
            generatorThread.start();
        }
    }
}
