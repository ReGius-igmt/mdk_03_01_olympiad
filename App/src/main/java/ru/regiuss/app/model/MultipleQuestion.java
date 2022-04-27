package ru.regiuss.app.model;

import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;

public class MultipleQuestion extends Question{

    protected String[] variants;
    protected Set<Integer> correctAnswerIndex;
    protected Set<Integer> currentAnswerIndex;

    public MultipleQuestion(String description, double score){
        super(description, score);
        currentAnswerIndex = new HashSet<>();
        correctAnswerIndex = new HashSet<>();
    }

    public MultipleQuestion(String description, double score, String[] variants, Set<Integer> correctAnswerIndex){
        super(description, score);
        this.variants = variants;
        currentAnswerIndex = new HashSet<>();
        this.correctAnswerIndex = correctAnswerIndex;
    }

    @Override
    public void load(Pane parent) {
        for(int i = 0; i < variants.length; i++){
            String variant = variants[i];
            CheckBox checkBox = new CheckBox(variant);
            checkBox.setFont(new Font(14));
            checkBox.setAlignment(Pos.CENTER_LEFT);
            checkBox.setWrapText(true);
            int finalI = i;
            checkBox.selectedProperty().addListener((observableValue, aBoolean, t1) -> {
                if(t1)currentAnswerIndex.add(finalI);
                else currentAnswerIndex.remove(finalI);
            });
            parent.getChildren().add(checkBox);
        }
    }

    public static Question load(JSONObject obj) {
        MultipleQuestion response = new MultipleQuestion(obj.getString("text"),obj.getDouble("score"));
        double scoreMax = 0;
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
            if(json.has("success")){
                response.addCorrectAnswerIndex(i);
                scoreMax += response.score;
            }
        }
        response.setScoreMax(scoreMax);
        response.setVariants(variants);
        return response;
    }

    @Override
    public boolean isDone() {
        if(!done)done = !currentAnswerIndex.isEmpty();
        return done;
    }

    @Override
    public boolean isSuccess() {
        for(int i : currentAnswerIndex){
            if(correctAnswerIndex.contains(i))return true;
        }
        return false;
    }

    @Override
    public String getUserAnswer() {
        StringBuilder sb = new StringBuilder();
        for(int i : currentAnswerIndex){
            sb.append(variants[i]).append(" ");
        }
        return sb.toString();
    }

    @Override
    public String getCorrectAnswer() {
        StringBuilder sb = new StringBuilder();
        for(int i : correctAnswerIndex){
            sb.append(variants[i]).append(" ");
        }
        return sb.toString();
    }

    public String[] getVariants() {
        return variants;
    }

    public void setVariants(String[] variants) {
        this.variants = variants;
    }

    public Set<Integer> getCorrectAnswerIndex() {
        return correctAnswerIndex;
    }

    public void addCorrectAnswerIndex(int correctAnswerIndex) {
        this.correctAnswerIndex.add(correctAnswerIndex);
    }

    @Override
    public double getScore() {
        double score = 0;
        for(int i : currentAnswerIndex){
            if(correctAnswerIndex.contains(i))score += this.score;
            else score -= 1;
        }
        if(score < 0)score = 0;
        return score;
    }
}
