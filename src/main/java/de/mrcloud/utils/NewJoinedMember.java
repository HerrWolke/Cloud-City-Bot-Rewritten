package de.mrcloud.utils;

import net.dv8tion.jda.api.entities.TextChannel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class NewJoinedMember {
    private long memberId;
    private long channelId;
    private Date joinTime;
    private List<String> reactedTo = new ArrayList<>();
    private long guildId;

    public NewJoinedMember(long memberId, long channelId, Date joinTime) {
        this.memberId = memberId;
        this.channelId = channelId;
        this.joinTime = joinTime;
    }


    public long getChannelId() {
        return channelId;
    }

    public void setChannelId(long channelId) {
        this.channelId = channelId;
    }

    public Date getJoinTime() {
        return joinTime;
    }

    public void setJoinTime(Date joinTime) {
        this.joinTime = joinTime;
    }

    public List<String> getReactedTo() {
        return reactedTo;
    }

    public void setReactedTo(List<String> reactedTo) {
        this.reactedTo = reactedTo;
    }

    public void addToReacted(String add) {
        reactedTo.add(add);
    }

    public void removeFromReacted(String remove) {
        reactedTo.remove(remove);
    }


    public long getMemberId() {
        return memberId;
    }

    public void setMemberId(long memberId) {
        this.memberId = memberId;
    }

    public long getGuildId() {
        return guildId;
    }

    public void setGuildId(long guildId) {
        this.guildId = guildId;
    }

    public void finished(TextChannel textChannel) {
        System.out.println(Arrays.toString(reactedTo.toArray()));
        for (String roleName : reactedTo) {
            JDAUtils.addRoleToMemberByName(textChannel.getGuild().getMemberById(memberId), roleName);
        }
        textChannel.getGuild().getTextChannelById(channelId).delete().queue();
        Settings.newJoinedMembers.remove(Long.toString(memberId));
        textChannel.delete().queue();
    }
}
