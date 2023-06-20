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
import pt.isec.pa.apoio_poe.model.data.Erros;
import pt.isec.pa.apoio_poe.ui.gui.resources.FontManager;
import pt.isec.pa.apoio_poe.ui.gui.states.utils.ToastMessage;

import java.util.ArrayList;
import java.util.List;

public class AddDocentesForm extends BorderPane {

    Manager manager;
    Stage stage;
    private Label lbNameWindow,lbName,lbEmail;
    private TextField tName,tEmail;
    private Button btAdd;
    private String backgorundColorError = "-fx-control-inner-background: #ff0000;";

    private String number,name,email;

    public AddDocentesForm(Stage stage, Manager manager) {
        this.stage = stage;
        this.manager = manager;
        initializeVariables();
        createViews();
        registerHandlers();
        update();
    }

    private void initializeVariables() {

        lbNameWindow = new Label("Adicionar Docente");
        lbNameWindow.setFont(FontManager.loadFont("BalsamiqSans-Regular.ttf",34));
        setMargin(lbNameWindow,new Insets(30,0,0,50));

        lbName = new Label("Nome: ");
        lbEmail = new Label("E-mail: ");

        tName = new TextField();
        tName.setPromptText("Insira o nome do aluno");
        tName.setMinWidth(180);
        tEmail = new TextField();
        tEmail.setPromptText("Insira o e-mail do aluno");
        tEmail.setMinWidth(180);


        btAdd = new Button("Adicionar");
        btAdd.setDefaultButton(true);
    }

    private void createViews() {

        this.setTop(lbNameWindow);

        HBox hBoxName = new HBox(lbName,tName);
        setMargin(hBoxName,new Insets(0,50,0,100));
        hBoxName.setSpacing(10);
        hBoxName.setAlignment(Pos.CENTER);

        HBox hBoxEmail = new HBox(lbEmail,tEmail);
        setMargin(hBoxEmail,new Insets(0,50,0,100));
        hBoxEmail.setSpacing(10);
        hBoxEmail.setAlignment(Pos.CENTER);

        VBox vBoxCenter = new VBox();
        setMargin(vBoxCenter,new Insets(50,50,50,50));
        vBoxCenter.setSpacing(20);
        vBoxCenter.getChildren().addAll(hBoxName,hBoxEmail);

        this.setCenter(vBoxCenter);

        VBox vBoxButtonBottomAdd = new VBox(btAdd);
        setMargin(vBoxButtonBottomAdd,new Insets(0,50,10,50));
        vBoxButtonBottomAdd.setAlignment(Pos.BOTTOM_RIGHT);

        this.setRight(vBoxButtonBottomAdd);
    }

    private void registerHandlers() {
        btAdd.addEventFilter(ActionEvent.ACTION,actionEvent -> {
            setColor();

            boolean erro = erroPreencher(name,email);

            if(!erro) {
                List<String> docente = new ArrayList<>();
                docente.add(name);
                docente.add(email);
                tratamentoErros(manager.addDocente(docente));
            }else
                ToastMessage.show(getScene().getWindow(),"Faltam parametros");
        });
    }

    private void setColor(){
        String color = "-fx-control-inner-backgorund: #ffffff;";
        tEmail.setStyle(color);
        tName.setStyle(color);
        name = String.valueOf(tName.getText());
        email = String.valueOf(tEmail.getText());
    }
    private boolean erroPreencher(String name, String email) {
        boolean erro = false;
        if(name.isBlank()) {
            tName.setStyle(backgorundColorError);
            erro = true;
        }
        if(email.isBlank()) {
            tEmail.setStyle(backgorundColorError);
            erro = true;
        }
        return erro;
    }

    private void tratamentoErros(Erros erros){
        System.out.println(erros);
        switch (erros){
            case DOCENTE_EMAIL -> {
                ToastMessage.show(getScene().getWindow(),"E-mail incorreto ou já existe");
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

    }
}
