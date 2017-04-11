package sample;

import java.io.File;

import javafx.application.Platform;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;

import java.net.URL;
import javafx.util.Duration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Created by pranav on 08/04/17.
 */
public class VideoController implements Initializable {
    @FXML private MediaView mv;
    MediaPlayer mp;
    private Media me;
    @FXML
    Slider volumeslider;
    @FXML
    Slider timeslider;
    @FXML
    public Label time;
    Connection connection;

    public static MediaPlayer stoper;
    @Override
    public void initialize(URL location, ResourceBundle resources) {

        try {
            connection = SqliteConnection.Connector();
            if (connection == null) {
                System.out.print("NOT CONNECTED");
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        String query = "SELECT * FROM DATA WHERE id = ? AND dateofday = ?";
        String mediapath = null;
        String datehere = EditorController.global_date;
        try{
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1,Controller.id_logged_in);
            if(datehere == null)
                preparedStatement.setString(2,ModifyingEditorController.global_date);
            else
                preparedStatement.setString(2,EditorController.global_date);
            resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                mediapath = resultSet.getString("mediapath");

            }
            else {
                System.out.println("NO VIDEO");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        me = new Media(mediapath);
        mp = new MediaPlayer(me);
        mv.setMediaPlayer(mp);
        mp.setAutoPlay(true);
        stoper = mp;
        DoubleProperty width = mv.fitWidthProperty();
        DoubleProperty height = mv.fitHeightProperty();
        width.bind(Bindings.selectDouble(mv.sceneProperty(),"width"));
        height.bind(Bindings.selectDouble(mv.sceneProperty(),"height"));
        volumeslider.setValue(mp.getVolume());
        volumeslider.valueProperty().addListener(new InvalidationListener() {
            @Override
            public void invalidated(Observable observable) {
                mp.setVolume(volumeslider.getValue()/100);
            }
        });
        timeslider.valueProperty().addListener(new InvalidationListener() {
            public void invalidated(Observable ov) {
                if (timeslider.isValueChanging()) {
                    // multiply duration by percentage calculated by slider position
                    mp.seek(mp.getMedia().getDuration().multiply(timeslider.getValue() / 100.0));
                }
            }
        });
        mp.currentTimeProperty().addListener(new InvalidationListener() {
            public void invalidated(Observable ov) {
                updateValues();
            }
        });


    }
    public void updateValues(){
        Platform.runLater(new Runnable() {
            public void run() {
                Duration currentTime = mp.getCurrentTime();
                time.setText(formatTime(currentTime, mp.getMedia().getDuration()));
                timeslider.setDisable(mp.getMedia().getDuration().isUnknown());
                if (!timeslider.isDisabled()
                        && mp.getMedia().getDuration().greaterThan(Duration.ZERO)
                        && !timeslider.isValueChanging()) {
                    timeslider.setValue(currentTime.divide(mp.getMedia().getDuration()).toMillis() * 100.0);
                }
                if (!volumeslider.isValueChanging()) {
                    volumeslider.setValue((int) Math.round(mp.getVolume() * 100));
                }
            }
        });
    }
    private static String formatTime(Duration elapsed, Duration duration) {
        int intElapsed = (int) Math.floor(elapsed.toSeconds());
        int elapsedHours = intElapsed / (60 * 60);
        if (elapsedHours > 0) {
            intElapsed -= elapsedHours * 60 * 60;
        }
        int elapsedMinutes = intElapsed / 60;
        int elapsedSeconds = intElapsed - elapsedHours * 60 * 60
                - elapsedMinutes * 60;

        if (duration.greaterThan(Duration.ZERO)) {
            int intDuration = (int) Math.floor(duration.toSeconds());
            int durationHours = intDuration / (60 * 60);
            if (durationHours > 0) {
                intDuration -= durationHours * 60 * 60;
            }
            int durationMinutes = intDuration / 60;
            int durationSeconds = intDuration - durationHours * 60 * 60 - durationMinutes * 60;
            if (durationHours > 0) {
                return String.format("%d:%02d:%02d/%d:%02d:%02d",
                        elapsedHours, elapsedMinutes, elapsedSeconds,
                        durationHours, durationMinutes, durationSeconds);
            } else {
                return String.format("%02d:%02d/%02d:%02d",
                        elapsedMinutes, elapsedSeconds, durationMinutes,
                        durationSeconds);
            }
        } else {
            if (elapsedHours > 0) {
                return String.format("%d:%02d:%02d", elapsedHours,
                        elapsedMinutes, elapsedSeconds);
            } else {
                return String.format("%02d:%02d", elapsedMinutes,
                        elapsedSeconds);
            }
        }
    }

    public void play(ActionEvent event){
        mp.setRate(1);
        mp.play();
    }
    public void pause(ActionEvent event){
        mp.pause();
    }
    public void fast(ActionEvent event){
        mp.setRate(1.5);
    }
    public void reload(ActionEvent event){
        mp.seek(mp.getStartTime());
        mp.play();
    }
    public void start(ActionEvent event){
        mp.seek(mp.getStartTime());
        mp.stop();
    }
    public void slow(ActionEvent event){
        mp.setRate(0.5);
    }
    public void last(ActionEvent event){
        mp.seek(mp.getTotalDuration());
        mp.stop();
    }

}
