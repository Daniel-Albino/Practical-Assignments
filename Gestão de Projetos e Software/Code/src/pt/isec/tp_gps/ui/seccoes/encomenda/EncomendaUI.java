package pt.isec.tp_gps.ui.seccoes.encomenda;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import pt.isec.tp_gps.model.Decorator;
import pt.isec.tp_gps.model.data.Encomenda;
import pt.isec.tp_gps.model.fsm.MEEstados;
import pt.isec.tp_gps.ui.RootPane1;
import pt.isec.tp_gps.ui.seccoes.ToastMessage;

import java.util.Objects;

public class EncomendaUI extends BorderPane {


    Decorator model;

    Label lb;

    TextField tf;

    Button lb0,lb1,lb2;
    Stage stageEncomenda;

    TableView<Encomenda> tableView;
    TableColumn<Encomenda,Long> ID;
    TableColumn<Encomenda,String> Nome;
    TableColumn<Encomenda,String> DataEncomenda;


    MenuItem detalhes,editar_utente,remover_utente;


    public EncomendaUI(Decorator model, Stage stageEncomenda) {

        this.model = model;
        this.stageEncomenda = stageEncomenda;
        createViews();
        registerHandlers();
        update();
    }

    private void createViews() {


        lb = new Label("Encomendas");
        lb.setTextFill(Color.DARKBLUE);
        lb.setId("Special");

        HBox hBox = new HBox(lb);
        hBox.setSpacing(250);



        Image ButtonAddUser = new Image(Objects.requireNonNull(getClass().getResourceAsStream("../../resources/images/plus1.png")),32,32,false,false);
        lb1 = new Button("Adicionar Encomenda");
        //lb1.setStyle("-fx-font: 22 arial; -fx-base: #b6e7c9;");
        lb1.setGraphic(new ImageView(ButtonAddUser));

        Image ButtonRemoveUser = new Image(Objects.requireNonNull(getClass().getResourceAsStream("../../resources/images/base-de-dados.png")),32,32,false,false);
        lb2 = new Button("Entrada de Encomenda");
        lb2.setGraphic(new ImageView(ButtonRemoveUser));

        HBox buttonsBox = new HBox(lb1,lb2);
        buttonsBox.setSpacing(10);
        buttonsBox.setAlignment(Pos.TOP_LEFT);


        final ContextMenu contextMenu = new ContextMenu();
        detalhes = new MenuItem("Detalhes");
        editar_utente = new MenuItem("Editar Utente");
        remover_utente = new MenuItem("Remover Utente");
        contextMenu.getItems().addAll(detalhes, editar_utente, remover_utente);




        tableView = new TableView<>();
        ID = new TableColumn<Encomenda,Long>("ID da encomenda");
        ID.setCellValueFactory(new PropertyValueFactory<>("IDEncomenda"));
        Nome = new TableColumn<Encomenda, String>("Nome");
        Nome.setCellValueFactory(new PropertyValueFactory<Encomenda,String>("nomeMedicamento"));
        DataEncomenda = new TableColumn<Encomenda,String>("Quantidade");
        DataEncomenda.setCellValueFactory(new PropertyValueFactory<Encomenda,String>("Quantidade"));
        tableView.setPlaceholder(new Label("Sem dados"));
        tableView.setMaxHeight(500);
        tableView.getColumns().add(ID);
        tableView.getColumns().add(Nome);
        tableView.getColumns().add(DataEncomenda);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);



        VBox Arrange = new VBox(hBox,buttonsBox,tableView);
        buttonsBox.setSpacing(10);
        Arrange.setPadding(new Insets(10,10,0,20));
        Arrange.setAlignment(Pos.TOP_LEFT);
        Arrange.setSpacing(20);
        this.setCenter(Arrange);

    }

    private void registerHandlers() {
        model.addPropertyChangeListener(evt -> { update(); });


        DropShadow shadow = new DropShadow();
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

        lb2.addEventHandler(MouseEvent.MOUSE_ENTERED,
                new EventHandler<MouseEvent>() {
                    @Override public void handle(MouseEvent e) {
                        lb2.setEffect(shadow);
                        lb2.setStyle("-fx-border-color: #1a83c6; -fx-border-width: 2");
                    }
                });

        lb2.setOnMouseClicked(event ->
        {
            if (event.getButton() == MouseButton.SECONDARY)
            {
                lb2.setStyle("-fx-background-color: #d2a806; -fx-border-width: 2");
            }
        });

        lb2.addEventHandler(MouseEvent.MOUSE_EXITED,
                new EventHandler<MouseEvent>() {
                    @Override public void handle(MouseEvent e) {
                        lb2.setEffect(null);
                        lb2.setStyle("-fx-border-color: none");
                        lb2.focusTraversableProperty().set(false);
                    }
                });

        lb2.setOnAction(actionEvent -> {
            Encomenda encomenda = tableView.getSelectionModel().getSelectedItem();
            if(encomenda != null) {
                if (model.removerEncomendas(encomenda))
                    ToastMessage.show(getScene().getWindow(), "A sua encomenda deu entrada no sistema!");
                else
                    ToastMessage.show(getScene().getWindow(), "A sua encomenda não deu entrada no sistema!");
            }
        });

        lb1.setOnAction(actionEvent -> {
            stageEncomenda.setScene(new Scene(new RootPane1(model,new AdicionarEncomendaUI(model)),950,600));
            stageEncomenda.setTitle("Adicionar Encomenda");
            stageEncomenda.show();

        });



    }

    private void update() {

        if (model.getState() != MEEstados.ESTADOENCOMENDAS) {
            this.setVisible(false);
            return;
        }
        this.setVisible(true);

        tableView.getItems().clear();
        Platform.runLater(()->{
            if(model.getEncomendas() != null){
                for(Encomenda a : model.getEncomendas())
                    tableView.getItems().add(a);

            }
        });



    }



}