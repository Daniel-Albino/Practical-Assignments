package pt.isec.tp_pd.ui.states;

import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import pt.isec.tp_pd.model.ModelManager;
import pt.isec.tp_pd.model.fsm.TicketPdState;
import pt.isec.tp_pd.sync.ConnectionsTCP;
import pt.isec.tp_pd.ui.resources.FontManager;

public class SignUpUI extends BorderPane {

    private static final int NR_TEXTFIELD = 2;
    ModelManager model;
    Label lb;
    TextField[] tf;
    PasswordField tf1;
    Button btnReg,btnCancel;

    String name, user, password;

    public SignUpUI(ModelManager model) {
        this.model=model;
        createViews();
        registerHandlers();
        update();
    }

    private void createViews() {

        lb = new Label("Register Form");
        lb.setFont(FontManager.loadFont("Bekinder Bold.otf",20));
        lb.setTextFill(Color.WHITE);

        tf = new TextField[NR_TEXTFIELD];

        for(int i=0;i<NR_TEXTFIELD;i++){
            tf[i] = new TextField();
            tf[i].setPrefWidth(Integer.MAX_VALUE);
            tf[i].setMaxWidth(350);
            tf[i].focusTraversableProperty().set(false);
            tf[i].setPrefHeight(60);
        }
        tf[0].setPromptText("Name");
        tf[1].setPromptText("Username");
        tf1 = new PasswordField();
        tf1.setPrefWidth(Integer.MAX_VALUE);
        tf1.setMaxWidth(350);
        tf1.focusTraversableProperty().set(false);
        tf1.setPrefHeight(60);
        tf1.setPromptText("Password");


        btnReg = new Button("Registar");
        btnReg.focusTraversableProperty().set(false);
        btnReg.setStyle("-fx-background-color: rgba(0,95,213,0.98)");
        btnReg.setPrefHeight(50);
        btnReg.setPrefWidth(Integer.MAX_VALUE);
        btnReg.setFont(FontManager.loadFont("Bekinder.otf",15));
        btnReg.setTextFill(Color.WHITE);
        btnCancel = new Button("Cancel");
        btnCancel.focusTraversableProperty().set(false);
        btnCancel.setStyle("-fx-background-color: rgb(171,38,38)");
        btnCancel.setPrefHeight(50);
        btnCancel.setPrefWidth(Integer.MAX_VALUE);
        btnCancel.setFont(FontManager.loadFont("Bekinder.otf",14));
        btnCancel.setTextFill(Color.WHITE);

        VBox vbox = new VBox();
        vbox.setSpacing(20);
        vbox.getChildren().add(lb);
        for(int i=0;i<NR_TEXTFIELD;i++){
            vbox.getChildren().addAll(tf[i]);
        }
        vbox.getChildren().addAll(tf1,new Separator(),btnReg,btnCancel);
        vbox.setAlignment(Pos.CENTER);
        vbox.setMaxWidth(400);
        vbox.setMaxHeight(350);
        vbox.getStyleClass().add("blurBox");

        setCenter(vbox);


    }

    private void registerHandlers() {
        model.addPropertyChangeListener(evt -> { update(); });

        btnReg.setOnAction( event -> {
            name = tf[0].getText().trim();
            user = tf[1].getText().trim();
            password = tf1.getText().trim();
            if(!name.isEmpty() && !user.isEmpty() && !password.isEmpty()) {
                ConnectionsTCP a = new ConnectionsTCP(model);
                model.setPageState(PagesEnum.SIGNUP);
                model.setSignUpAttributes(name, user, password);
                tf[0].clear();
                tf[1].clear();
                tf1.clear();
                a.start();
                model.ConnectionPage();
            }
        });

        btnCancel.setOnAction(event-> {
            model.InitialPage();
            tf[0].clear();
            tf[1].clear();
            tf1.clear();
        });


        DropShadow shadow = new DropShadow();

        btnReg.addEventHandler(MouseEvent.MOUSE_ENTERED,
                new EventHandler<MouseEvent>() {
                    @Override public void handle(MouseEvent e) {
                        btnReg.setEffect(shadow);
                        //accept.setStyle("-fx-border-color: #1a83c6; -fx-border-width: 2");
                    }
                });

        btnReg.addEventHandler(MouseEvent.MOUSE_EXITED,
                new EventHandler<MouseEvent>() {
                    @Override public void handle(MouseEvent e) {
                        btnReg.setEffect(null);
                    }
                });

        btnCancel.addEventHandler(MouseEvent.MOUSE_ENTERED,
                new EventHandler<MouseEvent>() {
                    @Override public void handle(MouseEvent e) {
                        btnCancel.setEffect(shadow);
                        //accept.setStyle("-fx-border-color: #1a83c6; -fx-border-width: 2");
                    }
                });

        btnCancel.addEventHandler(MouseEvent.MOUSE_EXITED,
                new EventHandler<MouseEvent>() {
                    @Override public void handle(MouseEvent e) {
                        btnCancel.setEffect(null);
                    }
                });

    }

    private void update() {

        if (model.getState() != TicketPdState.SIGNUPSTATE) {
            this.setVisible(false);
            return;
        }
        this.setVisible(true);
    }
}
