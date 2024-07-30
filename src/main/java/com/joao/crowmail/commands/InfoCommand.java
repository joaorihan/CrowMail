package com.joao.crowmail.commands;

import com.joao.crowmail.letters.LetterUtil;
import com.joao.crowmail.utils.Utilities;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class InfoCommand extends Command{


    public InfoCommand(){
        super("letterinfo",
                new String[]{"infocarta"},
                "Gets the description of a written letter.",
                "crowmail.info");

    }


    @Override
    public void execute(CommandSender sender, String[] args) {
        // Command checks
        if (!(sender instanceof Player)) return;
        Player player = (Player) sender;

        if (!player.hasPermission("crowmail.info")){
            player.sendMessage(Utilities.pullMessage("error-no-permission"));
            return;
        }

        if (args.length != 0){
            player.sendMessage(Utilities.pullMessage("incorrect-usage") + " /letterinfo");
            return;
        }

        if (!LetterUtil.isHoldingLetter(player)){
            System.out.println("final check err");
            player.sendMessage(Utilities.pullMessage("not-holding-letter"));
            return;
        }

        // Command execution
        player.sendMessage(Utilities.pullMessage("written-by") + LetterUtil.getLetterOwner(player));
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        //todo
        return List.of();
    }
}
