package pt.isec.pa.apoio_poe.ui.gui.states.docentes;

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
import pt.isec.pa.apoio_poe.ui.gui.resources.FontManager;
import pt.isec.pa.apoio_poe.ui.gui.states.alunos.TableViewAlunos;
import pt.isec.pa.apoio_poe.ui.gui.states.utils.ToastMessage;

public class RemoveDocentesForm extends BorderPane {

    Manager manager;
    Stage stage;
    private Label lbNameWindow, lbEmail;
    private TextField tEmail;
    private Button btRemove, btDocente;

    TableView<Docentes> tableView;
    private String backgorundColorError = "-fx-control-inner-background: #ff0000;";

    private String email;

    public RemoveDocentesForm(Stage stage,Manager manager) {
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

        lbEmail = new Label("Email: ");
        tEmail = new TextField();
        tEmail.setPromptText("Insira o número do aluno");
        tEmail.setMinWidth(180);

        tableView = TableViewDocentes.createTableView();

        btRemove = new Button("Remover");
        btRemove.setDefaultButton(true);
        btDocente = new Button("Ver docente");
    }

    private void createViews() {

        this.setTop(lbNameWindow);

        HBox hBoxNumber = new HBox(lbEmail, tEmail);
        setMargin(hBoxNumber,new Insets(0,50,0,100));
        hBoxNumber.setSpacing(10);
        hBoxNumber.setAlignment(Pos.CENTER);

        VBox vBoxButtonBottomRemove = new VBox(btDocente,btRemove);
        setMargin(vBoxButtonBottomRemove,new Insets(0,50,10,50));
        vBoxButtonBottomRemove.setAlignment(Pos.BOTTOM_RIGHT);
        vBoxButtonBottomRemove.setSpacing(10);

        this.setCenter(hBoxNumber);
        this.setRight(vBoxButtonBottomRemove);
        setMargin(tableView,new Insets(0,50,10,50));
        this.setBottom(tableView);
    }

    private void registerHandlers() {
        btDocente.setOnAction(actionEvent -> {
            email = String.valueOf(tEmail.getText());
            update();
        });
        btRemove.addEventFilter(ActionEvent.ACTION, actionEvent -> {
            setColor();
            email = String.valueOf(tEmail.getText());

            boolean erro = erroPreencher(email);

            if(!erro) {
                tratamentoErros(manager.removeDocente(email));
            }else
                ToastMessage.show(getScene().getWindow(),"Faltam parametros");
        });
    }

    private void setColor(){
        String color = "-fx-control-inner-backgorund: #ffffff;";
        tEmail.setStyle(color);
    }
    private boolean erroPreencher(String email) {
        if(email.isBlank())
            return true;
        return false;
    }

    private void tratamentoErros(Erros erros){
        System.out.println(erros);
        switch (erros){
            case VALOR_INCORRETO -> {
                tEmail.setStyle(backgorundColorError);
                tEmail.requestFocus();
                ToastMessage.show(getScene().getWindow(),"Valor incorreto");
            }
            case ALUNO_REMOVE ->{
                tEmail.setStyle(backgorundColorError);
                tEmail.requestFocus();
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
        Docentes docentes = manager.getDocenteEmail(email);
        if(docentes != null) {
            tableView.getItems().add(docentes);
        }
    }
}

