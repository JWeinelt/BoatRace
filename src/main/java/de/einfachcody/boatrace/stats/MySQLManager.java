package de.einfachcody.boatrace.stats;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class MySQLManager {
    private Statement s;
    private Connection con;

    public void connect(String host, String user, String pw, int port, String dbName) {
        final String url = "jdbc:mysql://" +host +":" + port + "/" + dbName;

        try {
            Class.forName("com.mysql.jdbc.Driver"); //Gets the driver class

            con = DriverManager.getConnection(dbName, user, pw); //Gets a connection to the database using the details you provided.

            s = con.createStatement(); //Creates a statement. You can execute queries on this.
        }
        catch(Exception ex) {
            ex.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            con.close();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void createTable() {
     try {
         s.execute("CREATE TABLE player_data (" +
                 "id CHAR(36) PRIMARY KEY," +
                 "name VARCHAR(50)," +
                 "level INT," +
                 "drivers VARCHAR(255)" +
                 ");");
     } catch (SQLException ignored) {}
    }
}
