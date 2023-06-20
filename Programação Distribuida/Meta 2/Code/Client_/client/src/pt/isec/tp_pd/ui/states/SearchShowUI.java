package pt.isec.tp_pd.ui.states;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import pt.isec.tp_pd.model.ModelManager;
import pt.isec.tp_pd.model.fsm.TicketPdState;
import pt.isec.tp_pd.ui.resources.FontManager;

public class SearchShowUI extends BorderPane {

    ModelManager model;
    Label lb;
    TextField filter;
    Button btnSearch,btnCancel;
    ComboBox comboBox;
    ChoiceBox choiceBox;
    VBox vb1;


    public SearchShowUI(ModelManager model) {
        this.model=model;
        createViews();
        registerHandlers();
        update();
    }

    private void createViews() {
        lb = new Label("Escolha um espetáculo:");
        lb.setFont(FontManager.loadFont("KernlGrotesk-Bold.otf",32));
        lb.setTextFill(Color.WHITE);

        filter = new TextField();
        filter.setPrefWidth(Integer.MAX_VALUE);
        filter.setMaxWidth(600);
        filter.focusTraversableProperty().set(false);
        filter.setPrefHeight(20);
        filter.setPromptText("Indique para pesquisar");



        comboBox = new ComboBox();
        comboBox.getItems().add("Nome");
        comboBox.getItems().add("Tipo");
        comboBox.getItems().add("Data(DD/MM/YYYY)");
        comboBox.getItems().add("Duração(min)");
        comboBox.getItems().add("Local");
        comboBox.getItems().add("Localidade");
        comboBox.getItems().add("País");
        comboBox.getItems().add("Classificação etária");
        comboBox.setValue("Nome");
        comboBox.setVisibleRowCount(4);



        btnSearch = new Button("Pesquisar");
        btnSearch.setMinWidth(80);
        btnCancel  = new Button("Cancelar");
        btnCancel.setMinWidth(80);
        HBox hbox = new HBox(btnSearch,btnCancel);
        hbox.setAlignment(Pos.CENTER);
        hbox.setSpacing(20);

        vb1 = new VBox(lb,comboBox,filter,new Separator(),hbox);
        vb1.setSpacing(20);
        vb1.setAlignment(Pos.CENTER);
        vb1.minWidth(500);
        vb1.setMaxWidth(750);
        vb1.setMaxHeight(50);
        vb1.getStyleClass().add("blurBoxOrder");
        setPadding(new Insets(20));
        setAlignment(vb1,Pos.TOP_CENTER);

        this.setCenter(vb1);

    }

    private void registerHandlers() {
        model.addPropertyChangeListener(evt -> { update(); });

        btnSearch.setOnAction( event -> {
            if(!filter.getText().isEmpty()) {
                model.setfilter(filter.getText());
                model.setOptionShow(comboBox.getValue().toString());
            }
            filter.clear();
            model.setPageState(PagesEnum.SEARCHSH);

        });

        btnCancel.setOnAction( event -> {
            model.OrdersPage();
        });



    }

    private void update() {
        if (model.getState() != TicketPdState.SEARCHSHOW) {
            this.setVisible(false);
            return;
        }
        this.setVisible(true);
    }
}
