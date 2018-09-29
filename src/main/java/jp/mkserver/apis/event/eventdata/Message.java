package jp.mkserver.apis.event.eventdata;

import jp.mkserver.apis.event.Cancellble;

public class Message implements Cancellble {
    private boolean canceled;
    private String message;

    public Message(String message){
        this.message = message;
    }

    public String getMessage() {
        return message;
    }


    @Override
    public void setCancelled(boolean cancel){
        canceled = cancel;
    }


    @Override
    public String toString() {
        return "『"+message+"』 "+canceled;
    }

    @Override
    public boolean isCancelled() {
        return canceled;
    }
}
