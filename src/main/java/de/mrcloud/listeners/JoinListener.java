package de.mrcloud.listeners;

import de.mrcloud.main.CloudCityBot2;
import de.mrcloud.utils.JDAUtils;
import de.mrcloud.utils.NewJoinedMember;
import de.mrcloud.utils.Settings;
import de.mrcloud.utils.SqlUtils;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent;
import net.dv8tion.jda.api.events.guild.member.GuildMemberRemoveEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionAddEvent;
import net.dv8tion.jda.api.events.message.guild.react.GuildMessageReactionRemoveEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;

public class JoinListener extends ListenerAdapter {
    @Override
    public void onGuildMemberJoin(@Nonnull GuildMemberJoinEvent e) {
        super.onGuildMemberJoin(e);
        Member member = e.getMember();
        Guild server = e.getGuild();

        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy  HH:mm:ss");
        String formated = member.getTimeJoined().format(format);


        Statement statement = null;
        try {
            statement = CloudCityBot2.getInstance().getDbHandler().getConnection().createStatement();


            ResultSet resultSetCheck = statement.executeQuery("SELECT * FROM Users WHERE userID = " + member.getUser().getId() + ";");

            if (!resultSetCheck.next()) {
                statement.executeQuery("INSERT INTO Users(UserName,dateJoined,UserID)" + "\n" + "VALUES('" + member.getUser().getName() + "','" + formated + "'," + member.getId() + ");");
                statement.executeQuery("INSERT INTO UserStatistics(UserID)" + "\n" + "VALUES(" + member.getId() + ");");
            }

        } catch (SQLException e1) {
            e1.printStackTrace();
            System.err.println("An SQL Error");
            System.out.println(e1.getLocalizedMessage());
            System.err.println("------------");
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }


        server.getCategoriesByName("╚═════ Introductions ═════╗", true).get(0).createTextChannel("introduction-for-" + member.getUser().getName()).queue(txtChannel -> {
            JDAUtils.greenBuilder("Settings", "Hello. What is your preferred language? \n \n \uD83C\uDDEC\uD83C\uDDE7 for English \n \uD83C\uDDE9\uD83C\uDDEA for German \n \n \n \n Hallo. Wähle deine bevorzugte Sprache. Klicke dafür einfach auf \n \n \uD83C\uDDEC\uD83C\uDDE7 für Englisch \n \uD83C\uDDE9\uD83C\uDDEA für Deutsch ", member, txtChannel, Arrays.asList("\uD83C\uDDE9\uD83C\uDDEA", "\uD83C\uDDEC\uD83C\uDDE7"));

            long epochMilli = member.getTimeJoined().toInstant().toEpochMilli();
            Date date = new Date(epochMilli);

            Settings.newJoinedMembers.put(member.getId(), new NewJoinedMember(member.getIdLong(), txtChannel.getIdLong(), date));
            txtChannel.getManager().sync().queue();
            txtChannel.putPermissionOverride(e.getMember()).setAllow(Collections.singletonList(Permission.MESSAGE_READ)).setDeny(Arrays.asList(Permission.MESSAGE_WRITE, Permission.MESSAGE_ADD_REACTION)).queue();
        });
    }

