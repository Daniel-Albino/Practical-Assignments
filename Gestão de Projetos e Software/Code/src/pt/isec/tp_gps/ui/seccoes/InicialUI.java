package pt.isec.tp_gps.ui.seccoes;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import pt.isec.tp_gps.model.Decorator;
import pt.isec.tp_gps.model.fsm.MEEstados;


public class InicialUI extends BorderPane {
    Decorator model;
    Button btnStart,btnExit;
    Label lb;

    public InicialUI(Decorator model) {
        this.model = model;
        createViews();
        registerHandlers();
        update();
    }

    private void createViews() {

        lb = new Label("Gestão de Medicamentos");
        lb.setTextFill(Color.DARKBLUE);
        lb.setId("Special");
        btnStart = new Button("Entrar");
        btnStart.setMinWidth(100);
        btnExit  = new Button("Sair");
        btnExit.setMinWidth(100);
        VBox vBox = new VBox(lb,btnStart,btnExit);
        vBox.setAlignment(Pos.CENTER);
        vBox.setSpacing(50);
        this.setCenter(vBox);
    }

    private void registerHandlers() {
        model.addPropertyChangeListener(evt -> { update(); });

        btnStart.setOnAction( event -> {
            model.next();
        });
        btnExit.setOnAction( event -> {
                Alert a = new Alert(Alert.AlertType.CONFIRMATION);
                a.setTitle("Confirmação de Saída!");
                a.setHeaderText(null);
                a.setContentText("Deseja mesmo sair?");
                final Stage stage = (Stage) a.getDialogPane().getScene().getWindow();
                try {
                    stage.getIcons().add(new Image(this.getClass().getResource("./resources/images/pills.png").toString()));
                }catch (NullPointerException e){}
                a.showAndWait();
                if(a.getResult() == ButtonType.OK) {
                    model.serializaObjeto();
                    stage.close();
                    Platform.exit();
                    System.exit(0);
                }
        });

    }


    private void update() {

        if (model.getState() != MEEstados.ESTADOINICIAL) {
            this.setVisible(false);
            return;
        }
        this.setVisible(true);
    }
}

