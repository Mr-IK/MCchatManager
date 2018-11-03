package jp.mkserver;

import com.github.steveice10.mc.auth.exception.request.RequestException;
import com.github.steveice10.mc.protocol.packet.ingame.client.ClientChatPacket;
import jp.mkserver.utils.DefaultContextMenu;
import jp.mkserver.utils.SettingSaver;
import jp.mkserver.utils.SoundSetting;
import jp.mkserver.utils.TextAreaOutputStream;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;
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

    public JPanel mainPanel;

    ////////////////ChatGUI//////
    JPanel p4;
    private final CardLayout layout = new CardLayout();
    /////////////////////////////

    ///////////////SettingsGUI///
    public SettingGUI setgui;
    /////////////////////////////

    public JTextField text1;
    public JTextArea area;
    public ChatClient client;
    public MainGUI maingui;
    public WindowClosing windowClosing;
    public int wave = 0;
    public String email;
    public String pass;
    public String serverip;
    public int port;

    public void setviewChat(){
        this.setResizable(true);
        layout.show(mainPanel,p4.getName());
    }

    public void setviewMain(){
        setSize(600,400);
        this.setResizable(false);
        layout.show(mainPanel,maingui.getName());
    }

    public void setviewSettings(){
        setSize(600,400);
        this.setResizable(false);
        layout.show(mainPanel,setgui.getName());
    }


    GUIManager (){
        this.client = new ChatClient(this);
        setTitle("MCchatManager");
        setSize(600,400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        windowClosing = new WindowClosing();
        addWindowListener(windowClosing);
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

        JLabel content = null;
        if(backgroundImg != null) {
            Icon backgroundIcon = new ImageIcon(backgroundImg);
            JLabel contentLabel = new JLabel(backgroundIcon);
            contentLabel.setLayout(new BorderLayout());
            area.setOpaque(false);
            scrollpane.setOpaque(false);
            scrollpane.getViewport().setOpaque(false);
            contentLabel.add(scrollpane, BorderLayout.CENTER);
            content = contentLabel;
        }
        JPanel p = new JPanel();
        p.setOpaque(true);
        text1 = new JTextField("", 20);
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
                    if (wave != 3) {
                        if (wave == 0) {
                            email = message;
                            text1.setText("");
                            System.out.println("パスワードを入力してください");
                            wave++;
                            return;
                        } else if (wave == 1) {
                            pass = message;
                            text1.setText("");
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
                            text1.setText("");
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
                    client.getSession().send(new ClientChatPacket(message));
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
        JButton button5 = new JButton("メインメニュー");
        button5.addActionListener(new ClickAction6(this));

        p.add(text1);
        p.add(button);

        p2.add(button1);
        p2.add(button2);
        p2.add(button3);
        p2.add(button4);
        p2.add(button5);

        JPanel p3 = new JPanel();
        p3.setLayout(new BorderLayout());
        p3.add(p,BorderLayout.CENTER);
        p3.add(p2,BorderLayout.SOUTH);

        wave = 0;
        maingui = new MainGUI(this);

        setgui = new SettingGUI(maingui);

        p4 = new JPanel();
        p4.setOpaque(false);
        p4.setLayout(new BorderLayout());
        if(content != null) {
            p4.add(content, BorderLayout.CENTER);
        }else{
            p4.add(scrollpane, BorderLayout.CENTER);
        }
        p4.add(p3,BorderLayout.SOUTH);
        p4.setName("ChatGUI");

        Container contentPane = getContentPane();

        mainPanel = new JPanel();
        mainPanel.setLayout(layout);

        mainPanel.add(maingui,maingui.getName());
        mainPanel.add(p4,p4.getName());
        mainPanel.add(setgui,setgui.getName());

        contentPane.add(mainPanel);

        SoundSetting sound = SettingSaver.loadSound();
        if(sound!=null){
            vold = sound.getVold();
            vol = sound.getVol();
            loop = sound.isOn();
        }
    }

    public void setTextColor(int r,int g,int b){
        area.setForeground(new Color(r,g,b));
        area.updateUI();
    }

    public void addWindowclose(){
        addWindowListener(windowClosing);
    }

    public void removeWindowclose(){
        removeWindowListener(windowClosing);
    }


    public void actionPerformed(ActionEvent e){
        String message = text1.getText();
        if(message.replace(" ","").replace("　","").equalsIgnoreCase("")){
            return;
        }
        playClickSound();
        inputlist.add(message);
        myinputget = inputlist.size()-1;
        if (wave != 3) {
            if (wave == 0) {
                email = message;
                text1.setText("");
                System.out.println("パスワードを入力してください");
                wave++;
                return;
            } else if (wave == 1) {
                pass = message;
                text1.setText("");
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
                text1.setText("");
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
        client.getSession().send(new ClientChatPacket(message));
        text1.setText("");
    }

    class ClickAction2 implements ActionListener {

        GUIManager guiManager;
        public ClickAction2(GUIManager guiManager){
            this.guiManager = guiManager;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            guiManager.playClickSound();
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
            guiManager.playClickSound();
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
            guiManager.playClickSound();
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
            guiManager.playClickSound();
            if(guiManager.client.session != null && guiManager.client.session.isConnected()) {
                guiManager.text1.setText("");
                client.disconnect();
            }else{
                System.out.println("Error: サーバーにjoinしてない時は離脱を行えません");
            }
        }
    }

    class ClickAction6 implements ActionListener {

        GUIManager guiManager;
        public ClickAction6(GUIManager guiManager){
            this.guiManager = guiManager;
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            guiManager.playClickSound();
            if(guiManager.client.session != null && guiManager.client.session.isConnected()) {
                guiManager.text1.setText("");
                client.disconnect();
            }
            guiManager.setviewMain();
        }
    }


    public void resetState(){
        wave = 0;
        email = null;
        pass = null;
        serverip = null;
        port = 25565;
        text1.setText("");
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
                SettingSaver.saveSound(vol,loop,vold);
                System.exit(0);
            }
        }
    }

    public synchronized void playClickSound() {
        new Thread(() -> {
            File file = new File("sounds"+File.separator+"click.wav");
            if(!file.exists()){
                System.out.println("[Error]Sounds/click.wavが存在しません！");
                return;
            }
            try (AudioInputStream ais = AudioSystem.getAudioInputStream(file)) {

                //ファイルの形式取得
                AudioFormat af = ais.getFormat();

                //単一のオーディオ形式を含む指定した情報からデータラインの情報オブジェクトを構築
                DataLine.Info dataLine = new DataLine.Info(SourceDataLine.class,af);

                //指定された Line.Info オブジェクトの記述に一致するラインを取得
                SourceDataLine s = (SourceDataLine)AudioSystem.getLine(dataLine);

                //再生準備完了
                s.open();

                //ラインの処理を開始
                s.start();

                //読み込みサイズ
                byte[] data = new byte[s.getBufferSize()];

                //読み込んだサイズ
                int size = -1;

                //再生処理のループ
                while(true) {
                    //オーディオデータの読み込み
                    size = ais.read(data);
                    if ( size == -1 ) {
                        //すべて読み込んだら終了
                        break;
                    }
                    //ラインにオーディオデータの書き込み
                    s.write(data, 0, size);
                }

                //残ったバッファをすべて再生するまで待つ
                s.drain();

                //ライン停止
                s.stop();

                //リソース解放
                s.close();

            } catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
                e.printStackTrace();
            }
        }).start();
    }

    private SourceDataLine bgm;
    private boolean loop = true;
    private float vol = 0.5f;
    private double vold = 1.0;

    public void playBGM() {
        new Thread(() -> {
            File file = new File("sounds" + File.separator + "bgm.wav");
            if (!file.exists()) {
                System.out.println("[Error]Sounds/bgm.wavが存在しません！");
                return;
            }
            if (bgm != null && bgm.isOpen()) {
                //ライン停止
                bgm.stop();

                //リソース解放
                bgm.close();
            }
            try {

                AudioInputStream ais = AudioSystem.getAudioInputStream(file);
                //ファイルの形式取得
                AudioFormat af = ais.getFormat();

                //単一のオーディオ形式を含む指定した情報からデータラインの情報オブジェクトを構築
                DataLine.Info dataLine = new DataLine.Info(SourceDataLine.class, af);

                //指定された Line.Info オブジェクトの記述に一致するラインを取得
                bgm = (SourceDataLine) AudioSystem.getLine(dataLine);

                //再生準備完了
                bgm.open();

                setBGMVol();

                //ラインの処理を開始
                bgm.start();

                //読み込みサイズ
                byte[] data = new byte[bgm.getBufferSize()];

                //読み込んだサイズ
                int size = -1;

                while (true) {
                    //再生処理のループ
                    while (true) {
                        //オーディオデータの読み込み
                        size = ais.read(data);
                        if (size == -1) {
                            //すべて読み込んだら終了
                            break;
                        }
                        //ラインにオーディオデータの書き込み
                        bgm.write(data, 0, size);
                    }
                    //残ったバッファをすべて再生するまで待つ
                    bgm.drain();
                    //ライン停止
                    bgm.stop();
                    //リソース解放
                    bgm.close();
                    if(!loop){
                        break;
                    }
                    ais = AudioSystem.getAudioInputStream(file);
                    //単一のオーディオ形式を含む指定した情報からデータラインの情報オブジェクトを構築
                    dataLine = new DataLine.Info(SourceDataLine.class, af);
                    //指定された Line.Info オブジェクトの記述に一致するラインを取得
                    bgm = (SourceDataLine) AudioSystem.getLine(dataLine);
                    //再生準備完了
                    bgm.open();
                    setBGMVol();
                    //ラインの処理を開始
                    bgm.start();
                    data = new byte[bgm.getBufferSize()];
                    size = -1;
                }

            } catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void setBGMVol(double i){
        FloatControl ctrl = (FloatControl)bgm.getControl(FloatControl.Type.MASTER_GAIN);
        ctrl.setValue((float)Math.log10((float)i / 20)*20);
        vol = (float)Math.log10((float)i / 20)*20;
        vold = i;
    }

    public void setBGMVol(float i){
        FloatControl ctrl = (FloatControl)bgm.getControl(FloatControl.Type.MASTER_GAIN);
        ctrl.setValue(i);
    }
    public void setBGMVol(){
        setBGMVol(vold);
    }

    public void setBGMloop(boolean loop){
        this.loop = loop;
    }

    public void stopBGM(){
        bgm.stop();
        bgm.close();
    }

}
