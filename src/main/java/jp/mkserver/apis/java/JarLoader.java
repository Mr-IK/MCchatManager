package jp.mkserver.apis.java;

import jp.mkserver.MCchatManager;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.CodeSource;
import java.security.ProtectionDomain;

public class JarLoader {

    public static Object callClass(File file) throws Exception {
        URL[] urls = { file.toURI().toURL() };
        ClassLoader loader = URLClassLoader.newInstance(urls);

        // クラスをロード
        Class<?> clazz = loader.loadClass(file.getName());
        return clazz.newInstance();
    }

    public static File[] getPluginsFile(){
        try {
            String pass = getApplicationPath(MCchatManager.class).getParent().toString()+"\\plugins";
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
