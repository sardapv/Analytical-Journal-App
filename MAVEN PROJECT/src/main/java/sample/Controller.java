package sample;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.web.HTMLEditor;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class Controller implements Initializable {
    @FXML
    private Label loginstatus,lblstatus;

    @FXML
    public TextField firstname, lastname, email, phno, username;

    @FXML
    public PasswordField password,cpassword;

    @FXML
    public TextArea descrip;
    @FXML
    public Button submit;

    @FXML
    public DatePicker datePicker;

    @FXML
    public RadioButton male,female,other;

    public SignUpModel signupmodel =  new SignUpModel();
    public LoginModel loginmodel =  new LoginModel();

    @FXML
    public TextField uid;
    @FXML
    public PasswordField upass;

    @FXML
    public HTMLEditor htmleditor;

    public static int id_logged_in;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        if(signupmodel.isDbconnected()){
            //loginstatus.setText("Connected");
        }
        else
            loginstatus.setText("Booo");

        if(loginmodel.isDbconnected()){
            //loginstatus.setText("Connected");
        }
        else
            loginstatus.setText("Booo");
    }
    public void submitit() throws SQLException {
        String fname = firstname.getText();
        String lname = lastname.getText();
        String gmail = email.getText();
        String phonenumber = phno.getText();
        String loginid = username.getText();
        String key = password.getText();
        String confirmpassword = cpassword.getText();
        String description = descrip.getText();
        String dob = datePicker.getValue().toString();

        String gender = null;
        if(male.isSelected()){
            gender = male.getId();
        }
        else if (female.isSelected()){
            gender = female.getId();
        }
        else{
            gender = other.getId();
        }
        if(confirmpassword.equals(key)) {
            signupmodel.signupuser(fname, lname, gmail, dob, phonenumber, gender, description, loginid, key);
            loginstatus.setText("Successfully Registered!");
        }
        else {
            loginstatus.setText("Password Doesn't Match");
        }
    }

    public void loginit(ActionEvent event) throws SQLException, InterruptedException {
        String userid = uid.getText();
        String userpass = upass.getText();
        try {
            if (loginmodel.login(userid, userpass)) {
                lblstatus.setText("Welcome Back!");

                ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();

                try {
                    FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("optionpane.fxml"));
                    //HTMLEditor htmleditor = new HTMLEditor();
                    //htmleditor.setHtmlText("<body style  = 'background-color:transparent;'/>");

                    Parent root1 = fxmlLoader.load();
                    Stage stage = new Stage();
                    stage.setScene(new Scene(root1));
                    stage.show();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            else
                lblstatus.setText("Invalid User!");
        } catch (SQLException e) {
            lblstatus.setText("No");
            e.printStackTrace();
        }
        id_logged_in = LoginModel.id_from_table;
    }

    public void signupbutton() throws IOException {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getClassLoader().getResource("signup.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
