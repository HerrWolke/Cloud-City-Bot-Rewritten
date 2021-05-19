package de.mrcloud.listeners.statistics;

import de.mrcloud.main.CloudCityBot2;
import de.mrcloud.utils.SqlUtils;
import net.dv8tion.jda.api.entities.Member;

import java.sql.Connection;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class ChannelTimeHandler {
    private LinkedHashMap<Long, Date> userVoiceChannelTime;

    public ChannelTimeHandler() {
        userVoiceChannelTime = new LinkedHashMap<>();
        moveNextMonth();
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
}
