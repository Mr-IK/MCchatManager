package jp.mkserver;


import com.google.gson.*;
import jp.mkserver.apis.MCMAPI;

import javax.swing.*;
import java.io.*;
import java.net.*;
import java.util.Enumeration;

import org.apache.tools.zip.*;

public class Updater {
    public static double thisversion  = 1.2;

    public static void enableUpdater(){
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL("https://api.github.com/repos/Mr-IK/MCchatManager/releases/latest").openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept","application/vnd.github.v3+json");
            int statuss = conn.getResponseCode();
            String msg;
            BufferedReader reader = null;
            if (statuss == HttpURLConnection.HTTP_OK) {
                // レスポンスを受け取る処理等
                StringBuilder stringBuilder = new StringBuilder();
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
                String inputLine;
                while ((inputLine = reader.readLine()) != null) {
                    stringBuilder.append(inputLine);
                }
                msg = stringBuilder.toString();
                double ver = 0;
                if(isJSONValid(msg)) {
                    JsonParser parser = new JsonParser();
                    JsonElement json = parser.parse(msg);
                    if(json.isJsonObject()) {
                        JsonObject jsono = json.getAsJsonObject();
                        ver = jsontoStringVersion(jsono);
                    }
                }
                if(ver == 0){
                    System.out.println("[Error]アップデーターが正常に作動しませんでした。");
                    return;
                }
                if(thisversion < ver){
                    System.out.println("最新バージョン(v"+ver+")が出ています！");
                    System.out.println("ダウンロードをメインGUIから行ってください！");
                }else{
                    System.out.println("このMCchatManagerは最新です。");
                }
            }else{
                System.out.println("[Error]アップデーターの接続に失敗しました。");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    private static final Gson gson = new Gson();

    public static boolean isJSONValid(String jsonInString) {
        try {
            gson.fromJson(jsonInString, Object.class);
            return true;
        } catch(com.google.gson.JsonSyntaxException ex) {
            return false;
        }
    }

    public static double jsontoStringVersion(JsonObject jsono){
        double version = 0;
        if(jsono.has("tag_name")) {
            JsonElement array = jsono.get("tag_name");
            String ver = array.getAsString();
            if(ver.startsWith("v")){
                ver = ver.replaceFirst("v","");
            }
            try{
                return Double.parseDouble(ver);
            }catch (NumberFormatException e){
                return version;
            }

        }
        return version;
    }

    public static double jsontoStringVersion() {
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL("https://api.github.com/repos/Mr-IK/MCchatManager/releases/latest").openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/vnd.github.v3+json");
            int statuss = conn.getResponseCode();
            String msg;
            BufferedReader reader = null;
            if (statuss == HttpURLConnection.HTTP_OK) {
                // レスポンスを受け取る処理等
                StringBuilder stringBuilder = new StringBuilder();
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                String inputLine;
                while ((inputLine = reader.readLine()) != null) {
                    stringBuilder.append(inputLine);
                }
                msg = stringBuilder.toString();
                if (isJSONValid(msg)) {
                    JsonParser parser = new JsonParser();
                    JsonElement json = parser.parse(msg);
                    if(json.isJsonObject()) {
                        JsonObject jsono = json.getAsJsonObject();
                        double version = 0;
                        if (jsono.has("tag_name")) {
                            JsonElement array = jsono.get("tag_name");
                            String ver = array.getAsString();
                            if (ver.startsWith("v")) {
                                ver = ver.replaceFirst("v", "");
                            }
                            try {
                                return Double.parseDouble(ver);
                            } catch (NumberFormatException e) {
                                return version;
                            }
                        }
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }


    public static String jsontoStringDownload(){
        try {
            HttpURLConnection conn = (HttpURLConnection) new URL("https://api.github.com/repos/Mr-IK/MCchatManager/releases/latest").openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept","application/vnd.github.v3+json");
            int statuss = conn.getResponseCode();
            String msg;
            BufferedReader reader = null;
            if (statuss == HttpURLConnection.HTTP_OK) {
                // レスポンスを受け取る処理等
                StringBuilder stringBuilder = new StringBuilder();
                reader = new BufferedReader(new InputStreamReader(conn.getInputStream(),"UTF-8"));
                String inputLine;
                while ((inputLine = reader.readLine()) != null) {
                    stringBuilder.append(inputLine);
                }
                msg = stringBuilder.toString();
                if(isJSONValid(msg)) {
                    JsonParser parser = new JsonParser();
                    JsonElement json = parser.parse(msg);
                    if(json.isJsonObject()) {
                        JsonObject jsono = json.getAsJsonObject();
                        if(jsono.has("assets")) {
                            JsonArray array = jsono.get("assets").getAsJsonArray();
                            for (int i = 0; i < array.size(); i++) {
                                JsonElement el = array.get(i);
                                if (el.isJsonObject()) {
                                    if (el.getAsJsonObject().has("browser_download_url")) {
                                        return el.getAsJsonObject().get("browser_download_url").getAsString();
                                    }
                                }
                            }
                        }
                    }
                }
            }else{
                System.out.println("[Error]アップデーターの接続に失敗しました。");
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return null ;
    }

    public static void download(){
        try {
            String urls = jsontoStringDownload();
            double ver = jsontoStringVersion();
            if (urls == null) {
                return;
            }
            URL url = new URL(urls);
            HttpURLConnection conn =
                    (HttpURLConnection) url.openConnection();
            conn.setAllowUserInteraction(false);
            conn.setInstanceFollowRedirects(true);
            conn.setRequestMethod("GET");
            conn.connect();

            int httpStatusCode = conn.getResponseCode();
            if (httpStatusCode != HttpURLConnection.HTTP_OK) {
                throw new Exception();
            }

            InputStream in = conn.getInputStream();

            FileOutputStream fos =new FileOutputStream(MCMAPI.getApplicationPath(MCchatManager.class).getParent().getParent().toString() + File.separator + "MCchatManager-v" + ver+".zip");
            // Read Data
            int line = -1;

            while ((line = in.read()) != -1) {
                fos.write(line);
            }

            in.close();
            fos.close();


            proccessUnZip2(MCMAPI.getApplicationPath(MCchatManager.class).getParent().getParent().toString() + File.separator + "MCchatManager-v" + ver+".zip");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void proccessUnZip2(String filename) throws IOException {
        try {
            ZipFile zipFile = new ZipFile(filename);
            Enumeration enumeration = zipFile.getEntries();
            double ver = jsontoStringVersion();
            while (enumeration.hasMoreElements()) {
                ZipEntry entry = (ZipEntry) enumeration.nextElement();
                if (entry.isDirectory()) {
                    new File(MCMAPI.getApplicationPath(MCchatManager.class).getParent().getParent().toString() + File.separator + "MCchatManager-v" +ver+File.separator+entry.getName()).mkdirs();
                } else {
                    File parent = new File(MCMAPI.getApplicationPath(MCchatManager.class).getParent().getParent().toString() + File.separator + "MCchatManager-v" +ver+File.separator+entry.getName()).getParentFile();
                    if(parent != null){
                        parent.mkdirs();
                    }
                    FileOutputStream out = new FileOutputStream(MCMAPI.getApplicationPath(MCchatManager.class).getParent().getParent().toString() + File.separator + "MCchatManager-v" +ver+File.separator+entry.getName());
                    InputStream in = zipFile.getInputStream(entry);
                    byte[] buf = new byte[1024];
                    int size = 0;
                    while ((size = in.read(buf)) != -1) {
                        out.write(buf, 0, size);
                    }
                    out.close();
                }
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
    }
}
