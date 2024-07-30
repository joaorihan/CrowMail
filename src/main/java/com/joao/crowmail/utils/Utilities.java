package com.joao.crowmail.utils;

import com.joao.crowmail.CrowMail;
import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class Utilities {

    private final CrowMail plugin = CrowMail.getPlugin();

    public String getPluginPrefix(){
        if (Configurations.getLoadedLanguage().equalsIgnoreCase("pt-BR"))
            return Configurations.getPtBRMessagesConfig().getString("plugin-prefix");
        else
            return Configurations.getEnUSMessagesConfig().getString("plugin-prefix");
    }

    /**
     * Gets a message from the currently loaded language file
     * @param id config key of the message
     * @return {@code String} raw message
     */
    public String pullMessage(String id){
        if (Configurations.getLoadedLanguage().equalsIgnoreCase("pt-BR"))
            return getPluginPrefix() + Configurations.getPtBRMessagesConfig().getString(id);
        else if (Configurations.getLoadedLanguage().equalsIgnoreCase("en-US"))
            return getPluginPrefix() + Configurations.getEnUSMessagesConfig().getString(id);
        else
            return getPluginPrefix() + Configurations.getEnUSMessagesConfig().getString(id);
    }

    public String pullMessageRaw(String id){
        if (Configurations.getLoadedLanguage().equalsIgnoreCase("pt-BR"))
            return Configurations.getPtBRMessagesConfig().getString(id);
        else if (Configurations.getLoadedLanguage().equalsIgnoreCase("en-US"))
            return Configurations.getEnUSMessagesConfig().getString(id);
        else
            return Configurations.getEnUSMessagesConfig().getString(id);
    }


    public String pullConfigString(String id){ return plugin.getConfig().getString(id); }

    public int pullConfigInt(String id){ return plugin.getConfig().getInt(id); }

    public boolean pullConfigBoolean(String id){ return plugin.getConfig().getBoolean(id); }

    public List<String> pullConfigList(String id){
        if (plugin.getConfig().getStringList(id).isEmpty())
            return new ArrayList<>();

        return new ArrayList<>(plugin.getConfig().getStringList(id));
    }

    public boolean isUsingCustomModelData(){
        return pullConfigBoolean("letters.use-custom-model-data");
    }


}
