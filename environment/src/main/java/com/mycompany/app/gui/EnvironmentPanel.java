package com.mycompany.app.gui;

import com.mycompany.app.common.RemoteInfo;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;

import java.util.List;

public class EnvironmentPanel extends JPanel {
    private JTextField infoTF;
    private JTextField rainfallTF;
    private JButton rainfallBT;
    private JCheckBox rainGeneratorCB;
    private JList<RemoteInfo> remotesList;
    private JTextField remotesTF;

    EnvironmentPanel() {
        super(new GridLayout(8, 2, 5, 5));
        JLabel infoLabel = new JLabel("Environment: ");
        infoTF = new JTextField();
        this.add(infoLabel);
        this.add(infoTF);

        rainfallTF = new JTextField("100");
        rainfallTF.addActionListener(e -> {
            try {
                Integer.parseInt(rainfallTF.getText());
                rainfallBT.setEnabled(true);
            } catch (NumberFormatException ex) {
                rainfallBT.setEnabled(false);
            }
        });
        rainfallBT = new JButton("Apply");
        rainGeneratorCB = new JCheckBox("Rain Generator");
        rainGeneratorCB.setSelected(true);

        this.add(rainfallTF);
        this.add(rainfallBT);
        this.add(rainGeneratorCB);
        this.add(new JSeparator());

        JLabel listLabel = new JLabel("Subscribed remotes: ");
        remotesList = new JList<>();
        remotesList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        remotesList.addListSelectionListener(e -> remotesTF.setText(remotesList.getSelectedValue().toString()));
        this.add(listLabel);
        this.add(remotesList);
        remotesTF = new JTextField();
        remotesTF.setEditable(false);
        this.add(remotesTF);
    }

    public void addRainValueListener(ActionListener l) {
        rainfallBT.addActionListener(l);
    }

    public void addGeneratorToggleListener(ActionListener l) {
        rainGeneratorCB.addActionListener(l);
    }

    public void setRemotesList(List<RemoteInfo> remotes) {
        remotesList.setListData(remotes.toArray(new RemoteInfo[0]));
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
