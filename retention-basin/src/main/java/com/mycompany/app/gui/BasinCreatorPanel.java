package com.mycompany.app.gui;

import com.mycompany.app.common.gui.CreatorPanel;
import com.mycompany.app.model.RetentionBasin;

import javax.swing.*;
import java.awt.*;

public class BasinCreatorPanel extends CreatorPanel<RetentionBasin> {
    private JTextField volumeTF;

    BasinCreatorPanel() {
        super("Name: ", "create");
    }

    @Override protected void initialize() {
        super.initialize();
        JLabel infoLabel = new JLabel("Volume cap: ");
        volumeTF = new JTextField("1000");
        volumeTF.addActionListener(e -> checkEnableButton());
        JPanel volumePanel = new JPanel();
        volumePanel.setPreferredSize(new Dimension(128, 30));
        volumePanel.add(infoLabel);
        volumePanel.add(volumeTF);
        this.add(volumePanel);
    }

    @Override
    protected RetentionBasin create() {
        return new RetentionBasin(Integer.parseInt(portTF.getText()), nameTF.getText(), Integer.parseInt(volumeTF.getText()));
    }

    @Override
    protected String getDefaultName() {
        return "Retention-Basin";
    }

    @Override
    protected void checkEnableButton() {
        boolean result = !nameTF.getText().isBlank();
        try {
            Integer.parseInt(volumeTF.getText());
            Integer.parseInt(portTF.getText());
            createBT.setEnabled(result);
        } catch (Exception e) {
            createBT.setEnabled(false);
        }
    }
}
