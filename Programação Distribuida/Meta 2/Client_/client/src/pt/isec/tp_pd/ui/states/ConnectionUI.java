package pt.isec.tp_pd.ui.states;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import pt.isec.tp_pd.model.ModelManager;
import pt.isec.tp_pd.model.fsm.TicketPdState;
import pt.isec.tp_pd.ui.resources.FontManager;

public class ConnectionUI extends BorderPane {
    ModelManager model;
    Label lb;

    public ConnectionUI(ModelManager model) {
        this.model=model;
        createViews();
        registerHandlers();
        update();
    }

    private void createViews() {

        lb = new Label("Connecting...");
        lb.setFont(FontManager.loadFont("KernlGrotesk-Bold.otf",35));
        lb.setTextFill(Color.WHITE);


        VBox confNet = new VBox(lb);
        confNet.setSpacing(20);
        confNet.setAlignment(Pos.CENTER);
        confNet.setMaxWidth(Integer.MAX_VALUE);
        confNet.setMaxHeight(Integer.MAX_VALUE);
        confNet.getStyleClass().add("blurBox");
        this.setCenter(confNet);
    }

    private void registerHandlers() {
        model.addPropertyChangeListener(evt -> { update(); });
    }

    private void update() {

        if (model.getState() != TicketPdState.CONNECTIONSTATE) {
            this.setVisible(false);
            return;
        }
        this.setVisible(true);
    }


}
