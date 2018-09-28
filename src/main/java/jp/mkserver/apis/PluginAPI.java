package jp.mkserver.apis;

import jp.mkserver.apis.java.JarLoader;

import java.io.File;
import java.util.ArrayList;


public class PluginAPI {

    public static ArrayList<File> plugins = new ArrayList<>();

    public static void loadPluginAll(){
        File[] files = JarLoader.getPluginsFile();
        if(files == null){
            return;
        }
        for(File file : files){
            loadPlugin(file);
        }
    }

    public static void loadPlugin(File file){
        plugins.add(file);
        try {
            PluginManager pm = (PluginManager) JarLoader.callClass(file);
            pm.onEnable();
        } catch (Exception ignored) {
        }
    }

    public static void unloadPlugin(File file){
        plugins.remove(file);
        try {
            PluginManager pm = (PluginManager) JarLoader.callClass(file);
            pm.onDisable();
        } catch (Exception ignored) {
        }
    }

    public static void unloadPluginAll(){
        for(File file : plugins){
            unloadPlugin(file);
        }
    }
}
