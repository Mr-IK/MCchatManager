package jp.mkserver;


import jp.mkserver.utils.ConfigFileManager;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.CodeSource;
import java.security.ProtectionDomain;

public class MCchatManager {


    static boolean power = false;
    static ConfigFileManager config;

    public static void main(String[] arg) {
        GUIManager gui = new GUIManager();
        System.out.println("Enabled MC chat Manager!");
        System.out.println("creator: Mr_IK");
        System.out.println("version: 0.6");
        sendUpdateMessage();
        config = new ConfigFileManager(gui);
        System.out.println( "メールアドレスを入力してください！" );
        System.out.println( "テンプレートで接続したい場合　テンプレJoinボタンを押してください" );
    }

    public static void sendUpdateMessage(){
        System.out.println(("--------------------------------------"));
        System.out.println(("★ v0.6 アップデート情報！ ★"));
        System.out.println(("* プレイヤーリストかもーんぬ"));
        System.out.println(("tabキーでプレイヤーリストが出てくるようになった"));
        System.out.println(("* アップ↑ダウン↓"));
        System.out.println(("↑↓キーで過去のチャット・コマンドを呼べるぞ！"));
        System.out.println(("--------------------------------------"));
    }

    public static Path getApplicationPath(Class<?> cls) throws URISyntaxException {
        ProtectionDomain pd = cls.getProtectionDomain();
        CodeSource cs = pd.getCodeSource();
        URL location = cs.getLocation();
        URI uri = location.toURI();
        Path path = Paths.get(uri);
        return path;
    }


}
