package com.joao.crowmail.commands;

import lombok.experimental.UtilityClass;

@UtilityClass
public class CommandManager {

    public void registerCommands(){
        new AdminCommand();
        new AnonLetterCommand();
        new BlockCommand();
        new HelpCommand();
        new InfoCommand();
        new LetterCommand();
        new SendCommand();
        new ShredCommand();
    }

}