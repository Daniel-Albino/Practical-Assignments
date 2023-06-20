package pt.isec.tp_pd.ui.states;

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

public class ConsultaReservesUI extends BorderPane {

    ModelManager model;
    Button btnSignup,btnLogin;
    Label lb;
    TextField ipAddress,listeningPort;
    String ip, port;
    public ConsultaReservesUI(ModelManager model) {
        this.model=model;
        createViews();
        registerHandlers();
        update();
    }

    private void createViews() {
        lb = new Label("Escolha um servidor:");
        lb.setFont(FontManager.loadFont("KernlGrotesk-Bold.otf",18));
        lb.setTextFill(Color.WHITE);

        ipAddress = new TextField();
        ipAddress.setPrefWidth(Integer.MAX_VALUE);
        ipAddress.setMaxWidth(200);
        ipAddress.focusTraversableProperty().set(false);
        ipAddress.setPrefHeight(20);
        ipAddress.setPromptText("IP-> Ex:(127.0.0.1) | (localhost)");

        listeningPort = new TextField();
        listeningPort.setPrefWidth(Integer.MAX_VALUE);
        listeningPort.setMaxWidth(200);
        listeningPort.focusTraversableProperty().set(false);
        listeningPort.setPrefHeight(20);
        listeningPort.setPromptText("Porto de escuta-> Ex:(5001)");


        btnLogin = new Button("Entrar");
        btnLogin.setMinWidth(80);
        btnSignup  = new Button("Registo");
        btnSignup.setMinWidth(80);
        HBox hbox = new HBox(btnLogin,btnSignup);
        hbox.setAlignment(Pos.CENTER);
        hbox.setSpacing(20);

        VBox confNet = new VBox(lb,ipAddress,listeningPort,hbox);
        confNet.setSpacing(20);
        confNet.setAlignment(Pos.CENTER);
        confNet.setMaxWidth(300);
        confNet.setMaxHeight(130);
        confNet.getStyleClass().add("blurBox");
        this.setCenter(confNet);
    }

    private void registerHandlers() {
        model.addPropertyChangeListener(evt -> { update(); });

        btnLogin.setOnAction( event -> {
            ip = ipAddress.getText().trim();
            port = listeningPort.getText().trim();
            if(!ip.isEmpty() && !port.isEmpty()) {

                model.setAttributes(ipAddress.getText(), listeningPort.getText());
                ipAddress.clear();
                listeningPort.clear();
                model.ConnectionPage();

            }
        });

        btnSignup.setOnAction( event -> {
            ip = ipAddress.getText().trim();
            port = listeningPort.getText().trim();
            if(!ip.isEmpty() && !port.isEmpty()) {

                model.setAttributes(ipAddress.getText(), listeningPort.getText());
                ipAddress.clear();
                listeningPort.clear();
                model.ConnectionPage();

            }
        });



    }

    private void update() {
        if (model.getState() != TicketPdState.REVIEWRESERVE) {
            this.setVisible(false);
            return;
        }
        this.setVisible(true);
    }
}
