package pt.isec.pa.apoio_poe.ui.gui.states.candidaturas;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import pt.isec.pa.apoio_poe.model.Manager;
import pt.isec.pa.apoio_poe.model.data.Erros;
import pt.isec.pa.apoio_poe.model.fsm.GPEState;
import pt.isec.pa.apoio_poe.ui.gui.resources.FontManager;
import pt.isec.pa.apoio_poe.ui.gui.states.utils.ToastMessage;

import java.io.File;
import java.util.List;


public class GestaoCandidaturasUI extends BorderPane {
    Manager manager;
    Button btnAdiciona, btnRemove, btnEditar,btnImportar, btnExportar, btnSair,btnMInfor,btnSeguinte,btnFechar;
    Label lbNameState,lbStatus;
    Font font;
    MenuBar menuBar;
    Menu mnFile,mnEdit;
    MenuItem mnEditar,mnSave, mnExit, mnAdicionar, mnRemover, mnImportar, mnExportar,mnMaisInfo;
    ListView<String> listView;
    public GestaoCandidaturasUI(Manager manager) {
        this.manager = manager;
        createViews();
        registerHandlers();
        update();
    }

    private void createButtons() {
        font = FontManager.loadFont("BalsamiqSans-Regular.ttf",20);

        btnAdiciona = new Button("Adicionar");
        btnAdiciona.setFont(font);

        btnRemove = new Button("Remover");
        btnRemove.setFont(font);

        btnEditar = new Button("Editar");
        btnEditar.setFont(font);

        btnImportar = new Button("Importar");
        btnImportar.setFont(font);

        btnExportar = new Button("Exportar");
        btnExportar.setFont(font);

        btnMInfor = new Button("Mais Informações");
        btnMInfor.setFont(font);

        btnSair = new Button("Voltar");
        btnSeguinte = new Button("Seguinte");
        btnFechar = new Button("Fechar Fase");

        btnSair.setFont(font);
        btnSeguinte.setFont(font);
        btnFechar.setFont(font);
    }

    private void createViews() {
        createButtons();

        //Parte de cima da interface
        BorderPane borderTop = new BorderPane();
        borderTop.setPrefWidth(654);
        borderTop.setPrefHeight(79);

        lbNameState = new Label("Candidaturas");
        lbNameState.setFont(FontManager.loadFont("BalsamiqSans-Regular.ttf",34));
        setMargin(lbNameState,new Insets(20,0,0,50));
        borderTop.setLeft(lbNameState);

        HBox hBoxButtomTop = new HBox(btnAdiciona,btnRemove,btnEditar,btnImportar,btnExportar,btnMInfor);
        hBoxButtomTop.setPadding(new Insets(10,0,0,160));
        hBoxButtomTop.setSpacing(20);
        hBoxButtomTop.setPrefWidth(600);
        hBoxButtomTop.setPrefHeight(27);
        borderTop.setBottom(hBoxButtomTop);

        menuBar = Toolbar();
        borderTop.setTop(menuBar);

        //Parte do centro

        listView = new ListView<>();
        listView.setStyle("-fx-font-family: Arial; -fx-font-size: 16;");
        setMargin(listView,new Insets(10,80,100,40));

        VBox vBoxButtonBottomSair = new VBox(btnSair);
        setMargin(vBoxButtonBottomSair,new Insets(0,0,10,50));
        vBoxButtonBottomSair.setAlignment(Pos.BOTTOM_LEFT);
        vBoxButtonBottomSair.setSpacing(10);

        VBox vBoxButtonBottom = new VBox(btnFechar,btnSeguinte);
        setMargin(vBoxButtonBottom,new Insets(10,50,10,0));
        vBoxButtonBottom.setAlignment(Pos.BOTTOM_RIGHT);
        vBoxButtonBottom.setSpacing(10);


        lbStatus = new Label();
        lbStatus.setPrefWidth(Integer.MAX_VALUE);
        //lbStatus.setFont(new Font());
        lbStatus.setStyle("-fx-background-color: #c0c0c0");
        lbStatus.setFont(FontManager.loadFont("BalsamiqSans-Regular.ttf",20));
        lbStatus.setBorder(new Border(new BorderStroke(Color.DARKGRAY,BorderStrokeStyle.SOLID,CornerRadii.EMPTY
                ,BorderWidths.DEFAULT)));
        lbStatus.setPadding(new Insets(10));

        this.setTop(borderTop);
        this.setCenter(listView);
        this.setLeft(vBoxButtonBottomSair);
        this.setBottom(lbStatus);
        this.setRight(vBoxButtonBottom);

    }

    private MenuBar Toolbar() {
        mnFile = new Menu("File");
        mnAdicionar = new MenuItem("Adicionar");
        mnRemover = new MenuItem("Remover");
        mnImportar = new MenuItem("Importar");
        mnExportar = new MenuItem("Exportar");
        mnMaisInfo = new MenuItem("Mais Informação");
        mnFile.getItems().addAll(mnAdicionar,mnRemover,mnImportar,mnExportar,mnMaisInfo);

        mnEdit = new Menu("Edit");
        mnSave = new MenuItem("Salvar");
        mnSave.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
        mnEditar = new MenuItem("Editar");
        mnExit = new MenuItem("Voltar");
        mnEdit.getItems().addAll(mnEditar,mnSave,mnExit);

        MenuBar menuBarAux = new MenuBar(mnFile,mnEdit);
        menuBarAux.setUseSystemMenuBar(true);
        return menuBarAux;
    }


