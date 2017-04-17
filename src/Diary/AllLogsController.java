package Diary;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Created by pranav on 10/04/17.
 */
public class AllLogsController implements Initializable {

    Connection connection;
    @FXML
    public ListView listView;
    public ObservableList<String> list = FXCollections.observableArrayList();;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            connection = SqliteConnection.Connector();
            if (connection == null) {
                System.out.print("No connection");
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String query = "SELECT * FROM DATA WHERE id = ?";
        String mediapath = null;
        try{
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1,Controller.id_logged_in);
            int count = 1;
            resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                mediapath = (count++) + " | "+ resultSet.getString("dateofday") + " | " + resultSet.getString("title");
                list.add(mediapath);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                listView.setItems(list);
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    public static String datehere;
    public void showEditor(){
        String record = listView.getSelectionModel().getSelectedItem().toString();
        String temp[] = record.split("\\ | ");
        datehere = temp[2];

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxmls/modifying_editor.fxml"));
            Parent root3 = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root3));
            stage.show();
            stage.setOnCloseRequest(event -> {
               datehere = null;
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
