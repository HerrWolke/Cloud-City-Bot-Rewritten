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

import static de.mrcloud.listeners.statistics.StatisticsHandler.xpNeededForFirstLevel;
import static de.mrcloud.listeners.statistics.StatisticsHandler.xpNeededForLevelUpGrowthFactor;

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
            int currentLevel = SqlUtils.getSqlColumnInt(CloudCityBot2.getInstance().getDbHandler().getConnection(), "level", member.getId(), "UserStatistics");
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle(Settings.languages.getTranslation(SqlUtils.getMemberPreferredLanguage(member), "stats-title"));
            embedBuilder.setAuthor(author.getName(), author.getAvatarUrl(), author.getAvatarUrl());
            embedBuilder.setColor(Color.CYAN);
            embedBuilder.setDescription("**CHANNEL ZEIT (Lifetime):** \n" + getFormattedTimeLifetime(member)
                    + "\n **CHANNEL ZEIT (Letzter Monat):** \n " + getFormattedTimeLastMonth(member) + "\n"
                    + "**CHANNEL ZEIT (Dieser Monat):** \n " + getFormattedTimeThisMonth(member) + "\n \n"
                    + "**NACHRICHTEN:** \n" + SqlUtils.getSqlColumnInt(CloudCityBot2.getInstance().getDbHandler().getConnection(), "messageCount", member.getId(), "UserStatistics")
                    + "\n \n **Level:** " + currentLevel
                    + "\n \n **XP:** " + SqlUtils.getSqlColumnInt(CloudCityBot2.getInstance().getDbHandler().getConnection(), "xp", member.getId(), "UserStatistics") + " / " + (Math.round((xpNeededForFirstLevel * Math.pow(xpNeededForLevelUpGrowthFactor, currentLevel)) * 10.0) / 10.0)
                    + "\n \n **Coins:** " + SqlUtils.getSqlColumnInt(CloudCityBot2.getInstance().getDbHandler().getConnection(), "coins", member.getId(), "Users"));
            textChannel.sendMessage(embedBuilder.build()).queue();
        } else {
            int currentLevel = SqlUtils.getSqlColumnInt(CloudCityBot2.getInstance().getDbHandler().getConnection(), "level", message.getMentionedMembers().get(0).getId(), "UserStatistics");
            EmbedBuilder embedBuilder = new EmbedBuilder();
            embedBuilder.setTitle(Settings.languages.getTranslation(SqlUtils.getMemberPreferredLanguage(member), "stats-title"));
            embedBuilder.setAuthor(author.getName(), author.getAvatarUrl(), author.getAvatarUrl());
            embedBuilder.setColor(Color.CYAN);
            embedBuilder.setDescription("**CHANNEL ZEIT (Lifetime):** \n" + getFormattedTimeLifetime(message.getMentionedMembers().get(0))
                    + "\n **CHANNEL ZEIT (Letzter Monat):** \n " + getFormattedTimeLastMonth(message.getMentionedMembers().get(0)) + "\n"
                    + "**CHANNEL ZEIT (Dieser Monat):** \n " + getFormattedTimeThisMonth(message.getMentionedMembers().get(0)) + "\n \n"
                    + "**NACHRICHTEN:** \n" + SqlUtils.getSqlColumnInt(CloudCityBot2.getInstance().getDbHandler().getConnection(), "messageCount", message.getMentionedMembers().get(0).getId(), "UserStatistics")
                    + "\n \n **Level:** " + currentLevel
                    + "\n \n **XP:** " + SqlUtils.getSqlColumnInt(CloudCityBot2.getInstance().getDbHandler().getConnection(), "xp", message.getMentionedMembers().get(0).getId(), "UserStatistics") + " / " + (Math.round((xpNeededForFirstLevel * Math.pow(xpNeededForLevelUpGrowthFactor, currentLevel)) * 10.0) / 10.0)
                    + "\n \n **Coins:** " + SqlUtils.getSqlColumnInt(CloudCityBot2.getInstance().getDbHandler().getConnection(), "coins", message.getMentionedMembers().get(0).getId(), "Users"));
            textChannel.sendMessage(embedBuilder.build()).queue();
        }

        return false;
    }

    @Override
    public boolean execute(MessageReceivedEvent e, String[] args) {
        return false;
    }

    private String getFormattedTimeLifetime(Member member) {
        Connection connection = CloudCityBot2.getInstance().getDbHandler().getConnection();
        return String.format(Settings.languages.getTranslation(SqlUtils.getMemberPreferredLanguage(member), "stats-desc"), SqlUtils.getSqlColumnInt(connection, "lifetimeDay", member.getId(), "UserStatistics"), SqlUtils.getSqlColumnInt(connection, "lifetimeHour", member.getId(), "UserStatistics"), SqlUtils.getSqlColumnInt(connection, "lifetimeMin", member.getId(), "UserStatistics"), SqlUtils.getSqlColumnInt(connection, "lifetimeSec", member.getId(), "UserStatistics"));
    }

    private String getFormattedTimeLastMonth(Member member) {
        Connection connection = CloudCityBot2.getInstance().getDbHandler().getConnection();
        return String.format(Settings.languages.getTranslation(SqlUtils.getMemberPreferredLanguage(member), "stats-desc"), SqlUtils.getSqlColumnInt(connection, "lastMonthDay", member.getId(), "UserStatistics"), SqlUtils.getSqlColumnInt(connection, "lastMonthHour", member.getId(), "UserStatistics"), SqlUtils.getSqlColumnInt(connection, "lastMonthMin", member.getId(), "UserStatistics"), SqlUtils.getSqlColumnInt(connection, "lastMonthSec", member.getId(), "UserStatistics"));
    }

    private String getFormattedTimeThisMonth(Member member) {
        Connection connection = CloudCityBot2.getInstance().getDbHandler().getConnection();
        return String.format(Settings.languages.getTranslation(SqlUtils.getMemberPreferredLanguage(member), "stats-desc"), SqlUtils.getSqlColumnInt(connection, "thisMonthDay", member.getId(), "UserStatistics"), SqlUtils.getSqlColumnInt(connection, "thisMonthHour", member.getId(), "UserStatistics"), SqlUtils.getSqlColumnInt(connection, "thisMonthMin", member.getId(), "UserStatistics"), SqlUtils.getSqlColumnInt(connection, "thisMonthSec", member.getId(), "UserStatistics"));
    }

}
