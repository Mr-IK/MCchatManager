package jp.mkserver.apis;

import com.github.steveice10.mc.auth.exception.request.RequestException;
import com.github.steveice10.mc.protocol.packet.ingame.client.ClientChatPacket;
import jp.mkserver.*;
import jp.mkserver.utils.ConfigFileManager;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.CodeSource;
import java.security.ProtectionDomain;

public class MCMAPI {

    public static GUIManager getGUIManager(){
        return MCchatManager.gui;
    }

    public static ChatClient getChatClient(){
        return MCchatManager.gui.client;
    }

    public static MainGUI getMainGUI(){
        return MCchatManager.gui.maingui;
    }

    public static PluginsGUI getPluginsGUI(){
        return MCchatManager.gui.plgui;
    }

    public static ConfigFileManager getConfigManager(){
        return MCchatManager.config;
    }

    public static void sendLog(String msg){
        System.out.println(msg);
    }

    public static void sendChat(String msg){
        MCchatManager.gui.client.getSession().send(new ClientChatPacket(msg));
    }

    public static boolean connectServer(String email,String pass,String serverip,int port){
        disconnectServer();
        try {
            getChatClient().login(email, pass);
        } catch (RequestException ee) {
            getGUIManager().resetState();
            return false;
        }
        getGUIManager().email = email;
        getGUIManager().pass = pass;
        getGUIManager().serverip = serverip;
        getGUIManager().port = port;
        getChatClient().connect(serverip, port, false);
        return true;
    }

    public static void disconnectServer(){
        if(getGUIManager().client.session != null && getGUIManager().client.session.isConnected()) {
            getChatClient().disconnect();
        }
    }

    public static Path getApplicationPath(Class<?> cls) throws URISyntaxException {
        ProtectionDomain pd = cls.getProtectionDomain();
        CodeSource cs = pd.getCodeSource();
        URL location = cs.getLocation();
        URI uri = location.toURI();
        return Paths.get(uri);
    }
}
