package ru.regiuss.app.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import ru.regiuss.app.core.ViewHandler;

public class SelectController {

    private ViewHandler vh;

    @FXML
    void openTestScreen(ActionEvent event) {
        vh.openTestStartScreen();
    }

    @FXML
    void openTextEditorScreen(ActionEvent event) {

    }

    public void init(ViewHandler vh){
        this.vh = vh;
    }

}
