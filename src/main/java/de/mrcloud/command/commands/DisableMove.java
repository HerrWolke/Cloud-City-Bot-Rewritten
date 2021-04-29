package de.mrcloud.command.commands;

import de.mrcloud.command.Command;
import de.mrcloud.listeners.moderation.DefenseListener;
import de.mrcloud.utils.Static;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class DisableMove extends Command {
    public DisableMove() {
        super("nomove","","",Category.STAFF);
    }

    @Override
    public boolean execute(GuildMessageReceivedEvent e, String[] args) {
        if(e.getMember().getId().equalsIgnoreCase(Static.CLOUD_ID_STRING)) {
            DefenseListener.cloudHasDisableMove = !DefenseListener.cloudHasDisableMove;
        }
        return false;
    }

    @Override
    public boolean execute(MessageReceivedEvent e, String[] args) {
        return false;
    }
}
