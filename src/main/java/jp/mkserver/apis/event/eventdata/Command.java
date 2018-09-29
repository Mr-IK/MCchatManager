package jp.mkserver.apis.event.eventdata;

import jp.mkserver.apis.event.Cancellble;

public class Command implements Cancellble {
    private boolean canceled;
    private String cmd;

    public Command(String cmd){
        this.cmd = cmd;
    }

    public String getCmd() {
        return cmd;
    }


    @Override
    public void setCancelled(boolean cancel){
        canceled = cancel;
    }


    @Override
    public String toString() {
        return "『"+cmd+"』 "+canceled;
    }

    @Override
    public boolean isCancelled() {
        return canceled;
    }
}

