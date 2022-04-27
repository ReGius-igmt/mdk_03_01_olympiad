package ru.regiuss.app.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.geometry.Pos;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class FieldQuestion extends Question{
    protected String correctAnswer;
    protected StringProperty currentAnswer;

    public FieldQuestion(String description, double score){
        super(description, score);
        currentAnswer = new SimpleStringProperty();
    }

    public FieldQuestion(String description, double score, String correctAnswerIndex){
        super(description, score);
        this.correctAnswer = correctAnswerIndex;
        currentAnswer = new SimpleStringProperty();
    }

    @Override
    public void load(Pane parent) {
        TextField field = new TextField();
        field.setFont(new Font(14));
        field.setMaxWidth(400);
        field.setPromptText("Ответ");
        field.textProperty().bindBidirectional(currentAnswer);
        parent.getChildren().add(field);
    }

    public static Question load(JSONObject obj) {
        FieldQuestion response = new FieldQuestion(obj.getString("text"),obj.getDouble("score"));
        if(obj.has("img")){
            JSONArray img = obj.getJSONArray("img");
            String[] imgs = new String[img.length()];
            for (int i = 0; i < img.length(); i++) {
                imgs[i] = img.getString(i);
            }
            response.setImages(imgs);
        }
        response.setScoreMax(obj.getDouble("score"));
        response.correctAnswer = obj.getString("correct");
        return response;
    }

    @Override
    public boolean isDone() {
        if(!done)done = currentAnswer.get() != null && !currentAnswer.get().isEmpty();
        return done;
    }

    @Override
    public boolean isSuccess() {
        return correctAnswer.toLowerCase().equals(currentAnswer.get().toLowerCase());
    }

    @Override
    public String getUserAnswer() {
        return currentAnswer.get();
    }

    @Override
    public String getCorrectAnswer() {
        return correctAnswer;
    }


    public void setCorrectAnswer(String correctAnswerIndex) {
        this.correctAnswer = correctAnswer;
    }
}
