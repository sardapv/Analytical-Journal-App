package Diary;

import edu.stanford.nlp.process.Stemmer;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

class Record{
    StringProperty title;
    StringProperty fileName;
    Record(String title,String fileName){
        this.title = new SimpleStringProperty(title);
        this.fileName = new SimpleStringProperty(fileName);
    }

    void setTitle(String title) {
        this.title.set(title);
    }

    void setFileName(String fileName) {
        this.fileName.set(fileName);
    }

    String getTitle() {

        return title.get();
    }

    StringProperty titleProperty() {
        return title;
    }

    String getFileName() {
        return fileName.get();
    }

    StringProperty fileNameProperty() {
        return fileName;
    }



}
class Nodes {
    public double distance;
    public int fileindex;

    Nodes(double distance, int fileindex) {
        this.distance = distance;
        this.fileindex = fileindex;
    }
}

public class WC implements Initializable {
    Connection connection;
    @FXML
    public TextField searchbar = new TextField();
    @FXML
    public Button searchbutton;
    @FXML
    public TableView<Record> tableView;
    @FXML
    public TableColumn<Record, String> Title;
    @FXML
    public TableColumn<Record, String> FileName;

    public Set<String> stopWordsSet = new HashSet<String>();
    double startTime = System.currentTimeMillis();
    String path;
    BufferedReader br = null;
    String line;
    Stemmer stemmer = new Stemmer();

    File file = new File("invertedIndexFile.txt");
    PrintStream output = new PrintStream(file);
    HashSet<String> set = new HashSet<String>();
    HashSet<String> wordset = new HashSet<String>();

    public WC() throws FileNotFoundException {
        output.printf("%-15s\t%25s\t%10s\n\n", "Term", "FileName", "Frequency");
    }

    TreeMap<String, TreeMap> hashMap = new TreeMap<String, TreeMap>();
    TreeMap<String, Double> list = null;

    public void calculate(String path) throws IOException {
        System.out.printf(path);
        File folder = new File(path);
        File[] Files = folder.listFiles();
        for (File file : Files) {
            String fileName = file.getName();
            if (file.isFile()) {
                set.add(fileName);
                br = new BufferedReader(new InputStreamReader(new FileInputStream(folder + "/" + fileName)));
                while ((line = br.readLine()) != null) {
                    //System.out.println(line);
                    line = line.replaceAll("[[!,\",#,$,%,&,\\,',(,),*,+,-,.,/,:,;,<,=,>,?,@,^,_,|,~,0-9]*+[,(){}`,,-]*+['\']*]", " ");
                    String afterStop = removeStopwords(line);
                    String tok = stemmer.stem(afterStop);
                    tok = removeStopwords(tok);
                    // String output = ExudeData.getInstance().filterStoppingsKeepDuplicates(line);
                    for (String stem : tok.split("\\s+")) {
                        list = hashMap.get(stem.toLowerCase());
                        wordset.add(stem.toLowerCase());
                        if (list != null) {
                            Double count = list.get(fileName);
                            if (count != null)
                                list.put(fileName, count + 1);
                            else
                                list.put(fileName, 1.0);
                        } else {
                            list = new TreeMap<String, Double>();
                            list.put(fileName, 1.0);
                            hashMap.put(stem.toLowerCase(), list);
                        }
                    }
                }
            } else {
                //System.out.println(file.getAbsolutePath());
                calculate(file.getAbsolutePath() + "/");

            }
        }
        double endTime = System.currentTimeMillis();
        double totalTime = endTime - startTime;
        // System.out.println(totalTime / 1000);
    }

    public String removeStopwords(String string) {
        String[] words = string.split("\\s+");
        string = "";
        for (String word : words) {
            if (word != "\\s+") {
                String wordCompare = word.toLowerCase();
                if (!stopWordsSet.contains(wordCompare)) {
                    string += word + " ";
                }
            }
        }
        return string;
    }

    public LinkedHashMap<String, Double> calculateForString(String query) {
        query = query.replaceAll("[[!,\",#,$,%,&,\\,',(,),*,+,-,.,/,:,;,<,=,>,?,@,^,_,|,~,0-9]*+[,(){}`,,-]*]", " ");
        LinkedHashMap<String, Double> hashMap = new LinkedHashMap<String, Double>();
        String afterStop = removeStopwords(query);
        String tok = stemmer.stem(afterStop);
        tok = removeStopwords(tok).toLowerCase();
        // System.out.println(tok);
        for (String stem : tok.split("\\s+")) {
            Double count = hashMap.get(stem);
            if (count != null)
                hashMap.put(stem, count + 1);
            else
                hashMap.put(stem, 1.0);
        }
        for (String mainkey : hashMap.keySet()) {
            Double ls = hashMap.get(mainkey);
            // System.out.println(mainkey+"\t"+ls);
        }
        return hashMap;
    }

