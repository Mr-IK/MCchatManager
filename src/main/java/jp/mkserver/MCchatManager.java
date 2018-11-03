package jp.mkserver;


import jp.mkserver.utils.ConfigFileManager;

public class MCchatManager {


    static boolean power = false;
    public static ConfigFileManager config;
    public static GUIManager gui;

    public static void main(String[] arg) {
        gui = new GUIManager();
        System.out.println("Enabled MC chat Manager!");
        System.out.println("creator: Mr_IK");
        System.out.println("version: 1.3");
        sendUpdateMessage();
        config = new ConfigFileManager(gui);
        System.out.println(" ");
        gui.setVisible(true);
        gui.playBGM();
        System.out.println(" ");
        System.out.println( "最新バージョンをチェック中…" );
        Updater.enableUpdater();
        System.out.println(" ");
        System.out.println( "メールアドレスを入力してください！" );
        System.out.println( "テンプレートで接続したい場合　テンプレJoinボタンを押してください" );
    }

    public static void sendUpdateMessage(){
        System.out.println(("--------------------------------------"));
        System.out.println(("★ v1.3 アップデート情報！ ★"));
        System.out.println(("* 音の世界へようこそ！"));
        System.out.println(("クリック音などが搭載！"));
        System.out.println(("--------------------------------------"));
    }


}
