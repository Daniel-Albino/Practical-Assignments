package pt.isec.pa.apoio_poe.ui.gui.states.atorientadores;

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
import pt.isec.pa.apoio_poe.model.data.Docentes;
import pt.isec.pa.apoio_poe.model.data.Erros;
import pt.isec.pa.apoio_poe.model.data.Proposta;
import pt.isec.pa.apoio_poe.ui.gui.resources.FontManager;
import pt.isec.pa.apoio_poe.ui.gui.states.alunos.TableViewAlunos;
import pt.isec.pa.apoio_poe.ui.gui.states.docentes.TableViewDocentes;
import pt.isec.pa.apoio_poe.ui.gui.states.propostas.TableViewPropostas;
import pt.isec.pa.apoio_poe.ui.gui.states.utils.ToastMessage;

public class RemoveAtOrientadoresForm extends BorderPane {
    private Stage stage;
    private Manager manager;
    private Label lbNameWindow, lbDocente,lbProp,lbTipo;
    private TextField tDocente,tProp;
    private Button btRemove, btDocente,btProp;
    private ChoiceBox<String> cbTipo;
    private TableView<Docentes> tableViewDocentes;
    private TableView<Proposta> tableViewProp;
    private String backgorundColorError = "-fx-control-inner-background: #ff0000;";
    private String email,prop;
    private String [] tipo = {"Selecione...","Remover uma proposta","Remover todas as propostas de um docente"};

    public RemoveAtOrientadoresForm(Stage stage, Manager manager) {
        this.manager = manager;
        this.stage = stage;
        initializeVariables();
        createViews();
        registerHandlers();
        update();
    }

    private void initializeVariables() {

        lbNameWindow = new Label("Remover Orientação");
        lbNameWindow.setFont(FontManager.loadFont("BalsamiqSans-Regular.ttf",34));
        setMargin(lbNameWindow,new Insets(30,0,0,50));

        lbDocente = new Label("Docente: ");
        lbProp = new Label("Código: ");
        lbTipo = new Label("Escolha um tipo de remoção: ");

        cbTipo = new ChoiceBox<>();
        cbTipo.setPrefWidth(210);
        cbTipo.getItems().addAll(tipo);
        cbTipo.setValue("Selecione...");


        tDocente = new TextField();
        tDocente.setPromptText("Insira o e-mail do docente");
        tDocente.setMinWidth(180);
        tProp = new TextField();
        tProp.setPromptText("Insira o código da proposta");
        tProp.setMinWidth(180);
        tDocente.setDisable(true);
        tProp.setDisable(true);

        tableViewDocentes = TableViewDocentes.createTableView();
        tableViewDocentes.setMaxHeight(60);
        tableViewProp = TableViewPropostas.createTableView();
        tableViewProp.setMaxHeight(60);

        btRemove = new Button("Remover");
        btRemove.setDefaultButton(true);

        btDocente = new Button("Ver docente");
        btProp = new Button("Ver proposta");
    }

    private void createViews() {

        this.setTop(lbNameWindow);

        HBox hBoxType = new HBox(lbTipo,cbTipo);
        setMargin(hBoxType,new Insets(0,50,0,100));
        hBoxType.setSpacing(10);

        HBox hBoxNumber = new HBox(lbDocente, tDocente);
        setMargin(hBoxNumber,new Insets(0,50,0,100));
        hBoxNumber.setSpacing(10);

        HBox hBoxProp = new HBox(lbProp,tProp);
        setMargin(hBoxProp,new Insets(0,50,0,100));
        hBoxProp.setSpacing(10);

        VBox vBoxTableView = new VBox();
        setMargin(vBoxTableView,new Insets(50,50,50,80));
        vBoxTableView.setSpacing(20);
        vBoxTableView.getChildren().addAll(tableViewDocentes,tableViewProp);

        VBox vBoxCenter = new VBox();
        setMargin(vBoxCenter,new Insets(50,50,50,80));
        vBoxCenter.setSpacing(20);
        vBoxCenter.getChildren().addAll(hBoxType,hBoxNumber,hBoxProp,vBoxTableView);

        this.setCenter(vBoxCenter);

        VBox vBoxButtonBottomAdd = new VBox(btDocente,btProp, btRemove);
        setMargin(vBoxButtonBottomAdd,new Insets(50,50,10,50));
        vBoxButtonBottomAdd.setSpacing(10);
        vBoxButtonBottomAdd.setAlignment(Pos.BOTTOM_RIGHT);

        this.setRight(vBoxButtonBottomAdd);
    }

