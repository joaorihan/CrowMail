package com.joao.crowmail.listeners;

import com.joao.crowmail.CrowMail;
import com.joao.crowmail.letters.Letter;
import com.joao.crowmail.utils.Utilities;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerGameModeChangeEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

@Setter
@Getter
public class LetterListener implements Listener {

    private CrowMail plugin;


    public LetterListener(CrowMail plugin){
        Bukkit.getPluginManager().registerEvents(this, plugin);
        setPlugin(plugin);
    }


    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Player player = e.getPlayer();
        if (getPlugin().getLetterManager().isInBlockedMode(player)){
            player.sendMessage(Utilities.pullMessage("in-blocked-mode"));
            return;
        }
        getPlugin().getOutgoingManager().sendPendingLetters(player, Utilities.pullConfigInt("on-join-delay"));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        for (Letter letter : getPlugin().getOutgoingManager().getPendingLetters(e.getPlayer()))
            letter.removeCrow();
    }

    @EventHandler
    public void onWorldChange(PlayerChangedWorldEvent e){
        Player player = e.getPlayer();
        if (Utilities.pullConfigList("blocked-worlds").contains(player.getWorld().toString())) {
            player.sendMessage(Utilities.pullMessage("in-blocked-world"));
            return;
        }
        getPlugin().getOutgoingManager().sendPendingLetters(player, Utilities.pullConfigInt("on-world-change-delay"));
    }

    @EventHandler
    public void onGamemodeChange(PlayerGameModeChangeEvent e){
        Player player = e.getPlayer();
        if (Utilities.pullConfigList("blocked-gamemodes").contains(player.getGameMode().toString())) {
            player.sendMessage(Utilities.pullMessage("in-blocked-gamemode"));
            return;
        }
        getPlugin().getOutgoingManager().sendPendingLetters(e.getPlayer(), Utilities.pullConfigInt("on-gamemode-delay"));
    }


}