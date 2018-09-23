package jp.mkserver;

import com.github.steveice10.mc.auth.exception.request.RequestException;
import com.github.steveice10.mc.protocol.packet.ingame.client.ClientChatPacket;
import jp.mkserver.utils.ConfigFileManager;
import jp.mkserver.utils.TextAreaOutputStream;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.Locale;

import static jp.mkserver.MCchatManager.sendHelp;

public class GUIManager extends JFrame implements ActionListener {
    JTextField text1;
    ChatClient client;
    int wave = 0;
    public String email;
    public String pass;
    public String serverip;
    public int port;

    GUIManager (){
        this.client = new ChatClient(this);
        setTitle("MCchatManager");
        setSize(500,300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        URL url=getClass().getClassLoader().getResource("Icon.png");
        ImageIcon icon=new ImageIcon(url);
        setIconImage(icon.getImage());

        JTextArea area = new JTextArea();
        area.setBackground(Color.BLACK);
        area.setForeground(Color.WHITE);
        area.setRows(14);
        area.setEditable(false);
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        Font fonts[] = ge.getAllFonts();
        for(Font font : fonts){
            if(font.getFontName(Locale.JAPAN).equalsIgnoreCase("メイリオ")){
                area.setFont(new Font("メイリオ", Font.PLAIN, 10));
                break;
            }
        }
        PrintStream printStream = null;
        try {
            printStream = new PrintStream(new TextAreaOutputStream(area),true,"UTF-8");
        } catch (UnsupportedEncodingException e) {
            printStream = new PrintStream(new TextAreaOutputStream(area),true);
        }
        System.setOut(printStream);
        System.setErr(printStream);
        JScrollPane scrollpane = new JScrollPane(area);

        Container contentPane = getContentPane();
        JPanel p = new JPanel();
        text1 = new JTextField("メールアドレスを入力", 20);
        JButton button = new JButton("送信");
        button.addActionListener(this);

        p.add(text1);
        p.add(button);
        contentPane.add(p, BorderLayout.CENTER);
        contentPane.add(scrollpane, BorderLayout.NORTH);

        setVisible(true);
    }


    public void actionPerformed(ActionEvent e){
        String message = text1.getText();
        if(message.equalsIgnoreCase("!end")) {
            System.out.println("See you!");
            client.end();
            return;
        }else if(message.equalsIgnoreCase("!template")){
            text1.setText("");
            MCchatManager.config.load();
            try {
                client.login(email,pass);
            } catch (RequestException ee) {
                System.out.println("Error: ログインに失敗しました。emailかpasswordが間違っています。");
                resetState();
                return;
            }
            client.connect(serverip,port);
            return;
        }
        if(wave != 3){
            if(wave == 0){
                email = message;
                text1.setText("パスワードを入力");
                System.out.println("パスワードを入力してください");
                wave++;
                return;
            }else if(wave == 1){
                pass = message;
                text1.setText("サーバーIP:ポートを入力");
                System.out.println("サーバーIP:ポートを入力してください");
                wave++;
                return;
            }else if(wave == 2){
                String[] argserver = message.split(":");
                if(argserver.length <= 1){
                    System.out.println("Error: ポート番号を記載してください！");
                    sendHelp();
                    resetState();
                    return;
                }
                serverip = argserver[0];
                try{
                    port = Integer.parseInt(argserver[1]);
                }catch (NumberFormatException ee){
                    System.out.println("Error: ポート番号が数字ではありません！");
                    sendHelp();
                    resetState();
                    return;
                }
                text1.setText("チャットを入力");
                wave++;
                try {
                    client.login(email,pass);
                } catch (RequestException ee) {
                    System.out.println("Error: ログインに失敗しました。emailかpasswordが間違っています。");
                    resetState();
                    return;
                }
                client.connect(serverip,port);
                return;
            }
        }
        if(message.equalsIgnoreCase("!server info")){
            System.out.println("IP: "+client.getSession().getHost()+" PORT: "+client.getSession().getPort());
            text1.setText("");
            return;
        }else if(message.equalsIgnoreCase("!quit")){
            text1.setText("");
            client.disconnect();
            return;
        }
        client.getSession().send(new ClientChatPacket(message));
        text1.setText("");
    }

    public void resetState(){
        wave = 0;
        email = null;
        pass = null;
        serverip = null;
        port = 25565;
        text1.setText("メールアドレスを入力");
        System.out.println( "メールアドレスを入力してください！" );
    }

    public void destroy(){
        dispose();
    }


}