    public void process(String query) {
        //  System.out.println("at process step");
        LinkedHashMap<String, Double> queryMap = calculateForString(query);
        Double[][] matrix = new Double[wordset.size() + 1][set.size() + 2];
        Iterator<String> setIt = set.iterator();
        String word = null;
        String file = null;
        Iterator<String> wordsetIt = wordset.iterator();
        int i = 0;
        int j = 0;
        while (wordsetIt.hasNext()) {
            word = wordsetIt.next();//System.out.print("<- "+word+" -> ");
            setIt = set.iterator();
            while (setIt.hasNext()) {
                file = setIt.next();
                if (hashMap.get(word).get(file) != null)
                    matrix[i][j] = Double.parseDouble(hashMap.get(word).get(file).toString());
                else
                    matrix[i][j] = 0.0;
                //System.out.print(matrix[i][j]+" ");
                j++;
            }
            // System.out.println(queryMap);
            if (queryMap.get(word) != null) {
                matrix[i][set.size()] = queryMap.get(word);
                //   System.out.print(matrix[i][set.size()] + " ");
            } else {
                matrix[i][set.size()] = 0.0;
            }
            j = 0;
            i++;
        }
        hashMap = null;
        tfIdf(matrix);
    }

    public void tfIdf(Double[][] matrix) {
        // System.out.println(set);
        //System.out.println("at tfIdf step");
        double sq = 0;
        double magnitude[] = new double[set.size() + 1];
        double tdf = 0.0;
        for (int i = 0; i < wordset.size(); i++) {
            for (int j = 0; j < set.size(); j++) {
                if (matrix[i][j] != 0)
                    tdf++;
            }
            matrix[i][set.size() + 1] = tdf;
            tdf = 0;
            // System.out.println(matrix[i][set.size()+1]);
            for (int j = 0; j < set.size() + 1; j++) {
                matrix[i][j] = Math.floor((matrix[i][j] * Math.log10(set.size() / matrix[i][set.size() + 1])) * 100) / 100;
            }
        }
//        for (int i = 0; i < wordset.size(); i++) {
//            for (int j = 0; j < set.size()+1; j++) {
//                System.out.print(matrix[i][j]+"  ");
//            }
//            //System.out.println();
//        }
        for (int i = 0; i < set.size() + 1; i++) {
            sq = 0;
            for (int j = 0; j < wordset.size(); j++) {
                sq += Math.pow(matrix[j][i], 2);
            }
            magnitude[i] = Math.floor(Math.sqrt(sq) * 100) / 100;
            // System.out.println(magnitude[i] + " ");

        }
//        System.out.println("Eucledian Distances : ");
//        distanceCalculate(matrix, magnitude, 2.0);
//        System.out.println("Manhattan Distances : ");
//        distanceCalculate(matrix, magnitude, 1.0);
        cosineSimilarity(matrix, magnitude);
    }


    public void cosineSimilarity(Double[][] matrix, double[] magnitude) {
        //System.out.println("cosineSimilarity  step");
        ArrayList<Nodes> nodelist = new ArrayList<Nodes>();
        String[] fileNames = new String[set.size()];
        set.toArray(fileNames);
        double distsq = 0.0;
        for (int i = 0; i < set.size(); i++) {
            for (int j = 0; j < wordset.size(); j++) {
                distsq += (matrix[j][i] * matrix[j][set.size()]);
            }
            distsq /= (magnitude[i] * magnitude[set.size()]);
            if(distsq !=0.0)
                nodelist.add(new Nodes(Math.floor(distsq * 100) / 100, i));
            distsq = 0.0;
        }
        Collections.sort(nodelist, Comparator.comparingDouble(o -> -(o.distance)));
        ObservableList<Record> searchlist = FXCollections.observableArrayList();
        Title.setCellValueFactory(cellData -> cellData.getValue().titleProperty());
        FileName.setCellValueFactory(cellData -> cellData.getValue().fileNameProperty());
        for (Nodes node : nodelist) {
           // System.out.println(fileNames[node.fileindex]);
            searchlist.add(new Record(fileNames[node.fileindex].split(" | ")[0], fileNames[node.fileindex].split(" | ")[2]));
//            System.out.println(node.distance + "  " + fileNames[node.fileindex]);

        }
        //  System.out.println(searchlist.get(2).fileName.toString());
        tableView.setItems(searchlist);
    }

    public void main() throws IOException {

        WC inst = new WC();
        br = new BufferedReader(new InputStreamReader(new FileInputStream("/Users/pranav/IdeaProjects/pranav/src/main/java/stopWords.txt")));
        while ((line = br.readLine()) != null) {
            stopWordsSet.add(line);
        }
        calculate(path);
        br.close();
        output.close();
        String query = searchbar.getText();
        process(query);
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
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
        String query = "SELECT * FROM user WHERE id = ?";
        String mediapath = null;
        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setInt(1, Controller.id_logged_in);
            int count = 1;
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                path = resultSet.getString("backupdir");
            }
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();

        }
    }
    public static String datehere;
    public void openeditor(){
        TablePosition pos = tableView.getSelectionModel().getSelectedCells().get(0);
        int row = pos.getRow();
        Record rec = tableView.getItems().get(row);
        TableColumn col = pos.getTableColumn();
        String data = (String) col.getCellObservableValue(rec).getValue();
        System.out.println(data);
        datehere = data;
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("fxmls/modifying_editor.fxml"));
            Parent root1 = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root1));
            stage.show();
            stage.setOnCloseRequest(event -> {
                datehere = null;
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
