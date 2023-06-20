package pt.isec.pa.apoio_poe.ui.gui;

import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;
import pt.isec.pa.apoio_poe.model.Manager;
import pt.isec.pa.apoio_poe.ui.gui.states.ConfiguracaoUI;
import pt.isec.pa.apoio_poe.ui.gui.states.alunos.GestaoAlunosUI;
import pt.isec.pa.apoio_poe.ui.gui.states.InicioUI;
import pt.isec.pa.apoio_poe.ui.gui.states.atorientadores.AtOrientadoresUI;
import pt.isec.pa.apoio_poe.ui.gui.states.atpropostas.AddAutoAtPropostas;
import pt.isec.pa.apoio_poe.ui.gui.states.atpropostas.AtPropostasUI;
import pt.isec.pa.apoio_poe.ui.gui.states.candidaturas.GestaoCandidaturasUI;
import pt.isec.pa.apoio_poe.ui.gui.states.consulta.ConsultaUI;
import pt.isec.pa.apoio_poe.ui.gui.states.docentes.GestaoDocentesUI;
import pt.isec.pa.apoio_poe.ui.gui.states.propostas.GestaoPropostasUI;

public class RootPane extends BorderPane {
    Manager manager;

    public RootPane(Manager manager) {
        this.manager = manager;
        createViews();
        registerHandlers();
        update();
    }

    private void createViews() {
        StackPane stackPane = new StackPane(
                new InicioUI(manager),
                new ConfiguracaoUI(manager),
                new GestaoAlunosUI(manager),
                new GestaoDocentesUI(manager),
                new GestaoPropostasUI(manager),
                new GestaoCandidaturasUI(manager),
                new AtPropostasUI(manager),
                new AddAutoAtPropostas(manager),
                new AtOrientadoresUI(manager),
                new ConsultaUI(manager)
        );
        this.setCenter(stackPane);
    }

    private void registerHandlers() {
    }

    private void update() {
    }
}
