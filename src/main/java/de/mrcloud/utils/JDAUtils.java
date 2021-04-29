package de.mrcloud.utils;


import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.*;
import net.dv8tion.jda.api.requests.restaction.MessageAction;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


/**
 * Created by Mr Cloud
 * Version 1.0
 * <p>
 * JDAUtils contains all sorts of utilities for programming discord bots. It is mostly there to keep your code cleaner
 */
public class JDAUtils {


    /**
     * @return Returns a boolean;
     * INFO: May need {@link net.dv8tion.jda.api.utils.MemberCachePolicy#ALL} turned on!
     */
    public static boolean hasRole(Member member, String roleName, boolean ignoreCase) {
        boolean hasRole = false;
        for (Role role : member.getRoles()) {
            if (ignoreCase) {
                if (role.getName().equalsIgnoreCase(roleName)) {
                    hasRole = true;
                    break;
                }
            } else {
                if (role.getName().equals(roleName)) {
                    hasRole = true;
                    break;
                }
            }
        }
        return hasRole;
    }

    public static boolean hasRoles(Member member, List<Role> roles) {
        boolean hasRole = false;

        if (member.getRoles().containsAll(roles)) {
            hasRole = true;
        }

        return hasRole;
    }


    /**
     * @return Returns a boolean;
     * INFO: May need {@link net.dv8tion.jda.api.utils.MemberCachePolicy#ALL} turned on!
     */
    public static boolean hasRole(Member member, Long roleID) {
        boolean hasRole = false;
        for (Role role : member.getRoles()) {
            if (role.getIdLong() == roleID) {
                hasRole = true;
                break;
            }
        }
        return hasRole;
    }

    /**
     * @return Returns a boolean;
     * INFO: May need {@link net.dv8tion.jda.api.utils.MemberCachePolicy#ALL} turned on!
     */
    public static boolean hasRole(Member member, Role role) {
        boolean hasRole = false;
        for (Role role1 : member.getRoles()) {
            if (role1.equals(role)) {
                hasRole = true;
                break;
            }
        }
        return hasRole;
    }

    /**
     * @param Title      Title of the Message
     * @param ErrorText  The content of the message body
     * @param member     The member who should be used for {@link EmbedBuilder#setAuthor(String, String, String)}
     * @param txtChannel Chooses the channel the message should be send to
     */
    public static void redBuilder(String Title, String ErrorText, Member member, TextChannel txtChannel) {
        EmbedBuilder embBuilder = new EmbedBuilder();
        embBuilder.setTitle(Title);
        embBuilder.setAuthor(member.getUser().getName(), member.getUser().getAvatarUrl(), member.getUser().getAvatarUrl());
        embBuilder.setColor(Color.decode("#d63031"));
        embBuilder.setDescription(ErrorText);
        txtChannel.sendMessage(embBuilder.build()).queue();

    }

    public static void redBuilder(String Title, String ErrorText, User user, TextChannel txtChannel) {
        EmbedBuilder embBuilder = new EmbedBuilder();
        embBuilder.setTitle(Title);
        embBuilder.setAuthor(user.getName(), user.getAvatarUrl(), user.getAvatarUrl());
        embBuilder.setColor(Color.decode("#d63031"));
        embBuilder.setDescription(ErrorText);
        txtChannel.sendMessage(embBuilder.build()).queue();

    }

    public static void redBuilder(String Title, String ErrorText, Member member, TextChannel txtChannel, int deleteAfter) {
        EmbedBuilder embBuilder = new EmbedBuilder();
        embBuilder.setTitle(Title);
        embBuilder.setAuthor(member.getUser().getName(), member.getUser().getAvatarUrl(), member.getUser().getAvatarUrl());
        embBuilder.setColor(Color.decode("#d63031"));
        embBuilder.setDescription(ErrorText);
        txtChannel.sendMessage(embBuilder.build()).queue(message -> message.delete().queueAfter(deleteAfter, TimeUnit.SECONDS));

    }

