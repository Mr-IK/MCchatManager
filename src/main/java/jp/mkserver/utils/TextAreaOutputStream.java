package jp.mkserver.utils;

import javax.swing.*;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class TextAreaOutputStream extends OutputStream {
    private final ByteArrayOutputStream buf = new ByteArrayOutputStream();
    private final JTextArea area;
    private final LogFileManager logFileManager;
    public TextAreaOutputStream(JTextArea area) {
        super();
        this.area = area;
        this.logFileManager = new LogFileManager();
    }
    @Override public void flush() throws IOException {
        area.append(ChatColour.repColor(buf.toString("UTF-8")));
        logFileManager.write(ChatColour.repColor(buf.toString("UTF-8")));
        buf.reset();
        area.setCaretPosition(area.getText().length());
    }
    @Override public void write(int b) throws IOException {
        buf.write(b);
    }
}
