package pt.isec.tp_gps.ui.seccoes;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

public class CreditsUI extends HBox{

    public CreditsUI() {
        createViews();
        registerHandlers();
        update();
    }

    private void createViews() {
        setAlignment(Pos.CENTER);
        setPadding(new Insets(10));
        setBackground(new Background(new BackgroundFill(Color.LIGHTCYAN,CornerRadii.EMPTY, Insets.EMPTY)));

        Label lbOut = new Label("Powered by G14-GPS");
        lbOut.setId("credits");
        lbOut.setAlignment(Pos.CENTER);
        lbOut.setMinWidth(100);


        /*
        hb = new HBox(lbOut);
        hb.setAlignment(Pos.CENTER);
        hb.setPrefWidth(Integer.MAX_VALUE);
        */
        this.getChildren().add(lbOut);
    }

    private void registerHandlers() {}
    private void update() {}
}