    public static void noPerm(Member member, TextChannel txtChannel, boolean delete, int deleteAfter) {
        EmbedBuilder embBuilder = new EmbedBuilder();
        embBuilder.setTitle("Error");
        embBuilder.setAuthor(member.getUser().getName(), member.getUser().getAvatarUrl(), member.getUser().getAvatarUrl());
        embBuilder.setColor(Color.decode("#d63031"));
        embBuilder.setDescription("You do not have permission to execute this command!");
        if (delete) {
            txtChannel.sendMessage(embBuilder.build()).queue(message -> message.delete().queueAfter(deleteAfter, TimeUnit.SECONDS));
        } else {
            txtChannel.sendMessage(embBuilder.build()).queue();
        }
    }

    public static void noPerm(Member member, TextChannel txtChannel) {
        EmbedBuilder embBuilder = new EmbedBuilder();
        embBuilder.setTitle("Error");
        embBuilder.setAuthor(member.getUser().getName(), member.getUser().getAvatarUrl(), member.getUser().getAvatarUrl());
        embBuilder.setColor(Color.decode("#d63031"));
        embBuilder.setDescription("You do not have permission to execute this command!");
        txtChannel.sendMessage(embBuilder.build()).queue();

    }

    /**
     * @param Title      Title of the Message
     * @param Text       The content of the message body
     * @param member     The member who should be used for {@link EmbedBuilder#setAuthor(String, String, String)}
     * @param txtChannel Chooses the channel the message should be send to
     */
    public static void blackBuilder(String Title, String Text, Member member, TextChannel txtChannel) {
        EmbedBuilder embBuilder = new EmbedBuilder();
        embBuilder.setTitle(Title);
        embBuilder.setAuthor(member.getUser().getName(), member.getUser().getAvatarUrl(), member.getUser().getAvatarUrl());
        embBuilder.setColor(Color.decode("#1e272e"));
        embBuilder.setDescription(Text);
        txtChannel.sendMessage(embBuilder.build()).queue();


    }

    /**
     * @param Title       Title of the Message
     * @param Text        The content of the message body
     * @param member      The member who should be used for {@link EmbedBuilder#setAuthor(String, String, String)}
     * @param txtChannel  Chooses the channel the message should be send to
     * @param deleteAfter After how many seconds the message should be deleted
     */
    public static void blackBuilder(String Title, String Text, Member member, TextChannel txtChannel, int deleteAfter) {
        EmbedBuilder embBuilder = new EmbedBuilder();
        embBuilder.setTitle(Title);
        embBuilder.setAuthor(member.getUser().getName(), member.getUser().getAvatarUrl(), member.getUser().getAvatarUrl());
        embBuilder.setColor(Color.decode("#1e272e"));
        embBuilder.setDescription(Text);
        txtChannel.sendMessage(embBuilder.build()).queue(message -> message.delete().queueAfter(deleteAfter, TimeUnit.SECONDS));
    }

    //Open a private channel with a user and sends them a message

    /**
     * @param Title Title of the Message
     * @param Text  The content of the message body
     * @param user  The member who should be used for {@link EmbedBuilder#setAuthor(String, String, String)} and the person the message is send to {@link User#openPrivateChannel()}
     */
    public static void privateBlackBuilder(String Title, String Text, User user) {
        EmbedBuilder embBuilder = new EmbedBuilder();
        embBuilder.setTitle(Title);
        embBuilder.setAuthor(user.getName(), user.getAvatarUrl(), user.getAvatarUrl());
        embBuilder.setColor(Color.decode("#1e272e"));
        embBuilder.setDescription(Text);
        user.openPrivateChannel().queue(PrivateChannel -> PrivateChannel.sendMessage(embBuilder.build()).queue());
    }

    public static void privateYellowBuilder(String Title, String Text, User user) {
        EmbedBuilder embBuilder = new EmbedBuilder();
        embBuilder.setTitle(Title);
        embBuilder.setAuthor(user.getName(), user.getAvatarUrl(), user.getAvatarUrl());
        embBuilder.setColor(Color.decode("#f9ca24"));
        embBuilder.setDescription(Text);
        user.openPrivateChannel().queue(PrivateChannel -> PrivateChannel.sendMessage(embBuilder.build()).queue());
    }

