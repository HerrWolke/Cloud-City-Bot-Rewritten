package de.mrcloud.listeners.statistics;

import de.mrcloud.main.CloudCityBot2;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.LinkedHashMap;


public class ChannelTimeListener extends ListenerAdapter {
    public static LinkedHashMap<Long, Date> inVoiceChannel = new LinkedHashMap<>();

    @Override
    public void onGuildVoiceJoin(@NotNull GuildVoiceJoinEvent e) {
        super.onGuildVoiceJoin(e);

        Member member = e.getMember();
        Date date = new Date();

        inVoiceChannel.put(member.getIdLong(), date);
    }

    @Override
    public void onGuildVoiceLeave(@NotNull GuildVoiceLeaveEvent e) {
        super.onGuildVoiceLeave(e);

        Member member = e.getMember();
        Date date = new Date();

        Date joinTime = inVoiceChannel.get(member.getIdLong());

        long diff = date.getTime() - joinTime.getTime();
        CloudCityBot2.getInstance().getTimeHandler().updateTime(diff, member.getIdLong());


        inVoiceChannel.remove(member.getIdLong());

    }

    @Override
    public void onGuildVoiceMove(@NotNull GuildVoiceMoveEvent e) {
        super.onGuildVoiceMove(e);

        Member member = e.getMember();
        Date date = new Date();

        //AFK Chanel
        if (e.getChannelJoined().getIdLong() == 514517861440421907L) {
            Date joinTime = inVoiceChannel.get(member.getIdLong());

            long diff = date.getTime() - joinTime.getTime();
            CloudCityBot2.getInstance().getTimeHandler().updateTime(diff, member.getIdLong());

            inVoiceChannel.remove(member.getIdLong());
        } else if (e.getChannelLeft().getIdLong() == 514517861440421907L) {
            inVoiceChannel.put(member.getIdLong(), new Date());
        }
    }
}
