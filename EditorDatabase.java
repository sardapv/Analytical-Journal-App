package sample;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by pranav on 04/04/17.
 */
public class EditorDatabase {

    Connection connection;
    public EditorDatabase(){
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
    public void dailydata(Integer id,String title,String datechoosen,String matter,String htmlmatter,String mediapath,float rating) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String query = "INSERT INTO Data(id,dateofday,title,matter,mediapath,rating,htmlmatter) VALUES (?,?,?,?,?,?,?)";
        try{
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1,id);
            preparedStatement.setString(2,datechoosen);
            preparedStatement.setString(3,title);
            preparedStatement.setString(4,matter);
            preparedStatement.setString(5,mediapath);
            preparedStatement.setFloat(6,rating);
            preparedStatement.setString(7,htmlmatter);
            System.out.println(preparedStatement.executeUpdate());

        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            preparedStatement.close();
        }
    }
    public void modifyData(Integer id,String title,String datechoosen,String matter,String htmlmatter,String mediapath,float rating) throws SQLException {
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String query = "UPDATE Data SET title = ?, matter = ?, mediapath = ?, rating = ?, htmlmatter= ? WHERE (id = ? AND dateofday = ?)";
        try{
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1,title);
            preparedStatement.setString(2,matter);
            preparedStatement.setString(3,mediapath);
            preparedStatement.setFloat(4,rating);
            preparedStatement.setString(5,htmlmatter);
            preparedStatement.setInt(6,id);
            preparedStatement.setString(7,datechoosen);
            System.out.println(preparedStatement.executeUpdate());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            preparedStatement.close();
        }
    }
}
