package pt.isec.pa.apoio_poe.ui.gui.states.atpropostas;

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
import pt.isec.pa.apoio_poe.ui.gui.states.candidaturas.AddCandidaturasForm;
import pt.isec.pa.apoio_poe.ui.gui.states.candidaturas.EditCandidaturasForm;
import pt.isec.pa.apoio_poe.ui.gui.states.candidaturas.MaisInfo;
import pt.isec.pa.apoio_poe.ui.gui.states.candidaturas.RemoveCandidaturasForm;
import pt.isec.pa.apoio_poe.ui.gui.states.utils.ToastMessage;

import javax.security.auth.kerberos.KeyTab;
import java.io.File;
import java.util.List;

public class AtPropostasUI extends BorderPane {
    Manager manager;
    Button btnAddAuto,btnAdiciona,btnRemove, btnExportar, btnVoltar,btnFecharFase,btnSeguinte;
    Label lbNameState,lbStatus;
    Font font;
    MenuBar menuBar;
    Menu mnFile,mnEdit;
    MenuItem mnAddAuto,mnAdiciona,mnRemove,mnExportar,mnRedo,mnUndo,mnSave,mnExit;
    ListView<String> listView;

    public AtPropostasUI(Manager manager) {
        this.manager = manager;
        createViews();
        registerHandlers();
        update();
    }

    private void createButtons() {
        font = FontManager.loadFont("BalsamiqSans-Regular.ttf",20);

        btnAddAuto = new Button("Adicionar Automaticamente");
        btnAddAuto.setFont(font);

        btnAdiciona = new Button("Adicionar Manualmente");
        btnAdiciona.setFont(font);

        btnRemove = new Button("Remover");
        btnRemove.setFont(font);

        btnExportar = new Button("Exportar");
        btnExportar.setFont(font);

        btnVoltar = new Button("Voltar");
        btnSeguinte = new Button("Seguinte");
        btnFecharFase = new Button("Fechar Fase");

        btnVoltar.setFont(font);
        btnSeguinte.setFont(font);
        btnFecharFase.setFont(font);
    }

    private void createViews() {
        createButtons();

        //Parte de cima da interface
        BorderPane borderTop = new BorderPane();
        borderTop.setPrefWidth(654);
        borderTop.setPrefHeight(79);

        lbNameState = new Label("Atribuição de Propostas");
        lbNameState.setFont(FontManager.loadFont("BalsamiqSans-Regular.ttf",34));
        setMargin(lbNameState,new Insets(20,0,0,50));
        borderTop.setLeft(lbNameState);

        HBox hBoxButtomTop = new HBox(btnAddAuto,btnAdiciona,btnRemove,btnExportar);
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

        VBox vBoxButtonBottomSair = new VBox(btnVoltar);
        setMargin(vBoxButtonBottomSair,new Insets(0,0,10,50));
        vBoxButtonBottomSair.setAlignment(Pos.BOTTOM_LEFT);
        vBoxButtonBottomSair.setSpacing(10);

        VBox vBoxButtonBottom = new VBox(btnFecharFase,btnSeguinte);
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
        mnAddAuto = new MenuItem("Adicionar Automaticamente");
        mnAdiciona = new MenuItem("Adicionar Manualmente");
        mnRemove = new MenuItem("Remover");
        mnExportar = new MenuItem("Exportar");
        mnFile.getItems().addAll(mnAddAuto,mnAdiciona,mnRemove,mnExportar);

        mnEdit = new Menu("Edit");
        mnRedo = new MenuItem("Redo");
        mnRedo.setAccelerator(new KeyCodeCombination(KeyCode.Z, KeyCombination.SHIFT_DOWN,KeyCombination.CONTROL_DOWN));
        mnUndo = new MenuItem("Undo");
        mnUndo.setAccelerator(new KeyCodeCombination(KeyCode.Z, KeyCombination.CONTROL_DOWN));
        mnSave = new MenuItem("Salvar");
        mnSave.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
        mnExit = new MenuItem("Voltar");
        mnEdit.getItems().addAll(mnUndo,mnRedo,mnSave,mnExit);

        MenuBar menuBarAux = new MenuBar(mnFile,mnEdit);
        menuBarAux.setUseSystemMenuBar(true);
        return menuBarAux;
    }


