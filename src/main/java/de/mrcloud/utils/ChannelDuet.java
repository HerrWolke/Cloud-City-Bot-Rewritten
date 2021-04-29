package de.mrcloud.utils;

import net.dv8tion.jda.api.entities.TextChannel;
import net.dv8tion.jda.api.entities.VoiceChannel;

public class ChannelDuet {
    private TextChannel settingsChannel;
    private VoiceChannel voiceChannel;
    private int channelNumber;
    private long ownerID;

    public ChannelDuet(TextChannel settingsChannel, VoiceChannel voiceChannel, int channelNumber, long ownerID) {
        this.settingsChannel = settingsChannel;
        this.voiceChannel = voiceChannel;
        this.channelNumber = channelNumber;
        this.ownerID = ownerID;
    }

    public TextChannel getSettingsChannel() {
        return settingsChannel;
    }

    public void setSettingsChannel(TextChannel settingsChannel) {
        this.settingsChannel = settingsChannel;
    }

    public VoiceChannel getVoiceChannel() {
        return voiceChannel;
    }

    public void setVoiceChannel(VoiceChannel voiceChannel) {
        this.voiceChannel = voiceChannel;
    }

    public int getChannelNumber() {
        return channelNumber;
    }

    public void setChannelNumber(int channelNumber) {
        this.channelNumber = channelNumber;
        voiceChannel.getManager().setName("╠☕Chill Lounge " + channelNumber).queue();
        settingsChannel.getManager().setName("channel-settings-" + channelNumber).queue();
    }

    public long getOwnerID() {
        return ownerID;
    }

    public void setOwnerID(long ownerID) {
        this.ownerID = ownerID;
    }
}
