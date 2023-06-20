package pt.isec.tp_gps.ui.seccoes.utente;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import pt.isec.tp_gps.model.Decorator;
import pt.isec.tp_gps.model.data.MedUtente;
import pt.isec.tp_gps.model.data.Utente;
import pt.isec.tp_gps.model.threads.ThreadCalcTotalMed;
import pt.isec.tp_gps.ui.RootPane2;
import pt.isec.tp_gps.ui.seccoes.ToastMessage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
public class EditarUtenteUI extends BorderPane {
    private static final int NR_FIELDS = 5;
    Decorator model;
    Stage stage2;
    Label lbUtente[];
    Label dataNasc, sexo;
    TextField tfUtente[];
    DatePicker datePicker;
    ComboBox<String> combo;
    TableView tvUtente;

    Label alergias, doencas;
    ListView<String> lAUtente, lDUtente;
    ObservableList<String> names;
    ObservableList<String> names1;
    ContextMenu contextMenu, contextMenu1, contextMenu2;
    MenuItem remover, remover1, remover2;

    Image img1;
    Button view1,view2,view3;


    Button apply, cancel;
    HBox ButtonApplyCancel;

    HBox hb1, hb2, hb3, hb4, hb5, hb6, hb7, hb8, hb9, hb10, hb11, hb12;
    VBox vb1, vb2, vb3, vb4, vb5, vb6;

    ScrollPane scrollPane;
    Utente utenteAnterior;

    public EditarUtenteUI(Decorator model, Stage stage2, Utente utenteAnterior) {
        model.getTempAlergia().clear();
        model.getTempDoenca().clear();
        model.getTempMedUtente().clear();
        System.out.println(model.getTempAlergia() + " - " + model.getTempDoenca() + " - " + model.getTempMedUtente());
        this.utenteAnterior = utenteAnterior;
        this.model = model;
        this.stage2 = stage2;
        createViews();
        registerHandlers();
        update();
    }

