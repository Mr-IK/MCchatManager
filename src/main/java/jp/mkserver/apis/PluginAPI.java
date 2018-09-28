package jp.mkserver.apis;

import jp.mkserver.apis.java.JarLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.jar.JarFile;


public class PluginAPI {

    public static ArrayList<File> plugins = new ArrayList<>();

    public static void loadPluginAll(){
        File[] files = JarLoader.getPluginsFile();
        if(files == null){
            return;
        }
        for(File file : files){
            if ( file.isFile() && file.getName().endsWith( ".jar" ) ) {
                try ( JarFile jar = new JarFile( file ) ) {
                    loadPlugin(file);
                } catch ( Exception ignored) {
                }
            }
        }
    }

    public static void loadPlugin(File file){
        try {
            PluginManager pm = (PluginManager) JarLoader.callClass(file);
            if(pm==null){
                return;
            }
            pm.onEnable();
            plugins.add(file);
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
