package de.mrcloud.command.commands.moderation;

import de.mrcloud.command.Command;
import de.mrcloud.listeners.moderation.DefenseListener;
import de.mrcloud.utils.settings.Static;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class DisableMoveCommand extends Command {
    public DisableMoveCommand() {
        super("disable", "", "", Category.STAFF);
    }

    @Override
    public boolean execute(GuildMessageReceivedEvent e, String[] args) {
        if (e.getMember().getId().equalsIgnoreCase(Static.CLOUD_ID_STRING)) {
            switch (args[0]) {
                case "move":
                    DefenseListener.cloudHasDisableMove = !DefenseListener.cloudHasDisableMove;
                    e.getMessage().delete().queue();
                    break;
                case "kick":
                    DefenseListener.cloudHasDisableKick = !DefenseListener.cloudHasDisableKick;
                    e.getMessage().delete().queue();
                    break;
                case "mute":
                    DefenseListener.cloudHasDisableMute = !DefenseListener.cloudHasDisableMute;
                    e.getMessage().delete().queue();
                    break;
            }

        }
        return false;
    }

    @Override
    public boolean execute(MessageReceivedEvent e, String[] args) {
        return false;
    }
}
