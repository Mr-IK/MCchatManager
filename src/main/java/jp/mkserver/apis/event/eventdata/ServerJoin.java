package jp.mkserver.apis.event.eventdata;


public class ServerJoin{
    private final String email;
    private final String pass;
    private final String serverip;
    private final int port;
    private final boolean success;

    public ServerJoin(String email,String pass,String serverip,int port,boolean success){
        this.email = email;
        this.pass = pass;
        this.serverip = serverip;
        this.port = port;
        this.success =success;
    }

    public boolean isSuccess() {
        return success;
    }

    public int getPort() {
        return port;
    }

    public String getEmail() {
        return email;
    }

    public String getPass() {
        return pass;
    }

    public String getServerip() {
        return serverip;
    }

    @Override
    public String toString() {
        return getEmail()+" "+getPass()+" "+getServerip()+":"+getPort();
    }

}
