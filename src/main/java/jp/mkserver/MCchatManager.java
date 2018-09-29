package jp.mkserver;


import jp.mkserver.apis.PluginAPI;
import jp.mkserver.utils.ConfigFileManager;

public class MCchatManager {


    static boolean power = false;
    public static ConfigFileManager config;
    public static GUIManager gui;

    public static void main(String[] arg) {
        gui = new GUIManager();
        System.out.println("Enabled MC chat Manager!");
        System.out.println("creator: Mr_IK");
        System.out.println("version: 1.1");
        sendUpdateMessage();
        config = new ConfigFileManager(gui);
        System.out.println(" ");
        System.out.println( "プラグインをロード中…" );
        PluginAPI.loadPluginAll();
        System.out.println( "プラグインのロード完了!(ロードプラグイン数: "+PluginAPI.plugins.size()+")" );
        System.out.println(" ");
        System.out.println( "メールアドレスを入力してください！" );
        System.out.println( "テンプレートで接続したい場合　テンプレJoinボタンを押してください" );
    }

    public static void sendUpdateMessage(){
        System.out.println(("--------------------------------------"));
        System.out.println(("★ v1.1 アップデート情報！ ★"));
        System.out.println(("* プラグイン機能アップデート!"));
        System.out.println(("さらに沢山の機能を追加・修正！"));
        System.out.println(("* !endくんさらば！"));
        System.out.println(("もうXを押せばいいよね！"));
        System.out.println(("--------------------------------------"));
    }


}
