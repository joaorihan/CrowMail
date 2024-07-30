package com.joao.crowmail.letters;

import com.joao.crowmail.CrowMail;
import com.joao.crowmail.utils.Utilities;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Getter
@Setter
public class LetterManager {

    private CrowMail plugin;


    public LetterManager(CrowMail plugin) {
        setPlugin(plugin);
    }


    private final NamespacedKey key = new NamespacedKey(CrowMail.getPlugin(), "playerName");
    private List<Player> playersInBlockedMode = new ArrayList<>();

    public boolean addBlockedPlayer(Player player){
        if (isInBlockedMode(player))
            return false;

        playersInBlockedMode.add(player);
        return true;
    }

    public void removeBlockedPlayer(Player player){ playersInBlockedMode.remove(player); }

    public boolean isInBlockedMode(Player player) { return playersInBlockedMode.contains(player); }


    /**
     * Method used to create a plugin letter item
     * @param player Command sender
     * @param content args
     * @param anonymous boolean is letter anonymous
     * @return ItemStack written letter
     */
    public ItemStack createLetter(Player player, String[] content, boolean anonymous) {
        ItemStack book = new ItemStack(Material.WRITTEN_BOOK, 1);
        BookMeta bm = (BookMeta) book.getItemMeta();

        PersistentDataContainer pdc = bm.getPersistentDataContainer();
        pdc.set(key, PersistentDataType.STRING, player.getName());

        if (anonymous) {
            bm.setAuthor(Utilities.pullMessageRaw("anonymous"));
            bm.setTitle(Utilities.pullMessageRaw("letter-from") + Utilities.pullMessageRaw("anonymous"));
            if (Utilities.isUsingCustomModelData())
                bm.setCustomModelData(Utilities.pullConfigInt("cem-anonymous-letter"));
        }
        else {
            bm.setAuthor(player.getName());
            bm.setTitle(Utilities.pullMessageRaw("letter-from") + ChatColor.GREEN + player.getName());
            if (Utilities.isUsingCustomModelData())
                bm.setCustomModelData(Utilities.pullConfigInt("cem-letter"));
        }

        // Converts Agrs to the String
        StringBuilder builder = new StringBuilder();
        for (String arg : content) {
            builder.append(arg).append(" ");
        }
        String message = builder.toString();

        // Set the Book Pages
        ArrayList<String> pages = new ArrayList<>();
        pages.add(message);
        bm.setPages(pages);

        // Set Some of the text on the item lore
        ArrayList<String> lore = splitText(message, 30, 3);

        // Set the actual date to the item lore
        Calendar currentDate = Calendar.getInstance();
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm:ss dd/MM/yy");
        String dateNow = formatter.format(currentDate.getTime());

        lore.add(ChatColor.DARK_GRAY + dateNow);

        // Set the lore and item meta to the Book
        bm.setLore(lore);
        book.setItemMeta(bm);

        return book;

    }

    public ArrayList<String> splitText(String text, int size, int maxSize) {

        ArrayList<String> stringList = new ArrayList<>();

        maxSize = maxSize*size;
        if (text.length() < maxSize)
            maxSize = text.length();


        for (int i = 0; i < maxSize; i += size) {
            int end = Math.min(i + size, maxSize);
            stringList.add(text.substring(i, end));
        }

        return stringList;
    }


}
