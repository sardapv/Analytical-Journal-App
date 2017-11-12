package Diary;

import javafx.embed.swing.JFXPanel;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by pranav on 01/11/17.
 */
public class EditorControllerTest {
    @Test
    public void submit() throws Exception {
        EditorDatabase editorDatabase= new EditorDatabase();
        String titlename = "Testing Junit";
        String date = "2017-13-19";
        double rating = 8.8;
        String matter = "I am happy here yuhuhu";
        editorDatabase.dailydata(1, titlename, date, matter, "I am happy here yuhuhu", "", (float) rating);
        //print(titlename, date, matter, globaladd, (float) rating);

    }

}