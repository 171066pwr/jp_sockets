package com.mycompany.app.gui;

import com.mycompany.app.model.Environment;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public class CreatorPanel extends JPanel {
    private JTextField portTF;
    private JTextField nameTF;
    private JButton createBT;

    CreatorPanel() {
        super(new GridLayout(3, 2, 5, 5));
        JLabel infoLabel = new JLabel("port: ");
        portTF = new JTextField("4444");
        portTF.addActionListener(e -> checkEnableButton());
        this.add(infoLabel);
        this.add(portTF);

        JLabel nameLabel = new JLabel("name: ");
        nameTF = new JTextField("Environment");
        nameTF.addActionListener(e -> checkEnableButton());
        this.add(nameLabel);
        this.add(nameTF);

        createBT = new JButton("Create");
        this.add(createBT);
    }

    void addCreateListener(ActionListener l) {
        createBT.addActionListener(l);
    }

    Environment createEnvironment() {
        return new Environment(Integer.parseInt(portTF.getText()), nameTF.getText());
    }

    private void checkEnableButton() {
        boolean result = !nameTF.getText().isBlank();
        try {
            Integer.parseInt(portTF.getText());
            createBT.setEnabled(result);
        } catch (Exception e) {
            createBT.setEnabled(false);
        }
    }
}
