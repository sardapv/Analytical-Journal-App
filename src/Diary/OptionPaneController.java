package Diary;

import edu.stanford.nlp.classify.CrossValidator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.shape.Circle;
import javafx.stage.DirectoryChooser;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.awt.datatransfer.DataFlavor;
import java.io.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;


import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Date;


import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;

import com.itextpdf.text.Paragraph;

import com.itextpdf.text.pdf.PdfWriter;

/**
 * Created by pranav on 09/04/17.
 */


public class OptionPaneController implements Initializable {
    @FXML
    private Button logout;
    @FXML
    public Button search;


    public void writeNewLog() {
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

    public void modifyOldLog() {
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

    public void showAllLogs() {
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

    public void analyse() {
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
        ((Node) event.getSource()).getScene().getWindow().hide();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxmls/login.fxml"));
        Parent root3 = fxmlLoader.load();
        Stage stage = new Stage();
        stage.setScene(new Scene(root3));
        stage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        logout.setShape(new Circle(60));
        logout.setMinSize(120, 120);
        logout.setMaxSize(120, 120);
    }

    public void search(ActionEvent event) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxmls/search.fxml"));
        Parent root3 = fxmlLoader.load();
        Stage stage = new Stage();
        stage.setTitle("Search in Note!T");
        stage.setScene(new Scene(root3));
        stage.show();
    }

    public void printpdf(ActionEvent event) throws IOException, SQLException, DocumentException, ClassNotFoundException {
        String Titleset = "";
        String Matter = "";
        String dateset = "";
        String Dayrate = "";
        String mediapath = "";
        float dayrate = 0;
        Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD);
        Font lineFont = new Font(Font.FontFamily.TIMES_ROMAN, 18, Font.BOLD,BaseColor.GRAY);
        Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.NORMAL, BaseColor.RED);
        Font blueFont = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.NORMAL, BaseColor.BLUE);
        Font small = new Font(Font.FontFamily.TIMES_ROMAN, 14, Font.NORMAL);
        String FILE = "";
        FileChooser fileChooser= new FileChooser();
        fileChooser.setTitle("Choose one media file");
        //Show open file dialog
        String address = "";
        fileChooser.setInitialFileName(System.getProperty("user.name")+"Logs"+".pdf");
        File file = fileChooser.showSaveDialog(null);
        if (file != null) {
            FILE = file.getAbsolutePath();
        }
        System.out.println(FILE);
        connection = SqliteConnection.Connector();
        if (connection == null) {
            System.out.print("No connection");
        }
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        Document document= null;
        String query = "SELECT * FROM DATA WHERE id = ?";
        //String mediapath = null;
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, Controller.id_logged_in);
            resultSet = preparedStatement.executeQuery();
            document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream(FILE));
            document.open();

            while (resultSet.next()) {
                Paragraph preface = new Paragraph();
                Titleset = resultSet.getString("title");
                System.out.println(Titleset);
                Matter = resultSet.getString("matter");
                dateset= resultSet.getString("dateofday");
                dayrate = resultSet.getFloat("rating");
                mediapath = resultSet.getString("mediapath");
                if(mediapath != null && mediapath.contains(".mp4")){
                    mediapath = "file:/Users/pranav/IdeaProjects/Noteit/src/Diary/background/vd.jpeg";
                }

                if (dayrate <= 4) {
                    Dayrate= " SAD ";
                } else if (dayrate <= 7 && dayrate > 4) {
                    Dayrate=" MODERATE ";
                } else if (dayrate <= 10 && dayrate > 7) {
                    Dayrate=" HAPPY ";
                }
               // System.out.print(Dayrate);
                //System.out.println(Titleset + Matter + dateset + dayrate);
                String line  = "__________________________________________________________";



                Paragraph p = new Paragraph(Titleset, catFont);
                p.setAlignment(Element.ALIGN_CENTER);
                preface.add(p);
                preface.add(new Paragraph(line,lineFont));
                addEmptyLine(preface, 1);

                preface.add(new Paragraph("" +
                        "                                                     " +
                        "                                                     " +
                        "         Date :" +"  "+dateset, redFont));
                addEmptyLine(preface, 1);
                preface.add(new Paragraph("Secret :" +" \n\n\t\t"+Matter, small));

                addEmptyLine(preface, 2);

                preface.add(new Paragraph("How was my Day? :" +"  " + Dayrate, blueFont));
                addEmptyLine(preface, 1);
                if(mediapath !=null) {
                    Image img = Image.getInstance(mediapath);
                    img.scaleAbsolute(300, 200);

                    preface.add(img);
                }

                addEmptyLine(preface, 1);
                // Will create: Report generated by: _name, _date

                document.add(preface);
                preface.add(new Paragraph("Report generated by: " + System.getProperty("user.name") + ", " + new Date(),small)); //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                // Start a new page
                document.newPage();

            }
            Alert alert  = new Alert (Alert.AlertType.INFORMATION);
            alert.setTitle("Notebook Generator");
            alert.setHeaderText(null);
            alert.setContentText("Success!! Your Notebook is saved");
            alert.showAndWait();
        } catch (SQLException e) {
            //e.printStackTrace();
        } finally {
            connection.close();
            document.close();

        }


    }






    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }
}

