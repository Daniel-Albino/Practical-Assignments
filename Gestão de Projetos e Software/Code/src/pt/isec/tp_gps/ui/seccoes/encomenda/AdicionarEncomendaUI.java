package pt.isec.tp_gps.ui.seccoes.encomenda;


import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import pt.isec.tp_gps.model.Decorator;
import pt.isec.tp_gps.model.data.EncomendaTableData;
import pt.isec.tp_gps.ui.seccoes.ToastMessage;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class AdicionarEncomendaUI extends BorderPane {
    private static final int NR_LBENC = 3;
    private static final int NR_TFENC = 2;
    Decorator model;
    Label lbEncomenda[];
    TextField tfEncomenda[];
    TableView tvEncomenda;
    Image img1;
    Button view1;
    Button apply,cancel;
    HBox ButtonApplyCancel;
    HBox hb1,hb2,hb3,hb4,hb5,hb6,hb7,hb8,hb9,hb10,hb11,hb12;
    VBox vb1,vb2,vb3,vb4,vb5,vb6;
    List<EncomendaTableData> list;

    public AdicionarEncomendaUI(Decorator model) {
        this.model = model;
        this.list = new ArrayList<>();
        System.out.println(model.getTempEncomendaTableData());
        model.getTempEncomendaTableData().clear();
        createViews();
        registerHandlers();
        update();
    }

    private void createViews() {


        //TODO:: Labels(nome,CC,email,telef. de emergências,ocupação)
        lbEncomenda = new Label[NR_LBENC];
        lbEncomenda[0] = new Label();
        lbEncomenda[0].setText("ID: "+model.idProximaEncomenda());
        lbEncomenda[0].setMinWidth(80);
        lbEncomenda[1] = new Label();
        lbEncomenda[1].setText("Medicamento: ");
        lbEncomenda[1].setMinWidth(50);
        lbEncomenda[2] = new Label();
        lbEncomenda[2].setText("Quantidade: ");
        lbEncomenda[2].setMinWidth(75);

        //TODO:: TextFields(nome,CC,email,telef. de emergências,ocupação)
        tfEncomenda = new TextField[NR_TFENC];

        //TODO:: Criação efetiva das labels e dos textfields com as diferentes propriedades
        for(int i=0;i<NR_TFENC;i++){
            tfEncomenda[i] = new TextField();
            tfEncomenda[i].setPrefWidth(Integer.MAX_VALUE);
            tfEncomenda[i].focusTraversableProperty().set(false);
            tfEncomenda[i].setMaxWidth(100);
        }
        tfEncomenda[0].setMinWidth(130);
        tfEncomenda[1].setMinWidth(15);

        hb1 = new HBox(lbEncomenda[1],tfEncomenda[0]);
        hb2 = new HBox(lbEncomenda[2],tfEncomenda[1]);
        hb3 = new HBox(hb1,hb2);
        hb3.setSpacing(35);

        tfEncomenda[0].textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observableValue, String s, String t1) {
                if(tfEncomenda[0].getText().length() > 30){
                    String aux = tfEncomenda[0].getText().substring(0, 30);
                    tfEncomenda[0].setText(aux);
                }
            }
        });

        img1 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("../../resources/images/plus.png")),24,24,false,false);
        view1 = new Button();
        view1.setStyle("-fx-background-color: none");
        view1.setGraphic(new ImageView(img1));

        hb4 = new HBox(view1);
        hb4.setAlignment(Pos.CENTER_RIGHT);


        tvEncomenda = new TableView<>();
        TableColumn<EncomendaTableData,String> medicamento = new TableColumn<EncomendaTableData,String>("Medicamento");
        medicamento.setCellValueFactory(new PropertyValueFactory<EncomendaTableData,String>("medicamento"));
        TableColumn<EncomendaTableData, Integer> qtDia = new TableColumn<EncomendaTableData,Integer>("Quantidade");
        qtDia.setCellValueFactory(new PropertyValueFactory<EncomendaTableData,Integer>("quantidade"));
        tvEncomenda.setMaxHeight(400);
        tvEncomenda.setPlaceholder(new Label("Sem dados"));
        tvEncomenda.getColumns().add(medicamento);
        tvEncomenda.getColumns().add(qtDia);
        tvEncomenda.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        vb1 = new VBox(hb4, tvEncomenda);

        apply = new Button("Aplicar");
        apply.focusTraversableProperty().set(false);
        apply.setStyle("-fx-background-color: rgba(57,217,57,0.6);-fx-border-color: grey");

        cancel = new Button("Cancelar");
        cancel.focusTraversableProperty().set(false);
        cancel.setStyle("-fx-background-color: rgba(217,70,70,0.59);-fx-border-color: grey");

        ButtonApplyCancel = new HBox(apply,cancel);
        ButtonApplyCancel.setSpacing(10);
        ButtonApplyCancel.setAlignment(Pos.CENTER_RIGHT);
        ButtonApplyCancel.setPadding(new Insets(0,20,0,0));


        vb3 = new VBox(lbEncomenda[0],hb3,vb1,ButtonApplyCancel);
        vb3.setPadding(new Insets(20));
        vb3.setSpacing(15);
        this.setCenter(vb3);

    }


    private void registerHandlers() {
        model.addPropertyChangeListener(evt -> { update(); });
        view1.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Platform.runLater(()->{
                String medicamento = tfEncomenda[0].getText().trim();
                String quantidade = tfEncomenda[1].getText().trim();

                try {
                    Integer.parseInt(quantidade);
                    model.addTempListEncomenda(new EncomendaTableData(quantidade,medicamento));
                    tvEncomenda.getItems().clear();
                        for(EncomendaTableData a : model.getTempEncomendaTableData())
                            tvEncomenda.getItems().add(a);
                }catch (NumberFormatException e){
                    ToastMessage.show(getScene().getWindow(),"Coloca um número inteiro na quantidade!");
                }

                });
            }
        });

        apply.setOnAction(event ->{
            System.out.println(list);
            if(model.adicionarEncomendas(model.getTempEncomendaTableData())) {
                //model.gerarPDF(model.getTempEncomendaTableData());
                ToastMessage.show(getScene().getWindow(), "Encomenda adicionada com sucesso!");
                Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
                stage.close();
            }else {
                ToastMessage.show(getScene().getWindow(), "Encomenda não adicionada!");
            }
        });

        cancel.setOnAction(event->{
            Stage stage = (Stage)((Node) event.getSource()).getScene().getWindow();
            stage.close();
        });
    }

    private void update() {}

}
