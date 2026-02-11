package com.mycompany.app.common.gui;

import javax.swing.*;
import java.awt.*;

public class LogPanel extends JPanel implements Thread.UncaughtExceptionHandler{
    private OutputHijackingTextPane logTP;
    private JButton clearBT;

    public LogPanel() {
        super();
        setLayout(new BorderLayout(5, 5));
        logTP = new OutputHijackingTextPane();
        logTP.setEditable(false);
        add(new JScrollPane(logTP), BorderLayout.CENTER);
        clearBT = new JButton("Clear");
        add(clearBT, BorderLayout.SOUTH);
        clearBT.addActionListener(actionEvent -> logTP.setText(""));
        setPreferredSize(new Dimension(500, 100));
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        logError(String.format("%s: %s", t.getName(), e.getMessage()));
    }

    public void logInfo(String info) {
        logTP.appendLog(info, Color.BLACK);
    }

    public void logError(String error) {
        logTP.appendLog(error, Color.RED);
    }
}
