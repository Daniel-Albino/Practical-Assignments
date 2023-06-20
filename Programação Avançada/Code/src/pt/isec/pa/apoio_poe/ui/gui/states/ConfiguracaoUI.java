package pt.isec.pa.apoio_poe.ui.gui.states;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import pt.isec.pa.apoio_poe.model.Manager;

import pt.isec.pa.apoio_poe.model.data.Erros;
import pt.isec.pa.apoio_poe.model.fsm.GPEState;
import pt.isec.pa.apoio_poe.ui.gui.resources.FontManager;
import pt.isec.pa.apoio_poe.ui.gui.states.utils.ToastMessage;

import java.io.File;


public class ConfiguracaoUI extends BorderPane {
    Manager manager;
    Button btnAlunos,btnDocentes, btnPropostas,btnSair,btnSeguinte,btnFecharFase;
    Label lbNameState,lbStatus;
    Font font;
    MenuBar menuBar;
    Menu mnFile,mnEdit;
    MenuItem mnSave, mnExit,mnAlunos,mnDocentes,mnPropostas;


    public ConfiguracaoUI(Manager manager) {
        this.manager = manager;
        createViews();
        registerHandlers();
        update();
    }

    private void createButtons() {
        font = FontManager.loadFont("BalsamiqSans-Regular.ttf",20);

        btnAlunos = new Button("Gestão de alunos");
        btnAlunos.setMinWidth(700);
        btnAlunos.setPrefSize(40,40);
        btnAlunos.setFont(font);

        btnDocentes = new Button("Gestão de docentes");
        btnDocentes.setMinWidth(700);
        btnDocentes.setPrefSize(40,40);
        btnDocentes.setFont(font);

        btnPropostas = new Button("Gestão de propostas");
        btnPropostas.setMinWidth(700);
        btnPropostas.setPrefSize(40,40);
        btnPropostas.setFont(font);

        btnSair = new Button("Sair");
        btnSair.setMinWidth(100);
        btnSair.setPrefSize(40,40);
        btnSair.setFont(font);

        btnSeguinte = new Button("Seguinte");
        btnSeguinte.setMinWidth(200);
        btnSeguinte.setPrefSize(40,40);
        btnSeguinte.setFont(font);

        btnFecharFase = new Button("Fechar fase");
        btnFecharFase.setMinWidth(200);
        btnFecharFase.setPrefSize(40,40);
        btnFecharFase.setFont(font);
    }

