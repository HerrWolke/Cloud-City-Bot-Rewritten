package de.mrcloud.command;

import de.mrcloud.main.CloudCityBot2;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class GameCommand extends Command {
    public int requiredLevel;

    public GameCommand(String name, String description, String usage,int requiredLevel) {
        super(name, description, usage, Category.GAME);
        this.requiredLevel = requiredLevel;
    }

    public boolean checkLevel() {
        Connection connection = CloudCityBot2.getInstance().getDbHandler().getConnection();
        try {


            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT level FROM Users");

            if (requiredLevel <= resultSet.getInt("level")) return true;


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return false;
    }
}
