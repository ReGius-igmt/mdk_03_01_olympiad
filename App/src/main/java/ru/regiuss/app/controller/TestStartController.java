package ru.regiuss.app.controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import ru.regiuss.app.core.ViewHandler;
import ru.regiuss.app.modal.NotificationModal;
import ru.regiuss.app.model.Test;

import java.io.File;
import java.util.Objects;

public class TestStartController {

    @FXML
    private TextField group;

    @FXML
    private Text groupErrorMessage;

    @FXML
    private TextField name;

    @FXML
    private Text nameErrorMessage;

    private ViewHandler vh;

    public void init(ViewHandler vh) {
        this.vh = vh;
    }

    @FXML
    void start(ActionEvent event) {
        nameErrorMessage.setText(null);
        groupErrorMessage.setText(null);
        boolean success = true;
        if(name.getText() == null || name.getText().isEmpty()){
            nameErrorMessage.setText("Заполните это поле");
            success = false;
        }
        if(group.getText() == null || group.getText().isEmpty()){
            groupErrorMessage.setText("Заполните это поле");
            success = false;
        }
        if(nameErrorMessage.getText() == null || nameErrorMessage.getText().isEmpty()){
            String[] nameData = name.getText().split(" ");
            if(nameData.length < 3){
                nameErrorMessage.setText("Заполните ФИО полностью");
                success = false;
            }
        }
        File file = new File("./ответы");
        if(file.exists()){
            for(File f : Objects.requireNonNull(file.listFiles())){
                if(f.getName().substring(0, f.getName().length()-4).equals(name.getText() + " " + group.getText())){
                    NotificationModal modal = new NotificationModal("Пользователь с такими данными уже проходил тест");
                    modal.setOnClose(vh::hideModal);
                    vh.showModal(modal);
                    success = false;
                    break;
                }
            }
        }
        if(success){
            Test test = new Test(name.getText(), group.getText());
            test.load(getClass().getResourceAsStream("/test.json"));
            vh.openTestScreen(test);
        }
    }

}