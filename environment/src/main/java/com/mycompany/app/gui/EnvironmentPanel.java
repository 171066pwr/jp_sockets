package com.mycompany.app.gui;

import com.mycompany.app.common.gui.ServicePanel;
import com.mycompany.app.model.Environment;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class EnvironmentPanel extends ServicePanel<Environment> {
    private JTextField rainfallTF;
    private JButton rainfallBT;
    private JCheckBox rainGeneratorCB;

    EnvironmentPanel() {
        super();
        JPanel generatorPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        rainfallTF = new JTextField("100");
        rainfallTF.addActionListener(e -> {
            try {
                Integer.parseInt(rainfallTF.getText());
                rainfallBT.setEnabled(true);
            } catch (NumberFormatException ex) {
                rainfallBT.setEnabled(false);
            }
        });
        Label rainfallLB = new Label("Rainfall volume (avg value in auto mode");
        rainfallBT = new JButton("Apply rainfall");
        rainGeneratorCB = new JCheckBox("Auto rain generator");
        rainGeneratorCB.setSelected(true);
        generatorPanel.add(rainfallLB);
        generatorPanel.add(rainfallTF);
        generatorPanel.add(rainGeneratorCB);
        generatorPanel.add(new JSeparator());
        generatorPanel.add(new JSeparator());
        generatorPanel.add(rainfallBT);
        this.add(generatorPanel);
    }

    @Override
    public String getName() {
        return "Environment";
    }

    public void addRainValueListener(ActionListener l) {
        rainfallBT.addActionListener(l);
    }

    public void addGeneratorToggleListener(ActionListener l) {
        rainGeneratorCB.addActionListener(l);
    }

    public int getRainfall() {
        return Integer.parseInt(rainfallTF.getText());
    }

    public boolean isRainGenerator() {
        return rainGeneratorCB.isSelected();
    }

    void setGeneratorSelected(boolean selected) {
        rainGeneratorCB.setSelected(selected);
    }
}
