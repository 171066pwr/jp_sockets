package com.mycompany.app.common.gui;

import com.mycompany.app.common.Service;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

public abstract class CreatorPanel<T> extends JPanel {
    protected JTextField portTF;
    protected JTextField nameTF;
    protected JButton createBT;
    private String nameTitle;
    private String buttonTitle;


    protected CreatorPanel(String nameTitle, String buttonTitle) {
        super(new FlowLayout(FlowLayout.CENTER));
        this.buttonTitle = buttonTitle;
        this.nameTitle = nameTitle;
        initialize();
    }

    abstract protected T create();

    abstract protected String getDefaultName();

    public void addCreateListener(ActionListener l) {
        createBT.addActionListener(l);
    }

    protected void initialize() {
        JLabel nameLabel = new JLabel(this.nameTitle);
        nameTF = new JTextField(getDefaultName());
        nameTF.addActionListener(e -> checkEnableButton());
        JPanel namePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        namePanel.setPreferredSize(new Dimension(192, 50));
        namePanel.add(nameLabel);
        namePanel.add(nameTF);
        this.add(namePanel);

        JLabel infoLabel = new JLabel("port: ");
        portTF = new JTextField("4444");
        portTF.addActionListener(e -> checkEnableButton());
        JPanel portPanel = new JPanel();
        portPanel.setPreferredSize(new Dimension(128, 30));
        portPanel.add(infoLabel);
        portPanel.add(portTF);
        this.add(portPanel);
        createBT = new JButton(buttonTitle);
        createBT.setPreferredSize(new Dimension(100, 30));
        this.add(createBT);
        this.setMinimumSize(new Dimension(192, 40));
        this.setPreferredSize(new Dimension(400, 100));
    }

    protected void checkEnableButton() {
        boolean result = !nameTF.getText().isBlank();
        try {
            Integer.parseInt(portTF.getText());
            createBT.setEnabled(result);
        } catch (Exception e) {
            createBT.setEnabled(false);
        }
    }
}
