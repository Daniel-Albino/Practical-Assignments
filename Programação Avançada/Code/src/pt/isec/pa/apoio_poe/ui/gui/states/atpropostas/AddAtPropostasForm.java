package pt.isec.pa.apoio_poe.ui.gui.states.atpropostas;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import pt.isec.pa.apoio_poe.model.Manager;
import pt.isec.pa.apoio_poe.model.data.Alunos;
import pt.isec.pa.apoio_poe.model.data.Erros;
import pt.isec.pa.apoio_poe.model.data.Proposta;
import pt.isec.pa.apoio_poe.ui.gui.resources.FontManager;
import pt.isec.pa.apoio_poe.ui.gui.states.alunos.TableViewAlunos;
import pt.isec.pa.apoio_poe.ui.gui.states.propostas.TableViewPropostas;
import pt.isec.pa.apoio_poe.ui.gui.states.utils.ToastMessage;

public class AddAtPropostasForm extends BorderPane {
    private Stage stage;
    private Manager manager;
    private Label lbNameWindow,lbNumber,lbProp;
    private TextField tNumber,tProp;
    private Button btAdd,btAluno,btProp;
    private TableView<Alunos> tableViewAlunos;
    private TableView<Proposta> tableViewProp;
    private String backgorundColorError = "-fx-control-inner-background: #ff0000;";

    private String number,prop;
    private long nAluno;

    public AddAtPropostasForm(Stage stage, Manager manager) {
        this.manager = manager;
        this.stage = stage;
        initializeVariables();
        createViews();
        registerHandlers();
        update();
    }

    private void initializeVariables() {

        lbNameWindow = new Label("Atribuir Proposta");
        lbNameWindow.setFont(FontManager.loadFont("BalsamiqSans-Regular.ttf",34));
        setMargin(lbNameWindow,new Insets(30,0,0,50));

        lbNumber = new Label("Número: ");
        lbProp = new Label("Nome: ");

        tNumber = new TextField();
        tNumber.setPromptText("Insira o número do aluno");
        tNumber.setMinWidth(180);
        tProp = new TextField();
        tProp.setPromptText("Insira o nome do aluno");
        tProp.setMinWidth(180);

        tableViewAlunos = TableViewAlunos.createTableView();
        tableViewAlunos.setMaxHeight(60);
        tableViewProp = TableViewPropostas.createTableView();
        tableViewProp.setMaxHeight(60);

        btAdd = new Button("Adicionar");
        btAdd.setDefaultButton(true);

        btAluno = new Button("Ver aluno");
        btProp = new Button("Ver proposta");
    }

    private void createViews() {

        this.setTop(lbNameWindow);

        HBox hBoxNumber = new HBox(lbNumber,tNumber);
        setMargin(hBoxNumber,new Insets(0,50,0,100));
        hBoxNumber.setSpacing(10);

        HBox hBoxProp = new HBox(lbProp,tProp);
        setMargin(hBoxProp,new Insets(0,50,0,100));
        hBoxProp.setSpacing(10);

        VBox vBoxTableView = new VBox();
        setMargin(vBoxTableView,new Insets(50,50,50,80));
        vBoxTableView.setSpacing(20);
        vBoxTableView.getChildren().addAll(tableViewAlunos,tableViewProp);

        VBox vBoxCenter = new VBox();
        setMargin(vBoxCenter,new Insets(50,50,50,80));
        vBoxCenter.setSpacing(20);
        vBoxCenter.getChildren().addAll(hBoxNumber,hBoxProp,vBoxTableView);

        this.setCenter(vBoxCenter);

        VBox vBoxButtonBottomAdd = new VBox(btAluno,btProp,btAdd);
        setMargin(vBoxButtonBottomAdd,new Insets(50,50,10,50));
        vBoxButtonBottomAdd.setSpacing(10);
        vBoxButtonBottomAdd.setAlignment(Pos.BOTTOM_RIGHT);

        this.setRight(vBoxButtonBottomAdd);
    }

    private void registerHandlers() {
        btAdd.addEventFilter(ActionEvent.ACTION, actionEvent -> {
            setColor();
            number = String.valueOf(tNumber.getText());
            prop = String.valueOf(tProp.getText());

            boolean erro = erroPreencher(number,prop);

            if(!erro){
                StringToLong();
                tratamentoErros(manager.addAtPropostasManualmente(prop,nAluno));
            }else
                ToastMessage.show(getScene().getWindow(),"Faltam parametros");
        });
        btAluno.setOnAction(actionEvent -> {
            number = String.valueOf(tNumber.getText());
            StringToLong();
            System.out.println(number + " " + nAluno);
            update();
        });
        btProp.setOnAction(actionEvent -> {
            prop = String.valueOf(tProp.getText());
            update();
        });
    }
    private void StringToLong(){
        try {
            nAluno = Long.parseLong(number);
        } catch (Exception e) {
            nAluno = -1;
        }
    }
    private void setColor(){
        String color = "-fx-control-inner-backgorund: #ffffff;";
        tNumber.setStyle(color);
    }

    private boolean erroPreencher(String number, String prop) {
        boolean erro = false;
        if(number.isBlank()) {
            tNumber.setStyle(backgorundColorError);
            erro = true;
        }
        if(prop.isBlank()) {
            tProp.setStyle(backgorundColorError);
            erro = true;
        }
        return erro;
    }
    private void tratamentoErros(Erros erros){
        System.out.println(erros);
        switch (erros){
            case FASE_NFECHADA -> {
                ToastMessage.show(getScene().getWindow(),"A fase 'Candidaturas' não se encontra fechada");
            }
            case ATRIBUICAO_ADD_IDPROPOSTA -> {
                ToastMessage.show(getScene().getWindow(),"Proposta inválida");
                tProp.setStyle(backgorundColorError);
                tProp.requestFocus();
            }
            case ATRIBUICAO_ADD_NALUNO -> {
                ToastMessage.show(getScene().getWindow(),"Número inválido");
                tNumber.setStyle(backgorundColorError);
                tNumber.requestFocus();
            }
            case ATRIBUICAO_ADD_ATPROPOSTA -> {
                ToastMessage.show(getScene().getWindow(),"Proposta já atribuida");
                tProp.setStyle(backgorundColorError);
                tProp.requestFocus();
            }
            case ATRIBUICAO_ADD_ATALUNO -> {
                ToastMessage.show(getScene().getWindow(),"Aluno já atribuido");
                tNumber.setStyle(backgorundColorError);
                tNumber.requestFocus();
            }
            case ATRIBUICAO_ADD_ALUNO_RAMO -> {
                ToastMessage.show(getScene().getWindow(),"Ramo da proposta é diferente do ramo do aluno");
                tNumber.setStyle(backgorundColorError);
                tNumber.requestFocus();
            }
            case NONE -> {
                ToastMessage.show(getScene().getWindow(),"Operação realizada com sucesso");
                stage.close();
            }
        }
    }

    private void update() {
        tableViewAlunos.getItems().clear();
        Alunos alunos = manager.getAlunoNumber(nAluno);
        if(alunos != null) {
            tableViewAlunos.getItems().add(alunos);
        }
        tableViewProp.getItems().clear();
        Proposta proposta = manager.getProp(prop);
        if(proposta != null) {
            tableViewProp.getItems().add(proposta);
        }

    }
}
