package ru.regiuss.app.model;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import org.json.JSONObject;
import ru.regiuss.app.core.ViewHandler;
import ru.regiuss.app.modal.ImagePreviewModal;

public abstract class Question {
    protected double scoreMax;
    protected double score;
    protected boolean done;
    protected String description;
    protected String[] images;
    protected Node stateBlock;

    public Question(String description, double score){
        this.description = description;
        this.score = score;
        this.images = new String[0];
    }

    public Node getStateBlock() {
        return stateBlock;
    }

    public void setStateBlock(Node stateBlock) {
        this.stateBlock = stateBlock;
    }

    public double getScore() {
        return score;
    }

    public void loadImages(Pane parent, ViewHandler vh){
        for (String image : images) {
            ImageView imageView = new ImageView();
            imageView.setImage(new Image(getClass().getResourceAsStream("/img/" + image)));
            imageView.setFitWidth(530);
            imageView.setFitHeight(200);
            imageView.setPreserveRatio(true);
            imageView.setOnMouseClicked(mouseEvent -> {
                ImagePreviewModal modal = new ImagePreviewModal(imageView.getImage());
                modal.setOnClose(vh::hideModal);
                vh.showModal(modal);
            });
            parent.getChildren().add(imageView);
        }
    }

    public abstract void load(Pane parent);

    public abstract boolean isDone();

    public abstract boolean isSuccess();

    public String getDescription() {
        return description;
    }

    public String[] getImages() {
        return images;
    }

    public Question setImages(String[] images) {
        this.images = images;
        return this;
    }

    public void setScoreMax(double scoreMax) {
        this.scoreMax = scoreMax;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public abstract String getUserAnswer();
    public abstract String getCorrectAnswer();
}