    private void registerHandlers() {
        manager.addPropertyChangeListener(evt -> { update(); });
        btnAddAuto.setOnAction(event -> {
            manager.atribuirAutomaticamente();
        });
        btnAdiciona.setOnAction(event -> {
            Stage stage = new Stage();
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(btnAdiciona.getScene().getWindow());
            Scene scene = new Scene(new AddAtPropostasForm(stage,manager));
            stage.setScene(scene);
            stage.setTitle("Atribuir Proposta");
            stage.setMaxWidth(900);
            stage.setMaxHeight(500);
            stage.setMinWidth(700);
            stage.setMinHeight(450);
            stage.show();
        });
        mnAdiciona.setOnAction(actionEvent -> {
            btnAdiciona.fire();
        });
        btnRemove.setOnAction(event -> {
            Stage stage = new Stage();
            stage.initModality(Modality.WINDOW_MODAL);
            stage.initOwner(btnAdiciona.getScene().getWindow());
            Scene scene = new Scene(new RemoveAtPropostasForm(stage,manager));
            stage.setScene(scene);
            stage.setTitle("Remover Atribuição de Proposta");
            stage.setMinWidth(700);
            stage.setMinHeight(300);
            stage.setResizable(false);
            stage.show();
        });
        mnRemove.setOnAction(event ->{
            btnRemove.fire();
        });
        btnVoltar.setOnAction(actionEvent -> {
            manager.voltar();
        });
        mnExit.setOnAction(event ->{
            btnVoltar.fire();
        });
        btnExportar.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("File save...");
            fileChooser.setInitialDirectory(new File(".")); //Diretório corrente
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Atribuição de Propostas (*.csv)","*.csv")
            );
            File file = fileChooser.showSaveDialog(this.getScene().getWindow());
            if(file != null){
                if(manager.exportFile(file))
                    ToastMessage.show(getScene().getWindow(),"Informação exportada com sucesso");
            }
        });
        mnExportar.setOnAction(event ->{
            btnExportar.fire();
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
                ToastMessage.show(getScene().getWindow(),"Guardado com sucesso");
            }
        });
        mnUndo.setOnAction(event -> {
            manager.undo();
        });
        mnRedo.setOnAction(event -> {
            manager.execute();
        });
        btnSeguinte.setOnAction(evnt -> {
            manager.seguinte();
        });
        btnFecharFase.setOnAction(actionEvent -> {
            if(manager.fecharFase().equals(Erros.NONE))
                ToastMessage.show(getScene().getWindow(),"Fase Fechada");
            else
                ToastMessage.show(getScene().getWindow(),"Impossível fechar a fase");
        });
    }

    private void update() {
        listView.getItems().clear();
        List<String> atPropostas = manager.getAtPropostasList();
        if(atPropostas != null) {
            for (String s : atPropostas)
                listView.getItems().add(s);
        }else
            listView.getItems().add("Sem propostas atribuidas");

        lbStatus.setText(String.format("Alunos: %d Docentes: %d Propostas: %d Candidaturas: %d Atribuição de Propostas: %d Atribuição de Orientadores: %d",
                manager.nAlunos(),manager.nDocentes(),manager.nPropostas(),manager.nCandidaturas(),manager.nAtPropostas(),manager.nAtOrientadores()));
        this.setBackground(new Background(new BackgroundFill(Color.LIGHTGREY,CornerRadii.EMPTY,Insets.EMPTY)));

        if(manager.isFechado()) {
            btnAddAuto.setDisable(true);
            mnAddAuto.setDisable(true);
            btnAdiciona.setDisable(true);
            mnAdiciona.setDisable(true);
            btnRemove.setDisable(true);
            mnRemove.setDisable(true);
            btnFecharFase.setDisable(true);
            mnRedo.setDisable(true);
            mnUndo.setDisable(true);
        }else {
            btnAddAuto.setDisable(false);
            mnAddAuto.setDisable(false);
            btnAdiciona.setDisable(false);
            mnAdiciona.setDisable(false);
            btnRemove.setDisable(false);
            mnRemove.setDisable(false);
            btnFecharFase.setDisable(false);
            mnRedo.setDisable(false);
            mnUndo.setDisable(false);
        }

        if(manager.getState() != GPEState.ATRIBUICAO_PROPOSTAS){
            this.setVisible(false);
            return;
        }
        this.setVisible(true);
    }
}
