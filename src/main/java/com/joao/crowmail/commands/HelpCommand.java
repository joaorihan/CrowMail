package com.joao.crowmail.commands;

import org.bukkit.command.CommandSender;

import java.util.List;

public class HelpCommand extends Command{


    public HelpCommand(){
        super("crowhelp",
                new String[]{""},
                "Standard help command for CrowMail.",
                "crowmail.help");
    }


    @Override
    public void execute(CommandSender sender, String[] args) {

        //todo
    }


    @Override
    public List<String> onTabComplete(CommandSender sender, String[] args) {
        return List.of();
    }
}
