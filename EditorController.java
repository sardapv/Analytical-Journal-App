package sample;

import com.sun.javafx.scene.web.skin.HTMLEditorSkin;
import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.web.HTMLEditor;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.jsoup.Jsoup;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class EditorController implements Initializable {
    @FXML
    public TextField title;

    @FXML
    public Button submit,mediaupload,preview;

    @FXML
    public DatePicker datePicker;

    @FXML
    public HTMLEditor htmleditor;

    public SignUpModel signupmodel = new SignUpModel();
    public LoginModel loginmodel = new LoginModel();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        datePicker.setValue(LocalDate.now());

        if(signupmodel.isDbconnected()){
            //loginstatus.setText("Connected");
        }
        else
           ;

        if(loginmodel.isDbconnected()){
            //loginstatus.setText("Connected");
        }
        else
           ;
    }
    public void mediastore(){
        FileChooser fileChooser= new FileChooser();
        fileChooser.setTitle("Choose one media file");
        //Show open file dialog
        String address = null;
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            address = file.getPath().toString();
        }
        System.out.println(address);
    }
    public void submit(){
        String titlename = title.getText();
        String date = datePicker.getValue().toString();
        String matter = Jsoup.parse(htmleditor.getHtmlText()).text();

        System.out.println(date);

    }
    public void videopreview(){
//query media path from database
//play your media player code
    }
    public void imagepreview(){
        String date = datePicker.getValue().toString();
    }

}
