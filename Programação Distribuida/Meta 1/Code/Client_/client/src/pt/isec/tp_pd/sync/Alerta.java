package pt.isec.tp_pd.sync;

import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.util.Objects;
import java.util.concurrent.Callable;

public class Alerta implements Callable{

    private String error;
    private String message;
    public Alerta(String error, String message) {
        this.error = error; this.message = message;
    }

    @Override public String call(){
        Alert a = new Alert(Alert.AlertType.WARNING);
        a.setTitle(error);
        a.setHeaderText(null);
        a.setContentText(message);
        final Stage stage = (Stage) a.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image(Objects.requireNonNull(this.getClass().getResource("../ui/resources/images/isec_64x64.png")).toString()));
        a.showAndWait();
        return null;
    }
}
