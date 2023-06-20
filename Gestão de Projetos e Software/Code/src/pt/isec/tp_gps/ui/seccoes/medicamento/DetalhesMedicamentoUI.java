package pt.isec.tp_gps.ui.seccoes.medicamento;


import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import pt.isec.tp_gps.model.Decorator;
import pt.isec.tp_gps.model.data.Medicamento;
import pt.isec.tp_gps.model.fsm.MEEstados;
public class DetalhesMedicamentoUI extends BorderPane {
    private static final int NR_FIELDS = 5;
    Decorator model;
    Stage stage2;
    Label lbUtente[];
    Label dataNasc,sexo;
    TextField tfUtente[];
    TextArea ta;
    DatePicker datePicker;
    ComboBox<String> combo;
    TableView tvUtente;
    ListView<String> lAUtente, lDUtente;
    ObservableList<String> names;
    ObservableList<String> names1;

    Button apply,cancel,lb1;
    HBox ButtonAddCancel;

    HBox hb1,hb2,hb3,hb4,hb5,hb6,hb7,hb8;
    VBox vb1,vb2;

    Label nome,ID,modoAdministracao,laboratorio,principalAtivo,dataValidade,Bula;

    //Image and ImageView
    Medicamento medicamentoAnterior;

    public DetalhesMedicamentoUI(Decorator model, Medicamento medicamentoAnterior/*, Stage stage2*/) {
        this.model = model;
        //this.stage2 = stage2;
        this.medicamentoAnterior = medicamentoAnterior;
        createViews();
        registerHandlers();
        update();
    }

    private void createViews() {
        nome = new Label("Nome: " + medicamentoAnterior.getNome());
        nome.setId("labelnome");
        ID = new Label("ID: " + medicamentoAnterior.getID());
        modoAdministracao = new Label("Modo de Administração: " + medicamentoAnterior.getModoAdministracao());
        laboratorio = new Label("Laboratório: " + medicamentoAnterior.getLaboratorio());
        principalAtivo = new Label("Principal Ativo: " + medicamentoAnterior.getPrincipioAtivo());
        dataValidade = new Label("Data de Validade: " + medicamentoAnterior.getDataValidadeString());
        Bula = new Label("BULA: " + medicamentoAnterior.getBula());




        vb1 = new VBox(nome, ID, modoAdministracao, laboratorio, principalAtivo, dataValidade);
        vb1.setSpacing(10);

        vb2 = new VBox(vb1, Bula);
        vb2.setPadding(new Insets(20));
        vb2.setSpacing(45);
        this.setCenter(vb2);


    }
    private void registerHandlers() {
        model.addPropertyChangeListener(evt -> { update(); });



    }

    private void update() {
        if (model.getState() != MEEstados.ESTADOMEDICAMENTOS) {
            this.setVisible(false);
            return;
        }
        this.setVisible(true);


    }



}
