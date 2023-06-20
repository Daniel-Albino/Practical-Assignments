package pt.isec.pa.apoio_poe.ui.gui.states.atpropostas;

import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import pt.isec.pa.apoio_poe.model.Manager;
import pt.isec.pa.apoio_poe.model.data.Alunos;
import pt.isec.pa.apoio_poe.model.data.Erros;
import pt.isec.pa.apoio_poe.model.data.Proposta;
import pt.isec.pa.apoio_poe.model.fsm.GPEState;
import pt.isec.pa.apoio_poe.ui.gui.resources.FontManager;
import pt.isec.pa.apoio_poe.ui.gui.states.alunos.TableViewAlunos;
import pt.isec.pa.apoio_poe.ui.gui.states.propostas.TableViewPropostas;
import pt.isec.pa.apoio_poe.ui.gui.states.utils.ToastMessage;

import java.util.List;

public class AddAutoAtPropostas extends BorderPane {
    private Manager manager;
    private Button btnAdd,btnAtAuto,btnVoltar;
    private Label lbNameState;
    private TextField tNumber;
    Font font;
    private String number;
    private TableView<Proposta> tableViewProp;
    private TableView<Alunos> tableViewAluno;

    private String backgorundColorError = "-fx-control-inner-background: #ff0000;";
    public AddAutoAtPropostas(Manager manager) {
        this.manager = manager;
        createViews();
        registerHandlers();
        update();
    }
    private void createVars() {
        font = FontManager.loadFont("BalsamiqSans-Regular.ttf",24);

        btnAdd = new Button("Adicionar");
        btnAtAuto = new Button("Atribuir automáticamente");
        btnAtAuto.setFont(font);

        btnVoltar = new Button("Cancelar");

        tNumber = new TextField();
        tNumber.setPromptText("Insira o número do aluno");
        tNumber.setMinWidth(180);


        tableViewAluno = TableViewAlunos.createTableView();
        tableViewProp = TableViewPropostas.createTableView();
    }

    private void createViews() {
        createVars();

        BorderPane borderTop = new BorderPane();
        borderTop.setPrefWidth(654);
        borderTop.setPrefHeight(79);

        lbNameState = new Label("Atribuição Automática - Desempate");
        lbNameState.setFont(FontManager.loadFont("BalsamiqSans-Regular.ttf",34));
        setMargin(lbNameState,new Insets(20,0,0,50));
        borderTop.setLeft(lbNameState);

        VBox vBoxTableView = new VBox();
        setMargin(vBoxTableView,new Insets(50,50,50,80));
        vBoxTableView.setSpacing(20);
        vBoxTableView.getChildren().addAll(btnAtAuto,tableViewProp,tableViewAluno);

        HBox hBox = new HBox(tNumber,btnAdd,btnVoltar);
        setMargin(hBox,new Insets(0,50,50,100));
        hBox.setSpacing(10);

        this.setTop(borderTop);
        this.setCenter(vBoxTableView);
        this.setBottom(hBox);
    }

    private void registerHandlers() {
        manager.addPropertyChangeListener(evt -> { update(); });
        btnVoltar.setOnAction(event -> {
            manager.seguinte();
        });
        btnAtAuto.setOnAction(evnt -> {
            Erros erros = manager.addAtPropostas();
            if(erros != null && erros.equals(Erros.NONE))
                manager.seguinte();
        });
        btnAdd.setOnAction(event -> {
            setColor();
            number = String.valueOf(tNumber.getText());
            boolean erro = erroPreencher(number);

            if(!erro){
                tratamentoErros(manager.desempate(number));
                btnAtAuto.fire();
            }else
                ToastMessage.show(getScene().getWindow(),"Faltam parametros");
        });
    }
    private void tratamentoErros(Erros erros){
        System.out.println(erros);
        switch (erros){
            case FASE_NFECHADA -> {
                ToastMessage.show(getScene().getWindow(),"A fase 'Candidaturas' não se encontra fechada");
            }
            case VALOR_INCORRETO -> {
                ToastMessage.show(getScene().getWindow(),"Número incorreto");
                tNumber.setStyle(backgorundColorError);
                tNumber.requestFocus();
            }
            case NONE -> {
                ToastMessage.show(getScene().getWindow(),"Operação realizada com sucesso");
                btnAtAuto.fire();
            }
        }
    }
    private boolean erroPreencher(String n) {
        boolean erro = false;
        if (n.isBlank()) {
            tNumber.setStyle(backgorundColorError);
            erro = true;
        }
        return erro;
    }
    private void setColor(){
        String color = "-fx-control-inner-backgorund: #ffffff;";
        tNumber.setStyle(color);
    }
    private void update() {
        List<Alunos> alunos = manager.getAlunosEmpatadosList();
        Proposta prop = manager.getPropEmpatada();
        tableViewAluno.getItems().clear();
        tableViewProp.getItems().clear();
        if(alunos != null){
            for(Alunos a : alunos)
                tableViewAluno.getItems().add(a);
        }
        if(prop != null){
            tableViewProp.getItems().add(prop);
        }

        if(manager.getState() != GPEState.ATRIBUICAO_AUTOMATICA_PROPOSTAS){
            this.setVisible(false);
            return;
        }
        this.setVisible(true);

    }
}
