package ru.regiuss.app.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import ru.regiuss.app.core.ViewHandler;
import ru.regiuss.app.modal.NotificationModal;
import ru.regiuss.app.model.Question;
import ru.regiuss.app.model.Test;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;

public class TestEndController {

    @FXML
    private Text group;

    @FXML
    private Label name;

    @FXML
    private Text score;

    @FXML
    private Text maxScore;

    @FXML
    private Text time;

    @FXML
    private TilePane answersState;

    private ViewHandler vh;
    private Test test;
    private long startTime;
    private long endTime;

    @FXML
    void exit(ActionEvent event) {
        exit();
    }

    @FXML
    void saveAndExit(ActionEvent event) {
        try {
            File f = new File("./ответы/");
            if(!f.exists())f.mkdir();

            BufferedWriter writer = Files.newBufferedWriter(Path.of(f.getAbsolutePath() + "/" + test.getUserName() + " " + test.getUserGroup() + ".csv"), StandardCharsets.UTF_8);
            writer.write('\ufeff');
            CSVPrinter csvPrinter = new CSVPrinter(writer, CSVFormat.EXCEL);
            csvPrinter.printRecord("ФИО", test.getUserName());
            csvPrinter.printRecord("Группа", test.getUserGroup());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
            csvPrinter.printRecord("Начало", simpleDateFormat.format(new Date(startTime)));
            csvPrinter.printRecord("Конец", simpleDateFormat.format(new Date(endTime)));
            csvPrinter.printRecord("Вопрос", "Ответ", "Правильный ответ", "Баллы");
            for(Question q : test.getQuestions()){
                csvPrinter.printRecord(
                        q.getDescription(),
                        q.getUserAnswer(),
                        q.getCorrectAnswer(),
                        q.isSuccess() ? q.getScore() : 0
                );
            }
            csvPrinter.printRecord("Всего", test.getScoreSum());
            csvPrinter.flush();
            csvPrinter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        exit();
    }

    private void exit(){
        vh.openSelectScreen();
    }

    public void init(ViewHandler vh, Test test, long startTime, long endTime){
        this.vh = vh;
        this.test = test;
        this.startTime = startTime;
        this.endTime = endTime;
        //this.answersState.getChildren().addAll(answersState.getChildren());
        for(int i = 0; i < test.getQuestions().length; i++){
            Question question = test.getQuestions()[i];
            Text text = new Text((i+1) + "");
            text.setFont(new Font(14));
            HBox hBox = new HBox(text);
            hBox.setPrefSize(25,35);
            hBox.setAlignment(Pos.CENTER);
            if(question.isSuccess())hBox.getStyleClass().add("success");
            else hBox.getStyleClass().add("fail");
            answersState.getChildren().add(hBox);
        }
        name.setText(test.getUserName());
        group.setText(test.getUserGroup());
        score.setText(Double.toString(test.getScoreSum()));
        maxScore.setText(Double.toString(test.getScoreMax()));
        long s = (endTime - startTime)/1000;
        time.setText(String.format("%02d:%02d:%02d", s / 3600, (s % 3600) / 60, (s % 60)));
    }

}
