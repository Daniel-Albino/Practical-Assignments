package pt.isec.tp_gps.ui.seccoes.utente;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import pt.isec.tp_gps.model.Decorator;
import pt.isec.tp_gps.model.data.Utente;
import pt.isec.tp_gps.model.fsm.MEEstados;
import pt.isec.tp_gps.ui.RootPane1;
import pt.isec.tp_gps.ui.seccoes.ToastMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class UtenteUI extends BorderPane {
    Decorator model;
    Label lbUtente;
    HBox hbHistorico;
    Button btnHistorico;
    HBox hBoxHeader;
    TextField tfPesquisa;
    TableView<Utente> tableView;
    ContextMenu contextMenu;
    TableColumn<Utente,String> CC;
    TableColumn<Utente,String> Nome;
    TableColumn<Utente,String> Idade;
    TableColumn<Utente,String> TelEm;
    ComboBox comboBox;
    Button btnAddUtente, btnTomaMed;
    Stage stage1,stage2;
    MenuItem detalhes,editar_utente,remover_utente;
    Optional<ButtonType> result;
    List<Utente> aux=null;

    public UtenteUI(Decorator model, Stage stage1, Stage stage2) {
        this.model = model;
        this.stage1 = stage1;
        this.stage2 = stage2;
        this.aux = new ArrayList<>();
        createViews();
        registerHandlers();
        update();
    }

    private void createViews() {

        lbUtente = new Label("Utentes");
        lbUtente.setTextFill(Color.DARKBLUE);
        lbUtente.setId("Special");

        Image imgHistoric = new Image(Objects.requireNonNull(getClass().getResourceAsStream("../../resources/images/base-de-dados.png")),32,32,false,false);
        btnHistorico = new Button("Histórico de Utentes");
        btnHistorico.setGraphic(new ImageView(imgHistoric));

        hbHistorico = new HBox(btnHistorico);
        hbHistorico.setAlignment(Pos.CENTER_RIGHT);
        hbHistorico.setPadding(new Insets(0,20,0,0));

        hBoxHeader = new HBox(lbUtente, hbHistorico);
        HBox.setHgrow(hbHistorico,Priority.ALWAYS);

        Image imgAddUtente = new Image(Objects.requireNonNull(getClass().getResourceAsStream("../../resources/images/add-friend.png")),32,32,false,false);
        btnAddUtente = new Button("Adicionar Utente");
        btnAddUtente.setGraphic(new ImageView(imgAddUtente));

        Image imgTomaMed = new Image(Objects.requireNonNull(getClass().getResourceAsStream("../../resources/images/medicina.png")),32,32,false,false);
        btnTomaMed = new Button("Toma de medicamentos");
        btnTomaMed.setGraphic(new ImageView(imgTomaMed));

        HBox buttonsBox = new HBox(btnAddUtente, btnTomaMed);
        buttonsBox.setSpacing(10);
        buttonsBox.setAlignment(Pos.TOP_LEFT);

        tfPesquisa = new TextField();
        tfPesquisa.setPromptText("Escreva o que quer pesquisar");
        tfPesquisa.setPrefWidth(Integer.MAX_VALUE);

        ObservableList<String> options =
                FXCollections.observableArrayList(
                        "Procurar por CC",
                        "Procurar por Nome"
                );
        comboBox = new ComboBox(options);
        comboBox.setMinWidth(150);
        comboBox.setValue(options.get(0));


        HBox hb = new HBox(comboBox, tfPesquisa);
        hb.setSpacing(30);

        tableView = new TableView<>();
        CC = new TableColumn<Utente,String>("CC");
        CC.setCellValueFactory(new PropertyValueFactory<Utente,String>("CC"));
        Nome = new TableColumn<Utente,String>("Nome");
        Nome.setCellValueFactory(new PropertyValueFactory<Utente,String>("Nome"));
        Idade = new TableColumn<Utente,String>("Idade");
        Idade.setCellValueFactory(new PropertyValueFactory<Utente,String>("idade"));
        TelEm = new TableColumn<Utente,String>("Telefone de \nEmergência");
        TelEm.setCellValueFactory(new PropertyValueFactory<Utente,String>("TelefoneEme"));
        tableView.setPlaceholder(new Label("Sem dados"));
        tableView.setMaxHeight(500);
        tableView.getColumns().add(CC);
        tableView.getColumns().add(Nome);
        tableView.getColumns().add(Idade);
        tableView.getColumns().add(TelEm);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

        contextMenu = new ContextMenu();
        detalhes = new MenuItem("Detalhes");
        editar_utente = new MenuItem("Editar Utente");
        remover_utente = new MenuItem("Remover Utente");
        contextMenu.getItems().addAll(detalhes, editar_utente, remover_utente);
        tableView.setContextMenu(contextMenu);

        VBox Arrange = new VBox(hBoxHeader,buttonsBox,hb,tableView);
        buttonsBox.setSpacing(10);
        Arrange.setPadding(new Insets(10,10,0,20));
        Arrange.setAlignment(Pos.TOP_LEFT);
        Arrange.setSpacing(20);
        this.setCenter(Arrange);

    }

    private void registerHandlers() {
        model.addPropertyChangeListener(evt -> { update(); });

        tfPesquisa.setOnKeyPressed(event -> {
            if (event.getCode() == KeyCode.ENTER) {
                Platform.runLater(()->{
                if(comboBox.getValue().toString().equals("Procurar por CC")) {
                    if(tfPesquisa.getText().trim().isBlank()){
                        update();
                    }else {
                        Utente aux = model.getUtenteCC(tfPesquisa.getText().trim());
                        if (aux != null) {
                            tableView.getItems().clear();
                            tableView.getItems().add(aux);
                        } else {
                            ToastMessage.show(getScene().getWindow(), "Não existe nenhum Utente com esse numero registado!");
                        }
                    }
                }
                else if(comboBox.getValue().toString().equals("Procurar por Nome")){
                    if(tfPesquisa.getText().trim().isBlank()){
                        update();
                    }else {
                        aux = model.getUtenteNome(tfPesquisa.getText().trim());
                        if (aux.size() > 0) {
                            tableView.getItems().clear();
                            for (Utente a : aux)
                                tableView.getItems().add(a);
                        } else {
                            ToastMessage.show(getScene().getWindow(), "Não existe nenhum Utente com esse nome registado!");
                        }
                    }
                }
                });
            }
        });

        btnHistorico.setOnAction(actionEvent -> {
            stage1.setScene(new Scene(new RootPane1(model,new HistoricoUtentesUI(model)),950,600));
            stage1.setTitle("Histórico Utente");
            stage1.show();
        });

        btnAddUtente.setOnAction(actionEvent -> {
            stage1.setScene(new Scene(new RootPane1(model,new AdicionarUtenteUI(model,stage2)),950,600));
            stage1.setTitle("Adicionar Utente");
            stage1.show();

        });
        btnTomaMed.setOnAction(event->{
            stage1.setScene(new Scene(new RootPane1(model,new TomaMedicamentoUI(model)),950,600));
            //model.addUtentePage();
            stage1.setTitle("Toma Medicamento");
            stage1.show();
        });

        detalhes.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Utente aux = (Utente) tableView.getSelectionModel().getSelectedItem();
                model.setTempUtente(aux);
                stage1.setScene(new Scene(new RootPane1(model,new DetalhesUtenteUI(model)),950,600));
                stage1.setTitle("Detalhes");
                stage1.show();
            }
        });

        editar_utente.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Utente aux = (Utente) tableView.getSelectionModel().getSelectedItem();
                stage1.setScene(new Scene(new RootPane1(model,new EditarUtenteUI(model,stage2,aux)),950,600));
                stage1.setTitle("Editar Utente");
                stage1.show();
            }
        });


        remover_utente.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                Alert a = new Alert(Alert.AlertType.CONFIRMATION);
                a.setTitle("Alerta!");
                a.setHeaderText(null);
                a.setContentText("Deseja mesmo\ncolocar o utente no histórico?");
                final Stage stage = (Stage) a.getDialogPane().getScene().getWindow();
                try {
                    stage.getIcons().add(new Image(this.getClass().getResource("../../resources/images/pills.png").toString()));
                }catch (NullPointerException e){}
                a.showAndWait();
                if(a.getResult() == ButtonType.OK){
                    Utente utente = tableView.getSelectionModel().getSelectedItem();
                    model.removerUtente(utente.getCC());
                    stage.close();
                }else{
                    stage.close();
                }

            }
        });


        DropShadow shadow = new DropShadow();
        btnAddUtente.addEventHandler(MouseEvent.MOUSE_ENTERED,
                new EventHandler<MouseEvent>() {
                    @Override public void handle(MouseEvent e) {
                        btnAddUtente.setEffect(shadow);
                        btnAddUtente.setStyle("-fx-border-color: #1a83c6; -fx-border-width: 2");
                    }
                });

        btnAddUtente.addEventHandler(MouseEvent.MOUSE_EXITED,
                new EventHandler<MouseEvent>() {
                    @Override public void handle(MouseEvent e) {
                        btnAddUtente.setEffect(null);
                        btnAddUtente.setStyle("-fx-border-color: none");
                        btnAddUtente.focusTraversableProperty().set(false);
                    }
                });

        btnTomaMed.addEventHandler(MouseEvent.MOUSE_ENTERED,
                new EventHandler<MouseEvent>() {
                    @Override public void handle(MouseEvent e) {
                        btnTomaMed.setEffect(shadow);
                        btnTomaMed.setStyle("-fx-border-color: #1a83c6; -fx-border-width: 2");
                    }
                });

        btnTomaMed.setOnMouseClicked(event ->
        {
            if (event.getButton() == MouseButton.SECONDARY)
            {
                btnTomaMed.setStyle("-fx-background-color: #d2a806; -fx-border-width: 2");
            }
        });

        btnTomaMed.addEventHandler(MouseEvent.MOUSE_EXITED,
                new EventHandler<MouseEvent>() {
                    @Override public void handle(MouseEvent e) {
                        btnTomaMed.setEffect(null);
                        btnTomaMed.setStyle("-fx-border-color: none");
                        btnTomaMed.focusTraversableProperty().set(false);
                    }
                });

    }

    private void update() {
        if (model.getState() != MEEstados.ESTADOUTENTE){
            this.setVisible(false);
            return;
        }
        this.setVisible(true);

        tfPesquisa.clear();
        Platform.runLater(() -> {
            tableView.getItems().clear();
            aux = model.getUtentes();
            for (Utente a : aux)
                tableView.getItems().add(a);
        });
    }
}