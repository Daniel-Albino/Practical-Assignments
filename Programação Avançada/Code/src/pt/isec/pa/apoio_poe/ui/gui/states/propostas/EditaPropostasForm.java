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
import pt.isec.pa.apoio_poe.ui.gui.resources.FontManager;
import pt.isec.pa.apoio_poe.ui.gui.states.utils.ToastMessage;

import java.util.ArrayList;
import java.util.List;

public class EditaPropostasForm extends BorderPane {

    Manager manager;
    Stage stage;
    private Label lbNameWindow, lbCodigo, lbTitulo,lbTipo,lbEmpresa,lbEmail,lbNAluno,lbRamo;
    private TextField tCodigo, tTitulo,tEmpresa, tEmail,tNAluno;
    private Button btAdd;
    private RadioButton rbYes,rbNo;
    private ChoiceBox<String> cbTipo,cbRamo;
    private String backgorundColorError = "-fx-control-inner-background: #ff0000;";
    private String [] tipoProposta = {"Estagio","Projeto","Autoproposta"}; //T1 T2 T3
    private String [] ramoProposta = {"DA","SI","RAS","DA|RAS","DA|SI","DA|RAS|SI"};

    Font fontTitulo = FontManager.loadFont("BalsamiqSans-Regular.ttf",34);
    Font font = FontManager.loadFont("BalsamiqSans-Regular.ttf",14);
    private String tipo, codigo,ramo,titulo,empresa,email, nAluno;
    private boolean aluno;

    public EditaPropostasForm(Stage stage, Manager manager) {
        this.stage = stage;
        this.manager = manager;
        initializeVariables();
        createViews();
        registerHandlers();
        update();
    }

    private void initializeVariables() {

        lbNameWindow = new Label("Adicionar Propostas");
        lbNameWindow.setFont(fontTitulo);
        setMargin(lbNameWindow,new Insets(30,0,0,50));

        lbTipo = new Label("Tipo: ");
        lbCodigo = new Label("Código: ");
        lbRamo = new Label("Ramo: ");
        lbTitulo = new Label("Titulo: ");
        lbEmpresa = new Label("Empresa: ");
        lbEmail = new Label("Email do docente: ");
        lbNAluno = new Label("Número do aluno: ");

        cbTipo = new ChoiceBox<>();
        cbTipo.setPrefWidth(150);
        cbTipo.getItems().addAll(tipoProposta);
        cbTipo.setValue("Estagio");



        tCodigo = new TextField();
        tCodigo.setPromptText("Insira o código da propostas");
        tCodigo.setMinWidth(180);

        cbRamo = new ChoiceBox<>();
        cbRamo.setPrefWidth(150);
        cbRamo.getItems().addAll(ramoProposta);
        cbRamo.setValue("DA");

        tTitulo = new TextField();
        tTitulo.setPromptText("Insira o titulo da propostas");
        tTitulo.setMinWidth(180);

        tEmpresa = new TextField();
        tEmpresa.setPromptText("Insira a empresa da propostas");
        tEmpresa.setMinWidth(180);

        tEmail = new TextField();
        tEmail.setPromptText("Insira o email do docente");
        tEmail.setMinWidth(180);
        tEmail.setDisable(true);

        rbYes = new RadioButton("Sim");
        rbYes.setSelected(true);
        aluno = true;
        rbNo = new RadioButton("Não");

        tNAluno = new TextField();
        tNAluno.setPromptText("Insira o número de aluno");
        tNAluno.setMinWidth(180);


        btAdd = new Button("Adicionar");
        btAdd.setDefaultButton(true);
    }

    private void createViews() {

        this.setTop(lbNameWindow);

        HBox hBoxTipo = new HBox(lbTipo,cbTipo);
        setMargin(hBoxTipo,new Insets(0,0,0,50));
        hBoxTipo.setSpacing(10);

        HBox hBoxCodigo = new HBox(lbCodigo,tCodigo);
        setMargin(hBoxCodigo,new Insets(0,50,0,50));
        hBoxCodigo.setSpacing(10);

        HBox hBoxRamo = new HBox(lbRamo,cbRamo);
        setMargin(hBoxRamo,new Insets(0,50,0,50));
        hBoxRamo.setSpacing(10);

        HBox hBoxTitulo = new HBox(lbTitulo,tTitulo);
        setMargin(hBoxTitulo,new Insets(0,50,0,50));
        hBoxTitulo.setSpacing(10);

        HBox hBoxEmpresa = new HBox(lbEmpresa,tEmpresa);
        setMargin(hBoxEmpresa,new Insets(0,50,0,50));
        hBoxEmpresa.setSpacing(10);

        HBox hBoxEmail = new HBox(lbEmail,tEmail);
        setMargin(hBoxEmail,new Insets(0,50,0,50));
        hBoxEmail.setSpacing(10);


        HBox hBoxAluno = new HBox(lbNAluno,rbYes,rbNo,tNAluno);
        setMargin(hBoxAluno,new Insets(0,50,0,50));
        hBoxAluno.setSpacing(10);

        VBox vBoxCenter = new VBox();
        setMargin(vBoxCenter,new Insets(50,50,50,80));
        vBoxCenter.setSpacing(20);
        vBoxCenter.getChildren().addAll(hBoxTipo,hBoxCodigo,hBoxRamo,hBoxTitulo,hBoxEmpresa,hBoxEmail,hBoxAluno);

        this.setCenter(vBoxCenter);

        VBox vBoxButtonBottomAdd = new VBox(btAdd);
        setMargin(vBoxButtonBottomAdd,new Insets(0,50,10,50));
        vBoxButtonBottomAdd.setAlignment(Pos.BOTTOM_RIGHT);

        this.setRight(vBoxButtonBottomAdd);
    }

