package pt.isec.pa.apoio_poe.ui.gui.states.alunos;

import javafx.collections.FXCollections;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import pt.isec.pa.apoio_poe.model.data.Alunos;

public class TableViewAlunos {
    private TableViewAlunos() {
    }

    public static TableView<Alunos> createTableView() {
        TableView<Alunos> aux = new TableView<>();
        TableColumn<Alunos, Long> colunaNumero = new TableColumn<>("Número");
        TableColumn<Alunos, String> colunaNome = new TableColumn<>("Nome");
        TableColumn<Alunos, String> colunaEmail = new TableColumn<>("E-mail");
        TableColumn<Alunos, String> colunaCurso = new TableColumn<>("Curso");
        TableColumn<Alunos, String> colunaRamo = new TableColumn<>("Ramo");
        TableColumn<Alunos, String> colunaClassificacao = new TableColumn<>("Classificação");
        TableColumn<Alunos, String> colunaApto = new TableColumn<>("Apto");

        colunaNumero.setCellValueFactory(new PropertyValueFactory<>("numero"));
        colunaNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        colunaEmail.setCellValueFactory(new PropertyValueFactory<>("email"));
        colunaCurso.setCellValueFactory(new PropertyValueFactory<>("curso"));
        colunaRamo.setCellValueFactory(new PropertyValueFactory<>("ramo"));
        colunaClassificacao.setCellValueFactory(new PropertyValueFactory<>("classificacao"));
        colunaApto.setCellValueFactory(new PropertyValueFactory<>("apto"));

        aux.setItems(FXCollections.observableArrayList());
        aux.getColumns().addAll(colunaNumero,colunaNome,colunaEmail,colunaCurso,colunaRamo,colunaClassificacao,colunaApto);
        aux.setPlaceholder(new Label("Sem dados"));
        return aux;
    }
}
