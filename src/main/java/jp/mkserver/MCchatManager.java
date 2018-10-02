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
        System.out.println("version: 1.2");
        sendUpdateMessage();
        config = new ConfigFileManager(gui);
        System.out.println(" ");
        System.out.println( "プラグインをロード中…" );
        PluginAPI.loadPluginAll();
        System.out.println( "プラグインのロード完了!(ロードプラグイン数: "+PluginAPI.plugins.size()+")" );
        gui.setVisible(true);
        System.out.println(" ");
        System.out.println( "最新バージョンをチェック中…" );
        Updater.enableUpdater();
        System.out.println(" ");
        System.out.println( "メールアドレスを入力してください！" );
        System.out.println( "テンプレートで接続したい場合　テンプレJoinボタンを押してください" );
    }

    public static void sendUpdateMessage(){
        System.out.println(("--------------------------------------"));
        System.out.println(("★ v1.2 アップデート情報！ ★"));
        System.out.println(("* メインGUI追加！"));
        System.out.println(("大変だった…でも使いやすくなったね！"));
        System.out.println(("* アップデート機能追加！"));
        System.out.println(("使いすぎると制限がかかるよ！"));
        System.out.println(("* プラグインGUI追加！"));
        System.out.println(("入っているプラグインのGUIが使える"));
        System.out.println(("* その他もろもろ込み！"));
        System.out.println(("いままでで一番時間のかかったアプデだよ！"));
        System.out.println(("--------------------------------------"));
    }


}
