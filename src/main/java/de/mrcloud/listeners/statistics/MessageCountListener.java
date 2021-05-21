package de.mrcloud.listeners.statistics;

import de.mrcloud.main.CloudCityBot2;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class MessageCountListener extends ListenerAdapter {
    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent e) {
        super.onGuildMessageReceived(e);

        if (e.isWebhookMessage() || e.getMessage().getContentRaw().startsWith("!") || e.getMessage().getContentRaw().startsWith(";") || e.getMessage().getContentRaw().startsWith("&") || e.getAuthor().isBot())
            return;

        CloudCityBot2.getInstance().getTimeHandler().receivedMessage(e.getMember().getIdLong());

    }
}
