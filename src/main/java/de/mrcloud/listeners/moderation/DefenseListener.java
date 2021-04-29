package de.mrcloud.listeners.moderation;

import de.mrcloud.utils.Static;
import net.dv8tion.jda.api.audit.ActionType;
import net.dv8tion.jda.api.audit.AuditLogEntry;
import net.dv8tion.jda.api.audit.AuditLogOption;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceGuildDeafenEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceGuildMuteEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.util.LinkedHashMap;
import java.util.List;

public class DefenseListener extends ListenerAdapter {
    public static boolean cloudHasDisableMove = false;
    static LinkedHashMap<Long, Integer> timesMoved = new LinkedHashMap<>();
    static LinkedHashMap<Long,AuditLogEntry> lastListMoved = new LinkedHashMap<>();

    @Override
    public void onGuildVoiceGuildMute(@Nonnull GuildVoiceGuildMuteEvent e) {
        super.onGuildVoiceGuildMute(e);

        Member muted = e.getMember();
        Guild server = e.getGuild();
        if (muted.getId().equals(Static.CLOUD_ID_STRING) && !e.getMember().getUser().isBot()) {
            long muterId = server.retrieveAuditLogs().type(ActionType.MEMBER_UPDATE).complete().get(0).getUser().getIdLong();
            if (muterId != Static.CLOUD_ID_LONG) {
                Member muter = server.getMemberById(muterId);
                if (muter != null && muter.getVoiceState().inVoiceChannel()) {
                    muter.mute(true).queue();
                }
                muted.mute(false).queue();
            }

        }
    }

    @Override
    public void onGuildVoiceGuildDeafen(@Nonnull GuildVoiceGuildDeafenEvent e) {
        super.onGuildVoiceGuildDeafen(e);

        Member muted = e.getMember();
        Guild server = e.getGuild();
        if (muted.getId().equals(Static.CLOUD_ID_STRING) && !e.getMember().getUser().isBot()) {
            long muterId = server.retrieveAuditLogs().type(ActionType.MEMBER_UPDATE).complete().get(0).getUser().getIdLong();
            if (muterId != Static.CLOUD_ID_LONG) {
                Member muter = server.getMemberById(muterId);
                if (muter != null && muter.getVoiceState().inVoiceChannel()) {
                    muter.deafen(true).queue();
                }
                muted.deafen(false).queue();
            }
        }
    }

    @Override
    public void onGuildVoiceMove(@Nonnull GuildVoiceMoveEvent e) {
        super.onGuildVoiceMove(e);
        Member moved = e.getMember();
        Guild server = e.getGuild();

        if (moved.getId().equals(Static.CLOUD_ID_STRING) && cloudHasDisableMove) {
            List<AuditLogEntry> moversList = server.retrieveAuditLogs().type(ActionType.MEMBER_VOICE_MOVE).complete();
            User mover = null;
            if(!lastListMoved.isEmpty()) {
                for (AuditLogEntry entry : moversList) {
                    if(!(lastListMoved.get((entry.getUser().getIdLong())) == null)) continue;
                    if(Integer.parseInt(lastListMoved.get((entry.getUser().getIdLong())).getOption(AuditLogOption.COUNT) ) < Integer.parseInt(entry.getOption(AuditLogOption.COUNT))) {
                        mover = entry.getUser();

                        if(!mover.isBot() && e.getGuild().getMember(mover) != moved) break;
                    }
                }
                e.getGuild().moveVoiceMember(moved,e.getChannelLeft());


            } else {
                for (AuditLogEntry entry : moversList) {
                    lastListMoved.put(entry.getUser().getIdLong(),entry);
                }
            }
        }
    }
}
