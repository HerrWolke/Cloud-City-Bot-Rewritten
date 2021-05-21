package de.mrcloud.command;


import de.mrcloud.command.commands.*;
import de.mrcloud.utils.Settings;

public class Registerer {
    public Registerer() {
        Settings.registerCommands(
                new ResetCommand(),
                new DisableMoveCommand(),
                new ChannelTimeCommand(),
                new UpdateUserListCommand(),
                new UpdateExperienceForAllUsersCommand()
        );
    }
}

