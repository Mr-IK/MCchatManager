package jp.mkserver.apis.java;

import jp.mkserver.MCchatManager;
import jp.mkserver.utils.PluginData;
import jp.mkserver.utils.PlugintextLoader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.CodeSource;
import java.security.ProtectionDomain;
import java.util.HashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

public class JarLoader {

    public static Object callClass(File file) throws Exception {
        JarFile jar = new JarFile(file);
        JarEntry pdf = jar.getJarEntry( "plugin.txt" );
        try ( InputStream in = jar.getInputStream( pdf ) ) {
            URL[] urls = { file.toURI().toURL() };
            ClassLoader loader = URLClassLoader.newInstance(urls);
            PluginData load = PlugintextLoader.load(in);
            if(load == null || load.getMain()==null){
                return null;
            }
            // クラスをロード
            Class<?> clazz = loader.loadClass(load.getMain());
            return clazz.newInstance();
        }
    }

    public static PluginData callData(File file){
        JarFile jar = null;
        try {
            jar = new JarFile(file);
            JarEntry pdf = jar.getJarEntry( "plugin.txt" );
            InputStream in = jar.getInputStream( pdf );
            PluginData load = PlugintextLoader.load(in);
            if(load == null || load.getMain()==null){
                return null;
            }
            return load;
        } catch (IOException ignored) {}
        return null;
    }

    public static File[] getPluginsFile(){
        try {
            String pass = getApplicationPath(MCchatManager.class).getParent().toString()+File.separator+"plugins";
            File file = new File(pass);
            if(!file.exists()){
                file.mkdir();
            }
            return file.listFiles();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Path getApplicationPath(Class<?> cls) throws URISyntaxException {
        ProtectionDomain pd = cls.getProtectionDomain();
        CodeSource cs = pd.getCodeSource();
        URL location = cs.getLocation();
        URI uri = location.toURI();
        Path path = Paths.get(uri);
        return path;
    }


}