    /**
     * @param Title Title of the Message
     * @param Text  The content of the message body
     * @param user  The member who should be used for {@link EmbedBuilder#setAuthor(String, String, String)} and the person the message is send to {@link User#openPrivateChannel()}
     */
    public static void privateRedBuilder(String Title, String Text, User user) {
        EmbedBuilder embBuilder = new EmbedBuilder();
        embBuilder.setTitle(Title);
        embBuilder.setAuthor(user.getName(), user.getAvatarUrl(), user.getAvatarUrl());
        embBuilder.setColor(Color.decode("#d63031"));
        embBuilder.setDescription(Text);
        user.openPrivateChannel().queue(PrivateChannel -> PrivateChannel.sendMessage(embBuilder.build()).queue());
    }

    public static void privateRedBuilder(String Title, String Text, User user, int deleteAfter) {
        EmbedBuilder embBuilder = new EmbedBuilder();
        embBuilder.setTitle(Title);
        embBuilder.setAuthor(user.getName(), user.getAvatarUrl(), user.getAvatarUrl());
        embBuilder.setColor(Color.decode("#d63031"));
        embBuilder.setDescription(Text);
        user.openPrivateChannel().queue(PrivateChannel -> PrivateChannel.sendMessage(embBuilder.build()).queue(message -> message.delete().queueAfter(deleteAfter, TimeUnit.SECONDS)));
    }

    /**
     * @param Title      Title of the Message
     * @param InfoText   The content of the message body
     * @param member     The member who should be used for {@link EmbedBuilder#setAuthor(String, String, String)}
     * @param txtChannel Chooses the channel the message should be send to
     */
    public static void yellowBuilder(String Title, String InfoText, Member member, TextChannel txtChannel) {
        EmbedBuilder embBuilder = new EmbedBuilder();
        embBuilder.setTitle(Title);
        embBuilder.setAuthor(member.getUser().getName(), member.getUser().getAvatarUrl(), member.getUser().getAvatarUrl());
        embBuilder.setColor(Color.decode("#f9ca24"));
        embBuilder.setDescription(InfoText);
        txtChannel.sendMessage(embBuilder.build()).queue();
    }

    public static void yellowBuilder(String Title, String InfoText, Member member, TextChannel txtChannel, int deleteAfter) {
        EmbedBuilder embBuilder = new EmbedBuilder();
        embBuilder.setTitle(Title);
        embBuilder.setAuthor(member.getUser().getName(), member.getUser().getAvatarUrl(), member.getUser().getAvatarUrl());
        embBuilder.setColor(Color.decode("#f9ca24"));
        embBuilder.setDescription(InfoText);
        txtChannel.sendMessage(embBuilder.build()).queue();
        txtChannel.sendMessage(embBuilder.build()).queue(message -> message.delete().queueAfter(deleteAfter, TimeUnit.SECONDS));
    }


    public static void usageHelp(String InfoText, Member member, TextChannel txtChannel) {
        EmbedBuilder embBuilder = new EmbedBuilder();
        embBuilder.setTitle("Usage Help");
        embBuilder.setAuthor(member.getUser().getName(), member.getUser().getAvatarUrl(), member.getUser().getAvatarUrl());
        embBuilder.setColor(Color.decode("#f9ca24"));
        embBuilder.setDescription(InfoText);
        txtChannel.sendMessage(embBuilder.build()).complete().delete().queueAfter(10, TimeUnit.SECONDS);


    }

    public static void greenBuilder(String Title, String InfoText, Member member, TextChannel txtChannel) {
        EmbedBuilder embBuilder = new EmbedBuilder();
        embBuilder.setTitle(Title);
        embBuilder.setAuthor(member.getUser().getName(), member.getUser().getAvatarUrl(), member.getUser().getAvatarUrl());
        embBuilder.setColor(Color.decode("#2ecc71"));
        embBuilder.setDescription(InfoText);
        txtChannel.sendMessage(embBuilder.build()).queue();


    }


