package de.mrcloud.listeners.other;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.text.Normalizer;

public class JokeListener extends ListenerAdapter {
    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent e) {
        super.onGuildMessageReceived(e);

        Member member = e.getMember();
        Message message = e.getMessage();


        if(removeAccents(message.getContentRaw()).toLowerCase().contains("maus")) {
            message.addReaction("\uD83C\uDDE8").queue();
            message.addReaction("\uD83C\uDDFA").queue();
            message.addReaction("\uD83C\uDDF9").queue();
            message.addReaction("\uD83C\uDDEE").queue();
            message.addReaction("\uD83C\uDDEA").queue();
        }


    }

    public static String removeAccents(String text) {
        return text == null ? null :
                Normalizer.normalize(text, Normalizer.Form.NFD)
                        .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }
}
