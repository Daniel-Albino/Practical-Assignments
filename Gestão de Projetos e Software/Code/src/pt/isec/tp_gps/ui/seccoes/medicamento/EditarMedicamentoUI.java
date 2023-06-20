package pt.isec.tp_gps.ui.seccoes.medicamento;


import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import pt.isec.tp_gps.model.Decorator;
import pt.isec.tp_gps.model.data.Medicamento;
import pt.isec.tp_gps.model.fsm.MEEstados;
import pt.isec.tp_gps.ui.seccoes.ToastMessage;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
public class EditarMedicamentoUI extends BorderPane {
    private static final int NR_FIELDS = 5;
    Decorator model;

    Label lbMed[];
    Label dataNasc;
    DatePicker datePicker;

    Calendar calendar;
    TextField tfMed[];
    TextArea ta;

    ComboBox<String> combo;
    TableView tvUtente;
    ListView<String> lAUtente, lDUtente;
    ObservableList<String> names;
    ObservableList<String> names1;

    Button apply,cancel,lb1;

    HBox hb1,hb2,hb3,hb4,hb5,hb6,hb7,hb8,ButtonApplyCancel;
    VBox vb1,vb2;


    //Image and ImageView
    private Medicamento medicamentoAnterior;

    public EditarMedicamentoUI(Decorator model, Medicamento medicamentoAnterior) {
        this.model = model;
        this.medicamentoAnterior = medicamentoAnterior;
        createViews();
        registerHandlers();
        update();
    }

