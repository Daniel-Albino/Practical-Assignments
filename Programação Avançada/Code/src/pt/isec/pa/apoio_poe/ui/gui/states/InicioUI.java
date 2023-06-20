package pt.isec.pa.apoio_poe.ui.gui.states;



import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import pt.isec.pa.apoio_poe.model.Manager;
import pt.isec.pa.apoio_poe.model.fsm.GPEState;
import pt.isec.pa.apoio_poe.ui.gui.resources.FontManager;
import pt.isec.pa.apoio_poe.ui.gui.resources.ImageManager;


import java.io.File;


public class InicioUI extends BorderPane {
    Manager manager;
    Button btnStart,btnLoad,btnExit;
    Font font;
    public InicioUI(Manager manager) {
        this.manager = manager;
        createViews();
        registerHandlers();
        update();
    }

    private void createViews() {
        font = FontManager.loadFont("BalsamiqSans-Regular.ttf",20);
        btnStart = new Button("Iniciar");
        btnStart.setMinWidth(600);
        btnStart.setPrefSize(40,40);
        btnStart.setFont(font);

        btnLoad = new Button("Carregar");
        btnLoad.setMinWidth(600);
        btnLoad.setPrefSize(40,40);
        btnLoad.setFont(font);

        btnExit  = new Button("Sair");
        btnExit.setMinWidth(600);
        btnExit.setPrefSize(40,40);
        btnExit.setFont(font);

        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        setMargin(vBox,new Insets(50,0,0,0));
        vBox.setSpacing(15);
        vBox.getChildren().addAll(ImageManager.getImageView("isec.png",240),btnStart,btnLoad,btnExit);

        this.setCenter(vBox);
    }

    private void registerHandlers() {
        manager.addPropertyChangeListener(evt -> { update(); });
        btnStart.setOnAction(event -> {
            manager.iniciar();
        });
        btnLoad.setOnAction(event ->{
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("File open...");
            fileChooser.setInitialDirectory(new File(".")); //Diretório corrente
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("PA (*.dat)","*.dat")
            );
            File file = fileChooser.showOpenDialog(this.getScene().getWindow());
            if(file != null){
                manager.load(file);
            }
        });
        btnExit.setOnAction(event -> {
            Platform.exit();
        });
    }

    private void update() {
        if(manager.getState() != GPEState.INICIO){
            this.setVisible(false);
            return;
        }
        this.setVisible(true);
    }
}
