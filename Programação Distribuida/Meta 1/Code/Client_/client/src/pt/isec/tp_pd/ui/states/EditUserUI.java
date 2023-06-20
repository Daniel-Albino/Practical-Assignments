package pt.isec.tp_pd.ui.states;

import javafx.application.Platform;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import pt.isec.tp_pd.model.ModelManager;
import pt.isec.tp_pd.model.fsm.TicketPdState;
import pt.isec.tp_pd.ui.resources.FontManager;

public class EditUserUI extends BorderPane {

    ModelManager model;
    Label lb, lnome, lusername, lpassword;
    Button btnApply,btnCancel;
    TextField name,username,password;

    public EditUserUI(ModelManager model) {
        this.model=model;
        createViews();
        registerHandlers();
        update();
    }

    private void createViews() {
        lb = new Label("Editar dados:");
        lb.setFont(FontManager.loadFont("KernlGrotesk-Bold.otf",18));
        lb.setTextFill(Color.WHITE);

        lnome = new Label("Nome:");
        lnome.setFont(FontManager.loadFont("KernlGrotesk-Bold.otf",16));
        lnome.setTextFill(Color.WHITE);
        lusername = new Label("Username:");
        lusername.setFont(FontManager.loadFont("KernlGrotesk-Bold.otf",16));
        lusername.setTextFill(Color.WHITE);
        lpassword = new Label("Password:");
        lpassword.setFont(FontManager.loadFont("KernlGrotesk-Bold.otf",16));
        lpassword.setTextFill(Color.WHITE);

        name = new TextField();
        name.setPrefWidth(Integer.MAX_VALUE);
        name.setMaxWidth(200);
        name.focusTraversableProperty().set(false);
        name.setPrefHeight(20);
        //name.setPromptText("Enter your new name.");

        username = new TextField();
        username.setPrefWidth(Integer.MAX_VALUE);
        username.setMaxWidth(200);
        username.focusTraversableProperty().set(false);
        username.setPrefHeight(20);
        //username.setPromptText("Enter your new username.");

        password = new TextField();
        password.setPrefWidth(Integer.MAX_VALUE);
        password.setMaxWidth(200);
        password.focusTraversableProperty().set(false);
        password.setPrefHeight(20);
        //password.setPromptText("Enter your new name.");



        btnApply = new Button("Aplicar");
        btnApply.setMinWidth(80);
        btnCancel  = new Button("Cancelar");
        btnCancel.setMinWidth(80);

        HBox hbox = new HBox(btnApply,btnCancel);
        hbox.setAlignment(Pos.BOTTOM_RIGHT);
        hbox.setSpacing(20);

        VBox vb = new VBox(lb,lnome,name,lusername,username,lpassword,password,hbox);
        vb.setSpacing(20);
        vb.setAlignment(Pos.CENTER);
        vb.setMaxWidth(300);
        vb.setMaxHeight(130);
        vb.getStyleClass().add("blurBox");
        this.setCenter(vb);
    }

    private void registerHandlers() {
        model.addPropertyChangeListener(evt -> { update(); });

        btnApply.setOnAction( event -> {
            model.setNewName(name.getText());
            model.setNewUsername(username.getText());
            model.setNewPassword(password.getText());
            model.setPageState(PagesEnum.USERCHANGE);

        });

        btnCancel.setOnAction( event -> {
            model.OrdersPage();
        });



    }

    private void update() {
        if (model.getState() != TicketPdState.EDITUSER) {
            this.setVisible(false);
            return;
        }
        this.setVisible(true);


        Platform.runLater(() -> {
            name.setText(model.getName());
            username.setText(model.getUsername());
            password.setText(model.getPassword());

        });
    }
}