    private void registerHandlers() {
        rbYes.setOnAction(actionEvent -> {
            aluno = true;
            rbNo.setSelected(false);
            tNAluno.setDisable(false);
        });
        rbNo.setOnAction(actionEvent -> {
            aluno = false;
            rbYes.setSelected(false);
            tNAluno.setDisable(true);
        });
        cbTipo.addEventFilter(ActionEvent.ACTION,actionEvent -> {
            setColor();

            switch (cbTipo.getValue()){
                case "Estagio" -> {
                    tEmpresa.setDisable(false);
                    tEmail.setDisable(true);
                    rbYes.setDisable(false);
                    rbNo.setDisable(false);
                }
                case "Projeto" ->{
                    tEmail.setDisable(false);
                    tEmpresa.setDisable(true);
                    rbYes.setDisable(false);
                    rbNo.setDisable(false);
                }
                case "Autoproposta" ->{
                    tEmail.setDisable(true);
                    tEmpresa.setDisable(true);
                    tNAluno.setDisable(false);
                    rbYes.setDisable(true);
                    rbNo.setDisable(true);
                }
            }
        });

        btAdd.addEventFilter(ActionEvent.ACTION,actionEvent -> {
            setColor();

            String aux = String.valueOf(cbTipo.getValue());
            switch (aux){
                case "Estagio" -> tipo = "T1";
                case "Projeto" -> tipo = "T2";
                case "Autoproposta" -> tipo = "T3";
                default -> tipo = "";
            }

            codigo = String.valueOf(tCodigo.getText());
            ramo = String.valueOf(cbRamo.getValue());
            titulo = String.valueOf(tTitulo.getText());
            empresa = String.valueOf(tEmpresa.getText());
            email = String.valueOf(tEmail.getText());
            if(aluno)
                nAluno = String.valueOf(tNAluno.getText());
            else
                nAluno = "0";

            if(!erroPreencher(codigo,titulo,empresa,email,nAluno)){
                List<String> proposta = new ArrayList<>();
                proposta.add(tipo);
                proposta.add(codigo);
                proposta.add(ramo);
                proposta.add(titulo);
                if(tipo.equals("T1"))
                    proposta.add(empresa);
                if(tipo.equals("T2"))
                    proposta.add(email);
                proposta.add(nAluno);
                tratamentoErros(manager.addProposta(proposta));
            }else{
                ToastMessage.show(getScene().getWindow(),"Faltam parametros");
            }

        });
    }

    private void setColor(){
        String color = "-fx-control-inner-backgorund: #ffffff;";
        tNAluno.setStyle(color);
        tCodigo.setStyle(color);
        tEmpresa.setStyle(color);
        tTitulo.setStyle(color);
        tEmail.setStyle(color);
    }

    private boolean erroPreencher(String codigo, String titulo, String empresa, String email, String nAluno) {
        boolean erro = false;
        if(codigo.isBlank()) {
            tCodigo.setStyle(backgorundColorError);
            erro = true;
        }
        if(titulo.isBlank()) {
            tTitulo.setStyle(backgorundColorError);
            erro = true;
        }
        if(empresa.isBlank()) {
            if(tipo.equals("T1")) {
                tEmpresa.setStyle(backgorundColorError);
                erro = true;
            }
        }
        if(email.isBlank()) {
            if(tipo.equals("T2")) {
                tEmail.setStyle(backgorundColorError);
                erro = true;
            }
        }
        if(nAluno.isBlank()) {
            if(aluno) {
                tNAluno.setStyle(backgorundColorError);
                erro = true;
            }
        }
        return erro;
    }

    private void tratamentoErros(Erros erros){
        System.out.println(erros);
        switch (erros){
            case PROPOSTA_ID -> {
                ToastMessage.show(getScene().getWindow(),"Codigo já existe");
                tCodigo.setStyle(backgorundColorError);
                tCodigo.requestFocus();
            }
            case PROPOSTA_DOCENTES -> {
                ToastMessage.show(getScene().getWindow(),"Docente não existe");
                tEmail.setStyle(backgorundColorError);
                tEmail.requestFocus();
            }
            case PROPOSTA_ADD -> {
                ToastMessage.show(getScene().getWindow(),"Número do aluno inválido");
                tNAluno.setStyle(backgorundColorError);
                tNAluno.requestFocus();
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

