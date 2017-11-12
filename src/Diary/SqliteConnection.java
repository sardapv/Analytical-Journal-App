package Diary;

import java.sql.*;
import java.sql.DriverManager;

/**
 * Created by pranav on 25/03/17.
 */
public class SqliteConnection {
    public static Connection Connector() throws ClassNotFoundException {
        try {
            Class.forName("org.sqlite.JDBC");
//            System.out.println(SqliteConnection.class.getProtectionDomain().getCodeSource().getLocation().getPath()+"Diary.sqlite");


            Connection conn = DriverManager.getConnection("jdbc:sqlite:Diary.sqlite");
            return conn;
        }
        catch (SQLException e) {
            return null;
        }
    }
}