    private void createViews() {

        lbMed = new Label[NR_FIELDS];
        tfMed = new TextField[NR_FIELDS];

        for (int i = 0; i < NR_FIELDS; i++) {
            lbMed[i] = new Label();
            tfMed[i] = new TextField();
            tfMed[i].setPrefWidth(Integer.MAX_VALUE);
            tfMed[i].setMaxWidth(220);
            tfMed[i].focusTraversableProperty().set(false);
            tfMed[i].setPrefHeight(20);
        }

        String[] medAux = new String[7];
        medAux[0] = medicamentoAnterior.getNome();
        medAux[1] = medicamentoAnterior.getID();
        medAux[2] = medicamentoAnterior.getModoAdministracao();
        medAux[3] = medicamentoAnterior.getPrincipioAtivo();
        medAux[4] = medicamentoAnterior.getLaboratorio();
        medAux[5] = medicamentoAnterior.getBula();


        for (int i = 0; i < NR_FIELDS; i++) {
            tfMed[i].setText(medAux[i]);
        }

        lbMed[0].setText("Nome:");
        lbMed[1].setText("ID:");
        lbMed[2].setText("Modo de administração:");
        lbMed[3].setText("Princípio ativo:");
        lbMed[4].setText("Laboratório:");
        dataNasc = new Label("Data de Validade:");
        tfMed[0].textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if(lbMed[0].getText().length() > 30){
                    String aux = lbMed[0].getText().substring(0, 30);
                    lbMed[0].setText(aux);
                }
            }
        });
        tfMed[1].textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if(lbMed[1].getText().length() > 10){
                    String aux = lbMed[1].getText().substring(0, 10);
                    lbMed[1].setText(aux);
                }
            }
        });
        tfMed[2].textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if(lbMed[2].getText().length() > 20){
                    String aux = lbMed[2].getText().substring(0, 20);
                    lbMed[2].setText(aux);
                }
            }
        });
        tfMed[3].textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if(lbMed[3].getText().length() > 50){
                    String aux = lbMed[3].getText().substring(0, 50);
                    lbMed[3].setText(aux);
                }
            }
        });
        tfMed[4].textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if(lbMed[4].getText().length() > 30){
                    String aux = lbMed[4].getText().substring(0, 30);
                    lbMed[4].setText(aux);
                }
            }
        });
        ta.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if(ta.getText().length() > 200){
                    String aux = ta.getText().substring(0, 200);
                    ta.setText(aux);
                }
            }
        });
        datePicker = new DatePicker();
        datePicker.focusTraversableProperty().set(false);
        datePicker.setEditable(false);
        datePicker.setDayCellFactory(d ->{
            return new DateCell() {
                @Override
                public void updateItem(LocalDate item, boolean empty) {
                    super.updateItem(item, empty);
                    setDisable(item.isBefore(LocalDate.now()));
                }
            };
        });
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        System.out.println(medicamentoAnterior.getDataValidadeString());

        datePicker.setValue(LocalDate.parse(medicamentoAnterior.getDataValidadeString(), formatter));

        ta = new TextArea();
        ta.setText(medAux[5] = medicamentoAnterior.getBula());
        ta.setPrefWidth(Integer.MAX_VALUE);



        hb1 = new HBox(lbMed[0], tfMed[0]);
        hb1.setSpacing(15);
        hb2 = new HBox(lbMed[1], tfMed[1]);
        hb2.setSpacing(15);
        hb3 = new HBox(lbMed[2], tfMed[2]);
        hb3.setSpacing(15);
        hb4 = new HBox(lbMed[3], tfMed[3]);
        hb4.setSpacing(15);
        hb5 = new HBox(lbMed[4], tfMed[4]);
        hb5.setSpacing(15);
        hb6 = new HBox(dataNasc, datePicker);
        hb6.setSpacing(15);

        vb2 = new VBox(hb1, hb2, hb6, hb3, hb4,hb5,ta);
        vb2.setSpacing(15);

        apply = new Button("Aplicar");
        apply.focusTraversableProperty().set(false);
        apply.setStyle("-fx-background-color: rgba(57,217,57,0.6);-fx-border-color: grey");

        cancel = new Button("Cancelar");
        cancel.focusTraversableProperty().set(false);
        cancel.setStyle("-fx-background-color: rgba(217,70,70,0.59);-fx-border-color: grey");

        ButtonApplyCancel = new HBox(apply,cancel);
        ButtonApplyCancel.setSpacing(10);
        ButtonApplyCancel.setAlignment(Pos.CENTER_RIGHT);
        ButtonApplyCancel.setPadding(new Insets(0,20,0,0));



        HBox buttonsBox = new HBox(apply,cancel);
        buttonsBox.setSpacing(10);
        buttonsBox.setAlignment(Pos.CENTER_RIGHT);

        LocalDate value = datePicker.getValue();


        // 2. get system default zone
        try {
            ZoneId zoneId = ZoneId.systemDefault();

            // 3. convert LocalDate to java.util.Date
            Date date = Date.from(value.atStartOfDay(zoneId).toInstant());


            // 4. convert java.util.Date to java.util.Calendar
            calendar = Calendar.getInstance();
            calendar.setTime(date);
        }catch (NullPointerException e){}





        VBox vb1 = new VBox(vb2,buttonsBox);
        vb1.setPadding(new Insets(20));
        vb1.setSpacing(15);
        setCenter(vb1);
    }
    private void registerHandlers() {
        model.addPropertyChangeListener(evt -> { update(); });


        DropShadow shadow = new DropShadow();

        apply.setOnAction(event->{
            Calendar calendar1 = Calendar.getInstance();
            LocalDate value = datePicker.getValue();
            try {
                ZoneId zoneId = ZoneId.systemDefault();

                // 3. convert LocalDate to java.util.Date
                Date date = Date.from(value.atStartOfDay(zoneId).toInstant());


                // 4. convert java.util.Date to java.util.Calendar
                calendar = Calendar.getInstance();
                calendar.setTime(date);
            }catch (NullPointerException e){}
            Medicamento medicamentoEditado = new Medicamento(tfMed[1].getText(),tfMed[0].getText(),tfMed[2].getText(),
                    calendar,tfMed[3].getText(),tfMed[4].getText(),ta.getText());

            if(model.editarMedicamento(medicamentoAnterior,medicamentoEditado)){
                ToastMessage.show(getScene().getWindow(), "Medicamento adicionado com sucesso!");
                Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
                stage.close();
            } else {
                ToastMessage.show(getScene().getWindow(), "Medicamento não foi adicionado com sucesso!");
            }

        });

        cancel.setOnAction(event->{
            Stage stage = (Stage)((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.close();
        });
        /*
        lb1.addEventHandler(MouseEvent.MOUSE_ENTERED,
                new EventHandler<MouseEvent>() {
                    @Override public void handle(MouseEvent e) {
                        lb1.setEffect(shadow);
                        lb1.setStyle("-fx-border-color: #1a83c6; -fx-border-width: 2");
                    }
                });

        lb1.addEventHandler(MouseEvent.MOUSE_EXITED,
                new EventHandler<MouseEvent>() {
                    @Override public void handle(MouseEvent e) {
                        lb1.setEffect(null);
                        lb1.setStyle("-fx-border-color: none");
                        lb1.focusTraversableProperty().set(false);
                    }
                });


        lb1.setOnAction(actionEvent -> {

            String textarea = ta.getText();
            //String Nome = tfUtente[0].getText();
            //String ID = tfUtente[1].getText();
            //String ModoAdministracao = tfUtente[2].getText();
            //String Laboratorio = tfUtente[3].getText();
            //String PrincipalAtivo = tfUtente[4].getText();
            LocalDate value = datePicker.getValue();


            // 2. get system default zone
            ZoneId zoneId = ZoneId.systemDefault();

            // 3. convert LocalDate to java.util.Date
            Date date = Date.from(value.atStartOfDay(zoneId).toInstant());


            // 4. convert java.util.Date to java.util.Calendar
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            System.out.print("\nConversion of LocalDate to java.util.Calendar is :- \n"
                    + date);


            //Medicamento a = new Medicamento(ID,Nome,ModoAdministracao,calendar,PrincipalAtivo,Laboratorio,textarea);

            //model.addPill(a);

        });*/



    }

    private void update() {
        if (model.getState() != MEEstados.ESTADOMEDICAMENTOS) {
            this.setVisible(false);
            return;
        }
        this.setVisible(true);


    }



}