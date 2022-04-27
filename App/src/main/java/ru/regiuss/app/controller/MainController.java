package ru.regiuss.app.controller;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.util.Duration;
import ru.regiuss.app.core.ViewHandler;
import ru.regiuss.app.modal.Modal;

public class MainController {

    @FXML
    private BorderPane contentPane;

    @FXML
    private BorderPane modalPane;

    @FXML
    private AnchorPane root;

    private ViewHandler vh;

    public void init(ViewHandler vh, ObjectProperty<Parent> content, ObjectProperty<Modal> modal, BooleanProperty modalView){
        this.vh = vh;
        contentPane.centerProperty().bind(content);
        modalPane.centerProperty().bind(modal);

        modalPane.setOnMouseReleased(mouseEvent -> {
            if(mouseEvent.getTarget().equals(modalPane))vh.hideModal();
        });

        modalView.addListener((observableValue, oldValue, newValue) -> {
            if(newValue){
                modalPane.setVisible(true);
                modalPane.setManaged(true);
                FadeTransition ft = new FadeTransition(Duration.millis(250), modalPane);
                ft.setFromValue(0);
                ft.setToValue(1);
                ParallelTransition transition = new ParallelTransition(ft, modal.get().getShowTransition());
                transition.setOnFinished(actionEvent -> modal.get().onShow());
                transition.play();
            }else{
                FadeTransition ft = new FadeTransition(Duration.millis(250), modalPane);
                ft.setFromValue(1);
                ft.setToValue(0);
                ParallelTransition transition = new ParallelTransition(ft, modal.get().getHideTransition());
                transition.setOnFinished(actionEvent -> {
                    modalPane.setVisible(false);
                    modalPane.setManaged(false);
                    modal.get().onHide();
                });
                transition.play();
            }
        });
    }

}
