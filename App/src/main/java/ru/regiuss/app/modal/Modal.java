package ru.regiuss.app.modal;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.Transition;
import javafx.animation.TranslateTransition;
import javafx.scene.layout.AnchorPane;
import javafx.util.Duration;

public abstract class Modal extends AnchorPane {
    public Transition getShowTransition(){
        FadeTransition ft = new FadeTransition(Duration.millis(250));
        ft.setToValue(1);
        ft.setFromValue(0);
        TranslateTransition tt = new TranslateTransition(Duration.millis(200));
        tt.setToX(0);
        tt.setFromX(45);
        ParallelTransition transition = new ParallelTransition(this);
        transition.getChildren().addAll(ft, tt);
        return transition;
    }

    public Transition getHideTransition(){
        FadeTransition ft = new FadeTransition(Duration.millis(200), this);
        ft.setToValue(0);
        ft.setFromValue(1);
        return ft;
    }

    public void onShow(){}
    public void onHide(){}

    @FunctionalInterface
    public interface Action{
        void execute();
    }
}
