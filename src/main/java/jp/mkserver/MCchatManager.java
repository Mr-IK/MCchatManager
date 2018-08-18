package jp.mkserver;

import com.github.steveice10.mc.auth.exception.request.RequestException;
import com.github.steveice10.packetlib.Client;

import java.net.Proxy;
import java.util.Scanner;

public class MCchatManager {

    static String ip = "localhost";
    static int port = 25565;
    private static final boolean VERIFY_USERS = false;
    static String username = "User";
    static String password = "Password";
    static Client chatclient = null;
    private static final Proxy PROXY = Proxy.NO_PROXY;
    private static final Proxy AUTH_PROXY = Proxy.NO_PROXY;
    static boolean power = false;

    public static void main(String[] arg) {
        System.out.println("Enabled MC chat Manager!");
        System.out.println("creator: Mr_IK");
        System.out.println("version: 1.0-SNAPSHOT");
        sendUpdateMessage();
        System.out.println("サーバーipを入力してください！");
        Scanner scan = new Scanner(System.in);
        String[] args = new String[4];
        args[0] = scan.next();
        System.out.println("ポートを入力してください！");
        args[1] = scan.next();
        System.out.println("ユーザー名(メールアドレス)を入力してください！");
        args[2] = scan.next();
        System.out.println("パスワードを入力してください！");
        args[3] = scan.next();
        ip = args[0];
        try{
            port = Integer.parseInt(args[1]);
        }catch (NumberFormatException e){
            System.out.println("おっと！ポートが数字じゃないね！ 次は数字で入力してね！");
            return;
        }
        username = args[2];
        password = args[3];
        ChatClient client = new ChatClient();
        try {
            client.login(username,password);
        } catch (RequestException e) {
            e.printStackTrace();
            return;
        }
        client.connect(ip,port);
    }

    public static void sendUpdateMessage(){
        System.out.println("--------------------------------------");
        System.out.println("★ v0.1 アップデート情報！ ★");
        System.out.println("* ついに 公開だ！");
        System.out.println("MCchatManagerをベータ版公開したよ！");
        System.out.println("* バグは報告してね！");
        System.out.println("Discord Mr_IK#4782 に報告お願いします！");
        System.out.println("--------------------------------------");
    }



}
