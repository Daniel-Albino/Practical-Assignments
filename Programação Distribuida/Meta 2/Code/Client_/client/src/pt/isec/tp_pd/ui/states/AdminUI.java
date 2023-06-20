package pt.isec.tp_pd.ui.states;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import pt.isec.tp_pd.data.Espetaculo;
import pt.isec.tp_pd.data.Lugar;
import pt.isec.tp_pd.model.ModelManager;
import pt.isec.tp_pd.model.fsm.TicketPdState;
import pt.isec.tp_pd.ui.resources.FontManager;

import java.io.File;
import java.util.List;

public class AdminUI extends BorderPane {

    ModelManager model;
    Label lb;

    Button insert, delete, apply;

    Espetaculo espetaculo;

    String selected;

    ObservableList<String> names;
    ListView<String> listView;

    List<Espetaculo> espetaculos;

    CheckBox visivel;

    FileChooser fileChooser;
    File file;


    public AdminUI(ModelManager model) {
        this.model = model;
        createViews();
        registerHandlers();
        update();
    }

    private void createViews() {

        fileChooser = new FileChooser();

        lb = new Label("Administrador:");
        lb.setFont(FontManager.loadFont("KernlGrotesk-Bold.otf", 32));
        lb.setTextFill(Color.WHITE);


        insert = new Button("Inserir espetáculo");
        delete = new Button("Eliminar espetáculo");

        HBox hb = new HBox(insert, delete);
        hb.setSpacing(10);
        hb.setAlignment(Pos.CENTER);

        names = FXCollections.observableArrayList();
        listView = new ListView<String>(names);
        listView.setMaxSize(200, 160);


        visivel = new CheckBox("visivel");
        visivel.setTextFill(Color.WHITE);


        apply = new Button("Aplicar");


        VBox vb = new VBox(lb, hb, listView, visivel, apply);
        vb.setSpacing(10);
        vb.setAlignment(Pos.CENTER);
        vb.setMaxWidth(800);
        vb.setMaxHeight(Integer.MAX_VALUE);
        vb.getStyleClass().add("blurBox");

        this.setCenter(vb);


    }

    private void registerHandlers() {

        model.addPropertyChangeListener(evt -> {
            update();
        });

        apply.setOnAction(event -> {
            if (file != null) {
                model.setFile(file);
                model.setPageState(PagesEnum.UPLOADFILE);
            }

        });

        insert.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                Stage stage = (Stage) insert.getScene().getWindow();
                file = fileChooser.showOpenDialog(stage);

            }

        });

        delete.setOnAction(event -> {
            selected = listView.getSelectionModel().getSelectedItems().toString();
            selected = selected.replaceAll("[\\[\\]\\(\\)]", "");
            System.out.println(selected);
            if(selected != null && !selected.isEmpty()) {
                for (Espetaculo a : espetaculos) {
                    System.out.println(a.toString());
                    if (a.getDescricao().equals(selected)) {
                        System.out.println(a.getDescricao()+" "+selected);
                        model.setSelectedEspetaculo(a);
                        break;
                    }
                }
                System.out.println(model.getSeletedEspetaculo());

                model.setPageState(PagesEnum.ELIMINAESPE);
            }
        });


    }

    private void update() {

        if (model.getState() != TicketPdState.ADMINSTATE) {
            this.setVisible(false);
            return;
        }
        this.setVisible(true);


        espetaculos = model.getEspetaculos();

        if (espetaculos.size() > 0) {
            Platform.runLater(() -> {
                for (Espetaculo a : espetaculos)
                    names.add(a.getDescricao());
            });


        }


    }
}
