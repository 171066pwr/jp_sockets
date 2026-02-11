package com.mycompany.app.common.gui;

import com.mycompany.app.common.RemoteInfo;

import javax.swing.*;

public class SubscriptionPanel extends CreatorPanel<RemoteInfo> {
    protected SubscriptionPanel(String title) {
        super("host (or RemoteInfo): ", "subscribe");
        this.setBorder(BorderFactory.createTitledBorder(title));
    }

    @Override
    public RemoteInfo create() {
        RemoteInfo info;
        try {
            info = new RemoteInfo(nameTF.getText());
        } catch (RemoteInfo.RemoteInfoParseException e) {
            info = new RemoteInfo(nameTF.getText(), Integer.parseInt(portTF.getText()), "", 0);
        }
        return info;
    }

    @Override
    protected String getDefaultName() {
        return "localhost";
    }
}
