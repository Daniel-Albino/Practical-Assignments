package pt.isec.tp_gps.ui.seccoes.utente;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.ListView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import pt.isec.tp_gps.model.Decorator;

import java.util.List;
public class HistoricoUtentesUI extends BorderPane {
    private static final int NR_FIELDS = 5;
    Decorator model;
    ListView<String> lvHistoric;
    VBox vb;

    public HistoricoUtentesUI(Decorator model) {
        this.model = model;
        createViews();
        registerHandlers();
        update();
    }

    private void createViews() {

        lvHistoric = new ListView<String>();
        ObservableList<String> items = FXCollections.observableArrayList ();
        lvHistoric.setItems(items);
        lvHistoric.setPrefHeight(Integer.MAX_VALUE);


        vb = new VBox(lvHistoric);
        vb.setPadding(new Insets(20));
        vb.setSpacing(15);
        this.setCenter(vb);

    }



    private void registerHandlers() {
        model.addPropertyChangeListener(evt -> { update(); });
    }

    private void update() {
        lvHistoric.getItems().clear();
        List<String> historico = model.getHistoricoUtentes();
        for(String s : historico)
            lvHistoric.getItems().add(s);
    }


}
