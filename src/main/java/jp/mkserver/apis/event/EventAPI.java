package jp.mkserver.apis.event;

import com.github.steveice10.mc.protocol.packet.ingame.client.ClientChatPacket;
import jp.mkserver.MCchatManager;
import jp.mkserver.apis.Message;

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

}
