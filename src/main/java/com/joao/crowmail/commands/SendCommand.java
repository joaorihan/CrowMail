package com.joao.crowmail.commands;

import com.joao.crowmail.letters.Letter;
import com.joao.crowmail.letters.LetterUtil;
import com.joao.crowmail.utils.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.List;

public class SendCommand extends Command{


    public SendCommand(){
        super("post",
                new String[]{"enviar"},
                "Sends a letter being held to another player",
                "crowmail.post");

    }


    @Override
    public void execute(CommandSender sender, String[] args) {
        // Required command checks
        if (!(sender instanceof Player)) return;
        Player player = (Player) sender;

        if (!player.hasPermission("crowmail.post")){
            player.sendMessage(Utilities.pullMessage("error-no-permission"));
            return;
        }

        if (!LetterUtil.isHoldingOwnLetter(player)){
            player.sendMessage(Utilities.pullMessage("not-holding-own-letter"));
            return;
        }

        ItemStack book = new ItemStack(player.getInventory().getItemInMainHand());
        System.out.println("ItemStack book:\n"+book.toString());

        if (LetterUtil.wasAlreadySent(book)){
            player.sendMessage(Utilities.pullMessage("invalid-letter"));
            return;
        }

        if (args.length == 0){
            player.sendMessage(Utilities.pullMessage("add-receiver"));
            return;
        }

        // Command execution
        // TODO: send to all players
        @SuppressWarnings("deprecation")
        OfflinePlayer receiver = Bukkit.getOfflinePlayer(args[0]);
        if (receiver.isOnline() && (player.getWorld() == ((Player) receiver).getWorld()))
            new Letter(receiver, book, player.getLocation().distance(((Player) receiver).getLocation()));
        else
            new Letter(receiver, book, Utilities.pullConfigInt("different-dimension-delay"));

        player.sendMessage(Utilities.pullMessage("letter-sent"));
        player.getInventory().getItemInMainHand().setAmount(0);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        List<String> names = new ArrayList<>();
        for (Player player : Bukkit.getOnlinePlayers())
            names.add(player.getName());

        return StringUtil.copyPartialMatches(args[0], names, new ArrayList<>());
    }


}
