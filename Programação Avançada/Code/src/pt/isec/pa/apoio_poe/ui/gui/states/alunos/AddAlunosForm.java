package pt.isec.pa.apoio_poe.ui.gui.states.alunos;

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

public class AddAlunosForm extends BorderPane {

    Manager manager;
    Stage stage;
    private Label lbNameWindow,lbNumber,lbName,lbEmail,lbCourse,lbBranch,lbClassification,lbApt;
    private TextField tNumber,tName,tEmail,tClassification;
    private ChoiceBox<String> cbCourse,cbBranch;
    private RadioButton rbYes,rbNo;
    private Button btAdd;
    private String [] courseType = {"LEI","LEI-PL"};
    private String [] branchType = {"DA","SI","RAS"};
    private String backgorundColorError = "-fx-control-inner-background: #ff0000;";
    private String number,name,email,course,branch,classification,apt;

    public AddAlunosForm(Stage stage,Manager manager) {
        this.stage = stage;
        this.manager = manager;
        initializeVariables();
        createViews();
        registerHandlers();
        update();
    }

    private void initializeVariables() {

        lbNameWindow = new Label("Adicionar Aluno");
        lbNameWindow.setFont(FontManager.loadFont("BalsamiqSans-Regular.ttf",34));
        setMargin(lbNameWindow,new Insets(30,0,0,50));

        lbNumber = new Label("Número: ");
        lbName = new Label("Nome: ");
        lbEmail = new Label("E-mail: ");
        lbCourse = new Label("Curso: ");
        lbBranch = new Label("Ramo: ");
        lbClassification = new Label("Classificação: ");
        lbApt = new Label("Apto: ");

        tNumber = new TextField();
        tNumber.setPromptText("Insira o número do aluno");
        tNumber.setMinWidth(180);
        tName = new TextField();
        tName.setPromptText("Insira o nome do aluno");
        tName.setMinWidth(180);
        tEmail = new TextField();
        tEmail.setPromptText("Insira o e-mail do aluno");
        tEmail.setMinWidth(180);
        tClassification = new TextField();
        tClassification.setPromptText("Insira a classificação do aluno");
        tClassification.setMinWidth(180);

        cbCourse = new ChoiceBox<>();
        cbCourse.setPrefWidth(150);
        cbCourse.getItems().addAll(courseType);
        cbCourse.setValue("LEI");
        cbBranch = new ChoiceBox<>();
        cbBranch.setPrefWidth(150);
        cbBranch.setValue("DA");
        cbBranch.getItems().addAll(branchType);

        rbYes = new RadioButton("Sim");
        rbYes.setSelected(true);
        apt = "true";
        rbNo = new RadioButton("Não");

        btAdd = new Button("Adicionar");
        btAdd.setDefaultButton(true);
    }

    private void createViews() {

        this.setTop(lbNameWindow);

        HBox hBoxNumber = new HBox(lbNumber,tNumber);
        setMargin(hBoxNumber,new Insets(0,50,0,100));
        hBoxNumber.setSpacing(10);

        HBox hBoxName = new HBox(lbName,tName);
        setMargin(hBoxName,new Insets(0,50,0,100));
        hBoxName.setSpacing(10);

        HBox hBoxEmail = new HBox(lbEmail,tEmail);
        setMargin(hBoxEmail,new Insets(0,50,0,100));
        hBoxEmail.setSpacing(10);

        HBox hBoxCourse = new HBox(lbCourse,cbCourse);
        setMargin(hBoxCourse,new Insets(0,50,0,100));
        hBoxCourse.setSpacing(10);

        HBox hBoxBranch = new HBox(lbBranch,cbBranch);
        setMargin(hBoxBranch,new Insets(0,50,0,100));
        hBoxBranch.setSpacing(10);

        HBox hBoxClassification = new HBox(lbClassification,tClassification);
        setMargin(hBoxClassification,new Insets(0,50,0,100));
        hBoxClassification.setSpacing(10);

        HBox hBoxApt = new HBox(lbApt,rbYes,rbNo);
        setMargin(hBoxApt,new Insets(0,50,0,100));
        hBoxApt.setSpacing(10);

        VBox vBoxCenter = new VBox();
        setMargin(vBoxCenter,new Insets(50,50,50,80));
        vBoxCenter.setSpacing(20);
        vBoxCenter.getChildren().addAll(hBoxNumber,hBoxName,hBoxEmail,hBoxCourse,hBoxBranch,hBoxClassification,hBoxApt);

        this.setCenter(vBoxCenter);

        VBox vBoxButtonBottomAdd = new VBox(btAdd);
        setMargin(vBoxButtonBottomAdd,new Insets(0,50,10,50));
        vBoxButtonBottomAdd.setAlignment(Pos.BOTTOM_RIGHT);

        this.setRight(vBoxButtonBottomAdd);
    }

    private void registerHandlers() {
        rbYes.setOnAction(actionEvent -> {
            apt = "true";
            rbNo.setSelected(false);
        });
        rbNo.setOnAction(actionEvent -> {
            apt = "false";
            rbYes.setSelected(false);
        });
        btAdd.addEventFilter(ActionEvent.ACTION,actionEvent -> {

            setColor();

            number = String.valueOf(tNumber.getText());
            name = String.valueOf(tName.getText());
            email = String.valueOf(tEmail.getText());
            course = String.valueOf(cbCourse.getValue());
            branch = String.valueOf(cbBranch.getValue());
            classification = String.valueOf(tClassification.getText());

            boolean erro = erroPreencher(number,name,email,classification);

            if(!erro) {
                List<String> aluno = new ArrayList<>();
                aluno.add(number);
                aluno.add(name);
                aluno.add(email);
                aluno.add(course);
                aluno.add(branch);
                aluno.add(classification);
                aluno.add(apt);
                tratamentoErros(manager.addAlunos(aluno));
            }else
                ToastMessage.show(getScene().getWindow(),"Faltam parametros");
        });
    }

    private void setColor(){
        String color = "-fx-control-inner-backgorund: #ffffff;";
        tClassification.setStyle(color);
        tName.setStyle(color);
        tNumber.setStyle(color);
        tEmail.setStyle(color);
    }

    private boolean erroPreencher(String number, String name, String email, String classification) {
        boolean erro = false;
        if(number.isBlank()) {
            tNumber.setStyle(backgorundColorError);
            erro = true;
        }
        if(name.isBlank()) {
            tName.setStyle(backgorundColorError);
            erro = true;
        }
        if(email.isBlank()) {
            tEmail.setStyle(backgorundColorError);
            erro = true;
        }
        if(classification.isBlank()) {
            tClassification.setStyle(backgorundColorError);
            erro = true;
        }
        return erro;
    }

    private void tratamentoErros(Erros erros){
        System.out.println(erros);
        switch (erros){
            case ALUNO_CLASSIFICACAO -> {
                ToastMessage.show(getScene().getWindow(),"Classificação incorreta");
                tClassification.setStyle(backgorundColorError);
                tClassification.requestFocus();
            }
            case ALUNO_ID -> {
                ToastMessage.show(getScene().getWindow(),"Número incorreto ou já existe");
                tNumber.setStyle(backgorundColorError);
                tNumber.requestFocus();
            }
            case ALUNO_EMAIL ->{
                ToastMessage.show(getScene().getWindow(),"E-mail incorreto");
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
