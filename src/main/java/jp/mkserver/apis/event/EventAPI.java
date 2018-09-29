package jp.mkserver.apis.event;

import com.github.steveice10.mc.protocol.packet.ingame.client.ClientChatPacket;
import com.github.steveice10.packetlib.packet.Packet;
import jp.mkserver.MCchatManager;
import jp.mkserver.apis.event.eventdata.Command;
import jp.mkserver.apis.event.eventdata.Message;
import jp.mkserver.apis.event.eventdata.ServerJoin;

import java.util.ArrayList;

public class EventAPI {

    private static ArrayList<EventManager> listeners = new ArrayList<>();

    public static void addListener(EventManager event){
        listeners.add(event);
    }

    public static void removeListener(EventManager event){
        listeners.remove(event);
    }

    public static void sendMessage(String message){
        Message msg = new Message(message);
        for(EventManager event : listeners){
            event.onSendMessage(msg);
        }
        if(!msg.isCancelled()){
            MCchatManager.gui.client.getSession().send(new ClientChatPacket(msg.getMessage()));
        }
    }

    public static boolean executeCommand(String cmd){
        Command cmds = new Command(cmd);
        for(EventManager event : listeners){
            event.onCommand(cmds);
        }
        if(cmds.isCancelled()){
            return false;
        }
        return true;
    }

    public static String viewMessage(String message){
        Message msg = new Message(message);
        for(EventManager event : listeners){
            event.onMessageReceive(msg);
        }
        if(!msg.isCancelled()){
            return msg.getMessage();
        }
        return null;
    }

    public static void receivePacket(Packet packet){
        for(EventManager event : listeners){
            event.onPacketReceive(packet);
        }
    }

    public static void serverJoin(String email,String pass,String serverip,int port,boolean success){
        ServerJoin s = new ServerJoin(email,pass,serverip,port,success);
        for(EventManager event : listeners){
            event.onServerJoin(s);
        }
    }

}
