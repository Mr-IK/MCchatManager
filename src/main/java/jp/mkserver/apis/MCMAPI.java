package jp.mkserver.apis;

import com.github.steveice10.mc.protocol.packet.ingame.client.ClientChatPacket;
import jp.mkserver.GUIManager;
import jp.mkserver.MCchatManager;
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

    public static ConfigFileManager getConfigManager(){
        return MCchatManager.config;
    }

    public static void sendLog(String msg){
        System.out.println(msg);
    }

    public static void sendChat(String msg){
        MCchatManager.gui.client.getSession().send(new ClientChatPacket(msg));
    }

    public static Path getApplicationPath(Class<?> cls) throws URISyntaxException {
        ProtectionDomain pd = cls.getProtectionDomain();
        CodeSource cs = pd.getCodeSource();
        URL location = cs.getLocation();
        URI uri = location.toURI();
        return Paths.get(uri);
    }
}
