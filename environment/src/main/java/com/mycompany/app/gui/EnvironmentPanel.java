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
    private RemoteList remotes;
    private JTextField remotesTF;

    EnvironmentPanel() {
        super(new GridLayout(4, 2, 5, 5));
        JLabel infoLabel = new JLabel("Environment: ");
        infoTF = new JTextField();
        infoTF.setEditable(false);
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

        JLabel listLabel = new JLabel("Subscribed remotes: ");
        remotes = new RemoteList();
        remotes.addListSelectionListener(e -> remotesTF.setText(remotes.getSelectedValue().toString()));
        this.add(listLabel);
        this.add(remotes);
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

    public void setRemotesList(List<RemoteInfo> remoteList) {
        remotes.setRemotes(remoteList);
    }

    public void setSelfInfo(RemoteInfo remote) {
        infoTF.setText(remote.toString());
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
