package com.joao.crowmail.listeners;

import com.joao.crowmail.CrowMail;
import com.joao.crowmail.crows.Crow;
import com.joao.crowmail.letters.Letter;
import com.joao.crowmail.utils.Utilities;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

@Setter
@Getter
public class PlayerListener implements Listener {

    private CrowMail plugin;


    public PlayerListener(CrowMail plugin) {

        Bukkit.getPluginManager().registerEvents(this, plugin);
        setPlugin(plugin);
    }


    @EventHandler
    public void onEntityInteract(PlayerInteractEntityEvent e){
        Player player = e.getPlayer();

        for (Letter letter : getPlugin().getOutgoingManager().getPendingLetters(player)){
            if (letter == getPlugin().getOutgoingManager().getCrowsLetter(e.getRightClicked())){
                Crow crow = letter.getCrow();

                if (player.getInventory().firstEmpty() == -1) {
                    player.sendMessage(Utilities.pullMessage("inventory-full"));
                    crow.playFeedbackBad(player);
                    crow.remove();
                    return;
                }

//                ItemStack book = letter.getLetter();
//                book.setType(Material.WRITTEN_BOOK);
//                book.setAmount(1);

                System.out.println("Letter letter:\n"+letter.getLetter().toString());

                player.getInventory().addItem(letter.getLetter());
                crow.playFeedbackGood(player);
                player.sendMessage(Utilities.pullMessage("letter-received"));
                letter.setDelivered(true);
                crow.remove();
                getPlugin().getOutgoingManager().getOutgoingLetters().remove(letter);
            }
        }

    }


}
