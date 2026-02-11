package com.mycompany.app.gui;

import com.mycompany.app.common.gui.CreatorPanel;
import com.mycompany.app.model.Environment;

public class EnvCreatorPanel extends CreatorPanel<Environment> {
    EnvCreatorPanel() {
        super("Name: ", "create environment");
    }

    @Override
    protected Environment create() {
        return new Environment(Integer.parseInt(portTF.getText()), nameTF.getText());
    }

    @Override
    protected String getDefaultName() {
        return "Environment";
    }
}
