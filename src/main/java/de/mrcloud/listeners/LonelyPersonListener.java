package de.mrcloud.listeners;

import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;
import java.text.Normalizer;

public class LonelyPersonListener extends ListenerAdapter {
    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent e) {
        super.onGuildMessageReceived(e);

        Member member = e.getMember();
        Message message = e.getMessage();
        String messageContent =message.getContentRaw();

//
//        if(isSynonym())

    }

    public boolean isSynonym(String toSearchForSyn,String syn) {

//        if (!(synsets.length > 0 )) return true;
        return false;
    }

    public static String removeAccents(String text) {
        return text == null ? null :
                Normalizer.normalize(text, Normalizer.Form.NFD)
                        .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }
}
