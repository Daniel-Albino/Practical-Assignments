package pt.isec.tp_gps.ui.seccoes;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import pt.isec.tp_gps.model.Decorator;
import pt.isec.tp_gps.model.data.UtentesPorMedicamento;
import pt.isec.tp_gps.model.fsm.MEEstados;

import java.util.List;
import java.util.Map;


public class PrincipalUI extends BorderPane {

    Decorator model;

    Label lb;

    TableView tableView;

    final CategoryAxis xAxis = new CategoryAxis();
    final NumberAxis yAxis = new NumberAxis();

    XYChart.Series series;
    LineChart<String,Number> lineChart;
    //LineChart<String,Number> lineChart1;

    HBox hb;


    public PrincipalUI(Decorator model) {
        lineChart = new LineChart<String,Number>(xAxis,yAxis);
        xAxis.setLabel("Mês");
        series = new XYChart.Series();
        this.model = model;
        createViews();
        registerHandlers();
        update();
    }




    private void createViews() {



        lb = new Label("Principal");
        lb.setTextFill(Color.DARKBLUE);
        lb.setId("Special");



        lineChart.setTitle("Crescimento de utentes");
        lineChart.animatedProperty().set(true);
        XYChart.Series series = new XYChart.Series();
        series.setName("NºUtentes");
        series.getData().add(new XYChart.Data("Jan", 0));
        series.getData().add(new XYChart.Data("Feb", 0));
        series.getData().add(new XYChart.Data("Mar", 0));
        series.getData().add(new XYChart.Data("Apr", 0));
        series.getData().add(new XYChart.Data("May", 0));
        series.getData().add(new XYChart.Data("Jun", 0));
        series.getData().add(new XYChart.Data("Jul", 0));
        series.getData().add(new XYChart.Data("Aug", 0));
        series.getData().add(new XYChart.Data("Sep", 0));
        series.getData().add(new XYChart.Data("Oct", 0));
        series.getData().add(new XYChart.Data("Nov", 0));
        series.getData().add(new XYChart.Data("Dec", 0));
        lineChart.getData().add(series);
        lineChart.setPrefWidth(Integer.MAX_VALUE);
        lineChart.setPrefHeight(500);



        tableView = new TableView<>();
        TableColumn<UtentesPorMedicamento,String> medicamento = new TableColumn<UtentesPorMedicamento,String>("Nome(Medicamento)");
        medicamento.setCellValueFactory(new PropertyValueFactory<UtentesPorMedicamento,String>("nomeMedicamento"));
        TableColumn<UtentesPorMedicamento,Integer> nUte = new TableColumn<UtentesPorMedicamento,Integer>("N.Utentes \nque tomam \nmedicamento hoje");
        nUte.setCellValueFactory(new PropertyValueFactory<UtentesPorMedicamento,Integer>("quantidade"));
        tableView.setPlaceholder(new Label("Sem dados"));



        tableView.getColumns().add(medicamento);
        tableView.getColumns().add(nUte);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        tableView.setPrefWidth(Integer.MAX_VALUE);
        tableView.setMaxHeight(500);



        hb = new HBox(lineChart,tableView);

        VBox Arrange = new VBox(lb,hb);
        Arrange.setPadding(new Insets(10,10,0,20));
        Arrange.setAlignment(Pos.TOP_LEFT);
        Arrange.setSpacing(20);
        this.setCenter(Arrange);

    }

    private void registerHandlers() {
        model.addPropertyChangeListener(evt -> { update(); });
    }

    private void update() {
        if (model.getState() != MEEstados.ESTADOPRINCIPAL) {
            this.setVisible(false);
            return;
        }
        this.setVisible(true);

        tableView.getItems().clear();
        List<UtentesPorMedicamento> NUtentesPorMedicamento =  model.getNUtentesPorMedicamento();
        if(NUtentesPorMedicamento.size() != 0) {
            for (UtentesPorMedicamento u : NUtentesPorMedicamento) {
                tableView.getItems().add(u);
            }
        }






        Platform.runLater(()->{
                Map<Integer, Integer> numeroUtentesPorMes = model.getNumeroUtentesPorMes();
                System.out.println("Entrei no update-> "+numeroUtentesPorMes);

                lineChart = new LineChart<String,Number>(xAxis,yAxis);
                lineChart.setTitle("Crescimento de utentes");
                lineChart.animatedProperty().set(true);
                series = new XYChart.Series();
                series.setName("NºUtentes");
                for(Integer s : numeroUtentesPorMes.keySet()){
                    String mes = obterMes(s);
                    series.getData().add(new XYChart.Data(mes, numeroUtentesPorMes.get(s)));
                }
                lineChart.getData().add(series);
                lineChart.setPrefWidth(Integer.MAX_VALUE);
                lineChart.setPrefHeight(500);
                hb.getChildren().set(0,lineChart);
            });


    }

    private String obterMes(int month) {
        return switch (month){
            case 1 -> "Jan";
            case 2 -> "Feb";
            case 3 -> "Mar";
            case 4 -> "Apr";
            case 5 -> "May";
            case 6 -> "Jun";
            case 7 -> "Jul";
            case 8 -> "Aug";
            case 9 -> "Sep";
            case 10 -> "Oct";
            case 11 -> "Nov";
            case 12 -> "Dec";
            default -> "";
        };
    }

}