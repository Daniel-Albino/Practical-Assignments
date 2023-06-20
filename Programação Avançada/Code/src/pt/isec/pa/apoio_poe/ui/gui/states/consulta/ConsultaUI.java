package pt.isec.pa.apoio_poe.ui.gui.states.consulta;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import pt.isec.pa.apoio_poe.model.Manager;
import pt.isec.pa.apoio_poe.model.fsm.GPEState;
import pt.isec.pa.apoio_poe.ui.gui.resources.FontManager;

import java.io.File;
import java.util.Map;


public class ConsultaUI extends BorderPane {
    Manager manager;
    PieChart pieChartRamos,pieChartAt;
    BarChart barChartEmpresas;
    BarChart barChartDocentes;
    private ChoiceBox<String> cbInfo;
    private String[] tipoInfoGrafs = {"Selecione...", "Distribuição dos projetos por ramos",
            "Top 5 - Empresas com mais estágios", "Top 5 - Docentes com mais orientações", "Percentagem de Propostas atribuidas e não atribuidas"};
    Label lbNameState, lbStatus;
    Font font;
    MenuBar menuBar;
    Menu mnEdit;
    MenuItem mnSave, mnExport;

    public ConsultaUI(Manager manager) {
        this.manager = manager;
        initializeVariables();
        createViews();
        registerHandlers();
        update();
    }

    private void initializeVariables() {
        font = FontManager.loadFont("BalsamiqSans-Regular.ttf", 28);

        cbInfo = new ChoiceBox<>();
        cbInfo.setPrefWidth(300);
        cbInfo.getItems().addAll(tipoInfoGrafs);
        cbInfo.setValue("Selecione...");

        pieChartRamos = new PieChart();
        pieChartAt = new PieChart();

        CategoryAxis xAxis1    = new CategoryAxis();
        xAxis1.setLabel("Número Estagiários");
        NumberAxis yAxis1 = new NumberAxis();
        yAxis1.setLabel("Empresa");
        barChartEmpresas = new BarChart<>(xAxis1, yAxis1);

        CategoryAxis xAxis    = new CategoryAxis();
        xAxis.setLabel("Orientações");

        NumberAxis yAxis = new NumberAxis();
        yAxis.setLabel("Docentes");
        barChartDocentes = new BarChart<>(xAxis,yAxis);
    }

    private void createViews() {

        //Parte de cima da interface
        BorderPane borderTop = new BorderPane();
        borderTop.setPrefWidth(654);
        borderTop.setPrefHeight(79);

        lbNameState = new Label("Consulta");
        lbNameState.setFont(FontManager.loadFont("BalsamiqSans-Regular.ttf", 34));
        setMargin(lbNameState, new Insets(20, 0, 0, 50));
        borderTop.setLeft(lbNameState);

        menuBar = Toolbar();
        borderTop.setTop(menuBar);


        HBox hBoxButtomTop = new HBox(cbInfo);
        hBoxButtomTop.setPadding(new Insets(10, 0, 0, 160));
        hBoxButtomTop.setSpacing(20);
        hBoxButtomTop.setPrefWidth(600);
        hBoxButtomTop.setPrefHeight(27);
        borderTop.setBottom(hBoxButtomTop);


        lbStatus = new Label();
        lbStatus.setPrefWidth(Integer.MAX_VALUE);
        lbStatus.setStyle("-fx-background-color: #c0c0c0");
        lbStatus.setFont(FontManager.loadFont("BalsamiqSans-Regular.ttf", 20));
        lbStatus.setBorder(new Border(new BorderStroke(Color.DARKGRAY, BorderStrokeStyle.SOLID, CornerRadii.EMPTY
                , BorderWidths.DEFAULT)));
        lbStatus.setPadding(new Insets(10));

        this.setTop(borderTop);
        this.setBottom(lbStatus);

    }

    private MenuBar Toolbar() {
        mnEdit = new Menu("Edit");
        mnSave = new MenuItem("Salvar");
        mnSave.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
        mnExport = new MenuItem("Exportar");
        mnEdit.getItems().addAll(mnExport,mnSave);

        MenuBar menuBarAux = new MenuBar(mnEdit);
        menuBarAux.setUseSystemMenuBar(true);
        return menuBarAux;
    }


