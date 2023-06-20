package pt.isec.pa.apoio_poe.ui.gui.states.propostas;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import pt.isec.pa.apoio_poe.model.Manager;
import pt.isec.pa.apoio_poe.model.data.Erros;
import pt.isec.pa.apoio_poe.model.data.Proposta;
import pt.isec.pa.apoio_poe.ui.gui.resources.FontManager;
import pt.isec.pa.apoio_poe.ui.gui.states.utils.ToastMessage;

public class RemovePropostasForm extends BorderPane {

    Manager manager;
    Stage stage;
    private Label lbNameWindow, lbCodigo;
    private TextField tCodigo;
    private Button btRemove,btProposta;
    private TableView<Proposta> tableView;
    private String backgorundColorError = "-fx-control-inner-background: #ff0000;";

    Font fontTitulo = FontManager.loadFont("BalsamiqSans-Regular.ttf",34);
    private String codigo;

    public RemovePropostasForm(Stage stage, Manager manager) {
        this.stage = stage;
        this.manager = manager;
        initializeVariables();
        createViews();
        registerHandlers();
        update();
    }

    private void initializeVariables() {

        lbNameWindow = new Label("Remover Propostas");
        lbNameWindow.setFont(fontTitulo);
        setMargin(lbNameWindow,new Insets(30,0,0,50));

        lbCodigo = new Label("Código: ");

        tCodigo = new TextField();
        tCodigo.setPromptText("Insira o código da propostas");
        tCodigo.setMinWidth(180);

        tableView = TableViewPropostas.createTableView();

        btProposta = new Button("Ver Proposta");
        btRemove = new Button("Remover");
        btRemove.setDefaultButton(true);
    }

    private void createViews() {

        this.setTop(lbNameWindow);

        HBox hBoxCodigo = new HBox(lbCodigo,tCodigo);
        setMargin(hBoxCodigo,new Insets(0,50,0,50));
        hBoxCodigo.setSpacing(10);

        VBox vBoxCenter = new VBox();
        setMargin(vBoxCenter,new Insets(50,50,50,80));
        vBoxCenter.setSpacing(20);
        vBoxCenter.getChildren().addAll(hBoxCodigo);

        this.setCenter(vBoxCenter);

        VBox vBoxButtonBottomAdd = new VBox(btProposta,btRemove);
        setMargin(vBoxButtonBottomAdd,new Insets(0,50,10,50));
        vBoxButtonBottomAdd.setAlignment(Pos.BOTTOM_RIGHT);

        this.setRight(vBoxButtonBottomAdd);

        this.setBottom(tableView);
    }

    private void registerHandlers() {
        btProposta.setOnAction(actionEvent -> {
            codigo = String.valueOf(tCodigo.getText());
            update();
        });
        btRemove.addEventFilter(ActionEvent.ACTION, actionEvent -> {
            setColor();
            codigo = String.valueOf(tCodigo.getText());
            if(!erroPreencher(codigo)){
                tratamentoErros(manager.removeProposta(codigo));
            }else{
                ToastMessage.show(getScene().getWindow(),"Faltam parametros");
            }
        });
    }

    private void setColor(){
        String color = "-fx-control-inner-backgorund: #ffffff;";
        tCodigo.setStyle(color);
    }

    private boolean erroPreencher(String codigo) {
        boolean erro = false;
        if(codigo.isBlank()) {
            tCodigo.setStyle(backgorundColorError);
            erro = true;
        }
        return erro;
    }

    private void tratamentoErros(Erros erros){
        System.out.println(erros);
        switch (erros){
            case PROPOSTA_REMOVE -> {
                ToastMessage.show(getScene().getWindow(),"Impossível remover a proposta");
                tCodigo.setStyle(backgorundColorError);
                tCodigo.requestFocus();
            }
            case NONE -> {
                ToastMessage.show(getScene().getWindow(),"Operação realizada com sucesso");
                stage.close();
            }
        }
    }

    private void update() {
        tableView.getItems().clear();
        Proposta propostas = manager.getProp(codigo);
        tableView.getItems().add(propostas);
    }
}
