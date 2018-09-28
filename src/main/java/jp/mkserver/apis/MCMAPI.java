package jp.mkserver.apis;

import jp.mkserver.GUIManager;
import jp.mkserver.MCchatManager;
import jp.mkserver.utils.ConfigFileManager;

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
}
