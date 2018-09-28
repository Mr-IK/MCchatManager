package jp.mkserver.utils;

import jp.mkserver.apis.event.EventAPI;

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
        String msg = ChatColour.repColor(buf.toString("UTF-8"));
        msg = EventAPI.viewMessage(msg);
        if(msg==null){
            return;
        }
        area.append(msg);
        logFileManager.write(msg);
        buf.reset();
        area.setCaretPosition(area.getText().length());
    }
    @Override public void write(int b) throws IOException {
        buf.write(b);
    }
}