    private void registerHandlers() {
        manager.addPropertyChangeListener(evt -> { update(); });
        btnAdiciona.setOnAction(event -> {
            Stage stage = new Stage();
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(btnAdiciona.getScene().getWindow());
            Scene scene = new Scene(new AddCandidaturasForm(stage,manager));
            stage.setScene(scene);
            stage.setTitle("Adicionar Candidaturas");
            stage.setMaxWidth(700);
            stage.setMaxHeight(300);
            stage.setMinWidth(700);
            stage.setMinHeight(300);
            stage.show();
        });
        mnAdicionar.setOnAction(actionEvent -> {
            btnAdiciona.fire();
        });
        btnSair.setOnAction(event -> {
            manager.voltar();
        });
        mnExit.setOnAction(event ->{
            btnSair.fire();
        });
        btnImportar.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("File open...");
            fileChooser.setInitialDirectory(new File("."));
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Candidaturas (*.csv)","*.csv")
            );
            File file = fileChooser.showOpenDialog(this.getScene().getWindow());
            if(file != null){
                manager.importFile(file);
            }
        });
        mnImportar.setOnAction(event -> {
            btnImportar.fire();
        });
        btnRemove.setOnAction(event -> {
            Stage stage = new Stage();
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(btnAdiciona.getScene().getWindow());
            Scene scene = new Scene(new RemoveCandidaturasForm(stage,manager));
            stage.setScene(scene);
            stage.setTitle("Adicionar Candidaturas");
            stage.setMinWidth(700);
            stage.setMinHeight(300);
            stage.setResizable(false);
            stage.show();
        });
        mnRemover.setOnAction(event ->{
            btnRemove.fire();
        });
        btnEditar.setOnAction(event -> {
            Stage stage = new Stage();
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(btnAdiciona.getScene().getWindow());
            Scene scene = new Scene(new EditCandidaturasForm(stage,manager));
            stage.setScene(scene);
            stage.setTitle("Editar Candidaturas");
            stage.setMaxWidth(700);
            stage.setMaxHeight(300);
            stage.setMinWidth(700);
            stage.setMinHeight(300);
            stage.show();
        });
        mnEditar.setOnAction(event -> {
            btnEditar.fire();
        });

        btnExportar.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("File save...");
            fileChooser.setInitialDirectory(new File(".")); //Diretório corrente
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Candidaturas (*.csv)","*.csv")
            );
            File file = fileChooser.showSaveDialog(this.getScene().getWindow());
            if(file != null){
                manager.exportFile(file);
            }
        });
        mnExportar.setOnAction(event ->{
            btnExportar.fire();
        });
        btnMInfor.setOnAction(event -> {
            Stage stage = new Stage();
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(btnAdiciona.getScene().getWindow());
            Scene scene = new Scene(new MaisInfo(stage,manager));
            stage.setScene(scene);
            stage.setTitle("Adicionar Candidaturas");
            stage.setMaxWidth(1000);
            stage.setMaxHeight(750);
            stage.setMinWidth(700);
            stage.setMinHeight(700);
            stage.show();
        });
        mnMaisInfo.setOnAction(event -> {
           btnMInfor.fire();
        });
        mnSave.setOnAction(actionEvent -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("File save...");
            fileChooser.setInitialDirectory(new File(".")); //Diretório corrente
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Salvar (*.dat)","*.dat")
            );
            File file = fileChooser.showSaveDialog(this.getScene().getWindow());
            if(file != null){
                manager.save(file);
            }
        });
        btnSeguinte.setOnAction(actionEvent -> {
           manager.seguinte();
        });
        btnFechar.setOnAction(actionEvent -> {
            if(manager.fecharFase().equals(Erros.NONE))
                ToastMessage.show(getScene().getWindow(),"Fase Fechada");
            else
                ToastMessage.show(getScene().getWindow(),"Impossível fechar a fase");
        });
    }

    private void update() {
        listView.getItems().clear();
        List<String> list = manager.getCandidaturasList();
        if(list != null) {
            for (String s : list) {
                listView.getItems().add(s);
            }
        }else{
            listView.getItems().add("Sem candidaturas disponiveis");
        }
        if(manager.isFechado()) {
            btnAdiciona.setDisable(true);
            mnAdicionar.setDisable(true);
            btnRemove.setDisable(true);
            mnRemover.setDisable(true);
            btnEditar.setDisable(true);
            mnEditar.setDisable(true);
            btnImportar.setDisable(true);
            mnImportar.setDisable(true);
            btnFechar.setDisable(true);
        }else {
            btnAdiciona.setDisable(false);
            mnAdicionar.setDisable(false);
            btnRemove.setDisable(false);
            mnRemover.setDisable(false);
            btnEditar.setDisable(false);
            mnEditar.setDisable(false);
            btnImportar.setDisable(false);
            mnImportar.setDisable(false);
            btnFechar.setDisable(false);
        }

        lbStatus.setText(String.format("Alunos: %d Docentes: %d Propostas: %d Candidaturas: %d Atribuição de Propostas: %d Atribuição de Orientadores: %d",
                manager.nAlunos(),manager.nDocentes(),manager.nPropostas(),manager.nCandidaturas(),manager.nAtPropostas(),manager.nAtOrientadores()));
        this.setBackground(new Background(new BackgroundFill(Color.LIGHTGREY,CornerRadii.EMPTY,Insets.EMPTY)));

        if(manager.getState() != GPEState.OPCOES_CANDIDATURA){
            this.setVisible(false);
            return;
        }
        this.setVisible(true);
    }
}

