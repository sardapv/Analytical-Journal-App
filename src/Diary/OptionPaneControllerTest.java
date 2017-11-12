package Diary;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;
import org.junit.Test;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

import static org.junit.Assert.*;

/**
 * Created by pranav on 01/11/17.
 */
public class OptionPaneControllerTest {
    @Test
    public void printpdf() throws Exception {
        Document document = new Document();
        document.newPage();
    }

}