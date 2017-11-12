package Diary;

import org.junit.Test;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static org.junit.Assert.*;

/**
 * Created by pranav on 11/11/17.
 */
public class SignUpModelTest {
    @Test
    public void signupuser() throws Exception {
        Connection connection = null;
        try {
            connection = SqliteConnection.Connector();
            if (connection == null) {
                System.out.print("NOT CONNECTED");
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        String fname = "Thor";
        String lname = "Odin";
        String gmail = "thorasguard@gmail.com";
        String dob = "1968-12-09";
        String phonenumber= "898979898";
        String gender = "Male";
        String description = "I am thor, son of odin, also known as odinson and god of thunder!";
        String loginid = "thor";
        String key = "odin";
        String backupdirec = "/Users/pranav/Documents/thorLogs";
    boolean flag = true;
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
            if(loginid == "" || key == "" || backupdirec == "")
                flag = false;
            System.out.println(preparedStatement.executeUpdate());
        } catch (SQLException e) {
            //e.printStackTrace();
            System.out.println("Because user already exists\nChecking failing condition");
            flag = false;
        }
        //to check pass
       //    assertEquals(true,flag);
        //to check fail
        assertEquals(false,flag);
    }
}