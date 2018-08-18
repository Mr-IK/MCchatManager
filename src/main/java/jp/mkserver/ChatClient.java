package jp.mkserver;


import com.github.steveice10.mc.auth.exception.request.RequestException;
import com.github.steveice10.mc.protocol.MinecraftProtocol;
import com.github.steveice10.mc.protocol.data.message.Message;
import com.github.steveice10.mc.protocol.data.message.TranslationMessage;
import com.github.steveice10.mc.protocol.packet.ingame.client.ClientChatPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerChatPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerJoinGamePacket;
import com.github.steveice10.packetlib.Client;
import com.github.steveice10.packetlib.Session;
import com.github.steveice10.packetlib.event.session.DisconnectedEvent;
import com.github.steveice10.packetlib.event.session.PacketReceivedEvent;
import com.github.steveice10.packetlib.event.session.SessionAdapter;
import com.github.steveice10.packetlib.tcp.TcpSessionFactory;


public class ChatClient {

    private String  HOST;
    private int     PORT;
    private Session session;
    private Client client;
    MinecraftProtocol protocol;
    private boolean connected = false;
    GUIManager gui;

    public void login( String username, String password ) throws RequestException
    {
        System.out.println( "Logging in ..." );
        protocol = new MinecraftProtocol( username, password, false );
        System.out.println( "Login success!" );
    }

    public void connect( String host, int port) {
        HOST = host;
        PORT = port;
        System.out.println("Connecting to "+HOST +" …");
        client = new Client( HOST, PORT, protocol, new TcpSessionFactory() );
        client.getSession().addListener(new SessionAdapter() {
            @Override
            public void packetReceived(PacketReceivedEvent event) {
                if(event.getPacket() instanceof ServerJoinGamePacket) {
                    event.getSession().send(new ClientChatPacket("Connected with MCchatManager!"));
                } else if(event.getPacket() instanceof ServerChatPacket) {
                    Message message = event.<ServerChatPacket>getPacket().getMessage();
                    if(message instanceof TranslationMessage) {
                        System.out.println(repColor(((TranslationMessage) message).getTranslationKey()));
                    }else{
                        System.out.println(repColor(message.getFullText()));
                    }
                }
            }

            @Override
            public void disconnected(DisconnectedEvent event) {
                System.out.println("Disconnected: " + Message.fromString(event.getReason()).getFullText());
                if(event.getCause() != null) {
                    event.getCause().printStackTrace();
                }
            }
        });
        session = client.getSession();
        session.connect();
        System.out.println( "Connection succeeded!" );
        connected = true;
        MCchatManager.power = true;
        gui = new GUIManager(this);
    }

    public int getPORT() {
        return PORT;
    }

    public String getHOST() {
        return HOST;
    }

    public Session getSession() {
        return session;
    }

    public Client getClient() {
        return client;
    }

    public boolean isConnected() {
        return connected;
    }

    public void disconnect() {
        disconnect( "Quit" );
        gui.destroy();
        System.out.println("Quit succeeded!");
        System.exit(0);
    }

    public void disconnect(String reason) {
        session.disconnect( reason );
    }

    public String repColor(String msg){
        return msg.replaceAll("§1","")
                .replaceAll("§2","").replaceAll("§3","")
                .replaceAll("§4","").replaceAll("§5","")
                .replaceAll("§6","").replaceAll("§7","")
                .replaceAll("§8","").replaceAll("§9","")
                .replaceAll("§0","").replaceAll("§l","")
                .replaceAll("§m","").replaceAll("§n","")
                .replaceAll("§o","").replaceAll("§a","")
                .replaceAll("§b","").replaceAll("§c","")
                .replaceAll("§d","").replaceAll("§e","")
                .replaceAll("§f","").replaceAll("§r","");
    }

}