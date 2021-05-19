package de.mrcloud.listeners.statistics;

import de.mrcloud.main.CloudCityBot2;
import de.mrcloud.utils.SqlUtils;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

public class MessageCountListener extends ListenerAdapter {
    @Override
    public void onGuildMessageReceived(@NotNull GuildMessageReceivedEvent e) {
        super.onGuildMessageReceived(e);

        if(e.isWebhookMessage()) return;

        SqlUtils.increaseSQLCollumInt(CloudCityBot2.getInstance().getDbHandler().getConnection(),e.getMember().getId(),"MessageCount",1);
    }
}
