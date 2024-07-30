package com.joao.crowmail.letters;

import com.joao.crowmail.CrowMail;
import com.joao.crowmail.utils.Utilities;
import lombok.experimental.UtilityClass;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.persistence.PersistentDataType;

import java.util.Objects;

@UtilityClass
public class LetterUtil {

    /**
     * Used to check if an item is a plugin letter
     * @param item Item to be compared
     * @return if the item was a valid plugin letter or not
     */
    public boolean isValidLetter(ItemStack item){
        if (!item.getType().equals(Material.WRITTEN_BOOK))
            return false;

        return ((BookMeta) item.getItemMeta()).getTitle().startsWith(Utilities.pullMessageRaw("letter-from"));
    }

    /**
     * Used to check if a player is holding a letter
     * @param player Command sender
     * @return {@code boolean} item in main hand is a letter
     */
    public boolean isHoldingLetter(Player player) {
        player.sendMessage(String.valueOf(isValidLetter(player.getInventory().getItemInMainHand())));
        return isValidLetter(player.getInventory().getItemInMainHand());
    }

    /**
     * Used to check if a player is holding a letter written by them
     * @param player Command sender
     * @return {@code boolean} item in main hand is a letter written by @player
     */
    public boolean isHoldingOwnLetter(Player player) {
        return isHoldingLetter(player) && Objects.equals(LetterUtil.getLetterOwner(player), player.getName());
    }

    public boolean wasAlreadySent(ItemStack letter){
        return letter.getItemMeta().getLore().toString().contains("Destinatário: ") ||
                letter.getItemMeta().getLore().toString().contains("Recipient: ");
    }

    /**
     * Used to get the Player who wrote a letter
     * @param player command sender
     * @return String name of the letter's owner
     * might need to be changed to OfflinePlayer, in case of player name change issues
     */
    public String getLetterOwner(Player player){
        ItemStack book = player.getInventory().getItemInMainHand();
        BookMeta bm = (BookMeta) book.getItemMeta();
        NamespacedKey key = CrowMail.getPlugin().getLetterManager().getKey();

        if (bm.getPersistentDataContainer().has(key, PersistentDataType.STRING))
            return bm.getPersistentDataContainer().get(key, PersistentDataType.STRING);

        return null;
    }


}
