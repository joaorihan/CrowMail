package com.joao.crowmail.commands;

import com.joao.crowmail.letters.LetterUtil;
import com.joao.crowmail.utils.Utilities;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.Collections;
import java.util.List;

public class ShredCommand extends Command{


    public ShredCommand(){
        super("shred",
                new String[]{"rasgar"},
                "Destroys the letter the player is currently holding.",
                "crowmail.shred");
    }


    @Override
    public void execute(CommandSender sender, String[] args) {
        //Command checks
        if (!(sender instanceof Player)) return;
        Player player = (Player) sender;

        if (!player.hasPermission("crowmail.shred")){
            player.sendMessage(Utilities.pullMessage("error-no-permission"));
            return;
        }

        if (!LetterUtil.isHoldingLetter(player)){
            player.sendMessage(Utilities.pullMessage("not-holding-letter"));
            return;
        }

        // Command execution
        if (args.length == 0){
            // /shred
            player.getInventory().getItemInMainHand().setAmount(0);
            player.sendMessage(Utilities.pullMessage("shred-letter"));
            return;
        }

        if (args.length > 1 || !(args[0].equals("all") || args[0].equals("todas"))) {
            player.sendMessage(Utilities.pullMessage("incorrect-usage") + "/shred all");
            return;
        }

        // /shred all
        for (ItemStack item : player.getInventory().getContents())
            if (LetterUtil.isValidLetter(item))
                item.setAmount(0);

        player.sendMessage(Utilities.pullMessage("shred-all-letters"));
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        if (getLabel().equals("shred"))
            return Collections.singletonList("all");
        else
            return Collections.singletonList("todas");
    }
}
