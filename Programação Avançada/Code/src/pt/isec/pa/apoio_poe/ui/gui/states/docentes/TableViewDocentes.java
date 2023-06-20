package pt.isec.pa.apoio_poe.ui.gui.states.docentes;

import javafx.collections.FXCollections;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import pt.isec.pa.apoio_poe.model.data.Docentes;

public class TableViewDocentes {
    private TableViewDocentes() {
    }

    public static TableView<Docentes> createTableView() {
        TableView<Docentes> aux = new TableView<>();
        TableColumn<Docentes, String> colunaNome = new TableColumn<>("Nome");
        TableColumn<Docentes, String> colunaEmail = new TableColumn<>("E-mail");

        colunaNome.setCellValueFactory(new PropertyValueFactory<>("nomeDocente"));
        colunaEmail.setCellValueFactory(new PropertyValueFactory<>("emailDocente"));

        aux.setItems(FXCollections.observableArrayList());
        aux.getColumns().addAll(colunaNome,colunaEmail);
        aux.setPlaceholder(new Label("Sem dados"));
        return aux;
    }
}
