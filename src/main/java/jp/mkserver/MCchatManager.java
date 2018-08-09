package jp.mkserver;



import com.github.steveice10.mc.auth.data.GameProfile;
import com.github.steveice10.mc.auth.exception.request.RequestException;
import com.github.steveice10.mc.protocol.MinecraftConstants;
import com.github.steveice10.mc.protocol.MinecraftProtocol;
import com.github.steveice10.mc.protocol.data.message.Message;
import com.github.steveice10.mc.protocol.data.message.TranslationMessage;
import com.github.steveice10.mc.protocol.packet.ingame.client.ClientChatPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerChatPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerJoinGamePacket;
import com.github.steveice10.packetlib.Client;
import com.github.steveice10.packetlib.event.session.DisconnectedEvent;
import com.github.steveice10.packetlib.event.session.PacketReceivedEvent;
import com.github.steveice10.packetlib.event.session.SessionAdapter;
import com.github.steveice10.packetlib.tcp.TcpSessionFactory;

import java.net.Proxy;
import java.util.Arrays;
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

    public static void main(String[] args) {
        System.out.println("Enabled MC chat Manager!");
        System.out.println("creator: Mr_IK");
        System.out.println("version: 1.0-SNAPSHOT");
        if (args.length != 4) {
            System.out.println("OMG args is not 4!! 1 = server_ip 2 = port 3 = username 4 = password plz!!");
            return;
        }
        ip = args[0];
        try{
            port = Integer.parseInt(args[1]);
        }catch (NumberFormatException e){
            System.out.println("OMG arg2 is not Number!! plz type Number!!");
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



}
