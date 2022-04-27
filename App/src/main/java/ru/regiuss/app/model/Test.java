package ru.regiuss.app.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Test {
    private String userName;
    private String userGroup;
    private Question[] questions;
    private int currentQuestion = 0;

    public Test(String userName, String userGroup) {
        this.userName = userName;
        this.userGroup = userGroup;
    }

    public void load(InputStream is){
        List<JSONObject> ss = null;
        try {
            JSONArray data = new JSONArray(new String(is.readAllBytes(), StandardCharsets.UTF_8));
            is.close();
            questions = new Question[data.length()];
            ss = new ArrayList<>(data.length());
            for(Object o : data){
                ss.add((JSONObject) o);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Collections.shuffle(ss);
        for (int i = 0; i < ss.size(); i++) {
            JSONObject o = ss.get(i);
            switch (o.getString("type")){
                case "simple" -> questions[i] = SimpleQuestion.load(o);
                case "multiple" -> questions[i] = MultipleQuestion.load(o);
                case "field" -> questions[i] = FieldQuestion.load(o);
            }
        }
    }

    public String getUserName() {
        return userName;
    }

    public double getScoreSum(){
        double sum = 0;
        for(Question q : questions){
            if(q.isSuccess())sum += q.getScore();
        }
        return sum;
    }

    public double getScoreMax(){
        double sum = 0;
        for(Question q : questions){
            sum += q.scoreMax;
        }
        return sum;
    }

    public String getUserGroup() {
        return userGroup;
    }

    public Question next(){
        for(int i = currentQuestion+1; i < questions.length; i++){
            if(!questions[i].isDone()){
                currentQuestion = i;
                return getCurrentQuestion();
            }
        }
        for(int i = 0; i < currentQuestion; i++){
            if(!questions[i].isDone()){
                currentQuestion = i;
                return getCurrentQuestion();
            }
        }
        if(!questions[currentQuestion].isDone())return getCurrentQuestion();
        return null;
    }

    public Question previous(){
        for(int i = currentQuestion-1; i >= 0; i--){
            if(!questions[i].isDone()){
                currentQuestion = i;
                return getCurrentQuestion();
            }
        }
        for(int i = questions.length-1; i > currentQuestion; i--){
            if(!questions[i].isDone()){
                currentQuestion = i;
                return getCurrentQuestion();
            }
        }
        if(!questions[currentQuestion].isDone())return getCurrentQuestion();
        return null;
    }



    public Question getCurrentQuestion() {
        return questions[currentQuestion];
    }

    public int getCurrentQuestionIndex() {
        return currentQuestion;
    }

    public Question[] getQuestions() {
        return questions;
    }
}
