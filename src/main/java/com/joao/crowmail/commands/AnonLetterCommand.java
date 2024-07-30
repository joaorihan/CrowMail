package com.joao.crowmail.commands;

import com.joao.crowmail.utils.Utilities;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class AnonLetterCommand extends Command{

    public AnonLetterCommand(){
        super("anonletter",
                new String[]{"cartaanonima", "anonymousletter", "cartaanon"},
                "Creates an anonymous letter.",
                "crowmail.letter.anonymous");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        // Required command checks
        if (!(sender instanceof Player)) return;
        Player player = (Player) sender;

        if (!player.hasPermission("crowmail.letter.anonymous")) return;

        if (player.getInventory().firstEmpty() == -1){
            sender.sendMessage(Utilities.pullMessage("inventory-full"));
            return;
        }

        // Command execution
        player.sendMessage(Utilities.pullMessage("letter-created"));
        player.getInventory().addItem(getPlugin().getLetterManager().createLetter(player, args, true));
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return null;
    }

}
