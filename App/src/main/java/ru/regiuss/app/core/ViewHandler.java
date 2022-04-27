package ru.regiuss.app.core;

import javafx.animation.Transition;
import javafx.animation.TranslateTransition;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.TilePane;
import javafx.stage.Stage;
import javafx.util.Duration;
import ru.regiuss.app.controller.*;
import ru.regiuss.app.modal.Modal;
import ru.regiuss.app.model.Question;
import ru.regiuss.app.model.SimpleQuestion;
import ru.regiuss.app.model.Test;

import java.io.IOException;

public class ViewHandler {

    private Stage stage;
    private ObjectProperty<Parent> content;
    private ObjectProperty<Modal> modal;
    private BooleanProperty modalView;

    public ViewHandler(){
        content = new SimpleObjectProperty<>();
        modal = new SimpleObjectProperty<>();
        modalView = new SimpleBooleanProperty(false);
    }

    public void init(Stage stage){
        this.stage = stage;
        stage.setTitle("Олимпиада");
        stage.setMinWidth(800);
        stage.setMinHeight(600);

        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/main.fxml"));
        try {
            Parent root = loader.load();
            MainController controller = loader.getController();
            controller.init(this, content, modal, modalView);
            stage.setScene(new Scene(root));
        } catch (IOException e) {
            e.printStackTrace();
        }
        stage.show();
        openSelectScreen();
    }

    public void hideModal(){
        modalView.setValue(false);
    }

    public void setModal(Modal modal){
        this.modal.setValue(modal);
    }

    public void showModal(){
        modalView.setValue(true);
    }

    public void showModal(Modal modal){
        setModal(modal);
        showModal();
    }

    public void openSelectScreen(){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/select.fxml"));
        try {
            Parent root = loader.load();
            SelectController controller = loader.getController();
            controller.init(this);
            content.setValue(root);
            TranslateTransition transition = new TranslateTransition(Duration.millis(500), root);
            transition.setFromY(40);
            transition.setToY(0);
            transition.play();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openTestStartScreen(){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/testStart.fxml"));
        try {
            Parent root = loader.load();
            TestStartController controller = loader.getController();
            controller.init(this);
            content.setValue(root);
            TranslateTransition transition = new TranslateTransition(Duration.millis(500), root);
            transition.setFromY(40);
            transition.setToY(0);
            transition.play();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openTestScreen(Test test){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/test.fxml"));
        try {
            Parent root = loader.load();
            TestController controller = loader.getController();
            controller.init(this, test);
            content.setValue(root);
            TranslateTransition transition = new TranslateTransition(Duration.millis(500), root);
            transition.setFromY(40);
            transition.setToY(0);
            transition.play();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openTestEndScreen(Test test, long startTime, long endTime){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/testEnd.fxml"));
        try {
            Parent root = loader.load();
            TestEndController controller = loader.getController();
            controller.init(this, test, startTime, endTime);
            content.setValue(root);
            TranslateTransition transition = new TranslateTransition(Duration.millis(500), root);
            transition.setFromY(40);
            transition.setToY(0);
            transition.play();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