    private void createViews() {
        createButtons();

        //Parte de cima da interface
        BorderPane borderTop = new BorderPane();
        borderTop.setPrefWidth(654);
        borderTop.setPrefHeight(79);

        lbNameState = new Label("Configuração");
        lbNameState.setFont(FontManager.loadFont("BalsamiqSans-Regular.ttf",34));
        setMargin(lbNameState,new Insets(20,0,0,50));
        borderTop.setLeft(lbNameState);

        menuBar = Toolbar();
        borderTop.setTop(menuBar);

        //Parte do centro

        VBox vBoxButton = new VBox(btnAlunos,btnDocentes,btnPropostas);
        vBoxButton.setAlignment(Pos.CENTER);
        vBoxButton.setSpacing(10);
        vBoxButton.setPrefWidth(100);
        vBoxButton.setPrefHeight(200);

        VBox vBoxButtonBottom = new VBox(btnFecharFase,btnSeguinte);
        setMargin(vBoxButtonBottom,new Insets(10,50,10,0));
        vBoxButtonBottom.setAlignment(Pos.BOTTOM_RIGHT);
        vBoxButtonBottom.setSpacing(10);

        VBox vBoxButtonBottomSair = new VBox(btnSair);
        setMargin(vBoxButtonBottomSair,new Insets(10,50,10,50));
        vBoxButtonBottomSair.setAlignment(Pos.BOTTOM_LEFT);
        vBoxButtonBottomSair.setSpacing(10);

        //setMargin(btnSair,new Insets(0,0,20,50));
        this.setLeft(vBoxButtonBottomSair);
        lbStatus = new Label();
        lbStatus.setPrefWidth(Integer.MAX_VALUE);
        //lbStatus.setFont(new Font());
        lbStatus.setStyle("-fx-background-color: #c0c0c0");
        lbStatus.setFont(FontManager.loadFont("BalsamiqSans-Regular.ttf",20));
        lbStatus.setBorder(new Border(new BorderStroke(Color.DARKGRAY,BorderStrokeStyle.SOLID,CornerRadii.EMPTY
                ,BorderWidths.DEFAULT)));
        lbStatus.setPadding(new Insets(10));

        this.setTop(borderTop);
        this.setCenter(vBoxButton);
        this.setBottom(lbStatus);
        this.setRight(vBoxButtonBottom);

    }
    private MenuBar Toolbar() {
        mnFile = new Menu("File");
        mnAlunos = new MenuItem("Gestão Alunos");
        mnAlunos.setAccelerator(new KeyCodeCombination(KeyCode.A, KeyCombination.ALT_DOWN));
        mnDocentes = new MenuItem("Gestão Docentes");
        mnDocentes.setAccelerator(new KeyCodeCombination(KeyCode.D, KeyCombination.ALT_DOWN));
        mnPropostas = new MenuItem("Gestão Propostas");
        mnPropostas.setAccelerator(new KeyCodeCombination(KeyCode.P, KeyCombination.ALT_DOWN));
        mnFile.getItems().addAll(mnAlunos,mnDocentes,mnPropostas);

        mnEdit = new Menu("Edit");
        mnSave = new MenuItem("Salvar");
        mnSave.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
        mnExit = new MenuItem("Sair");
        mnEdit.getItems().addAll(mnSave,mnExit);

        MenuBar menuBarAux = new MenuBar(mnFile,mnEdit);
        menuBarAux.setUseSystemMenuBar(true);
        return menuBarAux;
    }


    private void registerHandlers() {
        manager.addPropertyChangeListener(evt -> { update(); });
        btnAlunos.setOnAction(event -> {
            manager.gerirAlunos();
        });
        btnDocentes.setOnAction(event -> {
            manager.gerirDocentes();
        });
        btnPropostas.setOnAction(event -> {
            manager.gerirPropostas();
        });
        btnSair.setOnAction(event -> {
            manager.terminar();
        });
        btnSeguinte.setOnAction(event -> {
            manager.seguinte();
        });
        btnFecharFase.setOnAction(event -> {
            manager.fecharFase();
        });
        mnAlunos.setOnAction(actionEvent -> {
            btnAlunos.fire();
        });
        mnDocentes.setOnAction(actionEvent -> {
            btnDocentes.fire();
        });
        mnPropostas.setOnAction(actionEvent -> {
            btnPropostas.fire();
        });
        mnExit.setOnAction(actionEvent -> {
            manager.terminar();
        });
        mnSave.setOnAction(actionEvent -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("File save...");
            fileChooser.setInitialDirectory(new File(".")); //Diretório corrente
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("PA (*.dat)","*.dat")
            );
            File file = fileChooser.showSaveDialog(this.getScene().getWindow());
            if(file != null){
                manager.save(file);
            }
        });
        btnFecharFase.setOnAction(actionEvent -> {
            if(manager.fecharFase().equals(Erros.NONE))
                ToastMessage.show(getScene().getWindow(),"Fase Fechada");
            else
                ToastMessage.show(getScene().getWindow(),"Impossível fechar a fase");
        });
    }

    private void update() {
        lbStatus.setText(String.format("Alunos: %d Docentes: %d Propostas: %d Candidaturas: %d Atribuição de Propostas: %d Atribuição de Orientadores: %d",
                manager.nAlunos(),manager.nDocentes(),manager.nPropostas(),manager.nCandidaturas(),manager.nAtPropostas(),manager.nAtOrientadores()));
        this.setBackground(new Background(new BackgroundFill(Color.LIGHTGREY,CornerRadii.EMPTY,Insets.EMPTY)));

        if(manager.isFechado())
            btnFecharFase.setDisable(true);
        else
            btnFecharFase.setDisable(false);


        if(manager.getState() != GPEState.CONFIGURACAO){
            this.setVisible(false);
            return;
        }
        this.setVisible(true);
    }
}