    @Override
    public void onGuildMessageReactionAdd(@Nonnull GuildMessageReactionAddEvent e) {
        super.onGuildMessageReactionAdd(e);

        MessageReaction.ReactionEmote reactionEmote = e.getReactionEmote();
        String emoteName = reactionEmote.getName();
        Member member = e.getMember();
        CloudCityBot2 bot = CloudCityBot2.getInstance();
        Connection connection = bot.getDbHandler().getConnection();

        if (e.getUser().isBot()) return;

        if (Settings.newJoinedMembers.containsKey(member.getId())) {
            NewJoinedMember joinedMember = Settings.newJoinedMembers.get(member.getId());
            TextChannel txtChannel = e.getGuild().getTextChannelById(joinedMember.getChannelId());
            Guild server = e.getGuild();

            //German flag
            switch (emoteName) {
                case "\uD83C\uDDE9\uD83C\uDDEA":
                    SqlUtils.setSQLCollum(connection, member.getId(), "language", "ger");
                    JDAUtils.addRoleToMemberByName(e.getMember(), "Deutsch");


                    JDAUtils.greenBuilderReturn("Settings", "Klicke die jeweiligen Emotes für die Spiele/Themen für die du dich interessierst. Du kannst auch mehrere anklicken. Wenn du fertig bist, klick auf " + server.getEmotesByName("prime", true).get(0) + " \n \n" + server.getEmotesByName("cs", true).get(0).getAsMention() + "CS:GO & Valorant \n Anime" + server.getEmotesByName("anime", true).get(0).getAsMention() + "\n Among Us" + server.getEmotesByName("among", true).get(0).getAsMention(), e.getMember(), txtChannel).queue(message -> {
                        message.addReaction(server.getEmotesByName("cs", true).get(0)).queue();
                        message.addReaction(server.getEmotesByName("among", true).get(0)).queue();
                        message.addReaction(server.getEmotesByName("anime", true).get(0)).queue();

                        message.addReaction("\u2705").queue();
                    });

                    JDAUtils.getChannelMessage(txtChannel, e.getMessageId()).delete().queue();
                    //English Flag
                    break;
                case "\uD83C\uDDEC\uD83C\uDDE7":
                    SqlUtils.setSQLCollum(connection, member.getId(), "language", "eng");
                    JDAUtils.addRoleToMemberByName(e.getMember(), "English");


                    JDAUtils.greenBuilderReturn("Settings", "Click the emotes for the games/subjects you are interested in. You can also click multiple. If you are done, click " + server.getEmotesByName("prime", true).get(0) + " \n \n" + server.getEmotesByName("cs", true).get(0).getAsMention() + "CS:GO & Valorant \n Anime" + server.getEmotesByName("anime", true).get(0).getAsMention() + "\n Among Us" + server.getEmotesByName("among", true).get(0).getAsMention(), e.getMember(), txtChannel).queue(message -> {
                        message.addReaction(server.getEmotesByName("cs", true).get(0)).queue();
                        message.addReaction(server.getEmotesByName("among", true).get(0)).queue();
                        message.addReaction(server.getEmotesByName("anime", true).get(0)).queue();
                        message.addReaction("\u2705").queue();
                    });

                    JDAUtils.getChannelMessage(txtChannel, e.getMessageId()).delete().queue();
                    break;
                case "cs":
                    if (!joinedMember.getReactedTo().contains("cs"))
                        joinedMember.addToReacted("CS:GO");
                    break;
                case "anime":
                    if (!joinedMember.getReactedTo().contains("anime"))
                        joinedMember.addToReacted("anime");
                    break;
                case "among":
                    if (!joinedMember.getReactedTo().contains("among us"))
                        joinedMember.addToReacted("among us");
                    break;
                case "\u2705":
                    if (joinedMember.getReactedTo().size() > 0) {
                        joinedMember.finished(e.getChannel());
                        Settings.newJoinedMembers.remove(member.getId());
                    } else
                        JDAUtils.redBuilder("Error", Settings.getLanguageTextByMember(member, "join-error"), member, txtChannel);
                    break;
            }


        }
    }

    @Override
    public void onGuildMessageReactionRemove(@Nonnull GuildMessageReactionRemoveEvent e) {
        super.onGuildMessageReactionRemove(e);

        MessageReaction.ReactionEmote reactionEmote = e.getReactionEmote();
        String emoteName = reactionEmote.getName();
        Member member = e.getMember();

        if (e.getUser().isBot()) return;

        if (Settings.newJoinedMembers.containsKey(member.getId())) {
            NewJoinedMember joinedMember = Settings.newJoinedMembers.get(member.getId());
            switch (emoteName) {
                case "cs":
                    if (!joinedMember.getReactedTo().contains("cs"))
                        joinedMember.removeFromReacted("CS:GO");
                    break;
                case "anime":
                    if (!joinedMember.getReactedTo().contains("anime"))
                        joinedMember.removeFromReacted("anime");
                    break;
                case "among":
                    if (!joinedMember.getReactedTo().contains("among us"))
                        joinedMember.removeFromReacted("among us");
                    break;
            }
        }
    }

    @Override
    public void onGuildMemberRemove(@Nonnull GuildMemberRemoveEvent e) {
        super.onGuildMemberRemove(e);

        User user = e.getUser();

        if (Settings.newJoinedMembers.isEmpty()) return;
        if (Settings.newJoinedMembers.containsKey(user.getId())) {
            TextChannel txtChannel = e.getGuild().getTextChannelById(Settings.newJoinedMembers.get(user.getId()).getChannelId());
            txtChannel.delete().queue();
        }
    }
}
