package sample;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.HTMLEditor;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.jsoup.Jsoup;

import javax.activation.MimetypesFileTypeMap;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class ModifyingEditorController implements Initializable {
    @FXML
    public TextField title;

    @FXML
    public Button submit,mediaupload;

    @FXML
    public DatePicker datePicker;

    @FXML
    public HTMLEditor htmleditor;

    @FXML
    public ImageView img;

    @FXML
    public Slider slider;

    @FXML
    public ProgressBar pbar;

    @FXML
    public Label datelabel;

    public SignUpModel signupmodel = new SignUpModel();
    public LoginModel loginmodel = new LoginModel();

    public EditorDatabase editorDatabase = new EditorDatabase();

    int id_logged_in ;

    public static String global_date;

    Connection connection;

    private static final String RED_BAR    = "red-bar";
    private static final String YELLOW_BAR = "yellow-bar";
    private static final String ORANGE_BAR = "orange-bar";
    private static final String GREEN_BAR  = "green-bar";

    private static final String[] barColorStyleClasses = { RED_BAR, ORANGE_BAR, YELLOW_BAR, GREEN_BAR };

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        slider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov, Number old_val, Number new_val) {
                pbar.setProgress(new_val.doubleValue()/10);
            }
        });

        pbar.progressProperty().addListener(new ChangeListener<Number>() {
            @Override public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                double progress = newValue == null ? 0 : newValue.doubleValue();
                if (progress < 0.2) {
                    setBarStyleClass(pbar, RED_BAR);
                } else if (progress < 0.4) {
                    setBarStyleClass(pbar, ORANGE_BAR);
                } else if (progress < 0.6) {
                    setBarStyleClass(pbar, YELLOW_BAR);
                } else {
                    setBarStyleClass(pbar, GREEN_BAR);
                }
            }
            private void setBarStyleClass(ProgressBar bar, String barStyleClass) {
                bar.getStyleClass().removeAll(barColorStyleClasses);
                bar.getStyleClass().add(barStyleClass);
            }
        });
        if(AllLogsController.datehere !=null) {
            try {
                datepickerAction();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
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
    String globaladd;

    public void mediastore() throws MalformedURLException {
        FileChooser fileChooser= new FileChooser();
        fileChooser.setTitle("Choose one media file");
        //Show open file dialog
        String address = null;
        File file = fileChooser.showOpenDialog(null);
        if (file != null) {
            address = file.toURI().toURL().toString();
        }
        System.out.println(file.getAbsolutePath());

        Image image = new Image(address);
        img.setImage(image);

        globaladd = address;
    }
    public void submit() throws SQLException {
        if(globaladd == null) {
            try {
                connection = SqliteConnection.Connector();
                if (connection == null) {
                    System.out.print("No connection");
                }
            }catch (ClassNotFoundException e) {
            e.printStackTrace();
            }

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String query = "SELECT * FROM DATA WHERE id = ? AND dateofday = ?";
        String mediapath = null;
        try{
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1,Controller.id_logged_in);
            preparedStatement.setString(2,datePicker.getValue().toString());
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                globaladd = resultSet.getString("mediapath");
            }
            else {
                System.out.println("Nothing to set");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            connection.close();
        }
        }

        String titlename = title.getText();
        String date = datePicker.getValue().toString();
        double rating = slider.getValue();
        String matter = Jsoup.parse(htmleditor.getHtmlText()).text();
        editorDatabase.modifyData(Controller.id_logged_in, titlename, date, matter, htmleditor.getHtmlText(), globaladd, (float) rating);

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/sample/success.fxml"));
            Parent root2 = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root2));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void videopreview(){
        global_date = datePicker.getValue().toString();
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/sample/videoplayer.fxml"));
            Parent root3 = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root3));
            stage.show();
            stage.setOnCloseRequest(event -> {
                VideoController.stoper.stop();
            });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void imagepreview() throws IOException, SQLException {

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
        String query = "SELECT * FROM DATA WHERE id = ? AND dateofday = ?";
        String mediapath = null;
        try{
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1,Controller.id_logged_in);
            preparedStatement.setString(2,datePicker.getValue().toString());
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                mediapath = resultSet.getString("mediapath");
            }
            else {
                System.out.println("Nothing to set");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            connection.close();
        }

        String date = datePicker.getValue().toString();
        //get mediapath based on date above
        Image image;
        if(mediapath == null)
            image = new Image("/sample/bg.jpg");
        else
            image = new Image(mediapath);

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/sample/imagepreview.fxml"));
            Parent root1 = fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            stage.show();
            stage.setMaximized(true);
            AnchorPane sp = new AnchorPane();
            ImageView img= new ImageView(image);
            sp.getChildren().add(img);

            //Adding HBox to the scene
            Scene scene = new Scene(sp);
            stage.setScene(scene);
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void datepickerAction() throws SQLException {
        datelabel.setText("");
        try {
            connection = SqliteConnection.Connector();
            if (connection == null) {
                System.out.print("No connection");
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        title.setText("Title Here");
        htmleditor.setHtmlText("");
        slider.setValue(0);
        pbar.setProgress(0);
        img.setImage(null);
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String query = "SELECT * FROM DATA WHERE id = ? AND dateofday = ?";
        String mediapath = null;
        try{
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1,Controller.id_logged_in);
            if(AllLogsController.datehere == null)
                preparedStatement.setString(2,datePicker.getValue().toString());
            else
                preparedStatement.setString(2,AllLogsController.datehere);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                title.setText(resultSet.getString("title"));
                htmleditor.setHtmlText(resultSet.getString("htmlmatter"));
                slider.setValue(resultSet.getFloat("rating"));
                pbar.setProgress(resultSet.getFloat("rating")/10);
                if(AllLogsController.datehere != null) {
                    String[] temp = AllLogsController.datehere.split("\\-");
                    datePicker.setValue(LocalDate.of(Integer.parseInt(temp[0]),Integer.parseInt(temp[1]),Integer.parseInt(temp[2])));
                }
                if(resultSet.getString("mediapath") != null) {
                    Image image = new Image(resultSet.getString("mediapath"));
                    img.setImage(image);
                }
            }
            else {
                System.out.println("Nothing to set");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            connection.close();
        }


    }

}
