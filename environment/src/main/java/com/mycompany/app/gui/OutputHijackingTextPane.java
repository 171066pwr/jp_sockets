package com.mycompany.app.gui;

import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.*;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.Configuration;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.config.plugins.Plugin;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.io.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Consumer;

@Log4j2
public class OutputHijackingTextPane extends JTextPane {
    public OutputHijackingTextPane() {
        super();
        try {
            hijackSystemOutput();
            registerAppender();
        } catch (IOException e) {
            log.error(e);
        }
    }

    public void logError(String error) {
        appendLog(error + "\n", Color.RED);
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

    private void registerAppender() {
        LoggerContext ctx = (LoggerContext) LogManager.getContext(false);
        Configuration config = ctx.getConfiguration();

        Layout<?> layout = config.getAppender("SwingAppender").getLayout();
        SwingAppender appender = new SwingAppender(
                "SwingAppender",
                null,
                layout,
                event -> {
                    String msg = layout.toSerializable(event).toString();
                    if(event.getLevel().equals(Level.ERROR)) {
                        appendLog(msg, Color.RED);
                    } else {
                        appendLog(msg, Color.BLACK);
                    }
                }
        );
        appender.start();
        config.addAppender(appender);
        config.getRootLogger().addAppender(appender, Level.ALL, null);
        ctx.updateLoggers();
    }

    @Plugin(
            name = "SwingAppender",
            category = Core.CATEGORY_NAME,
            elementType = Appender.ELEMENT_TYPE
    )
    public static class SwingAppender extends AbstractAppender {
        private final Consumer<LogEvent> consumer;

        protected SwingAppender(
                String name,
                Filter filter,
                Layout<? extends Serializable> layout,
                Consumer<LogEvent> consumer) {
            super(name, filter, layout, false, Property.EMPTY_ARRAY);
            this.consumer = consumer;
        }

        @Override
        public void append(LogEvent event) {
            consumer.accept(event.toImmutable());
        }
    }
}
