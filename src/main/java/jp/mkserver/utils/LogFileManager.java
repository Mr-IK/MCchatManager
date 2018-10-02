package jp.mkserver.utils;

import jp.mkserver.MCchatManager;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LogFileManager {
    File file;
    String s;
    public LogFileManager(){
        try{
            LocalDateTime d = LocalDateTime.now();
            DateTimeFormatter df1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH-mm-ss");
            s = df1.format(d);
            new File(getApplicationPath(MCchatManager.class).getParent().toString()+File.separator+"logs").mkdir();
            file = new File(getApplicationPath(MCchatManager.class).getParent().toString()+File.separator+"logs"+File.separator+"LogFile_"+s+".txt");
            file.createNewFile();
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true),"utf-8"));
            bw.write("MCchatManager log [Date: "+s+"]");
            bw.newLine();
            bw.write("version 0.4");
            bw.newLine();
            bw.close();
        }catch(IOException | URISyntaxException e){
            System.out.println(e.getMessage());
        }
    }

    public void write(String log){
        if(log == null || log.equalsIgnoreCase("") || !ConfigFileManager.log_write){
            return;
        }
        try{
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true),"utf-8"));
            LocalDateTime d = LocalDateTime.now();
            DateTimeFormatter df1 = DateTimeFormatter.ofPattern("HH:mm:ss");
            String time = df1.format(d);
            bw.write("["+time+"]"+log);
            bw.newLine();
            bw.close();
        }catch(IOException ignored){

        }
    }

    public Path getApplicationPath(Class<?> cls) throws URISyntaxException {
        ProtectionDomain pd = cls.getProtectionDomain();
        CodeSource cs = pd.getCodeSource();
        URL location = cs.getLocation();
        URI uri = location.toURI();
        Path path = Paths.get(uri);
        return path;
    }
}
