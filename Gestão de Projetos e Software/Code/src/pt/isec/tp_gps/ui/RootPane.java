package pt.isec.tp_gps.ui;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Modality;
import javafx.stage.Stage;
import pt.isec.tp_gps.model.Decorator;
import pt.isec.tp_gps.ui.resources.CSSManager;
import pt.isec.tp_gps.ui.seccoes.*;
import pt.isec.tp_gps.ui.seccoes.encomenda.EncomendaUI;
import pt.isec.tp_gps.ui.seccoes.medicamento.MedicamentoUI;
import pt.isec.tp_gps.ui.seccoes.utente.UtenteUI;

import java.util.Objects;

public class RootPane extends BorderPane {
    private static final int NR_BTNS = 4;
    private static final int BTN_W= 140;
    private static final int BTN_H = 40;
    Decorator model;
    Stage stage1;
    Stage stage2;
    Image img;
    Button btns[];
    Label lb;
    ToolBar toolBar;
    MenuBar menuBar;

    public RootPane(Decorator model, Stage stage1, Stage stage2)  {
        this.model = model;
        this.stage1 = stage1;
        this.stage2 = stage2;
        createViews();
        registerHandlers();
        update();
    }

    private void createViews() {

        CSSManager.applyCSS(this,"styles.css");
        StackPane stackPane = new StackPane(
                new InicialUI(model),
                new PrincipalUI(model),
                new UtenteUI(model,stage1,stage2),
                new EncomendaUI(model,stage1),
                new MedicamentoUI(model,stage1)
        );

        try {
            img = new Image(Objects.requireNonNull(getClass().getResourceAsStream("./resources/images/pills.png")));
            stackPane.setBackground(new Background(new BackgroundImage(
                    img,
                    BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                    BackgroundPosition.CENTER,
                    new BackgroundSize(1, 1, true, true, true, false)
            )));
        }catch(NullPointerException e){}
        this.setCenter(stackPane);

        lb = new Label("Secções:");
        lb.setTextFill(Color.DARKBLUE);
        lb.setAlignment(Pos.CENTER);

        btns = new Button[NR_BTNS];
        btns[0] = new Button(String.format("Principal"));
        btns[0].setPrefSize(BTN_W,BTN_H);
        btns[0].setAlignment(Pos.CENTER_LEFT);
        btns[1] = new Button(String.format("Utentes"));
        btns[1].setPrefSize(BTN_W,BTN_H);
        btns[1].setAlignment(Pos.CENTER_LEFT);
        btns[2] = new Button(String.format("Medicamentos"));
        btns[2].setPrefSize(BTN_W,BTN_H);
        btns[2].setAlignment(Pos.CENTER_LEFT);
        btns[3] = new Button(String.format("Encomendas"));
        btns[3].setPrefSize(BTN_W,BTN_H);
        btns[3].setAlignment(Pos.CENTER_LEFT);

        toolBar = new ToolBar(
                lb,
                new Separator(),
                new Separator(),
                new Separator(),
                btns[0],
                btns[1],
                btns[2],
                btns[3],
                new Separator()
        );
        toolBar.setOrientation(Orientation.VERTICAL);

        MenuItem mnAbout = new MenuItem("Sobre...");

        mnAbout.setOnAction(event -> showAbout());

        menuBar = new MenuBar(
                new Menu("Mais",null,mnAbout)
        );


    }

    private void registerHandlers() {

        model.addPropertyChangeListener(evt -> {update();});

        btns[0].setOnAction(event -> {
            model.homePage();
        });

        btns[1].setOnAction(event -> {
            model.personPage();
        });

        btns[2].setOnAction(event -> {
            model.pillPage();
        });

        btns[3].setOnAction(event -> {
            model.encomendasPage();
        });
    }

    private void update() {
        switch(model.getState()){
            case ESTADOINICIAL:
                setTop(null);
                setLeft(null);
                setBottom(new CreditsUI());
                break;
            case ESTADOPRINCIPAL:
            case ESTADOMEDICAMENTOS:
            case ESTADOUTENTE:
            case ESTADOENCOMENDAS:
                setTop(menuBar);
                setLeft(toolBar);
                break;
            default:
                break;

        }
    }

    private void showAbout() {
        final Stage stage = new Stage();
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("./resources/images/pills.png"))));
        String text = """
                       IPC-ISEC-DEIS
                            LEI
                       GPS - G14 (2022)
                     (c)Daniel Albino
                     (c)Diogo Silva
                     (c)Miguel Neves
                     (c)Nuno Domingues
                     (c)Rúben Mendes
                """;
        TextArea textArea = new TextArea();
        textArea.setWrapText(true);
        textArea.setEditable(false);
        textArea.setPrefWidth(200);
        textArea.setPrefHeight(150);

        textArea.setStyle("-fx-font-family: 'Courier New';");

        textArea.setText(text);
        Button btnClose = new Button("Fechar");
        btnClose.setOnAction(event -> stage.close());
        btnClose.setCursor(Cursor.DEFAULT);
        stage.setWidth(250);
        stage.setHeight(260);
        VBox vBox = new VBox(textArea,btnClose);
        vBox.setSpacing(10);
        vBox.setPadding(new Insets(10));
        vBox.setAlignment(Pos.CENTER);
        stage.setScene(new Scene(vBox));
        stage.setTitle("Sobre");
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(this.getScene().getWindow());
        stage.showAndWait();
    }


}
