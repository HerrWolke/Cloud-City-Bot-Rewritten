package de.mrcloud.command;


import de.mrcloud.command.commands.DisableMove;
import de.mrcloud.command.commands.ResetCommand;
import de.mrcloud.utils.Settings;

public class Registerer {
    public Registerer() {
        Settings.registerCommands(
            new ResetCommand(),
                new DisableMove()
        );
    }
}

