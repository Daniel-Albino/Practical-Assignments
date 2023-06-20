package pt.isec.pa.apoio_poe.ui.gui.states.atpropostas;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import pt.isec.pa.apoio_poe.model.Manager;
import pt.isec.pa.apoio_poe.model.data.Alunos;
import pt.isec.pa.apoio_poe.model.data.Erros;
import pt.isec.pa.apoio_poe.model.data.Proposta;
import pt.isec.pa.apoio_poe.ui.gui.resources.FontManager;
import pt.isec.pa.apoio_poe.ui.gui.states.alunos.TableViewAlunos;
import pt.isec.pa.apoio_poe.ui.gui.states.propostas.TableViewPropostas;
import pt.isec.pa.apoio_poe.ui.gui.states.utils.ToastMessage;

public class RemoveAtPropostasForm extends BorderPane {
    private Stage stage;
    private Manager manager;
    private Label lbNameWindow,lbNumber,lbProp,lbTipo;
    private TextField tNumber,tProp;
    private Button btRemove,btAluno,btProp;
    private ChoiceBox<String> cbTipo;
    private TableView<Alunos> tableViewAlunos;
    private TableView<Proposta> tableViewProp;
    private String backgorundColorError = "-fx-control-inner-background: #ff0000;";
    private String number,prop;
    private String [] tipo = {"Selecione...","Remover pelo número de aluno","Remover pelo código da proposta"};
    private long nAluno;

    public RemoveAtPropostasForm(Stage stage, Manager manager) {
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
        lbTipo = new Label("Escolha um tipo de remoção: ");

        cbTipo = new ChoiceBox<>();
        cbTipo.setPrefWidth(210);
        cbTipo.getItems().addAll(tipo);
        cbTipo.setValue("Selecione...");


        tNumber = new TextField();
        tNumber.setPromptText("Insira o número do aluno");
        tNumber.setMinWidth(180);
        tProp = new TextField();
        tProp.setPromptText("Insira o nome do aluno");
        tProp.setMinWidth(180);
        tNumber.setDisable(true);
        tProp.setDisable(true);

        tableViewAlunos = TableViewAlunos.createTableView();
        tableViewAlunos.setMaxHeight(60);
        tableViewProp = TableViewPropostas.createTableView();
        tableViewProp.setMaxHeight(60);

        btRemove = new Button("Adicionar");
        btRemove.setDefaultButton(true);

        btAluno = new Button("Ver aluno");
        btProp = new Button("Ver proposta");
    }

    private void createViews() {

        this.setTop(lbNameWindow);

        HBox hBoxType = new HBox(lbTipo,cbTipo);
        setMargin(hBoxType,new Insets(0,50,0,100));
        hBoxType.setSpacing(10);

        HBox hBoxChoiceBox = new HBox(lbTipo,cbTipo);
        setMargin(hBoxChoiceBox,new Insets(0,50,0,100));
        hBoxChoiceBox.setSpacing(10);

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
        vBoxCenter.getChildren().addAll(hBoxChoiceBox,hBoxNumber,hBoxProp,vBoxTableView);

        this.setCenter(vBoxCenter);

        VBox vBoxButtonBottomAdd = new VBox(btAluno,btProp, btRemove);
        setMargin(vBoxButtonBottomAdd,new Insets(50,50,10,50));
        vBoxButtonBottomAdd.setSpacing(10);
        vBoxButtonBottomAdd.setAlignment(Pos.BOTTOM_RIGHT);

        this.setRight(vBoxButtonBottomAdd);
    }

    private void registerHandlers() {
        cbTipo.setOnAction(actionEvent -> {
            switch (String.valueOf(cbTipo.getValue())){
                case "Remover pelo número de aluno" -> {
                    tProp.clear();
                    tNumber.setDisable(false);
                    btAluno.setDisable(false);
                    tProp.setDisable(true);
                    btProp.setDisable(true);
                }
                case "Remover pelo código da proposta" -> {
                    tNumber.clear();
                    tNumber.setDisable(true);
                    btAluno.setDisable(true);
                    tProp.setDisable(false);
                    btProp.setDisable(false);
                }
                default -> {
                    tNumber.setDisable(true);
                    btAluno.setDisable(true);
                    tProp.setDisable(true);
                    btProp.setDisable(true);
                }
            }
        });
        btRemove.addEventFilter(ActionEvent.ACTION, actionEvent -> {
            setColor();

            number = String.valueOf(tNumber.getText());
            prop = String.valueOf(tProp.getText());

            boolean erro = erroPreencher(number,prop);

            if(!erro){
                switch (String.valueOf(cbTipo.getValue())) {
                    case "Remover pelo número de aluno" -> {
                        StringToLong();
                        tratamentoErros(manager.removeAtPropostasAluno(nAluno));
                    }
                    case "Remover pelo código da proposta" -> {
                        tratamentoErros(manager.removeAtPropostasProp(prop));
                    }
                }
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
        if(number.isBlank() && !tNumber.isDisable()) {
            tNumber.setStyle(backgorundColorError);
            erro = true;
        }
        if(prop.isBlank() && !tProp.isDisable()) {
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
            case ATRIBUICAO_REMOVE_NALUNO -> {
                ToastMessage.show(getScene().getWindow(),"Número inválido");
                tNumber.setStyle(backgorundColorError);
                tNumber.requestFocus();
            }
            case ATRIBUICAO_REMOVE_IDPROPOSTA ->{
                ToastMessage.show(getScene().getWindow(),"Código da proposta inválido");
                tProp.setStyle(backgorundColorError);
                tProp.requestFocus();
            }
            case ATRIBUICAO_REMOVE -> {
                ToastMessage.show(getScene().getWindow(),"Impossível remover");
                switch (String.valueOf(cbTipo.getValue())) {
                    case "Remover pelo número de aluno" -> {
                        tNumber.setStyle(backgorundColorError);
                        tNumber.requestFocus();
                    }
                    case "Remover pelo código da proposta" -> {
                        tProp.setStyle(backgorundColorError);
                        tProp.requestFocus();
                    }
                }
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
