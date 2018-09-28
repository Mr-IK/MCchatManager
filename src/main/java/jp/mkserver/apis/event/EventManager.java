package jp.mkserver.apis.event;

import jp.mkserver.apis.Message;

import java.util.EventListener;

public interface EventManager extends EventListener {

    void onMessageReceive(Message m);

    void onSendMessage(Message m);

}
