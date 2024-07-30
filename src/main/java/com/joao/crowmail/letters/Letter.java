package com.joao.crowmail.letters;

import java.util.*;


import com.joao.crowmail.CrowMail;
import com.joao.crowmail.crows.Crow;
import com.joao.crowmail.utils.Configurations;
import com.joao.crowmail.utils.Utilities;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.scheduler.BukkitRunnable;

import net.md_5.bungee.api.ChatColor;

public class Letter {

    @Getter private OfflinePlayer receiver;
    @Getter private ItemStack letter;
    private final double distance;
    private boolean anonymous;
    @Getter @Setter private Crow crow;
    @Setter boolean isDelivered;


    /**
     * Creates a letter and stores it on the outgoing list
     *
     * @param receiver OfflinePlayer Receiver
     * @param letter ItemStack Letter to be sent
     * @param distance double Distance between Sender and Receiver
     */
    public Letter(OfflinePlayer receiver, ItemStack letter, double distance) {

        this.receiver = receiver;
        this.letter = letter;
        this.distance = distance;
        this.crow = null;
        this.isDelivered = false;

        BookMeta itemMeta = (BookMeta) this.letter.getItemMeta();
        List<String> lore = itemMeta.getLore();

        if (!LetterUtil.wasAlreadySent(letter)) {
            if (Configurations.getLoadedLanguage().equalsIgnoreCase("pt-BR"))
                lore.add(ChatColor.DARK_GRAY +"&TDestinatário: " + receiver.getName());
            else
                lore.add(ChatColor.DARK_GRAY +"&TRecipient: " + receiver.getName());
            itemMeta.setLore(lore);
        }

        if (itemMeta.getAuthor().equals(Utilities.pullMessageRaw("anonymous")))
            anonymous = true;

        this.letter.setItemMeta(itemMeta);

        addToList();

        if (receiver.isOnline()) firstSend();

    }

    public void firstSend(){
        send(distance * (Utilities.pullConfigInt("distance-modifier")));
    }

    private void addToList(){
        CrowMail.getPlugin().getOutgoingManager().addOutgoingLetter(this);
    }

    /**
     * Sends out a Letter
     *
     * @param time double Ticks before sending
     */
    public void send(double time) {

        new BukkitRunnable() {
            @Override
            public void run() {
                if (CrowMail.getPlugin().getLetterManager().getPlayersInBlockedMode().contains(((Player) receiver))) {
                    ((Player) receiver).sendMessage(Utilities.pullMessage("in-blocked-mode"));
                    return;
                    } else if (Utilities.pullConfigList("blocked-worlds").contains(((Player) receiver).getWorld().toString())) {
                    ((Player) receiver).sendMessage(Utilities.pullMessage("in-blocked-world"));
                    return;
                } else if (Utilities.pullConfigList("blocked-gamemodes").contains(((Player) receiver).getGameMode().toString())){
                    ((Player) receiver).sendMessage(Utilities.pullMessage("in-blocked-gamemode"));
                    return;
                }

                crow = new Crow((Player) receiver, anonymous);
                despawnCrow();
            }

        }.runTaskLater(CrowMail.getPlugin(), Utilities.pullConfigInt("minimum-send-time") + (int) time);

    }

    /**
     * Removes a crow Entity, and resends it if a letter wasn't received
     */
    public void despawnCrow() {

        new BukkitRunnable() {
            @Override
            public void run() {

                removeCrow();

                if (receiver.isOnline() && !isDelivered){
                    send(Utilities.pullConfigInt("resend-delay"));
                }
            }

        }.runTaskLater(CrowMail.getPlugin(), Utilities.pullConfigInt("despawn-delay"));

    }

    public void removeCrow(){
        crow.remove();
        crow = null;
    }


    public UUID getReceiverUUID(){ return receiver.getUniqueId(); }
    public void setPlayer(Player newPlayer){ receiver = newPlayer; }

}
