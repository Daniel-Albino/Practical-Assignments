package pt.isec.pa.apoio_poe.ui.gui.states.propostas;

import javafx.collections.FXCollections;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import pt.isec.pa.apoio_poe.model.data.Proposta;

public class TableViewPropostas {
    private TableViewPropostas() {
    }

    public static TableView<Proposta> createTableView() {
        TableView<Proposta> aux = new TableView<>();
        TableColumn<Proposta, Long> colunaTipo = new TableColumn<>("Tipo");
        TableColumn<Proposta, String> colunaCId = new TableColumn<>("Código Identificação");
        TableColumn<Proposta, String> colunaTitulo = new TableColumn<>("Titulo");
        TableColumn<Proposta, String> colunaRamo = new TableColumn<>("Ramo");
        TableColumn<Proposta, String> colunaEmpresa = new TableColumn<>("Empresa");
        TableColumn<Proposta, String> colunaEDoc = new TableColumn<>("Email Docente");
        TableColumn<Proposta, String> colunaNAluno = new TableColumn<>("Nº Aluno ");


        colunaTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        colunaCId.setCellValueFactory(new PropertyValueFactory<>("codId"));
        colunaTitulo.setCellValueFactory(new PropertyValueFactory<>("titulo"));
        colunaEmpresa.setCellValueFactory(new PropertyValueFactory<>("idEmpresa"));
        colunaEDoc.setCellValueFactory(new PropertyValueFactory<>("emailDocente"));
        colunaRamo.setCellValueFactory(new PropertyValueFactory<>("ramo"));
        colunaNAluno.setCellValueFactory(new PropertyValueFactory<>("idAluno"));


        aux.setItems(FXCollections.observableArrayList());
        aux.getColumns().addAll(colunaTipo,colunaCId,colunaTitulo,colunaEmpresa,colunaEDoc,colunaRamo,colunaNAluno);
        aux.setPlaceholder(new Label("Sem dados"));
        return aux;
    }
}
