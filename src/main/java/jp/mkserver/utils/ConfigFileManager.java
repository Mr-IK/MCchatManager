package jp.mkserver.utils;

import jp.mkserver.GUIManager;
import jp.mkserver.MCchatManager;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.Arrays;


public class ConfigFileManager {
    File file;
    GUIManager gui;
    public ConfigFileManager(GUIManager gui){
        this.gui = gui;
        try{
            file = new File(getApplicationPath(MCchatManager.class).getParent().toString()+"\\config.txt");
            if(file.createNewFile()) {
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file, true),"utf-8"));
                bw.write("※※余計な文字列を書かないでください。最悪バグります");
                bw.newLine();
                bw.write("※log_writeはログファイルを生成するかどうかです。");
                bw.newLine();
                bw.write("※trueで生成、それ以外の文字列でオフします");
                bw.newLine();
                bw.write("log_write=true");
                bw.newLine();
                bw.write("※templateはよく使う条件を簡単にロードできるシステムです。");
                bw.newLine();
                bw.write("※<メールアドレス> // <パスワード> // <サーバーip>:(ポート番号) と書いてください。 ");
                bw.newLine();
                bw.write("template=example@gmail.com // password // localhost");
                bw.newLine();
                bw.write("※login_templateはよく使うアカウントにすぐログインできるシステムです。");
                bw.newLine();
                bw.write("※<メールアドレス> // <パスワード> と書いてください。 ");
                bw.newLine();
                bw.write("login_template=example@gmail.com // password");
                bw.newLine();
                bw.write("※text_colorはテキストカラーを指定できます");
                bw.newLine();
                bw.write("※http://pdf-file.nnn2.com/?p=145 を参考に");
                bw.newLine();
                bw.write("text_color=255 // 255 // 255");
                bw.close();
            }else{
                load();
            }
        }catch(IOException | URISyntaxException e){
            System.out.println(e.getMessage());
        }
    }

    public static boolean log_write = true;

    public void load(){
        System.out.println("[CONFIG]コンフィグをロード中…");
        try{
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-8"));
            String str = br.readLine();
            while(str != null){
                if(str.startsWith("log_write=")){
                    String boor = str.replace("log_write=","");
                    if(!boor.equalsIgnoreCase("true")){
                        log_write = false;
                        System.out.println("[CONFIG]Logファイル書き込みを無効化しました！");
                    }
                }else if(str.startsWith("template=")){
                    String message = str.replace("template=","");
                    String[] argserver = message.split(" // ");
                    if(argserver.length <= 2){
                        System.out.println("[CONFIG]Error: (email // password // server_ip(:port) )すべてを記載してください！");
                        return;
                    }
                    System.out.println("[CONFIG]ロード: "+Arrays.toString(argserver));
                    gui.email = argserver[0];
                    gui.pass = argserver[1];
                    String[] argservers = argserver[2].split(":");
                    gui.serverip = argservers[0];
                    if(argservers.length >= 2){
                        try{
                            gui.port = Integer.parseInt(argservers[1]);
                        }catch (NumberFormatException ee){
                            System.out.println("Error: ポート番号が数字ではありません！");
                            gui.resetState();
                            return;
                        }
                    }else{
                        gui.port = 25565;
                    }
                }else if(str.startsWith("login_template=")) {
                    String message = str.replace("login_template=", "");
                    String[] argserver = message.split(" // ");
                    if (argserver.length <= 1) {
                        System.out.println("[CONFIG]Error: (email // password)どちらも記載してください！");
                        return;
                    }
                    System.out.println("[CONFIG]ロード: " + Arrays.toString(argserver));
                    gui.email = argserver[0];
                    gui.pass = argserver[1];
                }else if(str.startsWith("text_color=")) {
                    String message = str.replace("text_color=", "");
                    String[] argserver = message.split(" // ");
                    if (argserver.length <= 2) {
                        System.out.println("[CONFIG]Error: (r // g // b)すべて記載してください！");
                        return;
                    }
                    int r,g,b;
                    try{
                        r = Integer.parseInt(argserver[0]);
                        g = Integer.parseInt(argserver[1]);
                        b = Integer.parseInt(argserver[2]);
                    }catch (NumberFormatException ee){
                        System.out.println("Error: RGBが数字ではありません！");
                        gui.resetState();
                        return;
                    }
                    gui.setTextColor(r,g,b);
                }
                str = br.readLine();
            }

            br.close();
        }catch(IOException ignored){

        }
        System.out.println("[CONFIG]コンフィグのロード完了！");
    }
    public void load_template(){
        System.out.println("[CONFIG]コンフィグをロード中…");
        try{
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-8"));
            String str = br.readLine();
            while(str != null){
                if(str.startsWith("template=")){
                    String message = str.replace("template=","");
                    String[] argserver = message.split(" // ");
                    if(argserver.length <= 2){
                        System.out.println("[CONFIG]Error: (email // password // server_ip:port )すべてを記載してください！");
                        return;
                    }
                    System.out.println("[CONFIG]ロード: "+Arrays.toString(argserver));
                    gui.email = argserver[0];
                    gui.pass = argserver[1];
                    String[] argservers = argserver[2].split(":");
                    gui.serverip = argservers[0];
                    if(argservers.length >= 2){
                        try{
                            gui.port = Integer.parseInt(argservers[1]);
                        }catch (NumberFormatException ee){
                            System.out.println("Error: ポート番号が数字ではありません！");
                            gui.resetState();
                            return;
                        }
                    }else{
                        gui.port = 25565;
                    }
                }
                str = br.readLine();
            }

            br.close();
        }catch(IOException ignored){

        }
        System.out.println("[CONFIG]コンフィグのロード完了！");
    }

    public void load_login(){
        System.out.println("[CONFIG]コンフィグをロード中…");
        try{
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-8"));
            String str = br.readLine();
            while(str != null){
                if(str.startsWith("login_template=")){
                    String message = str.replace("login_template=","");
                    String[] argserver = message.split(" // ");
                    if(argserver.length <= 1){
                        System.out.println("[CONFIG]Error: (email // password)どちらも記載してください！");
                        return;
                    }
                    System.out.println("[CONFIG]ロード: "+Arrays.toString(argserver));
                    gui.email = argserver[0];
                    gui.pass = argserver[1];
                    gui.wave = 2;
                    gui.text1.setText("サーバーIP:ポートを入力してください");
                    System.out.println("サーバーIP:ポートを入力してください");
                }
                str = br.readLine();
            }

            br.close();
        }catch(IOException ignored){

        }
        System.out.println("[CONFIG]コンフィグのロード完了！");
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
