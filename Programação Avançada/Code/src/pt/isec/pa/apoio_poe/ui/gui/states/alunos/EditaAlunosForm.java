package pt.isec.pa.apoio_poe.ui.gui.states.alunos;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
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

public class EditaAlunosForm extends BorderPane {
    Manager manager;
    Stage stage;
    ChoiceBox<String> cbEditType;
    TableView<Alunos> tableView;
    private Label lbNameWindow,lbType,lbNumber,lbName,lbClassification,lbApt;
    private TextField tNumber,tName,tClassification;
    private Button btEdit,btAluno;
    private String backgorundColorError = "-fx-control-inner-background: #ff0000;";
    private String []tipoEdit = {"Nome","Classificação","Aptidão"};
    private String number,name,classification;
    private long nAluno;
    public EditaAlunosForm(Stage stage, Manager manager) {
        this.stage = stage;
        this.manager = manager;
        initializeVariables();
        createViews();
        registerHandlers();
        update();
    }

    private void initializeVariables() {

        lbNameWindow = new Label("Edita Aluno");
        lbNameWindow.setFont(FontManager.loadFont("BalsamiqSans-Regular.ttf",34));
        setMargin(lbNameWindow,new Insets(30,0,0,50));

        lbType = new Label("Tipo: ");

        cbEditType = new ChoiceBox<>();
        cbEditType.setPrefWidth(150);
        cbEditType.getItems().addAll(tipoEdit);
        cbEditType.setValue("Nome");

        lbNumber = new Label("Número: ");
        lbName = new Label("Nome: ");
        lbClassification = new Label("Classificação: ");
        lbApt = new Label("Apto: ");

        tNumber = new TextField();
        tNumber.setPromptText("Insira o número do aluno");
        tNumber.setMinWidth(180);
        tName = new TextField();
        tName.setPromptText("Insira o nome do aluno");
        tName.setMinWidth(180);

        tClassification = new TextField();
        tClassification.setPromptText("Insira a classificação do aluno");
        tClassification.setMinWidth(180);
        tClassification.setDisable(true);

        btAluno = new Button("Ver aluno");
        btEdit = new Button("Editar");
        btEdit.setDefaultButton(true);

        tableView = TableViewAlunos.createTableView();
        tableView.setMaxWidth(600);
        tableView.setMaxHeight(100);
    }

    private void createViews() {

        this.setTop(lbNameWindow);

        HBox hBoxType = new HBox(lbType,cbEditType);
        setMargin(hBoxType,new Insets(0,50,0,100));
        hBoxType.setSpacing(10);

        HBox hBoxNumber = new HBox(lbNumber,tNumber);
        setMargin(hBoxNumber,new Insets(0,50,0,100));
        hBoxNumber.setSpacing(10);

        HBox hBoxName = new HBox(lbName,tName);
        setMargin(hBoxName,new Insets(0,50,0,100));
        hBoxName.setSpacing(10);

        HBox hBoxClassification = new HBox(lbClassification,tClassification);
        setMargin(hBoxClassification,new Insets(0,50,0,100));
        hBoxClassification.setSpacing(10);

        VBox vBoxCenter = new VBox();
        setMargin(vBoxCenter,new Insets(50,50,50,80));
        vBoxCenter.setSpacing(20);
        vBoxCenter.getChildren().addAll(hBoxType,hBoxNumber,hBoxName,hBoxClassification);

        this.setCenter(vBoxCenter);


        VBox vBoxButtonBottom = new VBox(btAluno,btEdit);
        setMargin(vBoxButtonBottom,new Insets(0,50,10,50));
        vBoxButtonBottom.setAlignment(Pos.BOTTOM_RIGHT);
        vBoxButtonBottom.setSpacing(10);

        this.setRight(vBoxButtonBottom);
        setMargin(tableView,new Insets(0,50,10,50));
        this.setBottom(tableView);
    }

    private void registerHandlers() {
        cbEditType.addEventFilter(ActionEvent.ACTION,actionEvent -> {
            switch (String.valueOf(cbEditType.getValue())){
                case "Nome" -> {
                    tName.setDisable(false);
                    tClassification.setDisable(true);
                }
                case "Classificação" ->{
                    tName.setDisable(true);
                    tClassification.setDisable(false);
                }
                case "Aptidão" ->{
                    tName.setDisable(true);
                    tClassification.setDisable(true);
                }
            }
        });
        btAluno.setOnAction(actionEvent -> {
            try {
                nAluno = Long.parseLong(String.valueOf(tNumber.getText()));
            } catch (Exception e) {
                nAluno = -1;
            }
            update();
        });
        btEdit.addEventFilter(ActionEvent.ACTION, actionEvent -> {
            setColor();

            switch (String.valueOf(cbEditType.getValue())) {
                case "Nome" -> {
                    name = String.valueOf(tName.getText());
                }
                case "Classificação" -> {
                    classification = String.valueOf(tClassification.getText());
                }
            }

            number = String.valueOf(tNumber.getText());

            if(!erroPreencher(number,name,classification)) {
                if(String.valueOf(cbEditType.getValue()).equals("Nome"))
                    tratamentoErros(manager.setNomeAluno(nAluno,name));
                if(String.valueOf(cbEditType.getValue()).equals("Classificação"))
                    tratamentoErros(manager.setClassificacao(nAluno,classification));
                if(String.valueOf(cbEditType.getValue()).equals("Aptidão"))
                    tratamentoErros(manager.setAptoEstagio(nAluno));
            }else
                ToastMessage.show(getScene().getWindow(),"Faltam parametros ou estão incorretos");
        });
    }

    private void setColor(){
        String color = "-fx-control-inner-backgorund: #ffffff;";
        tClassification.setStyle(color);
        tNumber.setStyle(color);
        tName.setStyle(color);
    }

    private boolean erroPreencher(String number, String name, String classification) {
        boolean erro = false;

        if(number.isBlank()) {
            tNumber.setStyle(backgorundColorError);
            nAluno = -1;
        }else{
            try {
                nAluno = Long.parseLong(number);
            } catch (Exception e) {
                nAluno = -1;
            }
        }
        if(nAluno == -1)
            erro = true;
        if(String.valueOf(cbEditType.getValue()).equals("Nome")) {
            if (name.isBlank()) {
                tName.setStyle(backgorundColorError);
                erro = true;
            }
        }
        if(String.valueOf(cbEditType.getValue()).equals("Classificação")) {
            if (classification.isBlank()) {
                tClassification.setStyle(backgorundColorError);
                erro = true;
            }
        }

        return erro;
    }

    private void tratamentoErros(Erros erros){
        System.out.println(erros);
        switch (erros){
            case ALUNO_EDIT_CLASSIFICAO -> {
                ToastMessage.show(getScene().getWindow(),"Classificação incorreta");
                tClassification.setStyle(backgorundColorError);
                tClassification.requestFocus();
            }
            case ALUNO_ID -> {
                ToastMessage.show(getScene().getWindow(),"Número incorreto ou já existe");
                tNumber.setStyle(backgorundColorError);
                tNumber.requestFocus();
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
