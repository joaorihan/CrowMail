package com.joao.crowmail;

import com.joao.crowmail.commands.CommandManager;
import com.joao.crowmail.commands.DebugCommand;
import com.joao.crowmail.crows.CrowManager;
import com.joao.crowmail.letters.LetterManager;
import com.joao.crowmail.letters.OutgoingManager;
import com.joao.crowmail.listeners.LetterListener;
import com.joao.crowmail.listeners.PlayerListener;
import com.joao.crowmail.utils.Configurations;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;

public final class CrowMail extends JavaPlugin {

    @Getter @Setter private static CrowMail plugin;

    @Getter @Setter private OutgoingManager outgoingManager;
    @Getter @Setter private CrowManager crowManager;
    @Getter @Setter private LetterManager letterManager;

    @Override
    public void onEnable() {
        plugin = this;

        new Configurations(plugin);

        new LetterListener(plugin);
        new PlayerListener(plugin);

        CommandManager.registerCommands();

        setOutgoingManager(new OutgoingManager());
        setCrowManager(new CrowManager(plugin));
        setLetterManager(new LetterManager(plugin));

        getOutgoingManager().sendSavedLetters();

        // debug
        new DebugCommand();
    }

    @Override
    public void onDisable() {
        getLogger().info("Disabling CrowMail");
        try {
            getOutgoingManager().saveLettersOnFile();
        } catch (IOException e) {
            getLogger().severe("[ERROR] Unable to save Outgoing Letters.");
        }
        getCrowManager().removeAllCrows();
    }


}
