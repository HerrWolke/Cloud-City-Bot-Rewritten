package de.mrcloud.listeners.statistics;

import com.google.common.collect.ImmutableMap;
import de.mrcloud.main.CloudCityBot2;
import de.mrcloud.sql.DatabaseConnectionHandler;
import de.mrcloud.utils.discord.JDAUtils;
import de.mrcloud.utils.sql.SqlUtils;
import net.dv8tion.jda.api.entities.Member;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class StatisticsHandler {
    public static final double xpNeededForLevelUpGrowthFactor = 1.16;
    public static final double xpNeededForFirstLevel = 100;
    public static final double xpFactorPerSecondChannelTime = 0.023;
    public static final double baseCoinsPerLevel = 50.0;
    public static final double coinFactorOfLevel = 0.83;
    public static final ImmutableMap<Integer, String> rolesPerLevel = ImmutableMap.of(0, "Raindrop \uD83D\uDCA7", 5, "Snowflake❄️", 10, "Hailstone\uD83E\uDDCA", 20, "Snowball ⚪", 30, "Snowman ☃️");
    private LinkedHashMap<Long, Date> userVoiceChannelTime;

    public StatisticsHandler() {
        userVoiceChannelTime = new LinkedHashMap<>();
        moveNextMonth();
    }

    public void calculateExperienceTime(long diff, long memberId) {

        double xp = Math.round(TimeUnit.MILLISECONDS.toSeconds(diff) * xpFactorPerSecondChannelTime * 10.0) / 10.0;
        calculateLevel(xp, memberId);
    }

    public void calculateExperienceMessage(long memberId, double messageAmount) {
        double xp = messageAmount * 12.0;
        calculateLevel(xp, memberId);

    }

    private void updateMessageCount(long memberId) {
        SqlUtils.increaseSQLCollumInt(CloudCityBot2.getInstance().getDbHandler().getConnection(), Long.toString(memberId), "messageCount", 1, "UserStatistics");
    }

    private void calculateLevel(double xp, long memberId) {

        DatabaseConnectionHandler handler = CloudCityBot2.getInstance().getDbHandler();
        handler.haltRefresh(true, 5000);
        Connection connection = handler.getConnection();
        Statement statement = null;
        try {
            statement = connection.createStatement();
            ResultSet res = statement.executeQuery("SELECT * FROM UserStatistics WHERE userId = " + memberId);

            CloudCityBot2 cloudCityBot2 = CloudCityBot2.getInstance();
            if (!res.isBeforeFirst()) {
                JDAUtils.redBuilder("Fatal Error", "Attempted to update database information for user " + cloudCityBot2.getServer().getMemberById(memberId).getAsMention() + " but they dont have an entry in the database", cloudCityBot2.getServer().getSelfMember(), cloudCityBot2.getServer().getTextChannelById(617062477812989980L));
                try {
                    statement.executeUpdate("INSERT INTO UserStatistics(UserID) VALUES(" + memberId + ");");
                } catch (SQLException ex) {
                    JDAUtils.redBuilder("Fatal Error", "Attempted to insert the user " + cloudCityBot2.getServer().getMemberById(memberId).getAsMention() + " into the database but there was an error: " + ex.getMessage(), cloudCityBot2.getServer().getSelfMember(), cloudCityBot2.getServer().getTextChannelById(617062477812989980L));
                }

            }

            res.first();
            int currentLevel = res.getInt("level");
            double currentXp = res.getDouble("xp");

            double xpToLevelUp = Math.round((xpNeededForFirstLevel * Math.pow(xpNeededForLevelUpGrowthFactor, currentLevel)) * 10.0) / 10.0;
            int levelUps = 0;

            while ((currentXp + xp) >= xpToLevelUp) {
                levelUps++;
                xpToLevelUp = Math.round((xpNeededForFirstLevel * Math.pow(xpNeededForLevelUpGrowthFactor, currentLevel + levelUps)) * 10.0) / 10.0;

            }

            if (levelUps > 0) {
                if (!JDAUtils.hasRole(CloudCityBot2.getInstance().getServer().getMemberById(memberId), getRank(currentLevel + levelUps).getValue(), true)) {
                    JDAUtils.addRoleToMemberByName(CloudCityBot2.getInstance().getServer().getMemberById(memberId), getRank(currentLevel + levelUps).getValue());
                    JDAUtils.removeRoleFromMember(CloudCityBot2.getInstance().getServer().getMemberById(memberId), getLastRank(currentLevel + levelUps).getValue());
                }
                statement.executeUpdate("UPDATE UserStatistics SET level = " + (currentLevel + levelUps) + " WHERE userId = " + memberId);
                calculateCoins(currentLevel, memberId, levelUps);
            }
            statement.executeQuery("UPDATE UserStatistics SET xp = " + (currentXp + xp) + " WHERE userId = " + memberId);


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                statement.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }


    }


    private void calculateCoins(int level, long memberId, int levelUps) {
        double coins = 0;
        for (int i = 0; i < levelUps; i++) {
            coins += baseCoinsPerLevel + Math.round((level + i) * coinFactorOfLevel * 10.0) / 10.0;
        }


        DatabaseConnectionHandler handler = CloudCityBot2.getInstance().getDbHandler();
        handler.haltRefresh(true, 2000);
        Connection connection = handler.getConnection();
        Statement statement = null;
        try {
            statement = connection.createStatement();
            ResultSet res = statement.executeQuery("SELECT * FROM Users WHERE userId = " + memberId);
            res.first();
            double currentCoins = res.getDouble("coins");

            System.out.println("Settings coins to " + currentCoins + " + " + coins);
            statement.executeQuery("UPDATE Users SET coins = " + (currentCoins + coins) + " WHERE userId = " + memberId);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } finally {
            try {
                statement.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }

    }

    private void moveNextMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, calendar.get(Calendar.MONTH) + 1);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);


        Timer timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                for (Member member : CloudCityBot2.getInstance().getServer().getMembers()) {
                    setTimeLastMonth(member.getId());
                }
                moveNextMonth();
            }
        };
        timer.schedule(task, calendar.getTimeInMillis() - System.currentTimeMillis());
    }

    public void updateTime(long diff, long memberId) {
        updateTimeThisMonth(diff, memberId);
        updateAllTime(diff, memberId);
        calculateExperienceTime(diff, memberId);
    }

    private void updateTimeThisMonth(long diff, long memberId) {

        Connection connection = CloudCityBot2.getInstance().getDbHandler().getConnection();
        diff += TimeUnit.SECONDS.toMillis((SqlUtils.getSqlColumnInt(connection, "thisMonthSec", Long.toString(memberId), "UserStatistics")));
        diff += TimeUnit.MINUTES.toMillis((SqlUtils.getSqlColumnInt(connection, "thisMonthMin", Long.toString(memberId), "UserStatistics")));
        diff += TimeUnit.HOURS.toMillis((SqlUtils.getSqlColumnInt(connection, "thisMonthHour", Long.toString(memberId), "UserStatistics")));
        diff += TimeUnit.DAYS.toMillis((SqlUtils.getSqlColumnInt(connection, "thisMonthDay", Long.toString(memberId), "UserStatistics")));

        long days = TimeUnit.MILLISECONDS.toDays(diff);
        diff -= TimeUnit.DAYS.toMillis(days);
        long hours = TimeUnit.MILLISECONDS.toHours(diff);
        diff -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(diff);
        diff -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(diff);


        SqlUtils.setSQLCollumInt(connection, Long.toString(memberId), "thisMonthSec", (int) seconds, "UserStatistics");
        SqlUtils.setSQLCollumInt(connection, Long.toString(memberId), "thisMonthMin", (int) minutes, "UserStatistics");
        SqlUtils.setSQLCollumInt(connection, Long.toString(memberId), "thisMonthHour", (int) hours, "UserStatistics");
        SqlUtils.setSQLCollumInt(connection, Long.toString(memberId), "thisMonthDay", (int) days, "UserStatistics");
    }

    private void updateAllTime(long diff, long memberId) {
        Connection connection = CloudCityBot2.getInstance().getDbHandler().getConnection();
        diff += TimeUnit.SECONDS.toMillis((SqlUtils.getSqlColumnInt(connection, "lifetimeSec", Long.toString(memberId), "UserStatistics")));
        diff += TimeUnit.MINUTES.toMillis((SqlUtils.getSqlColumnInt(connection, "lifetimeMin", Long.toString(memberId), "UserStatistics")));
        diff += TimeUnit.HOURS.toMillis((SqlUtils.getSqlColumnInt(connection, "lifetimeHour", Long.toString(memberId), "UserStatistics")));
        diff += TimeUnit.DAYS.toMillis((SqlUtils.getSqlColumnInt(connection, "lifetimeDay", Long.toString(memberId), "UserStatistics")));

        long days = TimeUnit.MILLISECONDS.toDays(diff);
        diff -= TimeUnit.DAYS.toMillis(days);
        long hours = TimeUnit.MILLISECONDS.toHours(diff);
        diff -= TimeUnit.HOURS.toMillis(hours);
        long minutes = TimeUnit.MILLISECONDS.toMinutes(diff);
        diff -= TimeUnit.MINUTES.toMillis(minutes);
        long seconds = TimeUnit.MILLISECONDS.toSeconds(diff);


        SqlUtils.setSQLCollumInt(connection, Long.toString(memberId), "lifetimeSec", (int) seconds, "UserStatistics");
        SqlUtils.setSQLCollumInt(connection, Long.toString(memberId), "lifetimeMin", (int) minutes, "UserStatistics");
        SqlUtils.setSQLCollumInt(connection, Long.toString(memberId), "lifetimeHour", (int) hours, "UserStatistics");
        SqlUtils.setSQLCollumInt(connection, Long.toString(memberId), "lifetimeDay", (int) days, "UserStatistics");
    }

    private void setTimeLastMonth(String memberId) {
        Connection connection = CloudCityBot2.getInstance().getDbHandler().getConnection();


        SqlUtils.setSQLCollumInt(connection, memberId, "lastMonthSec", SqlUtils.getSqlColumnInt(connection, "thisMonthSec", memberId, "UserStatistics"), "UserStatistics");
        SqlUtils.setSQLCollumInt(connection, memberId, "lastMonthMin", SqlUtils.getSqlColumnInt(connection, "thisMonthMin", memberId, "UserStatistics"), "UserStatistics");
        SqlUtils.setSQLCollumInt(connection, memberId, "lastMonthHour", SqlUtils.getSqlColumnInt(connection, "thisMonthHour", memberId, "UserStatistics"), "UserStatistics");
        SqlUtils.setSQLCollumInt(connection, memberId, "lastMonthDay", SqlUtils.getSqlColumnInt(connection, "thisMonthDay", memberId, "UserStatistics"), "UserStatistics");
    }

    public void receivedMessage(long memberId) {
        calculateExperienceMessage(memberId, 1);
        updateMessageCount(memberId);
    }

    public Map.Entry<Integer, String> getRank(int level) {
        for (Map.Entry<Integer, String> entry : rolesPerLevel.entrySet()) {
            if (entry.getKey() <= level)
                return entry;
        }
        return null;
    }

    public Map.Entry<Integer, String> getLastRank(int level) {
        int lastLevel = 0;
        for (Map.Entry<Integer, String> entry : rolesPerLevel.entrySet()) {

            if (entry.getKey() <= level) {
                int finalLastLevel = lastLevel;
                return rolesPerLevel.entrySet().stream().filter(integerStringEntry -> integerStringEntry.getKey() == finalLastLevel).collect(Collectors.toList()).get(0);
            }

            lastLevel = entry.getKey();
        }
        return null;
    }
}
