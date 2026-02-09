package com.mycompany.app.gui;

import com.mycompany.app.common.RemoteInfo;

import javax.swing.*;
import javax.swing.event.ListSelectionListener;
import java.util.Collection;

public class RemoteList extends JScrollPane {
    DefaultListModel<RemoteInfo> listModel = new DefaultListModel<>();
    JList<RemoteInfo> list = new JList<>(listModel);

    public RemoteList() {
        list.setModel(listModel);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.setVisibleRowCount(2);
        this.add(list);
        this.setViewportView(list);
    }

    public void setRemotes(Collection<RemoteInfo> remotes) {
        RemoteInfo selected = list.getSelectedValue();
        listModel.clear();
        listModel.addAll(remotes);
        if(selected != null && remotes.contains(selected)) {
            list.setSelectedValue(selected, true);
        }
    }

    public RemoteInfo getSelectedValue() {
        return list.getSelectedValue();
    }

    public void addListSelectionListener(ListSelectionListener l) {
        list.addListSelectionListener(l);
    }
}