    private void registerHandlers() {
        cbTipo.setOnAction(actionEvent -> {
            switch (String.valueOf(cbTipo.getValue())){
                case "Remover todas as propostas de um docente" -> {
                    tProp.clear();
                    tDocente.setDisable(false);
                    btDocente.setDisable(false);
                    tProp.setDisable(true);
                    btProp.setDisable(true);
                }
                case "Remover uma proposta" -> {
                    tDocente.clear();
                    tDocente.setDisable(true);
                    btDocente.setDisable(true);
                    tProp.setDisable(false);
                    btProp.setDisable(false);
                }
                default -> {
                    tDocente.setDisable(true);
                    btDocente.setDisable(true);
                    tProp.setDisable(true);
                    btProp.setDisable(true);
                }
            }
        });
        btRemove.addEventFilter(ActionEvent.ACTION, actionEvent -> {
            setColor();

            email = String.valueOf(tDocente.getText());
            prop = String.valueOf(tProp.getText());

            boolean erro = erroPreencher(email,prop);

            if(!erro){
                switch (String.valueOf(cbTipo.getValue())) {
                    case "Remover uma proposta" -> {
                        tratamentoErros(manager.removeOrientadorProposta(prop));
                    }
                    case "Remover todas as propostas de um docente" -> {
                        tratamentoErros(manager.removeOrientadorTodasProposta(email));
                    }
                }
            }else
                ToastMessage.show(getScene().getWindow(),"Faltam parametros");
        });
        btDocente.setOnAction(actionEvent -> {
            email = String.valueOf(tDocente.getText());
            update();
        });
        btProp.setOnAction(actionEvent -> {
            prop = String.valueOf(tProp.getText());
            update();
        });
    }
    private void setColor(){
        String color = "-fx-control-inner-backgorund: #ffffff;";
        tDocente.setStyle(color);
        tProp.setStyle(color);
    }

    private boolean erroPreencher(String number, String prop) {
        boolean erro = false;
        if(number.isBlank() && !tDocente.isDisable()) {
            tDocente.setStyle(backgorundColorError);
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
                ToastMessage.show(getScene().getWindow(),"A fase 'Atribuição de Propostas' não se encontra fechada");
            }
            case PROPOSTA_ID, ORIENTADORES_REMOVE -> {
                ToastMessage.show(getScene().getWindow(),"Código incorreto");
                tProp.setStyle(backgorundColorError);
                tProp.requestFocus();
            }
            case ORIENTADORES_REMOVE_T2 -> {
                ToastMessage.show(getScene().getWindow(),"Impossível remover um projeto");
                tProp.setStyle(backgorundColorError);
                tProp.requestFocus();
            }
            case DOCENTE_EMAIL, ORIENTADORES_REMOVE_NEXISTE_DOCENTE -> {
                ToastMessage.show(getScene().getWindow(),"E-mail incorreto");
                tDocente.setStyle(backgorundColorError);
                tDocente.requestFocus();
            }
            case NONE -> {
                ToastMessage.show(getScene().getWindow(),"Operação realizada com sucesso");
                stage.close();
            }
        }
    }

    private void update() {
        tableViewDocentes.getItems().clear();
        Docentes docentes = manager.getDocenteEmail(email);
        if(docentes != null) {
            tableViewDocentes.getItems().add(docentes);
        }
        tableViewProp.getItems().clear();
        Proposta proposta = manager.getProp(prop);
        if(proposta != null) {
            tableViewProp.getItems().add(proposta);
        }
    }
}
