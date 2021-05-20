package de.mrcloud.sql;


import de.mrcloud.utils.Settings;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@SuppressWarnings("SpellCheckingInspection")
public class DatabaseConnectionHandler {
    private Connection connection = null;
    private boolean haltingRefresh;


    public void handleConnection() {
        new Thread(() -> {
            while (true) {
                try {
                    connection = mariaDB();


                    Thread.sleep(20000);
                    while (haltingRefresh) {
                        Thread.sleep(500);
                    }

                    connection.close();

                } catch (InterruptedException | SQLException e) {
                    e.printStackTrace();
                }
            }

        }).start();

    }

    private Connection mariaDB() {

        Connection conn;
        try {
            Class.forName("org.mariadb.jdbc.Driver");


            conn = DriverManager.getConnection(Settings.DB_CONNECT_URL_RASP, "root", Settings.DB_PW);

            return conn;
        } catch (Exception e) {
            try {
                conn = DriverManager.getConnection(Settings.DB_CONNECT_URL_PC, "root", Settings.DB_PW);
                return conn;
            } catch (SQLException throwables) {
                System.out.println(e.getLocalizedMessage());
                System.out.println("Error while connecting to DB");
                System.exit(0);
                return null;
            }

        }
    }


    public Connection getConnection() {
        return connection;
    }

    public void setConnection(Connection connection) {
        this.connection = connection;
    }


    public void setHaltRefresh(boolean haltingRefresh) {
        this.haltingRefresh = haltingRefresh;
    }

    /**
     * @param haltingRefresh If the refreshing should be halted or not
     * @param delay          Delay in ms
     */
    public void haltRefresh(boolean haltingRefresh, long delay) {
        new Thread(() -> {
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            this.haltingRefresh = haltingRefresh;
        });

    }
}
