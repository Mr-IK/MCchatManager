package jp.mkserver.utils;

import jp.mkserver.MCchatManager;
import jp.mkserver.apis.MCMAPI;

import java.io.*;
import java.net.URISyntaxException;

public class SettingSaver {

    public static void saveSound(float vol,boolean on,double vold){
        try {
            File file = new File(MCMAPI.getApplicationPath(MCchatManager.class).getParent().toString()+File.separator+"settings"+File.separator+"sound.bin");
            if(!file.getParentFile().exists()){
                file.getParentFile().mkdir();
            }
            if(file.exists()){
                file.delete();
            }
            file.createNewFile();
            ObjectOutputStream objOutStream = new ObjectOutputStream(new FileOutputStream(MCMAPI.getApplicationPath(MCchatManager.class).getParent().toString()+File.separator+"settings"+File.separator+"sound.bin"));

            SoundSetting set = new SoundSetting(vol,on,vold);

            objOutStream.writeObject(set);
            objOutStream.close();

        } catch (IOException | URISyntaxException e) {
            e.printStackTrace();
        }
    }


    public static SoundSetting loadSound(){
        try {
            ObjectInputStream objInStream
                    = new ObjectInputStream(
                    new FileInputStream(MCMAPI.getApplicationPath(MCchatManager.class).getParent().toString()+File.separator+"settings"+File.separator+"sound.bin"));

            SoundSetting sweets = (SoundSetting) objInStream.readObject();

            objInStream.close();
            return sweets;

        } catch (FileNotFoundException e) {
            System.out.println("[Setting]ファイルが存在しなかった");
        } catch (IOException | ClassNotFoundException | URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }

}
