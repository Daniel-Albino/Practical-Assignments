package pt.isec.pa.apoio_poe.ui.gui.states.candidaturas;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import pt.isec.pa.apoio_poe.model.Manager;
import pt.isec.pa.apoio_poe.model.data.Erros;
import pt.isec.pa.apoio_poe.ui.gui.resources.FontManager;
import pt.isec.pa.apoio_poe.ui.gui.states.utils.ToastMessage;

import java.util.ArrayList;
import java.util.List;

public class RemoveCandidaturasForm extends BorderPane {

    Manager manager;
    Stage stage;
    private Label lbNameWindow,lbNumber;
    private TextField tNumber;
    private Button btRemove, btVerCandidatura;
    private String backgorundColorError = "-fx-control-inner-background: #ff0000;";
    private String number;
    private long nAluno;

    private ListView<String> listView;

    public RemoveCandidaturasForm(Stage stage,Manager manager) {
        this.stage = stage;
        this.manager = manager;
        initializeVariables();
        createViews();
        registerHandlers();
        update();
    }

    private void initializeVariables() {

        lbNameWindow = new Label("Remover Candidatura");
        lbNameWindow.setFont(FontManager.loadFont("BalsamiqSans-Regular.ttf",34));
        setMargin(lbNameWindow,new Insets(30,0,0,50));

        lbNumber = new Label("Número: ");

        tNumber = new TextField();
        tNumber.setPromptText("Insira o número do aluno");
        tNumber.setMinWidth(180);

        btRemove = new Button("Remover");
        btRemove.setDefaultButton(true);
        btVerCandidatura = new Button("Ver candidatura");

        listView = new ListView<>();
        listView.setStyle("-fx-font-family: Arial; -fx-font-size: 16;");
        listView.setMaxWidth(600);
        listView.setMaxHeight(100);
        setMargin(listView,new Insets(10,10,10,10));
    }

    private void createViews() {

        this.setTop(lbNameWindow);

        HBox hBoxNumber = new HBox(lbNumber,tNumber);
        setMargin(hBoxNumber,new Insets(0,50,0,100));
        hBoxNumber.setSpacing(10);

        VBox vBoxCenter = new VBox();
        setMargin(vBoxCenter,new Insets(50,50,50,80));
        vBoxCenter.setSpacing(20);
        vBoxCenter.getChildren().addAll(hBoxNumber);

        this.setCenter(vBoxCenter);

        VBox vBoxButtonBottomAdd = new VBox(btVerCandidatura, btRemove);
        setMargin(vBoxButtonBottomAdd,new Insets(0,50,10,50));
        vBoxButtonBottomAdd.setAlignment(Pos.BOTTOM_RIGHT);
        vBoxButtonBottomAdd.setSpacing(10);

        this.setRight(vBoxButtonBottomAdd);
        this.setBottom(listView);
    }

    private void registerHandlers() {
        btVerCandidatura.addEventFilter(ActionEvent.ACTION,actionEvent ->{
            number = String.valueOf(tNumber.getText());
            try {
                nAluno = Long.parseLong(number);
            } catch (Exception e) {
                nAluno = -1;
            }
            update();
        });
        btRemove.addEventFilter(ActionEvent.ACTION, actionEvent -> {
            setColor();
            number = String.valueOf(tNumber.getText());
            nAluno = erroPreencher(number);

            if(nAluno != -1) {
                tratamentoErros(manager.removeCandidatura(nAluno));
            }
        });
    }

    private void setColor(){
        String color = "-fx-control-inner-backgorund: #ffffff;";
        tNumber.setStyle(color);
    }

    private long erroPreencher(String number) {
        long numeroAluno;
        if(number.isBlank()) {
            tNumber.setStyle(backgorundColorError);
            numeroAluno = -1;
        }else{
            try {
                numeroAluno = Long.parseLong(number);
            } catch (Exception e) {
                numeroAluno = -1;
            }
        }

        if(numeroAluno == -1)
            ToastMessage.show(getScene().getWindow(),"Número Incorreto");

        return numeroAluno;
    }

    private void tratamentoErros(Erros erros){
        System.out.println(erros);
        switch (erros){

            case NONE -> stage.close();
        }
    }

    private void update() {
        listView.getItems().clear();
        String list = manager.getCandidaturasAlunoList(nAluno);
        if(list != null) {
            listView.getItems().add(list);
        }
    }
}

