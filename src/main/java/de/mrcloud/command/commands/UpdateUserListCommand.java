package de.mrcloud.command.commands;

import de.mrcloud.command.Command;
import de.mrcloud.main.CloudCityBot2;
import de.mrcloud.utils.JDAUtils;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.events.message.guild.GuildMessageReceivedEvent;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class UpdateUserListCommand extends Command {
    public UpdateUserListCommand() {
        super("update", "description", "usage", Category.STAFF, 617062477812989980L);
    }

    @Override
    public boolean execute(GuildMessageReceivedEvent e, String[] args) {
        List<Long> userFromDb = new ArrayList<>();
        List<Long> usersFromOther = new ArrayList<>();
        if (checkChannelLock(e)) {
            Connection connection = CloudCityBot2.getInstance().getDbHandler().getConnection();
            try {
                Statement statement = connection.createStatement();

                ResultSet result = statement.executeQuery("SELECT * FROM Users;");
                ResultSet result2 = statement.executeQuery("SELECT * FROM UserStatistics;");
                while (result2.next()) {

                    usersFromOther.add(result2.getLong("userId"));
                }

                while (result.next()) {
                    userFromDb.add(result.getLong("UserID"));

                }


                for (Member member : e.getGuild().getMembers()) {
                    if (!userFromDb.contains(member.getIdLong())) {
                        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd-MM-yyyy  HH:mm:ss");
                        String formated = member.getTimeJoined().format(format);

                        statement.executeQuery("INSERT INTO Users(UserName,dateJoined,UserID)" + "\n" + "VALUES('" + member.getUser().getName() + "','" + formated + "'," + member.getId() + ");");

                    }

                    if (!usersFromOther.contains(member.getIdLong())) {
                        statement.executeQuery("INSERT INTO UserStatistics(UserID)" + "\n" + "VALUES(" + member.getId() + ");");
                    }
                }

            } catch (SQLException ex) {
                ex.printStackTrace();
                System.out.println(ex.getLocalizedMessage());
            }


        } else {
            JDAUtils.wrongChannel(e.getMember(), e.getChannel(), 10);
        }
        return false;
    }

    @Override
    public boolean execute(MessageReceivedEvent e, String[] args) {
        return false;
    }
}
