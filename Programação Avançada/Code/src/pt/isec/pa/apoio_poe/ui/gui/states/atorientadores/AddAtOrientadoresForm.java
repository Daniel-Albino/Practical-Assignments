package pt.isec.pa.apoio_poe.ui.gui.states.atorientadores;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import pt.isec.pa.apoio_poe.model.Manager;
import pt.isec.pa.apoio_poe.model.data.Docentes;
import pt.isec.pa.apoio_poe.model.data.Erros;
import pt.isec.pa.apoio_poe.model.data.Proposta;
import pt.isec.pa.apoio_poe.ui.gui.resources.FontManager;
import pt.isec.pa.apoio_poe.ui.gui.states.docentes.TableViewDocentes;
import pt.isec.pa.apoio_poe.ui.gui.states.propostas.TableViewPropostas;
import pt.isec.pa.apoio_poe.ui.gui.states.utils.ToastMessage;

public class AddAtOrientadoresForm extends BorderPane {
    private Stage stage;
    private Manager manager;
    private Label lbNameWindow, lbProp, lbDocente;
    private TextField tProp, tDocente;
    private Button btAdd, btProp, btDocente;
    private TableView<Proposta> tableViewProp;
    private TableView<Docentes> tableViewDocente;
    private String backgorundColorError = "-fx-control-inner-background: #ff0000;";
    private String prop, email;

    public AddAtOrientadoresForm(Stage stage, Manager manager) {
        this.manager = manager;
        this.stage = stage;
        initializeVariables();
        createViews();
        registerHandlers();
        update();
    }

    private void initializeVariables() {

        lbNameWindow = new Label("Atribuir Orientador");
        lbNameWindow.setFont(FontManager.loadFont("BalsamiqSans-Regular.ttf",34));
        setMargin(lbNameWindow,new Insets(30,0,0,50));

        lbDocente = new Label("E-mail: ");
        lbProp = new Label("Código: ");

        tProp = new TextField();
        tProp.setPromptText("Insira código da proposta");
        tProp.setMinWidth(180);
        tDocente = new TextField();
        tDocente.setPromptText("Insira o e-mail do docente");
        tDocente.setMinWidth(180);

        tableViewProp = TableViewPropostas.createTableView();
        tableViewProp.setMaxHeight(60);
        tableViewDocente = TableViewDocentes.createTableView();
        tableViewDocente.setMaxHeight(60);

        btAdd = new Button("Adicionar");
        btAdd.setDefaultButton(true);

        btProp = new Button("Ver proposta");
        btDocente = new Button("Ver docente");
    }

    private void createViews() {

        this.setTop(lbNameWindow);

        HBox hBoxNumber = new HBox(lbDocente, tDocente);
        setMargin(hBoxNumber,new Insets(0,50,0,100));
        hBoxNumber.setSpacing(10);

        HBox hBoxProp = new HBox(lbProp, tProp);
        setMargin(hBoxProp,new Insets(0,50,0,100));
        hBoxProp.setSpacing(10);

        VBox vBoxTableView = new VBox();
        setMargin(vBoxTableView,new Insets(50,50,50,80));
        vBoxTableView.setSpacing(20);
        vBoxTableView.getChildren().addAll(tableViewProp, tableViewDocente);

        VBox vBoxCenter = new VBox();
        setMargin(vBoxCenter,new Insets(50,50,50,80));
        vBoxCenter.setSpacing(20);
        vBoxCenter.getChildren().addAll(hBoxNumber,hBoxProp,vBoxTableView);

        this.setCenter(vBoxCenter);

        VBox vBoxButtonBottomAdd = new VBox(btDocente,btProp,btAdd);
        setMargin(vBoxButtonBottomAdd,new Insets(50,50,10,50));
        vBoxButtonBottomAdd.setSpacing(10);
        vBoxButtonBottomAdd.setAlignment(Pos.BOTTOM_RIGHT);

        this.setRight(vBoxButtonBottomAdd);
    }

    private void registerHandlers() {
        btAdd.addEventFilter(ActionEvent.ACTION, actionEvent -> {
            setColor();
            prop = String.valueOf(tProp.getText());
            email = String.valueOf(tDocente.getText());

            boolean erro = erroPreencher(prop, email);

            if(!erro){
                tratamentoErros(manager.addManOrientadores(email,prop));
            }else
                ToastMessage.show(getScene().getWindow(),"Faltam parametros");
        });
        btProp.setOnAction(actionEvent -> {
            prop = String.valueOf(tProp.getText());
            update();
        });
        btDocente.setOnAction(actionEvent -> {
            email = String.valueOf(tDocente.getText());
            update();
        });
    }
    private void setColor(){
        String color = "-fx-control-inner-backgorund: #ffffff;";
        tProp.setStyle(color);
    }

    private boolean erroPreencher(String number, String prop) {
        boolean erro = false;
        if(number.isBlank()) {
            tProp.setStyle(backgorundColorError);
            erro = true;
        }
        if(prop.isBlank()) {
            tDocente.setStyle(backgorundColorError);
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
            case PROPOSTA_EDIT_NEXISTE,ORIENTADORES_ATPROPOSTA_NEXISTE -> {
                ToastMessage.show(getScene().getWindow(),"Código incorreto");
                tProp.setStyle(backgorundColorError);
                tProp.requestFocus();
            }
            case ORIENTADORES_ADD_T2 -> {
                ToastMessage.show(getScene().getWindow(),"Impossível adicionar um projeto a um orientador diferente (usar a atribuição automática)");
                tProp.setStyle(backgorundColorError);
                tProp.requestFocus();
            }
            case ORIENTADORES_EXISTE -> {
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
        tableViewProp.getItems().clear();
        Proposta proposta = manager.getProp(prop);
        if(proposta != null) {
            tableViewProp.getItems().add(proposta);
        }
        tableViewDocente.getItems().clear();
        Docentes docente = manager.getDocenteEmail(email);
        if(docente != null) {
            tableViewDocente.getItems().add(docente);

        }

    }
}
