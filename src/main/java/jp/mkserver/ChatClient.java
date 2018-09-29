package jp.mkserver;


import com.github.steveice10.mc.auth.exception.request.RequestException;
import com.github.steveice10.mc.protocol.MinecraftProtocol;
import com.github.steveice10.mc.protocol.data.game.PlayerListEntry;
import com.github.steveice10.mc.protocol.data.message.*;
import com.github.steveice10.mc.protocol.packet.ingame.client.ClientChatPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerChatPacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerJoinGamePacket;
import com.github.steveice10.mc.protocol.packet.ingame.server.ServerPlayerListEntryPacket;
import com.github.steveice10.packetlib.Client;
import com.github.steveice10.packetlib.Session;
import com.github.steveice10.packetlib.event.session.DisconnectedEvent;
import com.github.steveice10.packetlib.event.session.PacketReceivedEvent;
import com.github.steveice10.packetlib.event.session.SessionAdapter;
import com.github.steveice10.packetlib.tcp.TcpSessionFactory;
import com.google.gson.*;
import jp.mkserver.apis.event.EventAPI;
import jp.mkserver.utils.ChatColour;
import org.apache.commons.lang.StringEscapeUtils;


import java.util.ArrayList;
import java.util.List;


public class ChatClient {

    enum Option{
        Template
    }

    private String USER;
    private String PASS;
    private String  HOST;
    private int     PORT;
    public Session session;
    private Client client;
    MinecraftProtocol protocol;
    private boolean connected = false;
    GUIManager gui;
    public ArrayList<String> playerlist = new ArrayList<>();

    public ChatClient(GUIManager gui){
        this.gui = gui;
    }

    public void login( String username, String password) throws RequestException {
        System.out.println( "ログイン中..." );
        protocol = new MinecraftProtocol( username, password, false );
        System.out.println( "ログイン成功!" );
        USER = username;
        PASS = password;
    }

    public void connect( String host, int port,boolean template) {
        if(session != null && session.isConnected()) {
            disconnect();
            if(template){
                MCchatManager.config.load_template();
            }else{
                gui.email  = USER;
                gui.pass = PASS;
            }
        }
        HOST = host;
        PORT = port;
        gui.wave = 3;
        gui.text1.setText("チャットしたい内容を入力");
        System.out.println(HOST +"に接続中 …");
        client = new Client( HOST, PORT, protocol, new TcpSessionFactory() );
        client.getSession().addListener(new SessionAdapter() {
            @Override
            public void packetReceived(PacketReceivedEvent event) {
                if(event.getPacket() instanceof ServerJoinGamePacket) {
                    event.getSession().send(new ClientChatPacket("MCchatManagerで接続しました！"));
                } else if(event.getPacket() instanceof ServerChatPacket) {
                    Message message = event.<ServerChatPacket>getPacket().getMessage();
                    handleChat(message);
                } else if(event.getPacket() instanceof ServerPlayerListEntryPacket){
                    for(PlayerListEntry ple : ((ServerPlayerListEntryPacket) event.getPacket() ).getEntries()){
                        switch(((ServerPlayerListEntryPacket) event.getPacket() ).getAction()){
                            case ADD_PLAYER:
                                playerlist.add(ple.getProfile().getName());
                                break;
                            case REMOVE_PLAYER:
                                playerlist.remove(ple.getProfile().getName());
                                break;
                            case UPDATE_DISPLAY_NAME:
                                break;
                            case UPDATE_GAMEMODE:
                                break;
                            case UPDATE_LATENCY:
                                break;
                        }
                    }
                }else{
                    EventAPI.receivePacket(event.getPacket());
                }
            }

            @Override
            public void disconnected(DisconnectedEvent event) {
                System.out.println("Disconnected: " + Message.fromString(event.getReason()).getFullText());
                if(event.getCause() != null) {
                    event.getCause().printStackTrace();
                }
                System.out.println("接続が切断されました。");
                gui.resetState();
            }
        });
        session = client.getSession();
        session.connect();
        if(session.isConnected()) {
            System.out.println("接続しました！");
            connected = true;
            MCchatManager.power = true;
        }else{
            System.out.println("接続に失敗しました…");
            connected = false;
        }
        EventAPI.serverJoin(USER,PASS,HOST,PORT,connected);
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

    public void end() {
        disconnect( "Quit" );
        gui.destroy();
        System.out.println("Quit succeeded!");
        System.exit(0);
    }

    public void disconnect() {
        disconnect( "Quit" );
        System.out.println("Quit succeeded!");
    }

    public void disconnect(String reason) {
        session.disconnect( reason );
    }


    private void handleChat( Message mes )
    {
        if ( mes == null )
        {
            return;
        }
        String message = "";

        MessageStyle messageType = mes.getStyle();
        messageType.clearFormats();
        messageType.setClickEvent(null);
        messageType.setHoverEvent(null);
        messageType.setInsertion(null);
        mes.setStyle(messageType);

        List<Message> subMessages = mes.getExtra();
        for ( Message m : subMessages )
        {
            if ( m == null || m.toJson() == null )
            {
                continue;
            }
            if ( m.getStyle().getColor() != null )
            {
                ChatColor colour = m.getStyle().getColor();
                message += getColourCode( colour.toString() );
            }
            if ( m.getStyle().getFormats().size() > 0 )
            {
                for ( ChatFormat format : m.getStyle().getFormats() )
                {
                    message += getColourCode( format.toString() );
                }
            }
            message += m.getText();
        }
        message = StringEscapeUtils.unescapeJava( message );
        if(message.equals("")){
            message = mes.getFullText();
        }
        String msg = message;
        if(isJSONValid(msg)) {
            JsonParser parser = new JsonParser();
            JsonElement json = parser.parse(msg);
            if(json.isJsonPrimitive()) {
                msg = json.getAsString();
            } else if(json.isJsonObject()) {
                JsonObject jsono = json.getAsJsonObject();
                msg = jsontoStringMC(jsono);
            }
        }
        //TODO: onMessageReceivedEvent(MessageReceivedEvent event);
        msg = msg + "§r";
        System.out.println( msg );
    }

    private static final Gson gson = new Gson();

    public boolean isJSONValid(String jsonInString) {
        try {
            gson.fromJson(jsonInString, Object.class);
            return true;
        } catch(com.google.gson.JsonSyntaxException ex) {
            return false;
        }
    }

    public String jsontoStringMC(JsonObject jsono){
        String msg = "";
        if(jsono.has("extra")) {
            JsonArray array = jsono.get("extra").getAsJsonArray();

            for (int i = 0; i < array.size(); i++) {
                JsonElement el = array.get(i);
                if (!el.isJsonPrimitive()) {
                    msg += jsontoStringMC(el.getAsJsonObject());
                    if (el.getAsJsonObject().has("text")) {
                        msg += el.getAsJsonObject().get("text").getAsString();
                    }
                }
            }
        }
        if (jsono.has("text")) {
            msg += jsono.get("text").getAsString();
        }
        return msg;
    }


    public static String getColourCode( String name )
    {
        return ChatColour.valueOf( name.toUpperCase() ).getCode();
    }

}