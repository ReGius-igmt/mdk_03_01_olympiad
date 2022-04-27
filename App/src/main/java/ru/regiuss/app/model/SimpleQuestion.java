package ru.regiuss.app.model;

import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SimpleQuestion extends Question{

    protected String[] variants;
    protected int correctAnswerIndex;
    protected Integer currentAnswerIndex;

    public SimpleQuestion(String description, double score){
        super(description, score);
    }

    public SimpleQuestion(String description, double score, String[] variants, int correctAnswerIndex){
        super(description, score);
        this.variants = variants;
        this.correctAnswerIndex = correctAnswerIndex;
    }

    @Override
    public void load(Pane parent) {
        ToggleGroup group = new ToggleGroup();
        for(int i = 0; i < variants.length; i++){
            String variant = variants[i];
            RadioButton checkBox = new RadioButton(variant);
            checkBox.setFont(new Font(14));
            checkBox.setToggleGroup(group);
            checkBox.setAlignment(Pos.CENTER_LEFT);
            checkBox.setWrapText(true);
            int finalI = i;
            checkBox.setOnAction(actionEvent -> {
                currentAnswerIndex = finalI;
            });
            parent.getChildren().add(checkBox);
        }
    }

    public static Question load(JSONObject obj) {
        SimpleQuestion response = new SimpleQuestion(obj.getString("text"),obj.getDouble("score"));
        response.setScoreMax(obj.getDouble("score"));
        if(obj.has("img")){
            JSONArray img = obj.getJSONArray("img");
            String[] imgs = new String[img.length()];
            for (int i = 0; i < img.length(); i++) {
                imgs[i] = img.getString(i);
            }
            response.setImages(imgs);
        }

        JSONArray variantsObj = obj.getJSONArray("variants");
        List<JSONObject> variantsList = new ArrayList<>(variantsObj.length());
        for(Object o : variantsObj)variantsList.add((JSONObject) o);
        String[] variants = new String[variantsObj.length()];
        Collections.shuffle(variantsList);
        for (int i = 0; i < variantsList.size(); i++) {
            JSONObject json = variantsList.get(i);
            variants[i] = json.getString("text");
            if(json.has("success"))response.setCorrectAnswerIndex(i);
        }
        response.setVariants(variants);
        return response;
    }

    @Override
    public boolean isDone() {
        if(!done)done = currentAnswerIndex != null;
        return done;
    }

    @Override
    public boolean isSuccess() {
        return correctAnswerIndex == currentAnswerIndex;
    }

    @Override
    public String getUserAnswer() {
        return variants[currentAnswerIndex];
    }

    @Override
    public String getCorrectAnswer() {
        return variants[correctAnswerIndex];
    }

    public String[] getVariants() {
        return variants;
    }

    public void setVariants(String[] variants) {
        this.variants = variants;
    }

    public int getCorrectAnswerIndex() {
        return correctAnswerIndex;
    }

    public void setCorrectAnswerIndex(int correctAnswerIndex) {
        this.correctAnswerIndex = correctAnswerIndex;
    }
}
