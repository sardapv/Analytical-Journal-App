/*
 * Copyright (c) 2012 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */

package sample;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.googleapis.media.MediaHttpDownloader;
import com.google.api.client.googleapis.media.MediaHttpUploader;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Preconditions;
import com.google.api.client.util.store.DataStoreFactory;
import com.google.api.client.util.store.FileDataStoreFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.*;
import java.net.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collections;

/**
 * A sample application that runs multiple requests against the Drive API. The requests this sample
 * makes are:
 * <ul>
 * <li>Does a resumable media upload</li>
 * <li>Updates the uploaded file by renaming it</li>
 * <li>Does a resumable media download</li>
 * <li>Does a direct media upload</li>
 * <li>Does a direct media download</li>
 * </ul>
 *
 * @author rmistry@google.com (Ravi Mistry)
 */


public class DriveSample {
    @FXML
    public Label downloadstatus;
    @FXML
    public Button btn;
    static Connection connection;

  /**
   * Be sure to specify the name of your application. If the application name is {@code null} or
   * blank, the application will log a warning. Suggested format is "MyCompany-ProductName/1.0".
   */
  private static final String APPLICATION_NAME = "D A Y O N E";

  private static  String UPLOAD_FILE_PATH = "";
  private static  String DIR_FOR_DOWNLOADS = "";
  private static  java.io.File UPLOAD_FILE = new java.io.File(UPLOAD_FILE_PATH);


  /** Directory to store user credentials. */
  private static final java.io.File DATA_STORE_DIR = new java.io.File(System.getProperty("user.home"), ".store/drive_sample");

  /**
   * Global instance of the {@link DataStoreFactory}. The best practice is to make it a single
   * globally shared instance across your application.
   */
  private static FileDataStoreFactory dataStoreFactory;

  /** Global instance of the HTTP transport. */
  private static HttpTransport httpTransport;

  /** Global instance of the JSON factory. */
  private static final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();

  /** Global Drive API client. */
  private static Drive drive;

