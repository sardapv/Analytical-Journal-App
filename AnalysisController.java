package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import sample.Controller;
import sample.SqliteConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Created by pranav on 15/04/17
 */

public class AnalysisController {
    @FXML
    public PieChart pieChart;
    @FXML
    public LineChart lineChart;
    @FXML
            public DatePicker datepickerfrom,datepickerto;
    @FXML
            public Label stats;

    ArrayList<Float> ratings = new ArrayList<Float>();
    ArrayList<Float> rating1 = new ArrayList<Float>();
    ArrayList<Float> rating2 = new ArrayList<Float>();
    ArrayList<Float> rating3 = new ArrayList<Float>();

    ArrayList<String> dayratings = new ArrayList<String>();
    ArrayList<String> daysrating1 = new ArrayList<String>();
    ArrayList<String> daysrating2 = new ArrayList<String>();
    ArrayList<String> daysrating3 = new ArrayList<String>();
    List<Date> allDates = new ArrayList<Date>();
    List<String> allDatesString = new ArrayList<String>();

    Connection connection;

    public void analyse() throws SQLException {
        ratings.clear();
        rating1.clear();
        rating2.clear();
        rating3.clear();

        float dayrate = 0;
        try {
            connection = SqliteConnection.Connector();
            if (connection == null) {
                System.out.print("No connection");
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        int countofday = 0;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String date1 = datepickerfrom.getValue().toString();
        String date2 = datepickerto.getValue().toString();
        try {
            Date d1 = myFormat.parse(date1);
            Date d2 = myFormat.parse(date2);

            while( d1.before(d2) ){
                d1 = addDays(d1, 1);
                allDates.add(d1);
                allDatesString.add(formatter.format(d1));
            }
            //System.out.println(allDatesString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        for(int i = 0; i < allDatesString.size(); i++) {
            String query = "SELECT * FROM DATA WHERE dateofday  = ? AND id = ?";
            float t = 0;
            try {
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setInt(2, Controller.id_logged_in);
                preparedStatement.setString(1, allDatesString.get(i));
                resultSet = preparedStatement.executeQuery();
                if(resultSet.next()) {
                    t = resultSet.getFloat("rating");
                    ratings.add(t);
                    countofday++;
                }
                else
                    ratings.add(Float.valueOf(5));
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        for( int j=0; j<ratings.size(); j++) {
            dayrate = ratings.get(j);
            if (dayrate <= 4 && dayrate >= 0) {
                rating1.add(dayrate);
                daysrating1.add(allDatesString.get(j));
            }
            else if (dayrate <= 7 && dayrate > 4) {
                rating2.add(dayrate);
                daysrating2.add(allDatesString.get(j));
            }
            else if (dayrate <= 10 && dayrate > 7) {
                rating3.add(dayrate);
                daysrating3.add(allDatesString.get(j));
            }
        }
        //System.out.println(ratings);
        int total = rating1.size()+rating2.size()+rating3.size();

        stats.setText("\t\t\t(A) Total Entries : "+countofday+"\n"+"\t  (B) Number of missing Entries : "+(DateDifference()-countofday)+"\n"+
                        "(C) Happy logs : "+rating3.size()+"  |  Moderate logs : "+rating2.size()+"  |  Sad logs : "+rating1.size());
        ObservableList<PieChart.Data> list = FXCollections.observableArrayList(
                new PieChart.Data("Happy",rating3.size()*100/(total)),
                new PieChart.Data("Moderate",rating2.size()*100/(total)),
                new PieChart.Data("Sad",rating1.size()*100/(total))
        );
        pieChart.setData(list);
        overall();

    }
    public int DateDifference() {
        Calendar cal1 = new GregorianCalendar();
        Calendar cal2 = new GregorianCalendar();
        String temp[] = datepickerfrom.getValue().toString().split("\\-");
        cal1.set(Integer.parseInt(temp[0]), Integer.parseInt(temp[1]), Integer.parseInt(temp[2]));
        temp = datepickerto.getValue().toString().split("\\-");
        cal2.set(Integer.parseInt(temp[0]), Integer.parseInt(temp[1]), Integer.parseInt(temp[2]));
        return daysBetween(cal1.getTime(), cal2.getTime());
    }

    public int daysBetween(Date d1, Date d2) {
        return (int) ((d2.getTime() - d1.getTime()) / (1000 * 60 * 60 * 24));
    }
    private static Date addDays(Date d1, int i) {
        GregorianCalendar cal = new GregorianCalendar();
        cal.setTime(d1);
        cal.add(Calendar.DATE, 1);
        return cal.getTime();
    }
    public void overall(){
        lineChart.getData().clear();
        XYChart.Series<String, Number> series = new XYChart.Series<String,Number>();
        for (int i = 0; i < ratings.size(); i++) {
            series.getData().add(new XYChart.Data<String,Number>(allDatesString.get(i),ratings.get(i)));
        }
        lineChart.getData().add(series);
    }
    public void sad(){
        lineChart.getData().clear();
        XYChart.Series<String, Number> series1 = new XYChart.Series<String,Number>();
        for (int i = 0; i < rating1.size(); i++) {
            series1.getData().add(new XYChart.Data<String,Number>(daysrating1.get(i),rating1.get(i)));
        }
        lineChart.getData().add(series1);
    }
    public void moderate(){
        lineChart.getData().clear();
        XYChart.Series<String, Number> series2 = new XYChart.Series<String,Number>();
        for (int i = 0; i < rating2.size(); i++) {
            series2.getData().add(new XYChart.Data<String,Number>(daysrating2.get(i),rating2.get(i)));
        }
        lineChart.getData().add(series2);
    }
    public void happy(){
        lineChart.getData().clear();
        XYChart.Series<String, Number> series3 = new XYChart.Series<String,Number>();
        for (int i = 0; i < rating3.size(); i++) {
            series3.getData().add(new XYChart.Data<String,Number>(daysrating3.get(i),rating3.get(i)));
        }
        lineChart.getData().add(series3);
    }

}
