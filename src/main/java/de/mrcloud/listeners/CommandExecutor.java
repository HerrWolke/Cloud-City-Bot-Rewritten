package de.mrcloud.listeners;

import de.mrcloud.command.Command;
import de.mrcloud.utils.Settings;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;

import javax.annotation.Nonnull;


public class CommandExecutor extends ListenerAdapter {
    @Override
    public void onGuildMessageReceived(@Nonnull GuildMessageReceivedEvent e) {
        super.onGuildMessageReceived(e);

        
        if (!e.getMessage().getContentRaw().startsWith(Command.PREFIX)) return;
        
        String[] splitContent = e.getMessage().getContentRaw().split("\\s++");
        String command = splitContent[0];
        if (command.startsWith(Command.PREFIX)) {
            String[] args = new String[splitContent.length - 1];
            for (int i = 0; i < splitContent.length; i++) {
                if (i > 0) args[i - 1] = splitContent[i];
            }

            for (Command cmd : Settings.commands) {
                
                if ((Command.PREFIX + cmd.name).equalsIgnoreCase(command)) {
                    
                    if (cmd.category != Command.Category.DEBUG && cmd.category != Command.Category.STAFF) {
                        cmd.execute(e, args);
                    } else {
                        
                        if (cmd.name.equals("startraid")) {
                            String[] args2 = e.getMessage().getContentRaw().split("\\s*\\|\\s*");
                            cmd.execute(e, args2);
                            return;
                        }
                        if (cmd.category.equals(Command.Category.DEBUG) && !e.getMember().hasPermission(Permission.ADMINISTRATOR))
                            return;

                        cmd.execute(e, args);
                    }

                    break;
                }
            }
        }
    }
}
