package ru.regiuss.app;

import javafx.application.Application;
import javafx.stage.Stage;
import ru.regiuss.app.core.ViewHandler;

public class App extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        ViewHandler vh = new ViewHandler();
        vh.init(stage);
    }
}
