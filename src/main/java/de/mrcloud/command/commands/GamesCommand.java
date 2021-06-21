package de.mrcloud.command.commands;

import de.mrcloud.command.Command;
import de.mrcloud.main.CloudCityBot2;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;

public class GamesCommand extends Command {
    public GamesCommand() {
        super("games","","",Category.OTHER);
    }

    @Override
    public boolean execute(GuildMessageReceivedEvent e, String[] args) {
        Member member = e.getMember();

        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("Minigames");
        builder.setAuthor(member.getUser().getName(), member.getUser().getAvatarUrl(), member.getUser().getAvatarUrl());
        builder.setColor(Color.decode("00a8ff"));

        for (Command command : CloudCityBot2.getInstance().getRegistry().getCommandsWithByCategory(Category.GAME)) {
        builder.addField(command.name,command.description,true);
        }
        e.getChannel().sendMessage(builder.build()).queue();

        return false;
    }

    @Override
    public boolean execute(MessageReceivedEvent e, String[] args) {
        return false;
    }
}