    public static void greenBuilder(String Title, String InfoText, Member member, TextChannel txtChannel, int deleteAfter) {
        EmbedBuilder embBuilder = new EmbedBuilder();
        embBuilder.setTitle(Title);
        embBuilder.setAuthor(member.getUser().getName(), member.getUser().getAvatarUrl(), member.getUser().getAvatarUrl());
        embBuilder.setColor(Color.decode("#2ecc71"));
        embBuilder.setDescription(InfoText);
        txtChannel.sendMessage(embBuilder.build()).queue(message -> message.delete().queueAfter(deleteAfter, TimeUnit.SECONDS));

    }

    public static void greenBuilder(String Title, String InfoText, Member member, TextChannel txtChannel, List<String> reactionToAdd) {
        EmbedBuilder embBuilder = new EmbedBuilder();
        embBuilder.setTitle(Title);
        embBuilder.setAuthor(member.getUser().getName(), member.getUser().getAvatarUrl(), member.getUser().getAvatarUrl());
        embBuilder.setColor(Color.decode("#2ecc71"));
        embBuilder.setDescription(InfoText);
        txtChannel.sendMessage(embBuilder.build()).queue(message -> {
            for (String react : reactionToAdd) {
                message.addReaction(react).queue();
            }
        });
    }
    public static MessageAction greenBuilderReturn(String Title, String InfoText, Member member, TextChannel txtChannel) {
        EmbedBuilder embBuilder = new EmbedBuilder();
        embBuilder.setTitle(Title);
        embBuilder.setAuthor(member.getUser().getName(), member.getUser().getAvatarUrl(), member.getUser().getAvatarUrl());
        embBuilder.setColor(Color.decode("#2ecc71"));
        embBuilder.setDescription(InfoText);
       return txtChannel.sendMessage(embBuilder.build());
    }


    public static void greenBuilderWithReactions(String Title, String InfoText, Member member, TextChannel txtChannel, List<Emote> reactionToAdd) {
        EmbedBuilder embBuilder = new EmbedBuilder();
        embBuilder.setTitle(Title);
        embBuilder.setAuthor(member.getUser().getName(), member.getUser().getAvatarUrl(), member.getUser().getAvatarUrl());
        embBuilder.setColor(Color.decode("#2ecc71"));
        embBuilder.setDescription(InfoText);
        txtChannel.sendMessage(embBuilder.build()).queue(message -> {
            for (Emote react : reactionToAdd) {
                message.addReaction(react).queue();
            }
        });
    }

    public static void blueBuilder(String Title, String InfoText, TextChannel txtChannel, Member member, boolean delete, int deleteAfter) {
        EmbedBuilder embBuilder = new EmbedBuilder();
        embBuilder.setTitle(Title);
        embBuilder.setAuthor(member.getUser().getName(), member.getUser().getAvatarUrl(), member.getUser().getAvatarUrl());
        embBuilder.setColor(Color.decode("#00a8ff"));
        embBuilder.setDescription(InfoText);
        if (delete) {
            txtChannel.sendMessage(embBuilder.build()).queue(message -> message.delete().queueAfter(deleteAfter, TimeUnit.SECONDS));
        } else {
            txtChannel.sendMessage(embBuilder.build()).queue();
        }

    }


    public static void generalBuilder(Member member, TextChannel txtChannel, String MessageColor, String Title, String Desc, int toDeleteAfter) {
        EmbedBuilder embBuilder = new EmbedBuilder();
        embBuilder.setTitle(Title);
        embBuilder.setAuthor(member.getUser().getName(), member.getUser().getAvatarUrl(), member.getUser().getAvatarUrl());
        embBuilder.setColor(Color.decode(MessageColor));
        embBuilder.setDescription(Desc);
        txtChannel.sendMessage(embBuilder.build()).complete().delete().queueAfter(toDeleteAfter, TimeUnit.SECONDS);
    }

    public static void generalBuilder(Member member, TextChannel txtChannel, String messageColor, String title, String description) {
        EmbedBuilder embBuilder = new EmbedBuilder();
        embBuilder.setTitle(title);
        embBuilder.setAuthor(member.getUser().getName(), member.getUser().getAvatarUrl(), member.getUser().getAvatarUrl());
        embBuilder.setColor(Color.decode(messageColor));
        embBuilder.setDescription(description);
        txtChannel.sendMessage(embBuilder.build()).queue();

    }


