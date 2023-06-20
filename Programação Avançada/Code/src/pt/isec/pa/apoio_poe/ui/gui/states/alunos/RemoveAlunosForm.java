package pt.isec.pa.apoio_poe.ui.gui.states.alunos;

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
import pt.isec.pa.apoio_poe.ui.gui.resources.FontManager;
import pt.isec.pa.apoio_poe.ui.gui.states.utils.ToastMessage;

import java.util.ArrayList;
import java.util.List;

public class RemoveAlunosForm extends BorderPane {

    Manager manager;
    Stage stage;
    private Label lbNameWindow,lbNumber;
    private TextField tNumber;
    private Button btRemove,btAluno;

    TableView<Alunos> tableView;
    private String backgorundColorError = "-fx-control-inner-background: #ff0000;";

    private String number;

    long nAluno;

    public RemoveAlunosForm(Stage stage,Manager manager) {
        this.stage = stage;
        this.manager = manager;
        initializeVariables();
        createViews();
        registerHandlers();
        update();
    }

    private void initializeVariables() {

        lbNameWindow = new Label("Remover Aluno");
        lbNameWindow.setFont(FontManager.loadFont("BalsamiqSans-Regular.ttf",34));
        setMargin(lbNameWindow,new Insets(30,0,0,50));

        lbNumber = new Label("Número: ");
        tNumber = new TextField();
        tNumber.setPromptText("Insira o número do aluno");
        tNumber.setMinWidth(180);

        tableView = TableViewAlunos.createTableView();

        btRemove = new Button("Remover");
        btRemove.setDefaultButton(true);
        btAluno = new Button("Ver aluno");
    }

    private void createViews() {

        this.setTop(lbNameWindow);

        HBox hBoxNumber = new HBox(lbNumber,tNumber);
        setMargin(hBoxNumber,new Insets(0,50,0,100));
        hBoxNumber.setSpacing(10);
        hBoxNumber.setAlignment(Pos.CENTER);

        VBox vBoxButtonBottomRemove = new VBox(btAluno,btRemove);
        setMargin(vBoxButtonBottomRemove,new Insets(0,50,10,50));
        vBoxButtonBottomRemove.setAlignment(Pos.BOTTOM_RIGHT);
        vBoxButtonBottomRemove.setSpacing(10);

        this.setCenter(hBoxNumber);
        this.setRight(vBoxButtonBottomRemove);
        setMargin(tableView,new Insets(0,50,10,50));
        this.setBottom(tableView);
    }

    private void registerHandlers() {
        btAluno.setOnAction(actionEvent -> {
            number = String.valueOf(tNumber.getText());
            nAluno = erroPreencher(number);
            update();
        });
        btRemove.addEventFilter(ActionEvent.ACTION, actionEvent -> {
            setColor();

            number = String.valueOf(tNumber.getText());

            nAluno = erroPreencher(number);

            if(nAluno != -1) {
                tratamentoErros(manager.removeAlunos(nAluno));
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
            case VALOR_INCORRETO -> {
                tNumber.setStyle(backgorundColorError);
                tNumber.requestFocus();
                ToastMessage.show(getScene().getWindow(),"Valor incorreto");
            }
            case ALUNO_REMOVE ->{
                tNumber.setStyle(backgorundColorError);
                tNumber.requestFocus();
                ToastMessage.show(getScene().getWindow(),"Não foi possível remover o aluno");
            }
            case NONE -> {
                ToastMessage.show(getScene().getWindow(),"Operação realizada com sucesso");
                stage.close();
            }
        }
    }

    private void update() {
        tableView.getItems().clear();
        Alunos alunos = manager.getAlunoNumber(nAluno);
        if(alunos != null) {
            tableView.getItems().add(alunos);
        }
    }
}
