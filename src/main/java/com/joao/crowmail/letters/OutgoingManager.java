package com.joao.crowmail.letters;

import com.joao.crowmail.utils.Configurations;
import com.joao.crowmail.utils.Utilities;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

@Getter
public class OutgoingManager {

    private ArrayList<Letter> outgoingLetters = new ArrayList<>();

    public void addOutgoingLetter(Letter letter){
        if (outgoingLetters.contains(letter)) return;

        outgoingLetters.add(letter);
    }

    public void sendSavedLetters(){
        HashMap<UUID, ArrayList<ItemStack>> outgoingFromConfig = new HashMap<>();
        for (String key : Configurations.getOutgoingConfig().getKeys(false))
            outgoingFromConfig.put(UUID.fromString(key), (ArrayList<ItemStack>) Configurations.getOutgoingConfig().getList(key));

        outgoingFromConfig.forEach((player, letter) -> {
            for (ItemStack book : letter)
                new Letter(Bukkit.getOfflinePlayer(player), book, Utilities.pullConfigInt("resend-delay"));
        });
    }

    public void saveLettersOnFile() throws IOException {
        HashMap<UUID, ArrayList<ItemStack>> outgoing  = convertArrayToHash();

        for (String key : Configurations.getOutgoingConfig().getKeys(false)){
            Configurations.getOutgoingConfig().set(key, null);
        }

        if (outgoing.isEmpty()) {
            Configurations.getOutgoingConfig().save(Configurations.getOutgoingFile());
            return;
        }

        outgoing.forEach((player, letter) -> {
            Configurations.getOutgoingConfig().set(String.valueOf(player), letter);
            try {
                Configurations.getOutgoingConfig().save(Configurations.getOutgoingFile());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

        Configurations.getOutgoingConfig().save(Configurations.getOutgoingFile());
    }

    public ArrayList<Letter> getPendingLetters(Player player){
        ArrayList<Letter> pendingPlayerLetters = new ArrayList<>();
        for (Letter outgoingLetter : outgoingLetters) {
            if (outgoingLetter.getReceiverUUID().equals(player.getUniqueId())){
                pendingPlayerLetters.add(outgoingLetter);
            }
        }
        return pendingPlayerLetters;
    }

    public void sendPendingLetters(Player player, double delay){
        for (Letter letter : getPendingLetters(player))
            letter.send(delay);
    }

    /**
     * Gets the Letter a Crow entity belongs to.
     *
     * @param crow Crow Entity
     * @return {@code OutgoingLetter}
     * @author Kuglin
     */
    public Letter getCrowsLetter(Entity crow) {
        for (Letter outgoingLetter : outgoingLetters)
            if (outgoingLetter.getCrow().getCrowEntity() == crow)
                return outgoingLetter;
        return null;
    }

    /**
     * Takes ArrayList outgoingLetters content and converts it into a HashMap
     *
     * @return Converted outgoingLetters HashMap
     * @author Kuglin
     */
    public HashMap<UUID, ArrayList<ItemStack>> convertArrayToHash() {

        HashMap<UUID, ArrayList<ItemStack>> hashMap = new HashMap<>();
        if (!outgoingLetters.isEmpty()) {
            for (Letter outLetter : outgoingLetters) {

                hashMap.computeIfAbsent(outLetter.getReceiverUUID(), uuid -> new ArrayList<>());
                hashMap.get(outLetter.getReceiverUUID()).add(outLetter.getLetter());
            }
        }
        return hashMap;
    }

}
