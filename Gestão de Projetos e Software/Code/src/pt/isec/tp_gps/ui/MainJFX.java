package pt.isec.tp_gps.ui;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import pt.isec.tp_gps.model.Decorator;

public class MainJFX extends Application {
    Decorator model;
    @Override
    public void init() throws Exception {
        super.init();
    }

    public MainJFX() {
        model = new Decorator();
    }


    @Override
    public void start(Stage stage) throws Exception {

        //Definição do stage1
        Stage stage1 = new Stage();
        stage1.setMinWidth(950);
        stage1.setMinHeight(600);
        stage1.initOwner(stage);
        stage1.initModality(Modality.WINDOW_MODAL);
        try {
            Image _new = new Image(getClass().getResourceAsStream("./resources/images/pills.png"));
            stage1.getIcons().add(_new);
        }catch (NullPointerException e){}

        //Definição do stage2
        Stage stage2 = new Stage();
        stage2.setMinWidth(800);
        stage2.setMinHeight(300);
        stage2.initOwner(stage1);
        stage2.initModality(Modality.WINDOW_MODAL);
        try {
            Image _new1 = new Image(getClass().getResourceAsStream("./resources/images/pills.png"));
            stage2.getIcons().add(_new1);
        }catch (NullPointerException e){}

        //Definição do RootPane principal
        RootPane root = new RootPane(model,stage1,stage2);
        Scene scene = new Scene(root,600,400);
        try {
            Image _new2 = new Image(getClass().getResourceAsStream("./resources/images/pills.png"));
            stage.getIcons().add(_new2);
        }catch (NullPointerException e){}
        stage.setScene(scene);
        stage.setTitle("GestãoDeMedicamentos");
        stage.setMinWidth(1200);
        stage.setMinHeight(700);
        stage.show();


        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                t.consume();
                Alert a = new Alert(Alert.AlertType.CONFIRMATION);
                a.setTitle("Confirmação de Saída!");
                a.setHeaderText(null);
                a.setContentText("Deseja mesmo sair?");
                final Stage stage = (Stage) a.getDialogPane().getScene().getWindow();
                try {
                    stage.getIcons().add(new Image(this.getClass().getResource("./resources/images/pills.png").toString()));
                }catch (NullPointerException e){}
                a.showAndWait();
                if(a.getResult() == ButtonType.OK){
                    model.serializaObjeto();
                    stage.close();
                    Platform.exit();
                    System.exit(0);
                }
            }
        });
    }
}
