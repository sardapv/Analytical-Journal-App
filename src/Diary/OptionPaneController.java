package Diary;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Created by pranav on 09/04/17.
 */


public class OptionPaneController implements Initializable {
    @FXML
    public Button logout;
    @FXML
    public Button search;

    public void writeNewLog(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxmls/editor.fxml"));
            Parent root3 = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root3));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void modifyOldLog(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxmls/modifying_editor.fxml"));
            Parent root3 = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root3));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void showAllLogs(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxmls/Alllogs.fxml"));
            Parent root3 = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root3));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void analyse(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxmls/analysis.fxml"));
            Parent root3 = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root3));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    Connection connection;
    public static String globalbackupdir;
    public void push2Cloud() throws IOException, SQLException {
      /*  try {
            connection = SqliteConnection.Connector();
            if (connection == null) {
                System.out.print("No connection");
            }
        }catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String query = "SELECT * FROM user WHERE id = ?";
        String query2 = "SELECT * FROM DATA WHERE id = ?";
        String backuppath = null;
        try{
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1,Controller.id_logged_in);

            resultSet = preparedStatement.executeQuery();

            if(resultSet.next()){
                backuppath = resultSet.getString("backupdir");
            }
            globalbackupdir = backuppath;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            preparedStatement = connection.prepareStatement(query2);
            preparedStatement.setInt(1,Controller.id_logged_in);
            resultSet = preparedStatement.executeQuery();
            int count = 1;
            while (resultSet.next()){
                File yourFile = new File(backuppath+"/"+resultSet.getString("dateofday")+ " | "+ resultSet.getString("title")+".txt");
                if(!yourFile.exists())
                    yourFile.createNewFile();
                FileWriter fileWriter = new FileWriter(backuppath+"/"+resultSet.getString("dateofday")+ " | " + resultSet.getString("title")+".txt");

                fileWriter.write("\nDate : "+resultSet.getString("dateofday"));
                fileWriter.write("\n\n---------------------------------------------------------------------------------------");
                fileWriter.write("\n\nTitle : "+resultSet.getString("title"));
                fileWriter.write("\n\n=======================================================================================");
                fileWriter.write("\n\nStory of the day : \n\n\t "+resultSet.getString("matter"));
                fileWriter.write("\n\n=======================================================================================");
                fileWriter.write("\n\nMy Day on scale of 0 to 10 : "+resultSet.getFloat("rating"));
                float temp = resultSet.getFloat("rating");
                if(temp <= 4){
                    fileWriter.write("( SAD )");
                }
                else if(temp <=7 && temp > 4){
                    fileWriter.write("( MODERATE )");
                }
                else if(temp <=10 && temp >7){
                    fileWriter.write("( HAPPY )");
                }
                fileWriter.write("\n\n---------------------------------------------------------------------------------------");
                fileWriter.write("\n\nMedia memory : "+resultSet.getString("mediapath"));
                fileWriter.write("\n\n---------------------------------------------------------------------------------------");
                fileWriter.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            connection.close();
        }
            */
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxmls/downloadsts.fxml"));
            Parent root3 = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root3));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void logoutwindow(ActionEvent event) throws IOException {
        ((Node)event.getSource()).getScene().getWindow().hide();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxmls/login.fxml"));
        Parent root3 = fxmlLoader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root3));
        stage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        logout.setShape(new Circle(60));
        logout.setMinSize(120,120);
        logout.setMaxSize(120,120);
    }
    public void search(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxmls/search.fxml"));
        Parent root3 = fxmlLoader.load();
        Stage stage = new Stage();
        stage.setTitle("Search in Note!T");
        stage.setScene(new Scene(root3));
        stage.show();
    }
}
