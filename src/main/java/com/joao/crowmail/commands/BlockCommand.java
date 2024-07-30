package com.joao.crowmail.commands;

import com.joao.crowmail.utils.Utilities;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class BlockCommand extends Command{

    public BlockCommand(){
        super("blockletters",
                new String[]{"bloquearcartas", "block", "bloquear"},
                "Command used to stop receiving letters.",
                "crowmail.block");

    }


    @Override
    public void execute(CommandSender sender, String[] args) {
        //Command checks
        if (!(sender instanceof Player)) return;
        Player player = (Player) sender;

        if (!player.hasPermission("crowmail.block")){
            player.sendMessage(Utilities.pullMessage("error-no-permission"));
            return;
        }

        //Command execution
        if (getPlugin().getLetterManager().addBlockedPlayer(player)){
            player.sendMessage(Utilities.pullMessage("enable-letters"));
            getPlugin().getOutgoingManager().sendPendingLetters(player, Utilities.pullConfigInt("on-enable-letters-delay"));
        } else {
            getPlugin().getLetterManager().removeBlockedPlayer(player);
            player.sendMessage(Utilities.pullMessage("disable-letters"));
        }

    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return null;
    }
}
