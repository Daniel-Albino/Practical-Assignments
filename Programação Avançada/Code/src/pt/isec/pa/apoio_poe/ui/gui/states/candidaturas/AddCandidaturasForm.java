package pt.isec.pa.apoio_poe.ui.gui.states.candidaturas;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import pt.isec.pa.apoio_poe.model.Manager;
import pt.isec.pa.apoio_poe.model.data.Erros;
import pt.isec.pa.apoio_poe.ui.gui.resources.FontManager;
import pt.isec.pa.apoio_poe.ui.gui.states.utils.ToastMessage;

import java.util.ArrayList;
import java.util.List;

public class AddCandidaturasForm extends BorderPane {

    Manager manager;
    Stage stage;
    private Label lbNameWindow,lbNumber,lbCandidaturas;
    private TextField tNumber,tCandidaturas;
    private Button btAdd,btAddCandidatura;
    private String backgorundColorError = "-fx-control-inner-background: #ff0000;";
    private String number;
    private List<String> candidaturas;

    public AddCandidaturasForm(Stage stage,Manager manager) {
        this.stage = stage;
        this.manager = manager;
        initializeVariables();
        createViews();
        registerHandlers();
        update();
    }

    private void initializeVariables() {

        lbNameWindow = new Label("Adicionar Candidaturas");
        lbNameWindow.setFont(FontManager.loadFont("BalsamiqSans-Regular.ttf",34));
        setMargin(lbNameWindow,new Insets(30,0,0,50));

        lbNumber = new Label("Número: ");
        lbCandidaturas = new Label("Candidaturas: ");

        tNumber = new TextField();
        tNumber.setPromptText("Insira o número do aluno");
        tNumber.setMinWidth(180);

        tCandidaturas = new TextField();
        tCandidaturas.setPromptText("Insira a candidatura");
        tCandidaturas.setMinWidth(180);

        btAdd = new Button("Adicionar");
        btAdd.setDefaultButton(true);
        btAddCandidatura = new Button("Adicionar candidatura");

        candidaturas = new ArrayList<>();
    }

    private void createViews() {

        this.setTop(lbNameWindow);

        HBox hBoxNumber = new HBox(lbNumber,tNumber);
        setMargin(hBoxNumber,new Insets(0,50,0,100));
        hBoxNumber.setSpacing(10);

        HBox hBoxCandidatura = new HBox(lbCandidaturas,tCandidaturas);
        setMargin(hBoxCandidatura,new Insets(0,50,0,100));
        hBoxNumber.setSpacing(10);


        VBox vBoxCenter = new VBox();
        setMargin(vBoxCenter,new Insets(50,50,50,80));
        vBoxCenter.setSpacing(20);
        vBoxCenter.getChildren().addAll(hBoxNumber,hBoxCandidatura);

        this.setCenter(vBoxCenter);

        VBox vBoxButtonBottomAdd = new VBox(btAddCandidatura,btAdd);
        setMargin(vBoxButtonBottomAdd,new Insets(0,50,10,50));
        vBoxButtonBottomAdd.setAlignment(Pos.BOTTOM_RIGHT);
        vBoxButtonBottomAdd.setSpacing(10);

        this.setRight(vBoxButtonBottomAdd);
    }

    private void registerHandlers() {
        btAddCandidatura.addEventFilter(ActionEvent.ACTION,actionEvent -> {
            if(String.valueOf(tCandidaturas.getText()).isBlank())
                tCandidaturas.setStyle(backgorundColorError);
            else{
                candidaturas.add(String.valueOf(tCandidaturas.getText()));
                tCandidaturas.clear();
            }
        });

        btAdd.addEventFilter(ActionEvent.ACTION,actionEvent -> {
            setColor();

            number = String.valueOf(tNumber.getText());
            if(!String.valueOf(tCandidaturas.getText()).isBlank())
                candidaturas.add(String.valueOf(tCandidaturas.getText()));

            boolean erro = erroPreencher(number);

            if(candidaturas.isEmpty()){
                tCandidaturas.setStyle(backgorundColorError);
                erro = true;
            }

            if(!erro) {
                List<String> cand = new ArrayList<>();
                cand.add(number);
                for(String s : candidaturas)
                    cand.add(s);

                tratamentoErros(manager.addCandidatura(cand));
            }else
                ToastMessage.show(getScene().getWindow(),"Faltam parametros");
        });
    }

    private void setColor(){
        String color = "-fx-control-inner-backgorund: #ffffff;";
        tNumber.setStyle(color);
        tCandidaturas.setStyle(color);
    }

    private boolean erroPreencher(String number) {
        boolean erro = false;
        if(number.isBlank()) {
            tNumber.setStyle(backgorundColorError);
            erro = true;
        }
        return erro;
    }

    private void tratamentoErros(Erros erros){
        System.out.println(erros);
        switch (erros){
            case CANDIDATURAS_ADD ->{
                tNumber.setStyle(backgorundColorError);
                tCandidaturas.setStyle(backgorundColorError);
                ToastMessage.show(getScene().getWindow(),"Impossível adicionar a candidatura");
            }
            case VALOR_INCORRETO, CANDIDATURAS_ADD_ALUNO,CANDIDATURAS_ADD_ALUNOEXISTE ->{
                tNumber.setStyle(backgorundColorError);
                ToastMessage.show(getScene().getWindow(),"Número incorreto");
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
