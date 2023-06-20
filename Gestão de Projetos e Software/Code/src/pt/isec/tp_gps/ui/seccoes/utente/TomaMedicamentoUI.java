package pt.isec.tp_gps.ui.seccoes.utente;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import pt.isec.tp_gps.model.Decorator;
import pt.isec.tp_gps.model.data.TomaMedicamento;

import java.util.List;
import java.util.Map;

public class TomaMedicamentoUI extends BorderPane {
    private static final int NR_FIELDS = 5;
    Decorator model;
    Label lb;
    TableView tableView;
    Button btnConfirm;
    VBox vb;


    public TomaMedicamentoUI(Decorator model) {
        this.model = model;
        createViews();
        registerHandlers();
        update();
    }

    private void createViews() {

        lb = new Label("Utentes que precisam de tomar medicamentos");

        tableView = new TableView<>();
        TableColumn<TomaMedicamento,String> Utente = new TableColumn<TomaMedicamento,String>("Utente");
        Utente.setCellValueFactory(new PropertyValueFactory<TomaMedicamento,String>("nomeUtente"));
        TableColumn<TomaMedicamento,String> Medicamento = new TableColumn<TomaMedicamento,String>("Medicamento");
        Medicamento.setCellValueFactory(new PropertyValueFactory<TomaMedicamento,String>("nomeMedicamento"));
        TableColumn<TomaMedicamento,String> hora = new TableColumn<TomaMedicamento,String>("Hora");
        hora.setCellValueFactory(new PropertyValueFactory<TomaMedicamento,String>("hora"));
        tableView.setPlaceholder(new Label("Sem dados"));
        tableView.setPrefHeight(Integer.MAX_VALUE);
        tableView.getColumns().add(Utente);
        tableView.getColumns().add(Medicamento);
        tableView.getColumns().add(hora);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        /*List<Utente> aux = model.DosageUtente();
        tableView.getItems().addAll(aux);*/

        btnConfirm = new Button("Confirmar");
        HBox hb = new HBox(btnConfirm);
        hb.setAlignment(Pos.CENTER_RIGHT);
        //btnConfirm.setAlignment(Pos.CENTER_RIGHT);

        vb = new VBox(lb,tableView,hb);
        vb.setPadding(new Insets(20));
        vb.setSpacing(15);
        this.setCenter(vb);

    }



    private void registerHandlers() {
        model.addPropertyChangeListener(evt -> { update(); });
        btnConfirm.setOnAction(evt ->{
            if(tableView.getSelectionModel().getSelectedItem() != null) {
                TomaMedicamento tomaMedicamento = (TomaMedicamento) tableView.getSelectionModel().getSelectedItem();
                model.removeUtentesEMedicamentos(tomaMedicamento);
                update();
            }
        });
    }

    private void update() {
        Platform.runLater(()->{ tableView.getItems().clear();
            Map<String,List<TomaMedicamento>> aux = model.getUtentesEMedicamentos();
            /*Platform.runLater(() ->{*/
                for(List<TomaMedicamento> tomaMedicamento : aux.values()){
                    for(TomaMedicamento t : tomaMedicamento){
                        tableView.getItems().add(t);
                    }
                }
            /*});*/
        });
    }
}
