package pt.isec.pa.apoio_poe.ui.gui.states.candidaturas;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import pt.isec.pa.apoio_poe.model.Manager;
import pt.isec.pa.apoio_poe.ui.gui.resources.FontManager;

import java.util.List;

public class MaisInfo extends BorderPane {
    private Manager manager;
    private Stage stage;
    private Label lbNameWindow;
    private Button btnSair;
    private ChoiceBox<String> cbInfo;
    private RadioButton rbAluno,rbPropostas;
    private String [] tipoInfoAluno = {"Selecione...","Alunos com autoproposta","Com candidatura já registada", "Sem candidatura já registada"};
    private String [] tipoInfoPropostas = {"Selecione...","Autopropostas de alunos","Propostas de docentes","Propostas com candidaturas","Propostas sem candidatura","Sem filtros"};
    private ListView<String> listView;

    public MaisInfo(Stage stage, Manager manager) {
        this.stage = stage;
        this.manager = manager;
        initializeVariables();
        createViews();
        registerHandlers();
        update();
    }

    private void initializeVariables() {
        lbNameWindow = new Label("Mais Informação");
        lbNameWindow.setFont(FontManager.loadFont("BalsamiqSans-Regular.ttf",34));
        setMargin(lbNameWindow,new Insets(30,0,0,50));

        btnSair = new Button("Voltar");

        cbInfo = new ChoiceBox<>();
        cbInfo.setPrefWidth(200);
        cbInfo.getItems().addAll(tipoInfoAluno);
        cbInfo.setValue("Selecione...");

        rbAluno = new RadioButton("Lista de alunos");
        rbAluno.setSelected(true);
        rbPropostas = new RadioButton("Lista de propostas");

        listView = new ListView<>();
        listView.setStyle("-fx-font-family: Arial; -fx-font-size: 14;");
        setMargin(listView,new Insets(10,20,50,40));
    }

    private void createViews() {
        this.setTop(lbNameWindow);

        HBox hBoxNumber = new HBox(rbAluno,rbPropostas);
        setMargin(hBoxNumber,new Insets(0,50,0,100));
        hBoxNumber.setSpacing(10);

        VBox vBox = new VBox();
        setMargin(vBox,new Insets(20,20,20,50));
        vBox.setSpacing(20);
        vBox.getChildren().addAll(hBoxNumber,cbInfo,listView);

        this.setCenter(vBox);
        VBox vBoxButtonBottom = new VBox(btnSair);
        setMargin(vBoxButtonBottom,new Insets(0,10,40,50));
        vBoxButtonBottom.setAlignment(Pos.BOTTOM_LEFT);
        vBoxButtonBottom.setSpacing(10);
        this.setLeft(vBoxButtonBottom);
    }

    private void registerHandlers() {

        rbAluno.setOnAction(actionEvent -> {
            cbInfo.getItems().clear();
            cbInfo.getItems().addAll(tipoInfoAluno);
            cbInfo.setValue("Selecione...");
            rbPropostas.setSelected(false);
        });
        rbPropostas.setOnAction(actionEvent -> {
            cbInfo.getItems().clear();
            cbInfo.getItems().addAll(tipoInfoPropostas);
            cbInfo.setValue("Selecione...");
            rbAluno.setSelected(false);
        });
        cbInfo.setOnAction(actionEvent -> {
            update();
        });
        btnSair.setOnAction(actionEvent -> {
            stage.close();
        });
    }

    private void update() {
        listView.getItems().clear();
        switch (String.valueOf(cbInfo.getValue())){
            case "Selecione..." -> listView.getItems().add("Sem dados");
            case "Alunos com autoproposta" -> {
                List<String> aux = manager.getCandAutopropostasAlunos();
                if(aux.isEmpty())
                    listView.getItems().add("Sem dados");
                else
                    for(String s : aux)
                        listView.getItems().add(s);
            }
            case "Com candidatura já registada" -> {
                List<String> aux = manager.getCandRegistadaAlunos();
                if(aux.isEmpty())
                    listView.getItems().add("Sem dados");
                else
                    for(String s : aux)
                        listView.getItems().add(s);
            }
            case "Sem candidatura já registada" -> {
                List<String> aux = manager.getSemCandRegistadaAlunos();
                if(aux.isEmpty())
                    listView.getItems().add("Sem dados");
                else
                    for(String s : aux)
                        listView.getItems().add(s);
            }
            case "Autopropostas de alunos" -> {
                List<String> aux = manager.getCandAutopropostasProp();
                if(aux.isEmpty())
                    listView.getItems().add("Sem dados");
                else
                    for(String s : aux)
                        listView.getItems().add(s);
            }
            case "Propostas de docentes" -> {
                List<String> aux = manager.getCandDocenteProp();
                if(aux.isEmpty())
                    listView.getItems().add("Sem dados");
                else
                    for(String s : aux)
                        listView.getItems().add(s);
            }
            case "Propostas com candidaturas" -> {
                List<String> aux = manager.getCandProp();
                if(aux.isEmpty())
                    listView.getItems().add("Sem dados");
                else
                    for(String s : aux)
                        listView.getItems().add(s);
            }
            case "Propostas sem candidatura" -> {
                List<String> aux = manager.getCandSemProp();
                if(aux.isEmpty())
                    listView.getItems().add("Sem dados");
                else
                    for(String s : aux)
                        listView.getItems().add(s);
            }
            case "Sem filtros" -> {
                List<String> aux = manager.getCandidaturasList();
                if(aux.isEmpty())
                    listView.getItems().add("Sem dados");
                else
                    for(String s : aux)
                        listView.getItems().add(s);
            }

        }
    }

}
