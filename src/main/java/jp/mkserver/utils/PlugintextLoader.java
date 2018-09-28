package jp.mkserver.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class PlugintextLoader {

    public static String load(InputStream input){
        BufferedReader br = new BufferedReader(new InputStreamReader(input));
        try {
            String str = br.readLine();
            while(str != null){
                if(str.startsWith("main: ")) {
                    return str.replace("main: ","");
                }
                str = br.readLine();
            }
        } catch (IOException e) {
            return null;
        }
        return null;
    }

}
