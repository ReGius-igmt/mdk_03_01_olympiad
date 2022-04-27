package ru.regiuss.app.controller;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import ru.regiuss.app.core.ViewHandler;
import ru.regiuss.app.modal.NotificationModal;
import ru.regiuss.app.model.Question;
import ru.regiuss.app.model.Test;

import java.util.Timer;
import java.util.TimerTask;

public class TestController {

    @FXML
    private Label answerDescription;

    @FXML
    private Text answerIndex;

    @FXML
    private TilePane answersState;

    @FXML
    private VBox bodyQuestion;

    @FXML
    private Text group;

    @FXML
    private Label name;

    @FXML
    private VBox content;

    @FXML
    private Text maxScore;

    @FXML
    private Text score;

    @FXML
    private FlowPane imagesPane;

    @FXML
    private Text time;

    private Test test;
    private ViewHandler vh;
    private double scores = 0;
    private Question current;
    private long startTime;
    private Timer timer;

    @FXML
    void complete(ActionEvent event) {
        for(Question q : test.getQuestions()){
            if(!q.isDone()){
                NotificationModal modal = new NotificationModal("Нужно ответить на все вопросы для завершения теста");
                modal.setOnClose(vh::hideModal);
                vh.showModal(modal);
                return;
            }
        }
        if(current != null)questionSwitch();
        timer.cancel();
        vh.openTestEndScreen(test, startTime, System.currentTimeMillis());
    }

    @FXML
    void next(ActionEvent event) {
        questionSwitch();
        current = test.next();
        if(current == null)complete(null);
        else loadQuestion();
    }

    @FXML
    void previous(ActionEvent event) {
        questionSwitch();
        current = test.previous();
        if(current == null)complete(null);
        else loadQuestion();
    }

    private void questionSwitch(){
        if(current == null)return;
        current.getStateBlock().getStyleClass().remove("current");
        if(current.isDone()){
            if(current.isSuccess()){
                current.getStateBlock().getStyleClass().add("success");
                scores += current.getScore();
                score.setText(Double.toString(scores));
            }
            else current.getStateBlock().getStyleClass().add("fail");
        }
    }

    public void init(ViewHandler vh, Test test){
        this.vh = vh;
        this.test = test;
        maxScore.setText(Double.toString(test.getScoreMax()));
        name.setText(test.getUserName());
        group.setText(test.getUserGroup());
        current = test.getCurrentQuestion();
        if(test.getQuestions().length > 0){
            for(int i = 0; i < test.getQuestions().length; i++){
                Question question = test.getQuestions()[i];
                Text text = new Text((i+1) + "");
                text.setFont(new Font(14));
                HBox hBox = new HBox(text);
                hBox.setPrefSize(25,35);
                hBox.setAlignment(Pos.CENTER);
                hBox.setOnMouseClicked(mouseEvent -> {
                    if(current.equals(question) || question.isDone())return;
                    questionSwitch();
                    current = question;
                    loadQuestion();
                });
                question.setStateBlock(hBox);
                answersState.getChildren().add(hBox);
            }
            loadQuestion();
        }
        startTimer();
    }

    private void loadQuestion(){
        current.getStateBlock().getStyleClass().add("current");
        imagesPane.getChildren().clear();
        bodyQuestion.getChildren().clear();
        answerIndex.setText("Вопрос " + (test.getCurrentQuestionIndex() + 1));
        current.loadImages(imagesPane, vh);
        current.load(bodyQuestion);
        answerDescription.setText(current.getDescription());
    }

    private void startTimer(){
        StringProperty timeProperty = new SimpleStringProperty("00:00:00");
        time.textProperty().bind(timeProperty);
        startTime = System.currentTimeMillis();
        timer = new Timer(true);
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                long s = (System.currentTimeMillis() - startTime)/1000;
                timeProperty.setValue(String.format("%02d:%02d:%02d", s / 3600, (s % 3600) / 60, (s % 60)));
            }
        }, 1000, 1000);
    }

}