  /** Authorizes the installed application to access user's protected data. */
  private static Credential authorize() throws Exception {
    // load client secrets
    GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(JSON_FACTORY, new InputStreamReader(DriveSample.class.getResourceAsStream("client_secrets.json")));
    if (clientSecrets.getDetails().getClientId().startsWith("Enter")
        || clientSecrets.getDetails().getClientSecret().startsWith("Enter ")) {
      System.out.println(
          "Enter Client ID and Secret from https://code.google.com/apis/console/?api=drive "
          + "into drive-cmdline-sample/src/main/resources/client_secrets.json");
      System.exit(1);
    }
    // set up authorization code flow
    GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
        httpTransport, JSON_FACTORY, clientSecrets,
        Collections.singleton(DriveScopes.DRIVE_FILE)).setDataStoreFactory(dataStoreFactory)
        .build();
    // authorize
    return new AuthorizationCodeInstalledApp(flow, new LocalServerReceiver()).authorize("user");
  }

  public void mainfunc() throws InterruptedException {
      if(netIsAvailable()) {
          downloadstatus.setText("It will take some time , Please wait . . .");
          downloadstatus.setText("Congrats! Your secrets are on your cloud :) ");
          btn.setVisible(false);
          Thread.sleep(2000);
      }else if(!netIsAvailable()) {
          downloadstatus.setText("Seems like you are not connected to Internet!");
          btn.setText("Retry");
      }
    Preconditions.checkArgument(
        !UPLOAD_FILE_PATH.startsWith("Enter ") && !DIR_FOR_DOWNLOADS.startsWith("Enter "),
        "Please enter the upload file path and download directory in %s", DriveSample.class);

    try {
      httpTransport = GoogleNetHttpTransport.newTrustedTransport();
      dataStoreFactory = new FileDataStoreFactory(DATA_STORE_DIR);
      // authorization
      Credential credential = authorize();
      // set up the global Drive instance
      drive = new Drive.Builder(httpTransport, JSON_FACTORY, credential).setApplicationName(
          APPLICATION_NAME).build();

      // run commands
      //downloadstatus.setText("Starting Resumable Media Upload");
      //View.header1("Starting Resumable Media Upload");
      //File uploadedFile = uploadFile(false);
      //downloadstatus.setText("Updating Uploaded File Name");
      //View.header1("Updating Uploaded File Name");
      //File updatedFile = updateFileWithTestSuffix(uploadedFile.getId());
      //downloadstatus.setText("Starting Resumable Media Download");
      View.header1("Starting Simple Media Upload");
      uploadFile(true);

      ///View.header1("Success!");
      return;
    } catch (IOException e) {
      System.err.println(e.getMessage());
    } catch (Throwable t) {
      t.printStackTrace();
    }
  }

  /** Uploads a file using either resumable or direct media upload. */
  private static File uploadFile(boolean useDirectUpload) throws IOException, SQLException {
      try {
          connection = SqliteConnection.Connector();
          if (connection == null) {
              System.out.print("No connection");
          }
      }catch (ClassNotFoundException e) {
          e.printStackTrace();
      }
      File tempfile = null;
      PreparedStatement preparedStatement = null;
      ResultSet resultSet = null;
      String query = "SELECT * FROM USER WHERE id = ?";
      String mediapath = null;
      try{
          preparedStatement = connection.prepareStatement(query);
          preparedStatement.setInt(1,Controller.id_logged_in);
          resultSet = preparedStatement.executeQuery();
          if(resultSet.next()){
              UPLOAD_FILE_PATH  = resultSet.getString("backupdir");
          }
          else {
              System.out.println("Nothing to set");
          }
      } catch (SQLException e) {
          e.printStackTrace();
      }
      try {
          String query2 = "SELECT * FROM DATA WHERE id = ?";
          preparedStatement = connection.prepareStatement(query2);
          preparedStatement.setInt(1,Controller.id_logged_in);
          resultSet = preparedStatement.executeQuery();
          int count = 1;
          while (resultSet.next()){
              String filetopbeuploaded = UPLOAD_FILE_PATH+"/"+resultSet.getString("dateofday")+ " | "+ resultSet.getString("title")+".txt";
              java.io.File yourFile = new java.io.File(filetopbeuploaded);
              if(!yourFile.exists())
                  yourFile.createNewFile();
              FileWriter fileWriter = new FileWriter(filetopbeuploaded);

              fileWriter.write("\nDate : "+resultSet.getString("dateofday"));
              fileWriter.write("\n\n---------------------------------------------------------------------------------------");
              fileWriter.write("\n\nTitle : "+resultSet.getString("title"));
              fileWriter.write("\n\n=======================================================================================");
              fileWriter.write("\n\nStory of the day : \n\n\t "+resultSet.getString("matter"));
              fileWriter.write("\n\n=======================================================================================");
              fileWriter.write("\n\nMy Day on scale of 0 to 10 : "+resultSet.getFloat("rating"));
              float temp = resultSet.getFloat("rating");
              if(temp <= 3){
                  fileWriter.write("( SAD )");
              }
              else if(temp <= 6 && temp > 3){
                  fileWriter.write("( MODERATE )");
              }
              else if(temp <=10 && temp >6){
                  fileWriter.write("( HAPPY )");
              }
              fileWriter.write("\n\n---------------------------------------------------------------------------------------");
              fileWriter.write("\n\nMedia memory : "+resultSet.getString("mediapath"));
              fileWriter.write("\n\n---------------------------------------------------------------------------------------");
              fileWriter.close();

              java.io.File UPLOAD_FILE = new java.io.File(filetopbeuploaded);

              File fileMetadata = new File();
              fileMetadata.setName(UPLOAD_FILE.getName());

              FileContent mediaContent = new FileContent("text/text", UPLOAD_FILE);

              Drive.Files.Create insert = drive.files().create(fileMetadata, mediaContent);
              MediaHttpUploader uploader = insert.getMediaHttpUploader();
              uploader.setDirectUploadEnabled(useDirectUpload);

              uploader.setProgressListener(new FileUploadProgressListener());

              tempfile = insert.execute();


          }
      } catch (SQLException e) {
          e.printStackTrace();
      }

      finally {
          connection.close();
      }
    return tempfile;
  }

    public boolean netIsAvailable()
    {
        boolean status = false;
        Socket sock = new Socket();
        InetSocketAddress address = new InetSocketAddress("www.google.com", 80);
        try {
            sock.connect(address, 3000);
            if(sock.isConnected()){
                status=true;
            }
        }
        catch(Exception e){}
        finally {
            try {
                sock.close();
            }
            catch(Exception e) {}
        }
        return status;
    }



    /** Updates the name of the uploaded file to have a "drivetest-" prefix. */
  private static File updateFileWithTestSuffix(String id) throws IOException {
    File fileMetadata = new File();
    fileMetadata.setName(UPLOAD_FILE.getName());

    Drive.Files.Update update = drive.files().update(id, fileMetadata);
    return update.execute();
  }

  /** Downloads a file using either resumable or direct media download. */
  private static void downloadFile(boolean useDirectDownload, File uploadedFile)
      throws IOException {
    // create parent directory (if necessary)
    /*java.io.File parentDir = new java.io.File(DIR_FOR_DOWNLOADS);
    if (!parentDir.exists() && !parentDir.mkdirs()) {
      throw new IOException("Unable to create parent directory");
    }
    OutputStream out = new FileOutputStream(new java.io.File(parentDir, uploadedFile.getName()));

    MediaHttpDownloader downloader =
        new MediaHttpDownloader(httpTransport, drive.getRequestFactory().getInitializer());
    downloader.setDirectDownloadEnabled(useDirectDownload);
    downloader.setProgressListener(new FileDownloadProgressListener());
    //downloader.download(new GenericUrl(uploadedFile.getWebViewLink()), out);

    OutputStream outputStream = new ByteArrayOutputStream();
    drive.files().get(uploadedFile.getId())
            .executeMediaAndDownloadTo(out);
*/
  }
}
