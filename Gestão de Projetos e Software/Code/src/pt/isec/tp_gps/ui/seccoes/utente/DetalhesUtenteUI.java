package pt.isec.tp_gps.ui.seccoes.utente;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import pt.isec.tp_gps.model.Decorator;
import pt.isec.tp_gps.model.data.ListaMedicamentosUtente;
import pt.isec.tp_gps.model.data.Utente;

import java.util.List;
public class DetalhesUtenteUI extends BorderPane {
    private static final int NR_FIELDS = 5;
    Decorator model;
    Label nome,CC,DataNascimento,Email,TelEmer,sexo,ocupacao;
    TableView tableView;
    ListView alergias,doencas;

    Stage stage2;
    Label lbUtente[];
    TextField tfUtente[];
    DatePicker datePicker;
    ComboBox<String> combo;
    TableView tvUtente;

    //Label alergias,doencas;
    ListView<String> lAUtente, lDUtente;
    ObservableList<String> names;
    ObservableList<String> names1;

    Image img1;
    ImageView view1,view2,view3;


    Button apply,cancel;
    HBox ButtonApplyCancel;

    HBox hb1,hb2,hb3,hb4,hb5,hb6,hb7,hb8,hb9,hb10,hb11,hb12;
    VBox vb1,vb2,vb3,vb4,vb5,vb6;

    ScrollPane scrollPane;


    public DetalhesUtenteUI(Decorator model/*, Stage stage2*/) {
        this.model = model;
        //this.stage2 = stage2;
        createViews();
        registerHandlers();
        update();
    }

    private void createViews() {

        nome = new Label("Nome");
        nome.setId("labelnome");
        CC = new Label("CC");
        DataNascimento = new Label("DataNascimento");
        Email = new Label("Email");
        TelEmer = new Label("Telefone de emergência");
        sexo = new Label("Sexo");
        ocupacao = new Label("Ocupação");

        vb1 = new VBox(nome,CC,DataNascimento,Email,TelEmer);
        vb1.setSpacing(15);

        vb2 = new VBox(sexo,ocupacao);
        vb2.setSpacing(15);


        hb1 = new HBox(vb1,vb2);
        hb1.setSpacing(20);

        tableView = new TableView<>();
        TableColumn<ListaMedicamentosUtente, String> medicamento = new TableColumn<ListaMedicamentosUtente,String>("Medicamento");
        medicamento.setCellValueFactory(new PropertyValueFactory<ListaMedicamentosUtente,String>("nomeMedicamento"));
        TableColumn<ListaMedicamentosUtente,Integer> qtDia = new TableColumn<ListaMedicamentosUtente,Integer>("Quantidade/Dia");
        qtDia.setCellValueFactory(new PropertyValueFactory<ListaMedicamentosUtente,Integer>("quantidade"));
        TableColumn<ListaMedicamentosUtente, String> obs = new TableColumn<ListaMedicamentosUtente,String>("Horas");
        obs.setCellValueFactory(new PropertyValueFactory<ListaMedicamentosUtente,String>("horas"));
        tableView.setPlaceholder(new Label("Sem dados"));
        tableView.setMaxHeight(300);

        tableView.getColumns().add(medicamento);
        tableView.getColumns().add(qtDia);
        tableView.getColumns().add(obs);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);


        vb1 = new VBox(hb1,tableView);
        vb1.setPadding(new Insets(20));
        vb1.setSpacing(15);
        this.setCenter(vb1);

    }







    private void registerHandlers() {
        model.addPropertyChangeListener(evt -> { update(); });

 /*
        AddDetails.setOnAction(event -> {
            stage2.show();
        });

        accept.setOnAction(event -> {
            System.out.println("clicked!");
        });

        DropShadow shadow = new DropShadow();
//Adding the shadow when the mouse cursor is on
        accept.addEventHandler(MouseEvent.MOUSE_ENTERED,
                new EventHandler<MouseEvent>() {
                    @Override public void handle(MouseEvent e) {
                        accept.setEffect(shadow);
                        //accept.setStyle("-fx-border-color: #1a83c6; -fx-border-width: 2");
                    }
                });

//Removing the shadow when the mouse cursor is off
        accept.addEventHandler(MouseEvent.MOUSE_EXITED,
                new EventHandler<MouseEvent>() {
                    @Override public void handle(MouseEvent e) {
                        accept.setEffect(null);
                    }
                });



        //Adding the shadow when the mouse cursor is on
        cancel.addEventHandler(MouseEvent.MOUSE_ENTERED,
                new EventHandler<MouseEvent>() {
                    @Override public void handle(MouseEvent e) {
                        cancel.setEffect(shadow);
                        //accept.setStyle("-fx-border-color: #1a83c6; -fx-border-width: 2");
                    }
                });

        //Removing the shadow when the mouse cursor is off
        cancel.addEventHandler(MouseEvent.MOUSE_EXITED,
                new EventHandler<MouseEvent>() {
                    @Override public void handle(MouseEvent e) {
                        cancel.setEffect(null);
                    }
                });
                */


    }

    private void update() {

        Platform.runLater(()->{
            Utente aux = model.getTempUtente();
            nome.setText("Nome: "+aux.getNome());
            CC.setText("CC: "+aux.getCC());
            DataNascimento.setText("DataNascimento: "+aux.getDataNascimentoString());
            Email.setText("Email: "+aux.getEmail());
            TelEmer.setText("Telefone de emergência: "+aux.getTelefoneEme());
            sexo.setText("Sexo: "+aux.getSexo());
            ocupacao.setText("Ocupação: "+aux.getOcupacao());

            List<ListaMedicamentosUtente> list = aux.getListaMedicamentos();

            if(list != null){
                tableView.getItems().clear();
                for(ListaMedicamentosUtente listaMedicamentosUtente : list){
                    tableView.getItems().add(listaMedicamentosUtente);
                }
            }


        });
    }
}
