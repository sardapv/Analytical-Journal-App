package sample;

import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class LoginModel {


    public static int id_from_table;

    Connection connection;
    public LoginModel(){
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
    public boolean login(String userid, String userpass) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String query = "SELECT * FROM user WHERE username = ? AND passkey = ?";
        try{
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,userid);
            preparedStatement.setString(2,userpass);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                id_from_table = resultSet.getInt("id");
                //System.out.println("HIHIH :"+id_from_table);
                return true;
            }
            else {
              // System.out.println(resultSet.next());
                return false;
            }
        }
        catch (SQLException e){
            e.printStackTrace();
        }

        finally {
            preparedStatement.close();
            resultSet.close();
        }
        return false;

    }

}
