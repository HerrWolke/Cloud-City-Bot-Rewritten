package de.mrcloud.command.commands;

import de.mrcloud.command.Command;
import de.mrcloud.main.CloudCityBot2;
import de.mrcloud.utils.Static;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

public class ResetCommand extends Command {
    public ResetCommand() {
        super("reset", "", "", Category.STAFF);
    }

    @Override
    public boolean execute(GuildMessageReceivedEvent e, String[] args) {
        if (e.getMember().getIdLong() != Static.CLOUD_ID_LONG) return false;
        for (TextChannel txtChannel : CloudCityBot2.getInstance().getServer().getCategoryById(836965987650633788L).getTextChannels()) {
            txtChannel.delete().queue();
        }
        return false;
    }

    @Override
    public boolean execute(MessageReceivedEvent e, String[] args) {
        return false;
    }
}
