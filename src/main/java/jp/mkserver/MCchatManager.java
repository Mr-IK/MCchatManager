package jp.mkserver;


import jp.mkserver.apis.PluginAPI;
import jp.mkserver.utils.ConfigFileManager;

public class MCchatManager implements Runnable {


    static boolean power = false;
    public static ConfigFileManager config;
    public static GUIManager gui;

    public static void main(String[] arg) {
        Runtime.getRuntime().addShutdownHook(new Thread(new MCchatManager(), "Shutdown"));
        gui = new GUIManager();
        System.out.println("Enabled MC chat Manager!");
        System.out.println("creator: Mr_IK");
        System.out.println("version: 1.0");
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
        System.out.println(("★ v1.0 アップデート情報！ ★"));
        System.out.println(("* プラグイン作成!"));
        System.out.println(("プラグインを作れるようになった！"));
        System.out.println(("* 文字の色設定！"));
        System.out.println(("configで設定できる！"));
        System.out.println(("--------------------------------------"));
    }

    public void run(){
        PluginAPI.unloadPluginAll();
    }


}
