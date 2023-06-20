package pt.isec.tp_pd.ui;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.stage.Modality;
import javafx.stage.Stage;
import pt.isec.tp_pd.model.ModelManager;
import pt.isec.tp_pd.ui.resources.CSSManager;
import pt.isec.tp_pd.ui.states.*;

import java.util.Objects;

public class RootPane extends BorderPane {

    ModelManager model;
    MenuBar menuBar;

    public RootPane(ModelManager model)  {
        this.model = model;
        createViews();
        registerHandlers();
        update();
    }

    private void createViews() {
        this.getParent();
        CSSManager.applyCSS(this,"styles.css");

        StackPane stackPane = new StackPane(
                new InitialUI(model),
                new LoginUI(model),
                new SignUpUI(model),
                new OrdersUI(model),
                new ConnectionUI(model),
                new ConsultaReservesUI(model),
                new EditUserUI(model),
                new SearchShowUI(model),
                new AdminUI(model)
        );

        stackPane.setBackground(new Background(new BackgroundImage(
                new Image(Objects.requireNonNull(getClass().getResourceAsStream("./resources/images/logoticket1.png"))),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT,
                BackgroundPosition.CENTER,
                new BackgroundSize(1, 1, true, true, false, false)

        )));
        this.setCenter(stackPane);

        MenuItem mnAbout = new MenuItem("About...");

        mnAbout.setOnAction(event -> showAbout());
        menuBar = new MenuBar(
                new Menu("File",null,mnAbout)
        );
    }

    private void registerHandlers() {}
    private void update() {

        switch(model.getState()){
            case INITIALSTATE:
                setTop(menuBar);
                setBottom(new CreditsUI());
                break;
            case LOGINSTATE:
            case SIGNUPSTATE:
            case ORDERSSTATE:
            case CONNECTIONSTATE:
            case SEARCHSHOW:
            case EDITUSER:
            case REVIEWRESERVE:
            case ADMINSTATE:
                break;
            default:
                break;
        }

    }


    private void showAbout() {
        final Stage stage = new Stage();
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("./resources/images/isec_64x64.png"))));
        String text = """
                       IPC-ISEC-DEIS
                            LEI
                      PD - G06 (2022)
                     (c)Daniel Albino
                     (c)Miguel Neves
                     (c)Nuno Domingues
                """;
        TextArea textArea = new TextArea();
        textArea.setWrapText(true);
        textArea.setEditable(false);
        textArea.setPrefWidth(200);
        textArea.setPrefHeight(150);

        textArea.setStyle("-fx-font-family: 'Courier New';");

        textArea.setText(text);
        Button btnClose = new Button("Close");
        btnClose.setOnAction(event -> stage.close());
        btnClose.setCursor(Cursor.DEFAULT);
        stage.setWidth(250);
        stage.setHeight(260);
        VBox vBox = new VBox(textArea,btnClose);
        vBox.setSpacing(10);
        vBox.setPadding(new Insets(10));
        vBox.setAlignment(Pos.CENTER);
        stage.setScene(new Scene(vBox));
        stage.setTitle("About");
        stage.setResizable(false);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initOwner(this.getScene().getWindow());
        stage.showAndWait();
    }
}
