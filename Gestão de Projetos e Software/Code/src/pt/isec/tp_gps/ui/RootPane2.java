package pt.isec.tp_gps.ui;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import pt.isec.tp_gps.model.Decorator;
import pt.isec.tp_gps.ui.resources.CSSManager;
import pt.isec.tp_gps.ui.seccoes.CreditsUI;

public class RootPane2 extends BorderPane {
    Decorator model;
    BorderPane childPane;
    public RootPane2(Decorator model, BorderPane childPane)  {
        this.model = model;
        this.childPane = childPane;
        createViews();
        registerHandlers();
        update();
    }
    private void createViews() {
        CSSManager.applyCSS(this,"styles.css");
        StackPane stackPane = new StackPane(
                childPane
        );
        this.setCenter(stackPane);
        setBottom(new CreditsUI());
    }

    private void registerHandlers() {
        model.addPropertyChangeListener(evt -> {update();});
    }
    private void update() {}
}
