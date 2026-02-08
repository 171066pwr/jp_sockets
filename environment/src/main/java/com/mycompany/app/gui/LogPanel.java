package com.mycompany.app.gui;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyleContext;
import java.awt.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class LogPanel extends JPanel implements Thread.UncaughtExceptionHandler{
    private JTextPane logTP;
    private JButton clearBT;

    LogPanel() {
        super();
        setLayout(new BorderLayout(5, 5));
        logTP = new JTextPane();
        logTP.setEditable(false);
        add(new JScrollPane(logTP), BorderLayout.CENTER);
        clearBT = new JButton("Clear");
        add(clearBT, BorderLayout.SOUTH);
        clearBT.addActionListener(actionEvent -> logTP.setText(""));
        setPreferredSize(new Dimension(500, 300));
        //hijackPrintStream();
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        logError(String.format("%s: %s", t.getName(), e.getMessage()));
    }

    public void logInfo(String info) {
        appendLog(info, Color.BLACK);
    }

    public void logError(String error) {
        appendLog(error, Color.RED);
    }

    private void appendLog(String log, Color color) {
        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, color);
        int len = logTP.getDocument().getLength();
        logTP.setCaretPosition(len);
        logTP.setCharacterAttributes(aset, false);
        logTP.replaceSelection(log + "\n");
    }

//    private void hijackPrintStream() {
//        ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        PrintStream ps = new PrintStream(baos);
//        System.setOut(ps);
//        baos.
//    }
}
