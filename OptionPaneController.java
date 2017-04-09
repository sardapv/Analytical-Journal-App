package sample;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Created by pranav on 09/04/17.
 */
public class OptionPaneController {
    public void writeNewLog(){
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/sample/editor.fxml"));
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
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/sample/modifying_editor.fxml"));
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
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/sample/Alllogs.fxml"));
            Parent root3 = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root3));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
