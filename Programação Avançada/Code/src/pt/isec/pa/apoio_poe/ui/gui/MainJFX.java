package pt.isec.pa.apoio_poe.ui.gui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import pt.isec.pa.apoio_poe.model.Manager;

public class MainJFX extends Application {
    Manager manager;

    @Override
    public void init() throws Exception {
        super.init();
        manager = new Manager();
    }

    @Override
    public void start(Stage stage) throws Exception {
        configureState(stage);
        //configureState(new Stage());
    }

    private void configureState(Stage stage){
        RootPane root = new RootPane(manager);
        Scene scene = new Scene(root,1200,600);
        stage.setScene(scene);
        stage.setScene(scene);
        stage.setTitle("PA");
        stage.setMinWidth(1150);
        stage.setMinHeight(600);
        stage.show();
        stage.setOnCloseRequest(evt -> {
            Platform.exit();
        });
    }
}
