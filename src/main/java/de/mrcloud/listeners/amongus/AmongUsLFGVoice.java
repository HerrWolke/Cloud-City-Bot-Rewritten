package de.mrcloud.listeners.amongus;

import de.mrcloud.utils.ChannelDuet;
import de.mrcloud.utils.JDAUtils;
import de.mrcloud.utils.Settings;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceJoinEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceLeaveEvent;
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceMoveEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.awt.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.List;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class AmongUsLFGVoice extends ListenerAdapter {
    public static List<ChannelDuet> channelDuets = new ArrayList<>();
    public static long channelCat = 514511396491231238L;
    public static long settingsCategoryID = 836965987650633788L;
    public static HashMap<String, Date> isOnCooldown = new HashMap<>();
    public static List<Permission> allow = Arrays.asList(Permission.VIEW_CHANNEL, Permission.MESSAGE_WRITE);
    public static List<Permission> deny = Arrays.asList(Permission.VIEW_CHANNEL, Permission.MESSAGE_WRITE);


    @Override
    public void onGuildVoiceJoin(@Nonnull GuildVoiceJoinEvent e) {
        super.onGuildVoiceJoin(e);

        VoiceChannel voiceChannel = e.getChannelJoined();
        Category category = voiceChannel.getParent();
        int channelNumber = channelDuets.size();
        Member member = e.getMember();
        Guild server = e.getGuild();

        if (category == null) {
            return;
        }

        if (voiceChannel.getId().equals("779406141708042310") || voiceChannel.getId().equals("718170766469890072")) {

            VoiceChannel newVoiceChannel = category.createVoiceChannel("╠🔪Among Us " + (channelNumber + 1)).complete();
            newVoiceChannel.getManager().setUserLimit(15).queue();
            TextChannel newTextChannel = server.getCategoryById(settingsCategoryID).createTextChannel("channel-settings-" + (channelNumber + 1)).addMemberPermissionOverride(member.getIdLong(), allow, null).addRolePermissionOverride(Settings.ROLE_ID_EVERYONE, null, deny).addRolePermissionOverride(754368478445699132L, null, Arrays.asList(Permission.VOICE_CONNECT)).complete();
            server.moveVoiceMember(member, newVoiceChannel).queue();
            channelDuets.add(new ChannelDuet(newTextChannel, newVoiceChannel, channelNumber + 1, member.getIdLong()));

            EmbedBuilder embBuilder = new EmbedBuilder();
            embBuilder.setTitle("Channel Size");
            embBuilder.setAuthor(member.getUser().getName(), member.getUser().getAvatarUrl(), member.getUser().getAvatarUrl());
            embBuilder.setColor(Color.decode("#00a8ff"));
            embBuilder.addField("Size", "Choose a number between 1️⃣ -> \uD83D\uDD1F. \n Or reply with a custom number in this channel", true);


            newTextChannel.sendMessage(embBuilder.build()).queue((message) -> {
                for (String emote : Settings.emotesToAdd) {
                    if (message != null) {
                        message.addReaction(emote).queue();
                    }
                }
            });
        }
    }

    @Override
    public void onGuildVoiceMove(@Nonnull GuildVoiceMoveEvent e) {
        super.onGuildVoiceMove(e);


        VoiceChannel voiceChannel = e.getChannelJoined();
        VoiceChannel voiceChannelLeft = e.getChannelLeft();
        Category category = voiceChannel.getParent();
        Guild server = e.getGuild();

        int channelNumber = channelDuets.size();
        Member member = e.getMember();
        if (category == null) {
            return;
        }

        if (voiceChannel.getId().equals("779406141708042310") || voiceChannel.getId().equals("718170766469890072")) {

            VoiceChannel newVoiceChannel = category.createVoiceChannel("╠🔪Among Us " + (channelNumber + 1)).complete();
            TextChannel newTextChannel = server.getCategoryById(settingsCategoryID).createTextChannel("channel-settings-" + (channelNumber + 1)).addMemberPermissionOverride(member.getIdLong(), allow, null).addRolePermissionOverride(Settings.ROLE_ID_EVERYONE, null, deny).complete();
            newVoiceChannel.getManager().setUserLimit(15).queue();
            server.moveVoiceMember(member, newVoiceChannel).queue();

            channelDuets.add(new ChannelDuet(newTextChannel, newVoiceChannel, channelNumber + 1, member.getIdLong()));

            EmbedBuilder embBuilder = new EmbedBuilder();
            embBuilder.setTitle("Channel Size");
            embBuilder.setAuthor(member.getUser().getName(), member.getUser().getAvatarUrl(), member.getUser().getAvatarUrl());
            embBuilder.setColor(Color.decode("#00a8ff"));
            embBuilder.addField("Size", "Choose a number between 1️⃣ -> \uD83D\uDD1F. \n Or reply with a custom number in this channel", true);


            newTextChannel.sendMessage(embBuilder.build()).queue((message) -> {
                for (String emote : Settings.emotesToAdd) {
                    if (message != null) {
                        message.addReaction(emote).queue();
                    }
                }
            });
        }
        if (voiceChannelLeft.getName().matches("╠🔪Among Us \\d*")) {
            int channelNumberToDelete = Integer.parseInt(voiceChannelLeft.getName().split("\\s")[2]);
            ChannelDuet toRemove = null;

            if (voiceChannelLeft.getMembers().size() == 0) {
                for (ChannelDuet channel : channelDuets) {
                    if (channel.getChannelNumber() == channelNumberToDelete) {
                        toRemove = channel;
                        break;
                    }
                }

                if (toRemove != null) {
                    toRemove.getVoiceChannel().delete().queue();
                    toRemove.getSettingsChannel().delete().queueAfter(5, TimeUnit.SECONDS);
                    channelDuets.remove(toRemove);
                }

                for (ChannelDuet channel : channelDuets) {
                    if (channel.getChannelNumber() > channelNumberToDelete) {
                        channel.setChannelNumber(channel.getChannelNumber() - 1);
                    }
                }
            } else {
                for (ChannelDuet channel : channelDuets) {
                    if (channel.getChannelNumber() == channelNumberToDelete) {
                        if (channel.getOwnerID() == member.getIdLong()) {
                            channel.setOwnerID(voiceChannelLeft.getMembers().get(0).getIdLong());

                            server.getTextChannelById(channel.getSettingsChannel().getId()).getManager().removePermissionOverride(e.getMember()).queue();
                            channel.getSettingsChannel().getManager().putPermissionOverride(voiceChannelLeft.getMembers().get(0), allow, null).queue();
                            channel.getSettingsChannel().sendMessage("You are now the channel owner " + voiceChannelLeft.getMembers().get(0).getAsMention() + "!").queue();
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onGuildVoiceLeave(@Nonnull GuildVoiceLeaveEvent e) {
        super.onGuildVoiceLeave(e);

        VoiceChannel voiceChannelLeft = e.getChannelLeft();

        if (voiceChannelLeft.getName().matches("╠🔪Among Us \\d*")) {
            int channelNumberToDelete = Integer.parseInt(voiceChannelLeft.getName().split("\\s")[2]);
            ChannelDuet toRemove = null;


            if (voiceChannelLeft.getMembers().size() == 0) {
                for (ChannelDuet channel : channelDuets) {
                    if (channel.getChannelNumber() == channelNumberToDelete) {
                        toRemove = channel;
                        break;
                    }
                }
                if (toRemove != null) {
                    toRemove.getVoiceChannel().delete().queue();
                    toRemove.getSettingsChannel().delete().queueAfter(5, TimeUnit.SECONDS);
                    channelDuets.remove(toRemove);
                }

                for (ChannelDuet channel : channelDuets) {
                    if (channel.getChannelNumber() > channelNumberToDelete) {
                        channel.setChannelNumber(channel.getChannelNumber() - 1);
                    }
                }
            } else {
                for (ChannelDuet channel : channelDuets) {
                    if (channel.getChannelNumber() == channelNumberToDelete) {
                        if (channel.getOwnerID() == e.getMember().getIdLong()) {
                            channel.setOwnerID(voiceChannelLeft.getMembers().get(0).getIdLong());
                            channel.getSettingsChannel().getManager().removePermissionOverride(e.getMember()).queue();
                            channel.getSettingsChannel().getManager().putPermissionOverride(voiceChannelLeft.getMembers().get(0), allow, null).queue();

                            channel.getSettingsChannel().sendMessage("You are now the channel owner " + voiceChannelLeft.getMembers().get(0).getAsMention() + "!").queue();
                        }
                    }
                }
            }
        }
    }

    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent e) {
        super.onGuildMessageReceived(e);

        if (!e.getChannel().getName().matches("channel-settings-\\d*") || e.getAuthor().isBot()) return;
        String messageContent = e.getMessage().getContentRaw();
        if (e.getMessage().getContentRaw().matches("\\d*")) {
            Member member = e.getMember();
            TextChannel txtChannel = e.getChannel();
            Message message = e.getMessage();
            Guild server = e.getGuild();

            int channelSize = Integer.parseInt(e.getMessage().getContentRaw());
            if (channelSize > 99) {
                JDAUtils.redBuilder("Error", "The max channel user limit is 99 or use infinite/inf/none for unlimited users", member, txtChannel, 10);
                return;
            }
            int channelNumber = Integer.parseInt(txtChannel.getName().replaceAll("-", " ").split("\\s++")[2]);
            VoiceChannel matchmakingChannel = server.getVoiceChannelsByName("╠🔪Among Us " + channelNumber, true).get(0);
            matchmakingChannel.getManager().setUserLimit(channelSize).queue();
        } else if (messageContent.equalsIgnoreCase("infinite") || messageContent.equalsIgnoreCase("inf") || messageContent.equalsIgnoreCase("none")) {
            TextChannel txtChannel = e.getChannel();
            Guild server = e.getGuild();
            int channelNumber = Integer.parseInt(txtChannel.getName().replaceAll("-", " ").split("\\s++")[2]);
            VoiceChannel matchmakingChannel = server.getVoiceChannelsByName("╠🔪Among Us " + channelNumber, true).get(0);
            matchmakingChannel.getManager().setUserLimit(0).queue();
        } else {
            e.getMessage().delete().queue();
        }
    }

    @Override
    public void onGuildMessageReactionAdd(@Nonnull GuildMessageReactionAddEvent e) {
        super.onGuildMessageReactionAdd(e);


        Guild server = e.getGuild();
        TextChannel txtChannel = e.getChannel();
        Member member = e.getMember();
        MessageReaction.ReactionEmote reacEmote = e.getReactionEmote();
        User user = e.getUser();
        String messageID = e.getMessageId();

        if (!e.getUser().isBot()) {
            if (!txtChannel.getName().matches("channel-settings-\\d*")) return;
            //Formats current time in a normal format
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM, FormatStyle.MEDIUM);
            ZonedDateTime hereAndNow = ZonedDateTime.now();
            String test = dateTimeFormatter.format(hereAndNow);
            String stopDate = test.replaceAll(",", "");

            SimpleDateFormat format = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

            //---------------------------------------

            Date d2 = null;
            Date d3 = null;
            try {
                //Formats the channel join and leave time
                d2 = format.parse(stopDate);
                d3 = format.parse(stopDate);
            } catch (ParseException ex) {
                ex.printStackTrace();
            }


            if (!reacEmote.getName().equals("📞")) {
                if (!isOnCooldown.containsKey(txtChannel.getName()) || isOnCooldown.get(txtChannel.getName()).before(new Date())) {
                    isOnCooldown.remove(txtChannel.getName());
                    //Aka w symbol / wingman channel
                    if (reacEmote.getName().equals("1️⃣")) {
                        int channelNumber = Integer.parseInt(txtChannel.getName().replaceAll("-", " ").split("\\s++")[2]);
                        VoiceChannel matchmakingChannel = server.getVoiceChannelsByName("╠🔪Among Us " + channelNumber, true).get(0);
                        matchmakingChannel.getManager().setUserLimit(1).queue();
                        getMessageToRemoveReaction(txtChannel, messageID).get(0).removeReaction(reacEmote.getEmoji(), user).queue();
                        //Aka m symbol / matchmaking channel
                    } else if (reacEmote.getName().equals("2️⃣")) {
                        int channelNumber = Integer.parseInt(txtChannel.getName().replaceAll("-", " ").split("\\s++")[2]);
                        VoiceChannel matchmakingChannel = server.getVoiceChannelsByName("╠🔪Among Us " + channelNumber, true).get(0);
                        matchmakingChannel.getManager().setUserLimit(2).queue();
                        getMessageToRemoveReaction(txtChannel, messageID).get(0).removeReaction(reacEmote.getEmoji(), user).queue();
                        //Aka green circle / open channel

                    } else if (reacEmote.getName().equals(("3️⃣"))) {
                        int channelNumber = Integer.parseInt(txtChannel.getName().replaceAll("-", " ").split("\\s++")[2]);
                        VoiceChannel matchmakingChannel = server.getVoiceChannelsByName("╠🔪Among Us " + channelNumber, true).get(0);
                        matchmakingChannel.getManager().setUserLimit(3).queue();
                        getMessageToRemoveReaction(txtChannel, messageID).get(0).removeReaction(reacEmote.getEmoji(), user).queue();
                        //Aka green circle / open channel

                    } else if (reacEmote.getName().equals("4️⃣")) {
                        int channelNumber = Integer.parseInt(txtChannel.getName().replaceAll("-", " ").split("\\s++")[2]);
                        VoiceChannel matchmakingChannel = server.getVoiceChannelsByName("╠🔪Among Us " + channelNumber, true).get(0);
                        matchmakingChannel.getManager().setUserLimit(4).queue();
                        getMessageToRemoveReaction(txtChannel, messageID).get(0).removeReaction(reacEmote.getEmoji(), user).queue();

                    } else if (reacEmote.getName().equals("5️⃣")) {
                        int channelNumber = Integer.parseInt(txtChannel.getName().replaceAll("-", " ").split("\\s++")[2]);
                        VoiceChannel matchmakingChannel = server.getVoiceChannelsByName("╠🔪Among Us " + channelNumber, true).get(0);
                        matchmakingChannel.getManager().setUserLimit(5).queue();
                        getMessageToRemoveReaction(txtChannel, messageID).get(0).removeReaction(reacEmote.getEmoji(), user).queue();

                    } else if (reacEmote.getName().equals("6️⃣")) {
                        int channelNumber = Integer.parseInt(txtChannel.getName().replaceAll("-", " ").split("\\s++")[2]);
                        VoiceChannel matchmakingChannel = server.getVoiceChannelsByName("╠🔪Among Us " + channelNumber, true).get(0);
                        matchmakingChannel.getManager().setUserLimit(6).queue();
                        getMessageToRemoveReaction(txtChannel, messageID).get(0).removeReaction(reacEmote.getEmoji(), user).queue();

                    } else if (reacEmote.getName().equals("7️⃣")) {
                        int channelNumber = Integer.parseInt(txtChannel.getName().replaceAll("-", " ").split("\\s++")[2]);
                        VoiceChannel matchmakingChannel = server.getVoiceChannelsByName("╠🔪Among Us " + channelNumber, true).get(0);
                        matchmakingChannel.getManager().setUserLimit(7).queue();
                        getMessageToRemoveReaction(txtChannel, messageID).get(0).removeReaction(reacEmote.getEmoji(), user).queue();

                    } else if (reacEmote.getName().equals("8️⃣")) {
                        int channelNumber = Integer.parseInt(txtChannel.getName().replaceAll("-", " ").split("\\s++")[2]);
                        VoiceChannel matchmakingChannel = server.getVoiceChannelsByName("╠🔪Among Us " + channelNumber, true).get(0);
                        matchmakingChannel.getManager().setUserLimit(8).queue();
                        getMessageToRemoveReaction(txtChannel, messageID).get(0).removeReaction(reacEmote.getEmoji(), user).queue();

                    } else if (reacEmote.getName().equals("9️⃣")) {
                        int channelNumber = Integer.parseInt(txtChannel.getName().replaceAll("-", " ").split("\\s++")[2]);
                        VoiceChannel matchmakingChannel = server.getVoiceChannelsByName("╠🔪Among Us " + channelNumber, true).get(0);
                        matchmakingChannel.getManager().setUserLimit(9).queue();
                        getMessageToRemoveReaction(txtChannel, messageID).get(0).removeReaction(reacEmote.getEmoji(), user).queue();

                    } else if (reacEmote.getName().equals("\uD83D\uDD1F")) {
                        int channelNumber = Integer.parseInt(txtChannel.getName().replaceAll("-", " ").split("\\s++")[2]);
                        VoiceChannel matchmakingChannel = server.getVoiceChannelsByName("╠🔪Among Us " + channelNumber, true).get(0);
                        matchmakingChannel.getManager().setUserLimit(10).queue();
                        getMessageToRemoveReaction(txtChannel, messageID).get(0).removeReaction(reacEmote.getEmoji(), user).queue();
                        //Aka green circle / open channel
                    } else if (reacEmote.getName().equals("\uD83D\uDFE2")) {
                        int channelNumber = Integer.parseInt(txtChannel.getName().replaceAll("-", " ").split("\\s++")[2]);
                        VoiceChannel matchmakingChannel = server.getVoiceChannelsByName("╠🔪Among Us " + channelNumber, true).get(0);
                        getMessageToRemoveReaction(txtChannel, messageID).get(0).removeReaction(reacEmote.getEmoji(), user).queue();
                        //Aka red circle / close channel
                    } else if (reacEmote.getName().equals("\uD83D\uDD34")) {
                        int channelNumber = Integer.parseInt(txtChannel.getName().replaceAll("-", " ").split("\\s++")[2]);
                        VoiceChannel matchmakingChannel = server.getVoiceChannelsByName("╠🔪Among Us " + channelNumber, true).get(0);
                        matchmakingChannel.getManager().setUserLimit(5).queue();
                        getMessageToRemoveReaction(txtChannel, messageID).get(0).removeReaction(reacEmote.getEmoji(), user).queue();
                    }
                    d3.setMinutes(d3.getMinutes() + 1);
                    isOnCooldown.put(txtChannel.getName(), d3);
                } else {
                    JDAUtils.privateBlackBuilder("Oops", Settings.getAsFormatted(member, "settings-error-message", Collections.singletonList(Long.toString(((isOnCooldown.get(txtChannel.getName()).getTime() - d2.getTime()) / 1000)))), member.getUser());
                    getMessageToRemoveReaction(txtChannel, messageID).get(0).removeReaction(reacEmote.getEmoji(), user).queue();
                }
            }
        }
    }

    public List<Message> getMessageToRemoveReaction(@NotNull MessageChannel channel, String messageID) {
        return channel.getIterableHistory().stream()
                .limit(4)
                .filter(m -> m.getId().equals(messageID))
                .collect(Collectors.toList());
    }
}

