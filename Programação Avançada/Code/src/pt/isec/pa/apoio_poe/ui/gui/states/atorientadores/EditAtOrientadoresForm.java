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
import pt.isec.pa.apoio_poe.model.data.Docentes;
import pt.isec.pa.apoio_poe.model.data.Erros;
import pt.isec.pa.apoio_poe.model.data.Proposta;
import pt.isec.pa.apoio_poe.ui.gui.resources.FontManager;
import pt.isec.pa.apoio_poe.ui.gui.states.docentes.TableViewDocentes;
import pt.isec.pa.apoio_poe.ui.gui.states.propostas.TableViewPropostas;
import pt.isec.pa.apoio_poe.ui.gui.states.utils.ToastMessage;

public class EditAtOrientadoresForm extends BorderPane {
    private Stage stage;
    private Manager manager;
    private Label lbNameWindow,lbDocente,lbProp;
    private TextField tDocente,tProp;
    private Button btEdit;
    private String backgorundColorError = "-fx-control-inner-background: #ff0000;";
    private String email,prop;

    public EditAtOrientadoresForm(Stage stage, Manager manager) {
        this.manager = manager;
        this.stage = stage;
        initializeVariables();
        createViews();
        registerHandlers();
        update();
    }

    private void initializeVariables() {

        lbNameWindow = new Label("Editar Orientação");
        lbNameWindow.setFont(FontManager.loadFont("BalsamiqSans-Regular.ttf",34));
        setMargin(lbNameWindow,new Insets(30,0,0,50));

        lbDocente = new Label("Novo docente: ");
        lbProp = new Label("Código: ");


        tDocente = new TextField();
        tDocente.setPromptText("Insira o e-mail do docente");
        tDocente.setMinWidth(180);
        tProp = new TextField();
        tProp.setPromptText("Insira o código da proposta");
        tProp.setMinWidth(180);

        btEdit = new Button("Editar");
        btEdit.setDefaultButton(true);

    }

    private void createViews() {

        this.setTop(lbNameWindow);


        HBox hBoxNumber = new HBox(lbDocente, tDocente);
        setMargin(hBoxNumber,new Insets(0,50,0,100));
        hBoxNumber.setSpacing(10);

        HBox hBoxProp = new HBox(lbProp,tProp);
        setMargin(hBoxProp,new Insets(0,50,0,100));
        hBoxProp.setSpacing(10);


        VBox vBoxCenter = new VBox();
        setMargin(vBoxCenter,new Insets(50,50,50,80));
        vBoxCenter.setSpacing(20);
        vBoxCenter.getChildren().addAll(hBoxProp,hBoxNumber);

        this.setCenter(vBoxCenter);

        VBox vBoxButtonBottomAdd = new VBox(btEdit);
        setMargin(vBoxButtonBottomAdd,new Insets(50,50,10,50));
        vBoxButtonBottomAdd.setSpacing(10);
        vBoxButtonBottomAdd.setAlignment(Pos.BOTTOM_RIGHT);

        this.setRight(vBoxButtonBottomAdd);
    }

    private void registerHandlers() {
        btEdit.addEventFilter(ActionEvent.ACTION, actionEvent -> {
            setColor();

            email = String.valueOf(tDocente.getText());
            prop = String.valueOf(tProp.getText());

            boolean erro = erroPreencher(email,prop);

            if(!erro){
                tratamentoErros(manager.editAtOrientadores(prop,email));
            }else
                ToastMessage.show(getScene().getWindow(),"Faltam parametros");
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
            case ORIENTADORES_EDIT_PROPOSTA -> {
                ToastMessage.show(getScene().getWindow(),"Código incorreto");
                tProp.setStyle(backgorundColorError);
                tProp.requestFocus();
            }
            case ORIENTADORES_EDIT_T2 -> {
                ToastMessage.show(getScene().getWindow(),"Impossível atribuir um projeto a um orientador diferente");
                tProp.setStyle(backgorundColorError);
                tProp.requestFocus();
            }
            case DOCENTE_EMAIL -> {
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
    }
}
