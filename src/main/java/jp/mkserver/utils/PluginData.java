package jp.mkserver.utils;

public class PluginData {

    private String main;
    private String pluginname;
    private String creator;
    private String description;


    public String getDescription() {
        return description;
    }

    public String getCreator() {
        return creator;
    }

    public String getPluginname() {
        return pluginname;
    }

    public String getMain() {
        return main;
    }

    public void setPluginname(String pluginname) {
        this.pluginname = pluginname;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setMain(String main) {
        this.main = main;
    }
}
