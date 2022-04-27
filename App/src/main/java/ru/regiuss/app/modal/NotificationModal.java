package ru.regiuss.app.modal;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

import java.io.IOException;

public class NotificationModal extends Modal{

    @FXML
    private Button closeBtn;

    @FXML
    private Label text;

    public NotificationModal(String text){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/notificationModal.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.text.setText(text);
    }

    public void setOnClose(Action action){
        closeBtn.setOnAction(actionEvent -> action.execute());
    }
}
