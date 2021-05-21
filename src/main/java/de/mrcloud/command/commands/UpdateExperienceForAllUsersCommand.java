package de.mrcloud.command.commands;

import de.mrcloud.command.Command;
import de.mrcloud.main.CloudCityBot2;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.TimeUnit;

public class UpdateExperienceForAllUsersCommand extends Command {
    public UpdateExperienceForAllUsersCommand() {
        super("updateXp", "", "", Category.STAFF, 617062477812989980L);
    }

    @Override
    public boolean execute(GuildMessageReceivedEvent e, String[] args) {
        if (checkChannelLock(e)) {
            Connection conn = CloudCityBot2.getInstance().getDbHandler().getConnection();
            Statement statement = null;
            try {
                statement = conn.createStatement();
                ResultSet set = statement.executeQuery("SELECT * FROM UserStatistics");


                while (set.next()) {
                    long diff = TimeUnit.DAYS.toMillis(set.getInt("lifetimeDay")) + TimeUnit.HOURS.toMillis(set.getInt("lifetimeHour")) + TimeUnit.MINUTES.toMillis(set.getInt("lifetimeMin")) + TimeUnit.SECONDS.toMillis(set.getInt("lifetimeSec"));
                    CloudCityBot2.getInstance().getTimeHandler().calculateExperienceTime(diff, set.getLong("userId"));
                    CloudCityBot2.getInstance().getTimeHandler().calculateExperienceMessage(set.getLong("userId"), set.getDouble("messageCount"));
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

        }
        return false;
    }

    @Override
    public boolean execute(MessageReceivedEvent e, String[] args) {
        return false;
    }
}
