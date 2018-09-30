package jp.mkserver;

import com.github.steveice10.mc.auth.exception.request.RequestException;
import jp.mkserver.apis.PluginAPI;
import jp.mkserver.apis.event.EventAPI;
import jp.mkserver.utils.DefaultContextMenu;
import jp.mkserver.utils.TextAreaOutputStream;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.Locale;

public class GUIManager extends JFrame implements ActionListener {



    public ArrayList<String> inputlist = new ArrayList<>();
    int myinputget = 0;

    public JTextField text1;
    public JTextArea area;
    public ChatClient client;
    public int wave = 0;
    public String email;
    public String pass;
    public String serverip;
    public int port;


    GUIManager (){
        this.client = new ChatClient(this);
        setTitle("MCchatManager");
        setSize(500,400);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowClosing());
        URL url=getClass().getClassLoader().getResource("Icon.png");
        ImageIcon icon=new ImageIcon(url);
        setIconImage(icon.getImage());

        area = new JTextArea();

        area.setBackground(Color.BLACK);
        area.setForeground(Color.WHITE);
        area.setRows(18);
        area.setEditable(false);

        DefaultContextMenu.addDefaultContextMenu(area);


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

        BufferedImage backgroundImg = null;
        try {
            backgroundImg = ImageIO.read(new File(getApplicationPath(MCchatManager.class).getParent().toString()+File.separator+"back.png"));
        } catch (IOException | URISyntaxException ignored) {
        }

