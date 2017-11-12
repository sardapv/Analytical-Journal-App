package Diary;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by pranav on 25/03/17.
 */
public class SignUpModel {



    Connection connection;
    public SignUpModel(){
        try {
            connection = SqliteConnection.Connector();
            if (connection == null) {
                System.out.print("NOT CONNECTED");
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
    public boolean isDbconnected() {
        try {
            return !(connection.isClosed());
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public void signupuser(String fname,String lname,String gmail,String dob,String phonenumber,String gender,String description, String loginid, String key, String backupdirec) throws SQLException {


        PreparedStatement preparedStatement = null;

        String query = "INSERT INTO user(firstname,lastname,email,dob,phonenumber,gender,about,username,passkey,backupdir) VALUES (?,?,?,?,?,?,?,?,?,?)";
        try{
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,fname);
            preparedStatement.setString(2,lname);
            preparedStatement.setString(3,gmail);
            preparedStatement.setString(4,dob);
            preparedStatement.setString(5,phonenumber);
            preparedStatement.setString(6,gender);
            preparedStatement.setString(7,description);
            preparedStatement.setString(8,loginid);
            preparedStatement.setString(9,key);
            preparedStatement.setString(10,backupdirec);
            System.out.println(preparedStatement.executeUpdate());

        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            preparedStatement.close();
        }
    }

}
