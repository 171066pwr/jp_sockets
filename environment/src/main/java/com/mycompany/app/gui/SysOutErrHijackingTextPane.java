package com.mycompany.app.gui;

import lombok.extern.log4j.Log4j2;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.io.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Log4j2
public class SysOutErrHijackingTextPane extends JTextPane {
    public SysOutErrHijackingTextPane() {
        super();
        try {
            hijackSystemOutput();
        } catch (IOException e) {
            log.error(e);
        }
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

    private void hijackSystemOutput() throws IOException {
        PipedOutputStream errOut = new PipedOutputStream();
        PipedInputStream errIn = new PipedInputStream(errOut);
        PrintStream errPs = new PrintStream(errOut, true);
        System.setErr(errPs);
        BufferedReader err = new BufferedReader(new InputStreamReader(errIn));

        PipedOutputStream sysOut = new PipedOutputStream();
        PipedInputStream sysIn = new PipedInputStream(sysOut);
        PrintStream sysPs = new PrintStream(sysOut, true);
        System.setOut(sysPs);
        BufferedReader out = new BufferedReader(new InputStreamReader(sysIn));

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
                    } catch (BadLocationException ignored) {
                    }
                });
            }
            Thread.sleep(3000);
        } catch (Exception ignored) {
            logError(ignored.getMessage());
        }
    }
}
