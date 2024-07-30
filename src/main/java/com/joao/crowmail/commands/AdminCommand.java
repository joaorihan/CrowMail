package com.joao.crowmail.commands;

import com.joao.crowmail.utils.Utilities;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class AdminCommand extends Command {

    public AdminCommand(){
        super("crowadmin",
                new String[]{"ca", "cadmin", "crowmail"},
                "Admin command for CrowMail.",
                "crowmail.admin");

    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        // Command checks
        if (!(sender instanceof Player)) return;
        Player player = (Player) sender;

        if (!player.hasPermission("crowmail.admin")) return;

        if (args.length == 0){
            player.sendMessage(Utilities.pullMessage("incorrect-usage"));
            return;
        }
        // Command execution
        // /crowadmin reload
        // /crowadmin block <target>

        if (args[0].equals("reload")){
            //TODO: RELOAD LOGIC
            return;
        }

        Player target = Bukkit.getPlayer(args[1]);
        if (target == null) {
            player.sendMessage(Utilities.pullMessage("target-not-found"));
            return;
        }

        if (args[0].equals("block")){

            if (getPlugin().getLetterManager().isInBlockedMode(target)){
                player.sendMessage(Utilities.pullMessage("error-already-blocked"));
                return;
            }

            getPlugin().getLetterManager().addBlockedPlayer(target);
            return;
        }

        if (args[0].equals("unblock")){
            if (!getPlugin().getLetterManager().isInBlockedMode(target)){
                player.sendMessage(Utilities.pullMessage("error-already-unblocked"));
                return;
            }

            getPlugin().getLetterManager().removeBlockedPlayer(target);
            return;
        }

    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        //todo
        return List.of();
    }
}
