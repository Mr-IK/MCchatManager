package jp.mkserver.apis.event;

import com.github.steveice10.packetlib.packet.Packet;
import jp.mkserver.apis.event.eventdata.Command;
import jp.mkserver.apis.event.eventdata.Message;
import jp.mkserver.apis.event.eventdata.ServerJoin;

import java.util.EventListener;

public interface EventManager extends EventListener {

    default void onMessageReceive(Message m){}

    default void onSendMessage(Message m){}

    default void onCommand(Command c){}

    default void onServerJoin(ServerJoin s){}

    default void onPacketReceive(Packet p){}

}
