package de.mrcloud.listeners.statistics;

import de.mrcloud.main.CloudCityBot2;
import de.mrcloud.utils.SqlUtils;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.concurrent.TimeUnit;


public class ChannelTimeListener extends ListenerAdapter {
    public static LinkedHashMap<Long, Date> inVoiceChannel = new LinkedHashMap<>();

    @Override
    public void onGuildVoiceJoin(@NotNull GuildVoiceJoinEvent e) {
        super.onGuildVoiceJoin(e);

        Member member = e.getMember();
        Guild guild = e.getGuild();
        Date date = new Date();

        inVoiceChannel.put(member.getIdLong(), date);
    }

    @Override
    public void onGuildVoiceLeave(@NotNull GuildVoiceLeaveEvent e) {
        super.onGuildVoiceLeave(e);

        Member member = e.getMember();
        Guild guild = e.getGuild();
        Date date = new Date();

        Date joinTime = inVoiceChannel.get(member.getIdLong());

        long diff = date.getTime() - joinTime.getTime();
        Connection connection = CloudCityBot2.getInstance().getDbHandler().getConnection();
        diff += TimeUnit.SECONDS.toMillis((SqlUtils.getSqlColumnInt(connection, "channelTimeSeconds", member)));
        diff += TimeUnit.MINUTES.toMillis((SqlUtils.getSqlColumnInt(connection, "channelTimeMinutes", member)));
        diff += TimeUnit.HOURS.toMillis((SqlUtils.getSqlColumnInt(connection, "channelTimeHours", member)));
        diff += TimeUnit.DAYS.toMillis((SqlUtils.getSqlColumnInt(connection, "channelTimeDays", member)));

        long days = TimeUnit.MILLISECONDS.toDays(diff);
        diff -= TimeUnit.DAYS.toMillis(days);
        long hours = TimeUnit.MILLISECONDS.toHours(diff);
        diff -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(diff);
        diff -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(diff);

        System.out.println("days " + days);
        System.out.println("hour " + hours);
        System.out.println("min " + minutes);
        System.out.println("sec " + seconds);


        SqlUtils.setSQLCollumInt(connection, member.getId(), "channelTimeSeconds", (int) seconds);
        SqlUtils.setSQLCollumInt(connection, member.getId(), "channelTimeMinutes", (int) minutes);
        SqlUtils.setSQLCollumInt(connection, member.getId(), "channelTimeHours", (int) hours);
        SqlUtils.setSQLCollumInt(connection, member.getId(), "channelTimeDays", (int) days);


        inVoiceChannel.remove(member.getIdLong());

    }
}