    public static void privateGeneral(Member member, String MessageColor, String Title, String Desc) {
        EmbedBuilder embBuilder = new EmbedBuilder();
        embBuilder.setTitle(Title);
        embBuilder.setAuthor(member.getUser().getName(), member.getUser().getAvatarUrl(), member.getUser().getAvatarUrl());
        embBuilder.setColor(Color.decode(MessageColor));
        embBuilder.setDescription(Desc);
        member.getUser().openPrivateChannel().queue(PrivateChannel -> PrivateChannel.sendMessage(embBuilder.build()).queue());

    }

    public static void privateGeneral(User user, String MessageColor, String Title, String Desc) {
        EmbedBuilder embBuilder = new EmbedBuilder();
        embBuilder.setTitle(Title);
        embBuilder.setAuthor(user.getName(), user.getAvatarUrl(), user.getAvatarUrl());
        embBuilder.setColor(Color.decode(MessageColor));
        embBuilder.setDescription(Desc);
        user.openPrivateChannel().queue(PrivateChannel -> PrivateChannel.sendMessage(embBuilder.build()).queue());

    }


    public static List<Member> getMembersWithRoleByString(Guild server, String roleName) {
        return server.getMembersWithRoles(server.getRolesByName(roleName, true).get(0));
    }


    public static void addRoleToMemberByName(Member member, String roleName) {
        member.getGuild().addRoleToMember(member, member.getGuild().getRolesByName(roleName, true).get(0)).queue();
    }

    public static void addRoleToMember(Member member, Role role) {
        member.getGuild().addRoleToMember(member, role).queue();
    }

    public static void addRoleToMemberByString(Member member, Guild server, String RoleName, int whichRoleToGet, boolean ignoreCase) {
        server.addRoleToMember(member, server.getRolesByName(RoleName, ignoreCase).get(whichRoleToGet)).queue();
    }


    public static void addRoleToMemberByID(Member member, long roleID) {
        if (member.getGuild().getRoleById(roleID) == null) throw new NullPointerException();
        member.getGuild().addRoleToMember(member, member.getGuild().getRoleById(roleID)).queue();
    }

    public static void removeRoleFromMember(Member member, Guild server, String RoleName, int whichRoleToGet, boolean ignoreCase) {
        server.removeRoleFromMember(member, server.getRolesByName(RoleName, ignoreCase).get(whichRoleToGet)).queue();
    }

    public static void removeRoleFromMember(Member member, String RoleName) {
        member.getGuild().removeRoleFromMember(member, member.getGuild().getRolesByName(RoleName, true).get(0)).queue();
    }


    public static void removeRoleFromMember(Member member, long roleId) {
        member.getGuild().removeRoleFromMember(member, member.getGuild().getRoleById(roleId)).queue();
    }

    /**
     * Sleep timer of 15sek because of gathering server members properly
     * <p>
     * INFO: May need {@link net.dv8tion.jda.api.utils.MemberCachePolicy#ALL} turned on!
     */
    public static void addRoleToAllMembers(Guild server, Role toAdd) {
        Timer timer = new Timer();

        List<Member> received = new ArrayList<>();
        server.loadMembers(received::add);

        TimerTask waitTask = new TimerTask() {
            @Override
            public void run() {
                if (received.isEmpty()) {
                    cancel();
                    return;
                }
                addRoleToMember(received.get(0), toAdd);
                received.remove(0);
            }
        };
        timer.scheduleAtFixedRate(waitTask, 15000L, 250L);
    }

    //Gets all messages in a channel
    public static List<Message> getChannelMessages(TextChannel textChannel, int limit) {
        return textChannel.getIterableHistory().stream()
                .limit(limit)
                .collect(Collectors.toList());
    }

    /**
     * Gets a message with a specific id
     */
    public static Message getChannelMessage(TextChannel textChannel, String id) {
        return textChannel.getIterableHistory().stream()
                .filter(message -> message.getId().equals(id))
                .collect(Collectors.toList()).get(0);
    }

    /**
     * Gets all messages in a channel
     */
    public static List<Message> getPrivateChannelMessages(PrivateChannel textChannel, int limit) {
        return textChannel.getIterableHistory().stream()
                .limit(limit)
                .collect(Collectors.toList());
    }


}