    private void registerHandlers() {
        manager.addPropertyChangeListener(evt -> {
            update();
        });
        cbInfo.setOnAction((event) -> {
            update();
        });
        mnSave.setOnAction(event ->{
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("File save...");
            fileChooser.setInitialDirectory(new File(".")); //Diretório corrente
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Salvar (*.dat)","*.dat")
            );
            File file = fileChooser.showSaveDialog(this.getScene().getWindow());
            if(file != null){
                manager.save(file);
            }
        });
        mnExport.setOnAction(event -> {
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("File save...");
            fileChooser.setInitialDirectory(new File("."));
            fileChooser.getExtensionFilters().addAll(
                    new FileChooser.ExtensionFilter("Consulta (*.csv)","*.csv")
            );
            File file = fileChooser.showSaveDialog(this.getScene().getWindow());
            if(file != null){
                manager.exportFile(file);
            }
        });
    }

    private void update() {
        pieChartRamos.getData().clear();
        pieChartAt.getData().clear();
        barChartDocentes.getData().clear();
        barChartEmpresas.getData().clear();

        switch (String.valueOf(cbInfo.getValue())) {
            case "Distribuição dos projetos por ramos" -> {
                pieChartRamos.setDisable(false);
                barChartDocentes.setDisable(true);
                barChartEmpresas.setDisable(true);
                pieChartAt.setDisable(true);
                ObservableList<PieChart.Data> data = FXCollections.observableArrayList(
                        new PieChart.Data("DA", manager.distAlunosRamos("DA")),
                        new PieChart.Data("SI", manager.distAlunosRamos("SI")),
                        new PieChart.Data("RAS", manager.distAlunosRamos("RAS")));
                pieChartRamos.setData(data);
                setMargin(pieChartRamos, new Insets(10, 80, 100, 40));
                this.setCenter(pieChartRamos);
            }
            case "Top 5 - Empresas com mais estágios" -> {
                pieChartRamos.setDisable(true);
                barChartDocentes.setDisable(true);
                barChartEmpresas.setDisable(false);
                pieChartAt.setDisable(true);

                Map<String, Integer> top5Emp = manager.getTop5EmpEstagio();
                XYChart.Series<String, Integer> aux = new XYChart.Series<>();
                if (top5Emp != null) {
                    for (Map.Entry<String, Integer> t : top5Emp.entrySet()) {
                        XYChart.Data<String, Integer> d = new XYChart.Data<>(t.getKey(), t.getValue());
                        aux.getData().add(d);
                    }
                    barChartEmpresas.getData().addAll(aux);
                } else {
                    barChartEmpresas.setTitle("Sem dados");
                }
                this.setCenter(barChartEmpresas);
            }
            case "Top 5 - Docentes com mais orientações" -> {
                pieChartRamos.setDisable(true);
                barChartDocentes.setDisable(false);
                barChartEmpresas.setDisable(true);
                pieChartAt.setDisable(true);

                Map<String, Integer> top5 = manager.getTop5Docentes();
                XYChart.Series<String, Integer> aux = new XYChart.Series<>();
                if (top5 != null) {
                    for (Map.Entry<String, Integer> t : top5.entrySet()) {
                        XYChart.Data<String, Integer> d = new XYChart.Data<>(t.getKey(), t.getValue());
                        aux.getData().add(d);
                    }
                    barChartDocentes.getData().add(aux);
                } else {
                    barChartDocentes.setTitle("Sem dados");
                }
                this.setCenter(barChartDocentes);
            }
            case "Percentagem de Propostas atribuidas e não atribuidas" -> {
                pieChartRamos.setDisable(true);
                barChartDocentes.setDisable(true);
                barChartEmpresas.setDisable(true);
                pieChartAt.setDisable(false);

                ObservableList<PieChart.Data> data = FXCollections.observableArrayList(
                        new PieChart.Data("Atribuido", manager.nPropAtNAt("Atribuido")),
                        new PieChart.Data("Não atribuido", manager.nPropAtNAt("Não atribuido")));
                pieChartRamos.setData(data);

                setMargin(pieChartRamos, new Insets(10, 80, 100, 40));
                this.setCenter(pieChartRamos);

            }
        }

        lbStatus.setText(String.format("Alunos: %d Docentes: %d Propostas: %d Candidaturas: %d Atribuição de Propostas: %d Atribuição de Orientadores: %d",
                manager.nAlunos(), manager.nDocentes(), manager.nPropostas(), manager.nCandidaturas(), manager.nAtPropostas(), manager.nAtOrientadores()));
        this.setBackground(new Background(new BackgroundFill(Color.LIGHTGREY, CornerRadii.EMPTY, Insets.EMPTY)));

        if (manager.getState() != GPEState.CONSULTA) {
            this.setVisible(false);
            return;
        }
        this.setVisible(true);
    }
}

