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

public class LoginUI extends BorderPane {
    ModelManager model;
    TextField username;
    PasswordField password;
    Button btnEnt, btnCancel;
    Label lb;
    String user, pass;
    public LoginUI(ModelManager model) {
        this.model=model;
        createViews();
        registerHandlers();
        update();

    }

    private void createViews() {

        lb = new Label("Credentials:");
        lb.setFont(FontManager.loadFont("Bekinder Bold.otf",20));
        lb.setTextFill(Color.WHITE);

        username = new TextField();
        username.setPrefWidth(Integer.MAX_VALUE);
        username.setMaxWidth(350);
        username.focusTraversableProperty().set(false);
        username.setPrefHeight(40);
        username.setPromptText("Username");

        password = new PasswordField();
        password.setPrefWidth(Integer.MAX_VALUE);
        password.setMaxWidth(350);
        password.focusTraversableProperty().set(false);
        password.setPrefHeight(40);
        password.setPromptText("Password");

        btnEnt = new Button("Entrar");
        btnEnt.focusTraversableProperty().set(false);
        btnEnt.setStyle("-fx-background-color: rgba(0,95,213,0.98)");
        btnEnt.setPrefHeight(50);
        btnEnt.setPrefWidth(Integer.MAX_VALUE);
        btnEnt.setFont(FontManager.loadFont("Bekinder.otf",15));
        btnEnt.setTextFill(Color.WHITE);
        btnCancel = new Button("Cancel");
        btnCancel.focusTraversableProperty().set(false);
        btnCancel.setStyle("-fx-background-color: rgb(171,38,38)");
        btnCancel.setPrefHeight(50);
        btnCancel.setPrefWidth(Integer.MAX_VALUE);
        btnCancel.setFont(FontManager.loadFont("Bekinder.otf",14));
        btnCancel.setTextFill(Color.WHITE);



        VBox vbox = new VBox();
        vbox.setSpacing(20);
        vbox.getChildren().addAll(lb,username,password,new Separator(),btnEnt,btnCancel);
        vbox.setAlignment(Pos.CENTER);
        vbox.setMaxWidth(400);
        vbox.setMaxHeight(350);
        vbox.getStyleClass().add("blurBox");

        setCenter(vbox);




    }

    private void registerHandlers() {
        model.addPropertyChangeListener(evt -> { update(); });

        btnEnt.setOnAction( event -> {
            user = username.getText().trim();
            pass = password.getText().trim();
            if(!user.isEmpty() && !pass.isEmpty()) {
                ConnectionsTCP a = new ConnectionsTCP(model);
                if(user.equals("admin")){
                    model.setPageState(PagesEnum.REQUESTADMIN);
                }else{
                    model.setPageState(PagesEnum.LOGIN);
                }
                model.setLoginAttributes(user,pass);
                a.start();
                username.clear();
                password.clear();
                model.ConnectionPage();
            }
        });
        btnCancel.setOnAction(event->{
            model.InitialPage();
            username.clear();
            password.clear();
        });

        DropShadow shadow = new DropShadow();

        btnEnt.addEventHandler(MouseEvent.MOUSE_ENTERED,
                new EventHandler<MouseEvent>() {
                    @Override public void handle(MouseEvent e) {
                        btnEnt.setEffect(shadow);
                        //accept.setStyle("-fx-border-color: #1a83c6; -fx-border-width: 2");
                    }
        });

//Removing the shadow when the mouse cursor is off
        btnEnt.addEventHandler(MouseEvent.MOUSE_EXITED,
                new EventHandler<MouseEvent>() {
                    @Override public void handle(MouseEvent e) {
                        btnEnt.setEffect(null);
                    }
        });



        //Adding the shadow when the mouse cursor is on
        btnCancel.addEventHandler(MouseEvent.MOUSE_ENTERED,
                new EventHandler<MouseEvent>() {
                    @Override public void handle(MouseEvent e) {
                        btnCancel.setEffect(shadow);
                        //accept.setStyle("-fx-border-color: #1a83c6; -fx-border-width: 2");
                    }
        });

        //Removing the shadow when the mouse cursor is off
        btnCancel.addEventHandler(MouseEvent.MOUSE_EXITED,
                new EventHandler<MouseEvent>() {
                    @Override public void handle(MouseEvent e) {
                        btnCancel.setEffect(null);
                    }
        });


    }

    private void update() {
        if (model.getState() != TicketPdState.LOGINSTATE) {
            this.setVisible(false);
            return;
        }
        this.setVisible(true);
    }
}
