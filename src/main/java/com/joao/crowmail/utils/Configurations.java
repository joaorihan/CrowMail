package com.joao.crowmail.utils;

import com.joao.crowmail.CrowMail;
import lombok.*;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

/**
 * Controller for all config and message Files
 */
public class Configurations {

    @Getter @Setter private CrowMail plugin;
    @Getter @Setter private static String loadedLanguage;

    @Getter @Setter private static File ptBRMessagesFile;
    @Getter @Setter private static FileConfiguration ptBRMessagesConfig;

    @Getter @Setter private static File enUSMessagesFile;
    @Getter @Setter private static FileConfiguration enUSMessagesConfig;

    @Getter @Setter private static File outgoingFile;
    @Getter @Setter private static FileConfiguration outgoingConfig;


    public Configurations(CrowMail plugin){
        setPlugin(plugin);

        plugin.getConfig().options().copyDefaults();
        plugin.saveDefaultConfig();

        generateOutgoingConfiguration();
        generateLanguageFiles();
        setupLanguage();
    }

    public void setupLanguage(){
        setLoadedLanguage(plugin.getConfig().getString("lang"));

        if (!(getLoadedLanguage().equalsIgnoreCase("en-US") || getLoadedLanguage().equalsIgnoreCase("pt-BR"))){
            setLoadedLanguage("en-US");
            plugin.getLogger().severe("Language " + plugin.getConfig().getString("lang") + " was not found! Loading standard english messages.");
        }

        System.out.println("Loaded language: " +loadedLanguage);
    }

    public void generateLanguageFiles(){
        new File(this.getPlugin().getDataFolder(), "lang").mkdir();

        setPtBRMessagesFile(new File(this.getPlugin().getDataFolder(), "lang/pt-BR.yml"));
        setEnUSMessagesFile(new File(this.getPlugin().getDataFolder(), "lang/en-US.yml"));

        if (!getPtBRMessagesFile().exists()){
            getPtBRMessagesFile().getParentFile().mkdirs();
            this.getPlugin().saveResource("lang/pt-BR.yml", false);
        }

        if (!getEnUSMessagesFile().exists()){
            getEnUSMessagesFile().getParentFile().mkdirs();
            this.getPlugin().saveResource("lang/en-US.yml", false);
        }

        setPtBRMessagesConfig(new YamlConfiguration());
        setEnUSMessagesConfig(new YamlConfiguration());

        try {
            getPtBRMessagesConfig().load(getPtBRMessagesFile());
            getEnUSMessagesConfig().load(getEnUSMessagesFile());
        } catch (InvalidConfigurationException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void generateOutgoingConfiguration(){
        setOutgoingFile(new File(this.getPlugin().getDataFolder(),"outgoing.yml"));
        if (!getOutgoingFile().exists()){
            this.getPlugin().saveResource("outgoing.yml", false);
        }

        setOutgoingConfig(new YamlConfiguration());
        try {
            getOutgoingConfig().load(getOutgoingFile());
        } catch (IOException | InvalidConfigurationException e) {
            throw new RuntimeException(e);
        }
    }



}