        Container contentPane = getContentPane();
        if(backgroundImg != null) {
            Icon backgroundIcon = new ImageIcon(backgroundImg);
            JLabel contentLabel = new JLabel(backgroundIcon);
            contentLabel.setLayout(new BorderLayout());
            area.setOpaque(false);
            scrollpane.setOpaque(false);
            scrollpane.getViewport().setOpaque(false);
            contentLabel.add(scrollpane, BorderLayout.CENTER);
            contentPane.add(contentLabel, BorderLayout.CENTER);
        }else{
            contentPane.add(scrollpane, BorderLayout.CENTER);
        }
        JPanel p = new JPanel();
        p.setOpaque(true);
        text1 = new JTextField("メールアドレスを入力", 20);
        text1.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode() == KeyEvent.VK_ENTER) {
                    String message = text1.getText();
                    if(message.replace(" ","").replace("　","").equalsIgnoreCase("")){
                        return;
                    }
                    inputlist.add(message);
                    myinputget = inputlist.size()-1;
                    if(message.startsWith("!")){
                        String cmd = message.replaceFirst("!","");
                        text1.setText("");
                        if(!EventAPI.executeCommand(cmd)){
                            return;
                        }
                    }
                    if (wave != 3) {
                        if (wave == 0) {
                            email = message;
                            text1.setText("パスワードを入力");
                            System.out.println("パスワードを入力してください");
                            wave++;
                            return;
                        } else if (wave == 1) {
                            pass = message;
                            text1.setText("サーバーIP:ポートを入力");
                            System.out.println("サーバーIP:ポートを入力してください");
                            wave++;
                            return;
                        } else if (wave == 2) {
                            String[] argserver = message.split(":");
                            serverip = argserver[0];
                            if (argserver.length >= 2) {
                                try {
                                    port = Integer.parseInt(argserver[1]);
                                } catch (NumberFormatException ee) {
                                    System.out.println("Error: ポート番号が数字ではありません！");
                                    resetState();
                                    return;
                                }
                            } else {
                                port = 25565;
                            }
                            text1.setText("チャット内容を入力");
                            wave++;
                            try {
                                client.login(email, pass);
                            } catch (RequestException ee) {
                                System.out.println("Error: ログインに失敗しました。emailかpasswordが間違っています。");
                                resetState();
                                return;
                            }
                            client.connect(serverip, port, false);
                            return;
                        }
                    }
                    if (message.equalsIgnoreCase("!server info")) {
                        System.out.println("IP: " + client.getSession().getHost() + " PORT: " + client.getSession().getPort());
                        text1.setText("");
                        return;
                    } else if (message.equalsIgnoreCase("!quit")) {
                        text1.setText("");
                        client.disconnect();
                        return;
                    }
                    EventAPI.sendMessage(message);
                    text1.setText("");
                }else if(e.getKeyCode() == KeyEvent.VK_UP) {
                    if(inputlist.size()==0){
                        return;
                    }
                    text1.setText(inputlist.get(myinputget));
                    if(myinputget != 0) {
                        myinputget--;
                    }
                }else if(e.getKeyCode() == KeyEvent.VK_DOWN) {
                    if(inputlist.size()==0){
                        return;
                    }
                    text1.setText(inputlist.get(myinputget));
                    if(myinputget != inputlist.size()-1) {
                        myinputget++;
                    }
                }else if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
                    if (client.session != null && client.session.isConnected()) {
                        int i = 0;
                        String str = "";
                        for (String p : client.playerlist) {
                            str += p;
                            if (i == 5) {
                                i = 0;
                                System.out.println("PlayerList: " + str);
                                str = "";
                            }
                            i++;
                        }
                        if (!str.equalsIgnoreCase("")) {
                            System.out.println("PlayerList: " + str);
                        }
                    } else {
                        System.out.println("Error: サーバーにjoinしてない時はTABでのリスト表示を行えません");
                    }
                }
            }
        });
        JButton button = new JButton("送信");
        button.addActionListener(this);

        JPanel p2 = new JPanel();
        JButton button1 = new JButton("テンプレLogin");
        button1.addActionListener(new ClickAction2(this));
        JButton button2 = new JButton("テンプレJoin");
        button2.addActionListener(new ClickAction3(this));
        JButton button3 = new JButton("情報reset");
        button3.addActionListener(new ClickAction4(this));
        JButton button4 = new JButton("サーバー離脱");
        button4.addActionListener(new ClickAction5(this));

        p.add(text1);
        p.add(button);

        p2.add(button1);
        p2.add(button2);
        p2.add(button3);
        p2.add(button4);

        JPanel p3 = new JPanel();
        p3.setLayout(new BorderLayout());
        p3.add(p,BorderLayout.CENTER);
        p3.add(p2,BorderLayout.SOUTH);

        contentPane.add(p3, BorderLayout.SOUTH);

        wave = 0;
        setVisible(true);
    }

    public void setTextColor(int r,int g,int b){
        area.setForeground(new Color(r,g,b));
        area.updateUI();
    }



    public void actionPerformed(ActionEvent e){
        String message = text1.getText();
        if(message.replace(" ","").replace("　","").equalsIgnoreCase("")){
            return;
        }
        inputlist.add(message);
        myinputget = inputlist.size()-1;
        if(message.startsWith("!")){
            String cmd = message.replaceFirst("!","");
            text1.setText("");
            if(!EventAPI.executeCommand(cmd)){
                return;
            }
        }
        if (wave != 3) {
            if (wave == 0) {
                email = message;
                text1.setText("パスワードを入力");
                System.out.println("パスワードを入力してください");
                wave++;
                return;
            } else if (wave == 1) {
                pass = message;
                text1.setText("サーバーIP:ポートを入力");
                System.out.println("サーバーIP:ポートを入力してください");
                wave++;
                return;
            } else if (wave == 2) {
                String[] argserver = message.split(":");
                serverip = argserver[0];
                if (argserver.length >= 2) {
                    try {
                        port = Integer.parseInt(argserver[1]);
                    } catch (NumberFormatException ee) {
                        System.out.println("Error: ポート番号が数字ではありません！");
                        resetState();
                        return;
                    }
                } else {
                    port = 25565;
                }
                text1.setText("チャット内容を入力");
                wave++;
                try {
                    client.login(email, pass);
                } catch (RequestException ee) {
                    System.out.println("Error: ログインに失敗しました。emailかpasswordが間違っています。");
                    resetState();
                    return;
                }
                client.connect(serverip, port, false);
                return;
            }
        }
        if (message.equalsIgnoreCase("!server info")) {
            System.out.println("IP: " + client.getSession().getHost() + " PORT: " + client.getSession().getPort());
            text1.setText("");
            return;
        }
        EventAPI.sendMessage(message);
        text1.setText("");
    }

    class ClickAction2 implements ActionListener {

        GUIManager guiManager;
        public ClickAction2(GUIManager guiManager){
            this.guiManager = guiManager;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if(guiManager.client.session == null || !guiManager.client.session.isConnected()) {
                MCchatManager.config.load_login();
            }else{
                System.out.println("Error: サーバーjoin中はloginを行えません");
            }
        }
    }

    class ClickAction3 implements ActionListener {

        GUIManager guiManager;
        public ClickAction3(GUIManager guiManager){
            this.guiManager = guiManager;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if(guiManager.client.session == null || !guiManager.client.session.isConnected()) {
                MCchatManager.config.load_template();
                try {
                    client.login(email, pass);
                } catch (RequestException ee) {
                    System.out.println("Error: ログインに失敗しました。emailかpasswordが間違っています。");
                    resetState();
                    return;
                }
                client.connect(serverip, port, true);
            }else{
                System.out.println("Error: サーバーjoin中はjoinを行えません");
            }
        }
    }

    class ClickAction4 implements ActionListener {

        GUIManager guiManager;
        public ClickAction4(GUIManager guiManager){
            this.guiManager = guiManager;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if(guiManager.client.session == null || !guiManager.client.session.isConnected()) {
                guiManager.resetState();
            }else{
                System.out.println("Error: サーバーjoin中はresetを行えません");
            }
        }
    }

    class ClickAction5 implements ActionListener {

        GUIManager guiManager;
        public ClickAction5(GUIManager guiManager){
            this.guiManager = guiManager;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            if(guiManager.client.session != null && guiManager.client.session.isConnected()) {
                guiManager.text1.setText("");
                client.disconnect();
            }else{
                System.out.println("Error: サーバーにjoinしてない時は離脱を行えません");
            }
        }
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

    public Path getApplicationPath(Class<?> cls) throws URISyntaxException {
        ProtectionDomain pd = cls.getProtectionDomain();
        CodeSource cs = pd.getCodeSource();
        URL location = cs.getLocation();
        URI uri = location.toURI();
        Path path = Paths.get(uri);
        return path;
    }

    class WindowClosing extends WindowAdapter{
        public void windowClosing(WindowEvent e) {
            int ans = JOptionPane.showConfirmDialog(GUIManager.this, "本当に終了しますか?","終了確認",JOptionPane.YES_NO_OPTION);
            if(ans == JOptionPane.YES_OPTION) {
                PluginAPI.unloadPluginAll();
                System.exit(0);
            }
        }
    }

}