    private void createViews() {

        //TODO:: Criação de uma instância do scrollPane
        scrollPane = new ScrollPane();

        //TODO:: Labels(nome,CC,email,telef. de emergências,ocupação)
        lbUtente = new Label[NR_FIELDS];

        //TODO:: TextFields(nome,CC,email,telef. de emergências,ocupação)
        tfUtente = new TextField[NR_FIELDS];


        //TODO:: Criação efetiva das labels e dos textfields com as diferentes propriedades
        for (int i = 0; i < NR_FIELDS; i++) {
            lbUtente[i] = new Label();
            tfUtente[i] = new TextField();
            tfUtente[i].setPrefWidth(Integer.MAX_VALUE);
            tfUtente[i].focusTraversableProperty().set(false);
            tfUtente[i].setPrefHeight(20);
        }
        lbUtente[0].setText("Nome:");
        lbUtente[0].setMinWidth(80);
        lbUtente[1].setText("CC:");
        lbUtente[1].setMinWidth(50);
        lbUtente[2].setText("Email:");
        lbUtente[2].setMinWidth(75);
        lbUtente[3].setText("Telef. de emergência:");
        lbUtente[3].setMinWidth(220);
        lbUtente[4].setText("Ocupação:");
        lbUtente[4].setMinWidth(110);
        tfUtente[4].setPromptText("(Optional)");
        dataNasc = new Label("Data de Nascimento:");
        sexo = new Label("Sexo:");


        tfUtente[0].textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if(tfUtente[0].getText().length() > 30){
                    String aux = tfUtente[0].getText().substring(0, 30);
                    tfUtente[0].setText(aux);
                }
            }
        });
        tfUtente[1].textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if(tfUtente[1].getText().length() > 12){
                    String aux = tfUtente[1].getText().substring(0, 12);
                    tfUtente[1].setText(aux);
                }
            }
        });
        tfUtente[2].textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if(tfUtente[2].getText().length() > 50){
                    String aux = tfUtente[2].getText().substring(0, 50);
                    tfUtente[2].setText(aux);
                }
            }
        });
        tfUtente[3].textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if(tfUtente[3].getText().length() > 9){
                    String aux = tfUtente[3].getText().substring(0, 9);
                    tfUtente[3].setText(aux);
                }
            }
        });
        tfUtente[4].textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if(tfUtente[4].getText().length() > 30){
                    String aux = tfUtente[4].getText().substring(0, 30);
                    tfUtente[4].setText(aux);
                }
            }
        });

        //TODO:: DatePicker para a Data de Nascimento
        datePicker = new DatePicker();
        datePicker.focusTraversableProperty().set(false);
        datePicker.setEditable(false);

        //TODO:: DropDown para escolher o genero
        combo = new ComboBox<String>();
        combo.setPromptText("Selecione um género");
        ObservableList<String> list = combo.getItems();
        list.add("Masculino");
        list.add("Feminino");
        list.add("Outro");


        hb1 = new HBox(lbUtente[0], tfUtente[0]);
        //HBox.setHgrow(hb1, Priority.ALWAYS);
        //hb1.setPrefWidth(Integer.MAX_VALUE);
        hb2 = new HBox(lbUtente[1], tfUtente[1]);
        hb3 = new HBox(lbUtente[2], tfUtente[2]);
        hb4 = new HBox(lbUtente[3], tfUtente[3]);
        hb5 = new HBox(lbUtente[4], tfUtente[4]);
        hb6 = new HBox(dataNasc, datePicker);
        hb7 = new HBox(sexo, combo);

        vb1 = new VBox(hb7, hb5);
        vb1.setPrefWidth(Integer.MAX_VALUE);
        vb1.setSpacing(15);

        vb2 = new VBox(hb1, hb2, hb6, hb3, hb4);
        vb2.setPrefWidth(Integer.MAX_VALUE);
        vb2.setSpacing(15);


        hb8 = new HBox(vb2, vb1);
        hb8.setPadding(new Insets(20));
        hb8.setSpacing(15);


        img1 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("../../resources/images/plus.png")), 24, 24, false, false);
        view1 = new Button();
        view1.setStyle("-fx-background-color: none");
        view1.setGraphic(new ImageView(img1));


        hb10 = new HBox(view1);
        hb10.setAlignment(Pos.CENTER_RIGHT);



        tvUtente = new TableView<>();
        TableColumn<MedUtente, String> medicamento = new TableColumn<MedUtente,String>("Medicamento");
        medicamento.setCellValueFactory(new PropertyValueFactory<MedUtente,String>("tempMed"));
        TableColumn<MedUtente, String> obs = new TableColumn<MedUtente,String>("Horas");
        obs.setCellValueFactory(new PropertyValueFactory<MedUtente,String>("tempHoras"));
        tvUtente.setPlaceholder(new Label("Sem dados"));
        tvUtente.setMaxHeight(300);
        tvUtente.getColumns().add(medicamento);
        tvUtente.getColumns().add(obs);
        tvUtente.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);


        vb6 = new VBox(hb10, tvUtente);


        view2 = new Button();
        view2.setStyle("-fx-background-color: none");
        view2.setGraphic(new ImageView(img1));


        alergias = new Label("Alergias");
        hb11 = new HBox(alergias, view2);
        hb11.setAlignment(Pos.CENTER);


        lAUtente = new ListView<String>();
        names = FXCollections.observableArrayList();
        lAUtente.setItems(names);
        lAUtente.setMaxHeight(50);
        lAUtente.setPrefWidth(Integer.MAX_VALUE);
        vb4 = new VBox(hb11, lAUtente);


        view3 = new Button();
        view3.setStyle("-fx-background-color: none");
        view3.setGraphic(new ImageView(img1));


        doencas = new Label("Doenças");
        hb12 = new HBox(doencas, view3);
        hb12.setAlignment(Pos.CENTER);

        lDUtente = new ListView<String>();
        //TODO::
        names1 = FXCollections.observableArrayList();
        lDUtente.setItems(names1);
        lDUtente.setMaxHeight(50);
        lDUtente.setPrefWidth(Integer.MAX_VALUE);
        vb5 = new VBox(hb12, lDUtente);


        contextMenu = new ContextMenu();
        remover = new MenuItem("Remover Medicamento");
        contextMenu.getItems().addAll(remover);
        tvUtente.setContextMenu(contextMenu);

        contextMenu1 = new ContextMenu();
        remover1 = new MenuItem("Remover Alergia");
        contextMenu1.getItems().addAll(remover1);
        lAUtente.setContextMenu(contextMenu1);

        contextMenu2 = new ContextMenu();
        remover2 = new MenuItem("Remover Doença");
        contextMenu2.getItems().addAll(remover2);
        lDUtente.setContextMenu(contextMenu2);


        hb9 = new HBox(vb4, vb5);
        hb9.setSpacing(20);
        hb9.setAlignment(Pos.CENTER);

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


        vb3 = new VBox(hb8, vb6, hb9, ButtonApplyCancel);
        vb3.setPadding(new Insets(20));
        vb3.setSpacing(15);


        scrollPane.setContent(vb3);
        scrollPane.fitToWidthProperty().set(true);
        this.setCenter(scrollPane);


        //HBox lbBAddCancelPerson = new HBox(AddPersonlb,ButtonAddCancel);
        //HBox.setHgrow(ButtonAddCancel, Priority.ALWAYS);



        tfUtente[0].setText(utenteAnterior.getNome());
        tfUtente[1].setText(utenteAnterior.getCC());
        tfUtente[2].setText(utenteAnterior.getEmail());
        tfUtente[3].setText(String.valueOf(utenteAnterior.getTelefoneEme()));
        tfUtente[4].setText(utenteAnterior.getOcupacao());
        combo.setValue(utenteAnterior.getSexo());
        LocalDate localDate = LocalDateTime.ofInstant(utenteAnterior.getDateNascimento().toInstant(), utenteAnterior.getDateNascimento().getTimeZone().toZoneId()).toLocalDate();
        datePicker.setValue(localDate);

        List<MedUtente> listMedUtente = utenteAnterior.getListaMedicamentosMedUtente();
        if(listMedUtente != null){
            tvUtente.getItems().clear();
            for(MedUtente medUtente : listMedUtente){
                tvUtente.getItems().add(medUtente);
            }
            model.setTempMedUtente(new ArrayList<>(listMedUtente));
        }

        List<String> doencas = utenteAnterior.getDoencas();
        System.out.println("doencas " + doencas);
        List<String> alergias = utenteAnterior.getAlergias();
        System.out.println("alergias " + alergias);
        if(doencas != null) {
            for (String s : doencas)
                lDUtente.getItems().add(s);
            model.setTempDoenca(new ArrayList<>(doencas));
        }
        if(alergias != null) {
            for (String s : alergias)
                lAUtente.getItems().add(s);
            model.setTempAlergia(new ArrayList<>(alergias));
        }
    }


    private void registerHandlers() {
        model.addPropertyChangeListener(evt -> {
            update();
        });

        remover.setOnAction(event -> {
            MedUtente selectedItem = (MedUtente) tvUtente.getSelectionModel().getSelectedItem();
            tvUtente.getItems().remove(selectedItem);
            model.removeTempMedUtente(selectedItem);
            System.out.println(tvUtente.getItems());
        });

        remover1.setOnAction(event -> {
            String selectedItem = String.valueOf(lAUtente.getSelectionModel().getSelectedItem());
            lAUtente.getItems().remove(selectedItem);
            model.removeTempAlergia(selectedItem);
        });

        remover2.setOnAction(event -> {
            String selectedItem = String.valueOf(lDUtente.getSelectionModel().getSelectedItem());
            lDUtente.getItems().remove(selectedItem);
            model.removeTempDoencas(selectedItem);
        });

        apply.setOnAction(event ->{
            LocalDate value = datePicker.getValue();
            Calendar calendar=null;
            try {
                ZoneId zoneId = ZoneId.systemDefault();
                // 3. convert LocalDate to java.util.Date
                Date date = Date.from(value.atStartOfDay(zoneId).toInstant());
                // 4. convert java.util.Date to java.util.Calendar
                calendar = Calendar.getInstance();
                calendar.setTime(date);
            }catch(NullPointerException e){}
            if(model.removerUtenteSemHistorico(utenteAnterior.getCC())){
                LocalDate value1 = datePicker.getValue();
                Calendar calendar1=null;
                try {
                    ZoneId zoneId = ZoneId.systemDefault();
                    // 3. convert LocalDate to java.util.Date
                    Date date = Date.from(value1.atStartOfDay(zoneId).toInstant());
                    // 4. convert java.util.Date to java.util.Calendar
                    calendar1 = Calendar.getInstance();
                    calendar1.setTime(date);
                }catch(NullPointerException e){}

                List<String> alergiasUtente = new ArrayList<>();
                List<String> doencasUtente = new ArrayList<>();

                for(String a : lAUtente.getItems())
                    alergiasUtente.add(a);

                for(String a : lDUtente.getItems())
                    doencasUtente.add(a);

                List<MedUtente> medUtentes = new ArrayList<>(tvUtente.getItems());
                System.out.println(tvUtente.getItems());

                List<String> dadosUtente = new ArrayList<>();

                for(int i = 0;i<4;i++) {
                    dadosUtente.add(tfUtente[i].getText());
                }
                dadosUtente.add(combo.getValue());
                dadosUtente.add(tfUtente[4].getText());


                System.out.println(dadosUtente);

                if(model.adicionarUtente(dadosUtente,calendar,alergiasUtente,doencasUtente,medUtentes)){
                    new ThreadCalcTotalMed(model).start();
                    ToastMessage.show(getScene().getWindow(),"Utente editado com sucesso!");
                    Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
                    stage.close();
                }else {
                    ToastMessage.show(getScene().getWindow(), "Utente não foi editado com sucesso!");
                    model.adicionarUtente(utenteAnterior);
                }
            }
        });

        cancel.setOnAction(event ->{
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            stage.close();
        });


        view1.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                stage2.setScene(new Scene(new RootPane2(model,new AdicionarMedUtenteUI(model)),800,300));
                stage2.setTitle("Adicionar Medicamento Utente");
                stage2.show();
            }
        });
        view2.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                stage2.setScene(new Scene(new RootPane2(model,new AdicionarAlergiaUI(model)),800,300));
                stage2.setTitle("Adicionar Alergia Utente");
                stage2.show();
            }
        });
        view3.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                stage2.setScene(new Scene(new RootPane2(model,new AdicionarDoencasUI(model)),800,300));
                stage2.setTitle("Adicionar Doença Utente");
                stage2.show();
            }
        });

 /*
        AddDetails.setOnAction(event -> {
            stage2.show();
        });

        accept.setOnAction(event -> {
            System.out.println("clicked!");
        });

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

        tvUtente.getItems().clear();
        names.clear();
        names1.clear();


        if(model.getTempMedUtente() != null && model.getTempMedUtente().size() > 0) {
            for(MedUtente a : model.getTempMedUtente())
                tvUtente.getItems().add(a);
        }

        if(model.getTempAlergia() != null && model.getTempAlergia().size() > 0){
            for(String a : model.getTempAlergia())
                names.add(a);
        }

        if(model.getTempDoenca() != null && model.getTempDoenca().size() > 0){
            for(String a : model.getTempDoenca())
                names1.add(a);
        }

        /*if(model.getTempUtente() != null){
            Platform.runLater(()->{
                if(model.getTempMedUtente() != null && model.getTempMedUtente().size() > 0) {
                    tvUtente.getItems().clear();
                    for(MedUtente a : model.getTempMedUtente())
                        tvUtente.getItems().add(a);
                }

                if(model.getTempAlergia() != null && model.getTempAlergia().size() > 0){
                    names.clear();
                    for(String a : model.getTempAlergia()){
                        names.add(a);
                    }
*//*                    for(String a : model.getTempUtente().getAlergias()) {
                        System.out.println("Alergias " + a);
                        names.add(a);
                    }*//*
                }

                if(model.getTempDoenca() != null && model.getTempDoenca().size() > 0){
                    names1.clear();
                    for(String a : model.getTempDoenca()){
                        names1.add(a);
                    }
*//*                    for(String a : model.getTempUtente().getDoencas()) {
                        System.out.println("Doencas " + a);
                        names1.add(a);
                    }*//*
                }
            });*/



        /*
        if (model.getAux() != Pages.EDIT) {
            this.setVisible(false);
            return;
        }
        this.setVisible(true);*/
    }
}
