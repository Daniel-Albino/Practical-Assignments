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

public class EditaDocentesForm extends BorderPane {
    Manager manager;
    Stage stage;
    TableView<Docentes> tableView;
    private Label lbNameWindow, lbEmail,lbName;
    private TextField tEmail,tName;
    private Button btEdit, btDocente;
    private String backgorundColorError = "-fx-control-inner-background: #ff0000;";
    private String email,name;

    public EditaDocentesForm(Stage stage, Manager manager) {
        this.stage = stage;
        this.manager = manager;
        initializeVariables();
        createViews();
        registerHandlers();
        update();
    }

    private void initializeVariables() {

        lbNameWindow = new Label("Edita Docente");
        lbNameWindow.setFont(FontManager.loadFont("BalsamiqSans-Regular.ttf",34));
        setMargin(lbNameWindow,new Insets(30,0,0,50));


        lbEmail = new Label("Email: ");
        lbName = new Label("Nome: ");

        tEmail = new TextField();
        tEmail.setPromptText("Insira o email do docente");
        tEmail.setMinWidth(180);
        tName = new TextField();
        tName.setPromptText("Insira o nome do docente");
        tName.setMinWidth(180);


        btDocente = new Button("Ver docente");
        btEdit = new Button("Editar");
        btEdit.setDefaultButton(true);

        tableView = TableViewDocentes.createTableView();
        tableView.setMaxWidth(600);
        tableView.setMaxHeight(100);
    }

    private void createViews() {

        this.setTop(lbNameWindow);


        HBox hBoxEmail = new HBox(lbEmail, tEmail);
        setMargin(hBoxEmail,new Insets(0,50,0,100));
        hBoxEmail.setSpacing(10);

        HBox hBoxName = new HBox(lbName,tName);
        setMargin(hBoxName,new Insets(0,50,0,100));
        hBoxName.setSpacing(10);


        VBox vBoxCenter = new VBox();
        setMargin(vBoxCenter,new Insets(50,50,50,80));
        vBoxCenter.setSpacing(20);
        vBoxCenter.getChildren().addAll(hBoxEmail,hBoxName);

        this.setCenter(vBoxCenter);


        VBox vBoxButtonBottom = new VBox(btDocente,btEdit);
        setMargin(vBoxButtonBottom,new Insets(0,50,10,50));
        vBoxButtonBottom.setAlignment(Pos.BOTTOM_RIGHT);
        vBoxButtonBottom.setSpacing(10);

        this.setRight(vBoxButtonBottom);
        setMargin(tableView,new Insets(0,50,10,50));
        this.setBottom(tableView);
    }

    private void registerHandlers() {
        btDocente.setOnAction(actionEvent -> {
            email = String.valueOf(tEmail.getText());
            update();
        });

        btEdit.addEventFilter(ActionEvent.ACTION, actionEvent -> {

            email = String.valueOf(tEmail.getText());
            name = String.valueOf(tName.getText());

            if(!erroPreencher(email,name)) {
                tratamentoErros(manager.setNomeDocente(email,name));
            }else
                ToastMessage.show(getScene().getWindow(),"Faltam parametros ou estão incorretos");
        });

    }

    private void setColor(){
        String color = "-fx-control-inner-backgorund: #ffffff;";
        tName.setStyle(color);
        tEmail.setStyle(color);
    }

    private boolean erroPreencher(String email, String name) {
        boolean erro = false;

        if(email.isBlank()) {
            tEmail.setStyle(backgorundColorError);
            erro = true;
        }
        if (name.isBlank()) {
            tName.setStyle(backgorundColorError);
            erro = true;
        }

        return erro;
    }

    private void tratamentoErros(Erros erros){
        System.out.println(erros);
        switch (erros){
            case DOCENTE_EMAIL ->{
                ToastMessage.show(getScene().getWindow(),"Email incorreto");
                tEmail.setStyle(backgorundColorError);
                tEmail.requestFocus();
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
