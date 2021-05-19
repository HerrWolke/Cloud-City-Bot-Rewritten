package de.mrcloud.command;


import de.mrcloud.command.commands.ChannelTimeCommand;
import de.mrcloud.command.commands.DisableMoveCommand;
import de.mrcloud.command.commands.ResetCommand;
import de.mrcloud.command.commands.UpdateUserListCommand;
import de.mrcloud.utils.Settings;

public class Registerer {
    public Registerer() {
        Settings.registerCommands(
            new ResetCommand(),
                new DisableMoveCommand(),
                new ChannelTimeCommand(),
                new UpdateUserListCommand()
        );
    }
}

