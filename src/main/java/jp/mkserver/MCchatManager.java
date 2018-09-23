package jp.mkserver;


import jp.mkserver.utils.ConfigFileManager;

public class MCchatManager {


    static boolean power = false;
    static ConfigFileManager config;

    public static void main(String[] arg) {
        GUIManager gui = new GUIManager();
        System.out.println("Enabled MC chat Manager!");
        System.out.println("creator: Mr_IK");
        System.out.println("version: 0.4");
        sendUpdateMessage();
        config = new ConfigFileManager(gui);
        System.out.println( "メールアドレスを入力してください！" );
        System.out.println( "テンプレートで接続したい場合 !template と送信してください" );
    }

    public static void sendUpdateMessage(){
        System.out.println(("--------------------------------------"));
        System.out.println(("★ v0.4 アップデート情報！ ★"));
        System.out.println(("* しゅうせいおわり！"));
        System.out.println(("各種バグを修正！"));
        System.out.println(("* コンフィグさまの お通りだ！"));
        System.out.println(("config.txtが生成されるようになったぞ"));
        System.out.println(("* ログファイル生成技術"));
        System.out.println(("ログを記録するようになったよ"));
        System.out.println(("--------------------------------------"));
    }

    public static void sendHelp(){
        System.out.println("--------------------------------------");
        System.out.println("★ MCchatManager v0.4 HELP！ ★");
        System.out.println("ログの指示に従って情報を入力してください！");
        System.out.println("--------------------------------------");
    }



}
