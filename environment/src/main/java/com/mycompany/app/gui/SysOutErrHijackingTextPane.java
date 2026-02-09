package com.mycompany.app.gui;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.io.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SysOutErrHijackingTextPane extends JTextPane {
    public SysOutErrHijackingTextPane() {
        super();
        hijackSystemOutput();
    }

    public void logError(String error) {
        appendLog(error, Color.RED);
    }

    public void appendLog(String log, Color color) {
        StyleContext sc = StyleContext.getDefaultStyleContext();
        AttributeSet aset = sc.addAttribute(SimpleAttributeSet.EMPTY, StyleConstants.Foreground, color);
        Document doc = getDocument();
        try {
            doc.insertString(doc.getLength(), log, aset);
        } catch (BadLocationException ignored) {}
    }

    private void hijackSystemOutput() {
        ByteArrayOutputStream baosOut = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baosOut);
        System.setOut(ps);
        BufferedReader out =
                new BufferedReader(new StringReader(baosOut.toString()));

        ByteArrayOutputStream baosErr = new ByteArrayOutputStream();
        PrintStream psErr = new PrintStream(baosOut);
        System.setErr(psErr);
        BufferedReader err =
                new BufferedReader(new StringReader(baosErr.toString()));

        Style style = this.addStyle("Red", null);
        StyleConstants.setForeground(style, Color.RED);

        ExecutorService exec = Executors.newFixedThreadPool(2);
        exec.submit(() -> readStream(out, null));
        exec.submit(() -> readStream(err, style));
    }

    private void readStream(BufferedReader reader, Style style) {
        String line;
        StyledDocument doc = this.getStyledDocument();
        try {
            while ((line = reader.readLine()) != null) {
                String text = line + "\n";
                SwingUtilities.invokeLater(() -> {
                    try {
                        doc.insertString(doc.getLength(), text, style);
                    } catch (BadLocationException ignored) {}
                });
            }
        } catch (IOException ignored) {
            logError(ignored.getMessage());
        }
    }
}
