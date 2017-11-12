package Diary;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by pranav on 01/11/17.
 */
public class VideoControllerTest {
    @FXML
    private MediaView mv;
    MediaPlayer mp;
    private Media me;

    @Test
    public void play() throws Exception {
        VideoController videoController = new VideoController();
    }

    @Test
    public void pause() throws Exception {
        VideoController videoController = new VideoController();
    }

    @Test
    public void fast() throws Exception {
        VideoController videoController = new VideoController();
    }

    @Test
    public void reload() throws Exception {
        VideoController videoController = new VideoController();
    }

    @Test
    public void start() throws Exception {
        VideoController videoController = new VideoController();
    }

    @Test
    public void slow() throws Exception {
        VideoController videoController = new VideoController();
    }

    @Test
    public void last() throws Exception {
        VideoController videoController = new VideoController();
    }

}