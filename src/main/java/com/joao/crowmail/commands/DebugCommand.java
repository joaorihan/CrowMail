package com.joao.crowmail.commands;

import com.joao.crowmail.utils.Configurations;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class DebugCommand extends Command{


    public DebugCommand(){
        super("debug",
                new String[]{""},
                "Debug command",
                "crowmail.admin.debug");
    }


    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player))
            return;
        Player player = (Player) sender;

        StringBuilder debug = new StringBuilder();
        getPlugin().getOutgoingManager().getOutgoingLetters().forEach(letter -> debug.append(letter.getLetter().toString()).append("\n"));

        player.sendMessage("ArrayList<Letter> outgoingLetters:\n" +
                debug +
                "\n\nFile outgoing.yml:\n" +
                Configurations.getOutgoingConfig().getKeys(false));
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return List.of();
    }
}
