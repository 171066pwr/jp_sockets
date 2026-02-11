package com.mycompany.app.gui;

import com.mycompany.app.common.RemoteInfo;
import com.mycompany.app.common.gui.SubscriptionPanel;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionListener;
import java.util.Arrays;

public class ControlCenterPanel extends SubscriptionPanel {
    public ControlCenterPanel(RemoteInfo cc) {
        this();
        if(cc != null) {
            setControlCenter(cc);
        }
    }

    public ControlCenterPanel() {
        super("Control Center");
        nameTF.setText("localhost");
        portTF.setText("5000");
        this.setPreferredSize(new Dimension(300, 80));
    }

    public void setControlCenter(RemoteInfo cc) {
        nameTF.setText(cc.toString());
        nameTF.setEditable(false);
        portTF.setText(Integer.toString(cc.getPort()));
        portTF.setEditable(false);
        createBT.setText("copy");
        Arrays.stream(createBT.getActionListeners()).findFirst().ifPresent(a -> {
            createBT.removeActionListener(a);
        });
        createBT.addActionListener(e -> {
            String info = nameTF.getText();
            Clipboard clip = Toolkit.getDefaultToolkit().getSystemClipboard();
            StringSelection selection = new StringSelection(info);
            clip.setContents(selection, selection);
        });
    }

    public void addSubscribeToCCListener(ActionListener l) {
        createBT.addActionListener(l);
    }

}
