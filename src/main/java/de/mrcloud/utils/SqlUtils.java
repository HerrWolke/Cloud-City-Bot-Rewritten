package de.mrcloud.utils;

import de.mrcloud.main.CloudCityBot2;
import net.dv8tion.jda.api.entities.Member;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SqlUtils {
    /**
     *
     * @param connection The connection this should be executed for
     * @param column The column this value should be fetched from
     * @param member Used to find the right line in the collum
     */
    public static String getSqlColumnString(Connection connection, String column, Member member) {
        String toGet = "";
        try {
            Statement statement = connection.createStatement();

            ResultSet result = statement.executeQuery("SELECT * FROM Users WHERE UserID = " + member.getId() + ";");
            while (result.next()) {
                toGet = result.getString(column);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getLocalizedMessage());
        }
        return toGet;
    }

    /*
    Same as for the string but returns a int
     */
    public static int getSqlColumnInt(Connection connection, String columnName, Member member) {
        int toGet = 0;
        try {
            Statement statement = connection.createStatement();

            ResultSet result = statement.executeQuery("SELECT * FROM Users WHERE UserID = " + member.getId() + ";");
            while (result.next()) {
                toGet = result.getInt(columnName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getLocalizedMessage());
        }
        return toGet;
    }

    public static int getSqlColumnInt(Connection connection, String columnName, Long memberID) {
        int toGet = 0;
        try {
            Statement statement = connection.createStatement();

            ResultSet result = statement.executeQuery("SELECT * FROM Users WHERE UserID = " +memberID + ";");
            while (result.next()) {
                toGet = result.getInt(columnName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println(e.getLocalizedMessage());
        }
        return toGet;
    }
    /*
    Sets a column
     */
    public static void setSQLCollum(Connection connection, String ID, String collumName, String toFillWith) {
        try {
            Statement statement = connection.createStatement();

            ResultSet result = statement.executeQuery("SELECT * FROM Users WHERE UserID = " + ID + ";");
            if (result != null && result.next()) {
                statement.executeQuery("UPDATE Users SET " + collumName + " = '" + toFillWith + "' WHERE UserID = " + ID + ";");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * WARNING: Requieres a database with a collum for userID.
     *
     * @param connection The database connecting from which the data should be written to
     * @param ID         The ID of the user that it should be written to
     * @param collumName The name of the collum that should be replaced with the int
     * @param toFillWith The value that should be written
     * @throws SQLException May cause a {@link SQLException} otherwise
     */
    public static void setSQLCollumInt(Connection connection, String ID, String collumName, int toFillWith) {
        try {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM Users WHERE UserID = " + ID + ";");
            if (result != null && result.next()) {
                statement.executeQuery("UPDATE Users SET " + collumName + " = '" + toFillWith + "' WHERE UserID = " + ID + ";");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * WARNING: Requires a database with a column for userID.
     *
     * @param connection The database connecting from which the data should be written to
     * @param ID         The ID of the user that it should be written to
     * @param collumName The name of the column that should be replaced with the int
     * @param toFillWith The value that should be written
     * @throws SQLException May cause a {@link SQLException} otherwise
     */
    public static void setSQLCollumBoolean(Connection connection, String ID, String collumName, boolean toFillWith) {
        try {
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery("SELECT * FROM Users WHERE UserID = " + ID + ";");
            if (result != null && result.next()) {
                statement.executeQuery("UPDATE Users SET " + collumName + " = " + toFillWith + " WHERE UserID = " + ID + ";");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static String getMemberPreferredLanguage(Member member) {
            return getSqlColumnString(CloudCityBot2.getInstance().getDbHandler().getConnection(),"language",member);
    }

}
