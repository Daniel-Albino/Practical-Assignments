package pt.isec.tp_pd.ui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import pt.isec.tp_pd.model.ModelManager;

import java.util.Objects;

public class MainJFX extends Application {
    ModelManager model;

    @Override
    public void init() throws Exception {
        super.init();
    }

    public MainJFX() {
        model = new ModelManager();
    }

    @Override
    public void start(Stage stage) throws Exception {
        RootPane root = new RootPane(model);
        Scene scene = new Scene(root,600,400);
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("./resources/images/isec_64x64.png"))));
        stage.setScene(scene);
        stage.setTitle("TicketPD");
        stage.setMinWidth(1000);
        stage.setMinHeight(700);
        stage.show();

        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                t.consume();
                Alert a = new Alert(Alert.AlertType.CONFIRMATION);
                a.setTitle("Confirmação de saída!");
                a.setHeaderText(null);
                a.setContentText("Quer mesmo sair?");
                final Stage stage = (Stage) a.getDialogPane().getScene().getWindow();
                stage.getIcons().add(new Image(Objects.requireNonNull(this.getClass().getResource("./resources/images/isec_64x64.png")).toString()));
                a.showAndWait();
                if(a.getResult() == ButtonType.OK){
                    stage.close();
                    Platform.exit();
                    System.exit(0);
                }
            }
        });
    }
}