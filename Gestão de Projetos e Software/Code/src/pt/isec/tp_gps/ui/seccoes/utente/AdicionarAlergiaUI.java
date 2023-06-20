package pt.isec.tp_gps.ui.seccoes.utente;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import pt.isec.tp_gps.model.Decorator;

public class AdicionarAlergiaUI extends BorderPane {
    private static final int NR_FIELDS = 5;
    private static final int NR_FIELDS_SEND = 8;
    Decorator model;
    Label lbNome;
    TextField tfNome;
    Button apply, cancel;
    HBox ButtonApplyCancel;


    HBox hb1, hb2, hb3;
    VBox vb1;


    public AdicionarAlergiaUI(Decorator model) {
        this.model = model;
        createViews();
        registerHandlers();
        update();
    }

    private void createViews() {


        lbNome = new Label("Alergia:");
        tfNome = new TextField();
        tfNome.setPrefWidth(200);
        tfNome.focusTraversableProperty().set(false);
        tfNome.setPrefHeight(20);
        tfNome.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if(tfNome.getText().length() > 100){
                    String aux = tfNome.getText().substring(0, 100);
                    tfNome.setText(aux);
                }
            }
        });
        apply = new Button("Aplicar");
        apply.focusTraversableProperty().set(false);
        apply.setStyle("-fx-background-color: rgba(57,217,57,0.6);-fx-border-color: grey");

        cancel = new Button("Cancelar");
        cancel.focusTraversableProperty().set(false);
        cancel.setStyle("-fx-background-color: rgba(217,70,70,0.59);-fx-border-color: grey");

        ButtonApplyCancel = new HBox(apply, cancel);
        ButtonApplyCancel.setSpacing(10);
        ButtonApplyCancel.setAlignment(Pos.CENTER_RIGHT);
        ButtonApplyCancel.setPadding(new Insets(0, 20, 0, 0));


        hb1 = new HBox(lbNome, tfNome);
        hb1.setSpacing(10);


        vb1 = new VBox(hb1, ButtonApplyCancel);
        vb1.setPadding(new Insets(20));
        vb1.setPrefWidth(Integer.MAX_VALUE);
        vb1.setSpacing(15);

        this.setCenter(vb1);


        //HBox lbBAddCancelPerson = new HBox(AddPersonlb,ButtonAddCancel);
        //HBox.setHgrow(ButtonAddCancel, Priority.ALWAYS);
    }

    private void registerHandlers() {
        model.addPropertyChangeListener(evt -> {
            update();
        });

        apply.setOnAction(event -> {
            model.addTempAlergia(tfNome.getText());
            model.updateUI();
        });

        cancel.setOnAction(event -> {
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.close();
        });

 /*

        DropShadow shadow = new DropShadow();
//Adding the shadow when the mouse cursor is on
        accept.addEventHandler(MouseEvent.MOUSE_ENTERED,
                new EventHandler<MouseEvent>() {
                    @Override public void handle(MouseEvent e) {
                        accept.setEffect(shadow);
                        //accept.setStyle("-fx-border-color: #1a83c6; -fx-border-width: 2");
                    }
                });

//Removing the shadow when the mouse cursor is off
        accept.addEventHandler(MouseEvent.MOUSE_EXITED,
                new EventHandler<MouseEvent>() {
                    @Override public void handle(MouseEvent e) {
                        accept.setEffect(null);
                    }
                });



        //Adding the shadow when the mouse cursor is on
        cancel.addEventHandler(MouseEvent.MOUSE_ENTERED,
                new EventHandler<MouseEvent>() {
                    @Override public void handle(MouseEvent e) {
                        cancel.setEffect(shadow);
                        //accept.setStyle("-fx-border-color: #1a83c6; -fx-border-width: 2");
                    }
                });

        //Removing the shadow when the mouse cursor is off
        cancel.addEventHandler(MouseEvent.MOUSE_EXITED,
                new EventHandler<MouseEvent>() {
                    @Override public void handle(MouseEvent e) {
                        cancel.setEffect(null);
                    }
                });
                */


    }

    private void update() {
    }
}