package jp.mkserver.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class PlugintextLoader {

    public static PluginData load(InputStream input){
        PluginData pluginData = new PluginData();
        BufferedReader br = new BufferedReader(new InputStreamReader(input));
        try {
            String str = br.readLine();
            while(str != null){
                if(str.startsWith("main: ")) {
                    pluginData.setMain(str.replace("main: ",""));
                }else if(str.startsWith("name: ")) {
                    pluginData.setPluginname(str.replace("name: ",""));
                }else if(str.startsWith("creator: ")) {
                    pluginData.setCreator(str.replace("creator: ",""));
                }else if(str.startsWith("description: ")) {
                    pluginData.setDescription(str.replace("description: ",""));
                }
                str = br.readLine();
            }
        } catch (IOException e) {
            return pluginData;
        }
        return pluginData;
    }

}
