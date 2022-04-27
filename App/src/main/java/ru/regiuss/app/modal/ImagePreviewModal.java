package ru.regiuss.app.modal;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.awt.event.MouseEvent;
import java.io.IOException;

public class ImagePreviewModal extends Modal{
    @FXML
    private Button closeBtn;

    @FXML
    private ImageView img;

    @FXML
    private HBox bg;

    public ImagePreviewModal(Image image){
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/imagePreviewModal.fxml"));
        loader.setRoot(this);
        loader.setController(this);
        try {
            loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        img.setImage(image);
        img.setPreserveRatio(true);
        if(image.getWidth() > 700)img.setFitWidth(700);
        if(image.getHeight() > 700)img.setFitWidth(700);
        img.setPreserveRatio(true);
    }

    public void setOnClose(Modal.Action action){
        closeBtn.setOnAction(actionEvent -> action.execute());
        bg.setOnMouseClicked(mouseEvent -> action.execute());
    }
}
