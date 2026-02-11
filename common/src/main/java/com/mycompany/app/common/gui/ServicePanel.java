package com.mycompany.app.common.gui;

import com.mycompany.app.common.RemoteInfo;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.List;
import java.util.Optional;

public abstract class ServicePanel <T> extends JPanel {
    private JTextField infoTF;
    private JButton copyInfoBT;
    private RemoteList remotes;
    private JButton copyRemoteBT;

    public ServicePanel() {
        super(new GridLayout(3,2));
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel infoLabel = new JLabel(getName() + ":");
        infoTF = new JTextField();
        infoTF.setEditable(false);
        copyInfoBT = new JButton("Copy");
        copyInfoBT.addActionListener(e -> {
            String info = infoTF.getText();
            Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
            StringSelection selection = new StringSelection(info);
            clip.setContents(selection, selection);
        });
        infoPanel.add(infoLabel);
        infoPanel.add(infoTF);
        infoPanel.add(copyInfoBT);
        this.add(infoPanel);

        JPanel remotePanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        remotes = new RemoteList();
        remotes.addListSelectionListener(e -> copyRemoteBT.setEnabled(true));
        copyRemoteBT = new JButton("Copy");
        copyRemoteBT.setEnabled(false);
        copyRemoteBT.addActionListener(e -> {
                    String remoteName = Optional.ofNullable(remotes.getSelectedValue().toString()).orElse("");
                    Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
                    StringSelection selection = new StringSelection(remoteName);
                    clip.setContents(selection, selection);
                });
        remotePanel.add(remotes);
        remotePanel.add(copyRemoteBT);
        this.add(remotePanel);
    }
    public abstract String getName();

    public void setRemotesList(List<RemoteInfo> remoteList) {
        remotes.setRemotes(remoteList);
    }

    public void setSelfInfo(RemoteInfo remote) {
        infoTF.setText(remote.toString());
    }
}
