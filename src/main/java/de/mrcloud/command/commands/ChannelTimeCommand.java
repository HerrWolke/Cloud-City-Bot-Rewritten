package de.mrcloud.command.commands;

import de.mrcloud.command.Command;
import de.mrcloud.main.CloudCityBot2;
import de.mrcloud.utils.JDAUtils;
import de.mrcloud.utils.Settings;
import de.mrcloud.utils.SqlUtils;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.awt.*;
import java.sql.Connection;

public class ChannelTimeCommand extends Command {

    public ChannelTimeCommand() {
        super("stats", "", "", Category.STATISTICS, 709298267208548433L);
    }

    @Override
    public boolean execute(GuildMessageReceivedEvent e, String[] args) {
        if (!checkChannelLock(e)) {
            e.getMessage().delete().queue();
            JDAUtils.wrongChannel(e.getMember(), e.getChannel(), 10);
            return false;
        }

        Message message = e.getMessage();
        TextChannel textChannel = e.getChannel();
        User author = e.getAuthor();
        Member member = e.getMember();

        if (message.getMentionedMembers().isEmpty()) {
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle(Settings.languages.getTranslation(SqlUtils.getMemberPreferredLanguage(member), "stats-title"));
            embedBuilder.setAuthor(author.getName(), author.getAvatarUrl(), author.getAvatarUrl());
            embedBuilder.setColor(Color.decode("#d63031"));
            embedBuilder.setDescription("**CHANNEL ZEIT:** \n" + getFormattedTime(member) + "\n \n" + "**NACHRICHTEN:** \n" + SqlUtils.getSqlColumnInt(CloudCityBot2.getInstance().getDbHandler().getConnection(), "MessageCount", member));
            textChannel.sendMessage(embedBuilder.build()).queue();
        }

        return false;
    }

    @Override
    public boolean execute(MessageReceivedEvent e, String[] args) {
        return false;
    }

    private String getFormattedTime(Member member) {
        Connection connection = CloudCityBot2.getInstance().getDbHandler().getConnection();
        return String.format(Settings.languages.getTranslation(SqlUtils.getMemberPreferredLanguage(member), "stats-desc"), SqlUtils.getSqlColumnInt(connection, "channelTimeDays", member), SqlUtils.getSqlColumnInt(connection, "channelTimeHours", member), SqlUtils.getSqlColumnInt(connection, "channelTimeMinutes", member), SqlUtils.getSqlColumnInt(connection, "channelTimeSeconds", member));
    }
}
