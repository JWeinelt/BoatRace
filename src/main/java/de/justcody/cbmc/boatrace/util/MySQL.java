package de.justcody.cbmc.boatrace.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQL {
    private Connection conn;

    /**
     * Connects to the MySQL database server.
     */
    public void connect() {
        final String DB_NAME = "jdbc:mysql://45.137.68.46:3306/waterfall?useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
        final String USER = "plugins";
        final String PASS = "3u&1,9P]TbswIkE+1zp=*=yaF8Kl92w30£=5fHe7bnChf|yvE)";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); //Gets the driver class

            conn = DriverManager.getConnection(DB_NAME, USER, PASS); //Gets a connection to the database using the details you provided.

        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
    }
    private void checkConnection() {
        try {
            if (conn == null || conn.isClosed()) connect();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
