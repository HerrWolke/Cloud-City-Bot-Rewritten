package de.mrcloud.command;


import de.mrcloud.command.commands.GamesCommand;
import de.mrcloud.command.commands.games.SlotMachine;
import de.mrcloud.command.commands.moderation.DisableMoveCommand;
import de.mrcloud.command.commands.moderation.UpdateExperienceForAllUsersCommand;
import de.mrcloud.command.commands.moderation.UpdateUserListCommand;
import de.mrcloud.command.commands.other.ResetCommand;
import de.mrcloud.command.commands.statistics.StatisticsCommand;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CommandRegistry {
    private List<Command> commands = new ArrayList<>();

    public CommandRegistry() {
        registerCommands(
                new ResetCommand(),
                new DisableMoveCommand(),
                new StatisticsCommand(),
                new UpdateUserListCommand(),
                new UpdateExperienceForAllUsersCommand(),
                new GamesCommand(),
                new SlotMachine()
        );
    }


    public ArrayList<Command> getCommandsWithByCategory(Command.Category category) {
        ArrayList<Command> toReturn = new ArrayList<>();
        for (Command command : commands) {
            if(command.category == category) {
                toReturn.add(command);
            }
        }
        return toReturn;
    }

    public void registerCommands(Command... commands) {

        this.commands.addAll(Arrays.asList(commands));
    }

    public List<Command> getCommands() {
        return new ArrayList<>(commands);
    }


    public void setCommands(List<Command> commands) {
        this.commands = commands;
    }
}

