package pt.isec.tp_pd.ui.states;

import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import pt.isec.tp_pd.data.Espetaculo;
import pt.isec.tp_pd.data.Lugar;
import pt.isec.tp_pd.model.ModelManager;
import pt.isec.tp_pd.model.fsm.TicketPdState;
import pt.isec.tp_pd.ui.resources.FontManager;

import java.util.List;
import java.util.Map;
import java.util.Objects;



public class OrdersUI extends BorderPane {

    private static final int NR_BT = 10;
    private int ind=0;
    private boolean another = true;

    private int count=0;
    private int rows=0;
    private int lugaresSize=0;




    ModelManager model;
    Label auth;
    Image img,img1,img2,img3;
    ImageView view,view1,view2,view3;
    Group gauth;
    Label show,category,date,hour,duration,local,localidade,pais,rating;
    Button consultaReserva, logout, pesquisaEspe;

    HBox hb,hb1,hb2;
    VBox vb,vb1,vb2,vb3;
    List<Espetaculo> espetaculos = null;


    String BSid;


    Label Row;
    ComboBox comboBox;
    Image ButtonAddUser;
    Button[] buttons;

    String[][] arr_b;


    HBox hseats;
    VBox vseats;

    Button reserva, pagar;


    public OrdersUI(ModelManager model) {
        this.model=model;
        createViews();
        registerHandlers();
        update();
    }

    private void createViews() {
        auth = new Label("Autenticado");
        img = new Image(Objects.requireNonNull(getClass().getResourceAsStream("../resources/images/authorize.png")),32,32,false,false);
        view = new ImageView(img);
        view.setPreserveRatio(true);
        auth.setGraphic(view);
        auth.setTextFill(Color.BEIGE);
        gauth = new Group();
        gauth.getChildren().add(auth);

        logout = new Button("Logout");
        consultaReserva = new Button("Consulta Reservas");
        pesquisaEspe = new Button("Pesquisa");

        img1 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("../resources/images/setas-duplas.png")),32,32,false,false);
        view1 = new ImageView(img1);
        view1.setTranslateX(130);




        hb1 = new HBox(gauth,logout,consultaReserva,pesquisaEspe,view1);
        hb1.setSpacing(15);
        hb1.setMaxWidth(750);
        hb1.getStyleClass().add("blurBox");
        hb1.setAlignment(Pos.CENTER);


        //Designação
        show = new Label("(SHOW) - ");
        show.setFont(FontManager.loadFont("KernlGrotesk-Bold.otf",32));
        show.setTextFill(Color.WHITE);
        //category.setAlignment(Pos.CENTER);
        show.setPadding(new Insets(10,0,0,0));

        //Categoria
        category = new Label("Categoria:");
        category.setFont(FontManager.loadFont("KernlGrotesk-Bold.otf",30));
        category.setTextFill(Color.WHITE);

        //Data
        date = new Label("Data:");
        date.setFont(FontManager.loadFont("KernlGrotesk-Bold.otf",16));
        date.setTextFill(Color.WHITE);

        //Hora
        hour = new Label("Hora:");
        hour.setFont(FontManager.loadFont("KernlGrotesk-Bold.otf",16));
        hour.setTextFill(Color.WHITE);


        //Duração
        duration = new Label("Duração:");
        duration.setFont(FontManager.loadFont("KernlGrotesk-Bold.otf",16));
        duration.setTextFill(Color.WHITE);

        //Local
        local = new Label("Local:");
        local.setFont(FontManager.loadFont("KernlGrotesk-Bold.otf",16));
        local.setTextFill(Color.WHITE);

        //Localidade
        localidade = new Label("Localidade:");
        localidade.setFont(FontManager.loadFont("KernlGrotesk-Bold.otf",16));
        localidade.setTextFill(Color.WHITE);

        //País
        pais = new Label("País:");
        pais.setFont(FontManager.loadFont("KernlGrotesk-Bold.otf",16));
        pais.setTextFill(Color.WHITE);

        //Classificação
        rating = new Label("Classificação etária:");
        rating.setFont(FontManager.loadFont("KernlGrotesk-Bold.otf",16));
        rating.setTextFill(Color.WHITE);
        rating.setPadding(new Insets(10));




        hb = new HBox(date, hour, duration);
        hb.setSpacing(20);
        hb.setAlignment(Pos.CENTER);

        vb = new VBox(local, localidade, pais);
        vb.setSpacing(10);



        vb1 = new VBox(show,category,new Separator(),hb,rating,vb);//area
        vb1.setSpacing(2);
        vb1.setAlignment(Pos.CENTER);
        vb1.minWidth(500);
        vb1.setMaxWidth(750);
        vb1.setMaxHeight(120);
        vb1.getStyleClass().add("blurBoxOrder");
        setAlignment(vb1,Pos.TOP_CENTER);

        vb2 = new VBox(hb1,vb1);
        vb2.setSpacing(5);
        vb2.setAlignment(Pos.CENTER);
        vb2.minWidth(500);
        vb2.setMaxWidth(750);
        vb2.setMaxHeight(50);
        setPadding(new Insets(10));
        setAlignment(vb2,Pos.TOP_CENTER);

        //=============


        //added...
        Row = new Label("Fila");
        Row.setFont(FontManager.loadFont("KernlGrotesk-Bold.otf",16));
        Row.setTextFill(Color.WHITE);

        comboBox = new ComboBox();

        img2 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("../resources/images/seta-para-baixo.png")),32,32,false,false);
        view2 = new ImageView(img2);
        img3 = new Image(Objects.requireNonNull(getClass().getResourceAsStream("../resources/images/seta-para-cima.png")),32,32,false,false);
        view3 = new ImageView(img3);


        vb3 = new VBox(view3,view2);
        vb3.setSpacing(5);


        reserva = new Button("Reservar");
        pagar = new Button("Pagar");


        //added..
        ButtonAddUser = new Image(Objects.requireNonNull(getClass().getResourceAsStream("../resources/images/chair.png")),64,64,false,false);

        buttons = new Button[NR_BT];

        for(int i=0;i<NR_BT ;++i){
            buttons[i] = new Button("Lugar");
            buttons[i].setId("b"+i);
            buttons[i].setGraphic(new ImageView(ButtonAddUser));
            buttons[i].setContentDisplay(ContentDisplay.TOP);
        }


        hseats = new HBox();
        for(int i=0;i<NR_BT ;++i){
            hseats.getChildren().add(buttons[i]);
        }
        hseats.setSpacing(10);
        hseats.setAlignment(Pos.CENTER);
        hseats.setMaxHeight(70);


        hb2 = new HBox(reserva, pagar, vb3);
        hb2.setSpacing(10);

        vseats = new VBox(Row,comboBox,hseats,hb2);
        vseats.setSpacing(10);
        vseats.setMaxHeight(90);
        vseats.getStyleClass().add("blurBox");
        this.setCenter(vseats);

        //setTop da primeira VBox
        this.setTop(vb2);



    }

    private void registerHandlers() {
        model.addPropertyChangeListener(evt -> { update(); });

        view1.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                another = true;
                if(ind < espetaculos.size()-1)
                    ind++;
                else
                    ind=0;
                
                model.setIdespe(ind);

                count=0;
                update();
            }

        });

        view2.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if((count+1) < rows && rows != 1)
                    count++;
                update();
            }

        });

        view3.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if((count-1)>=0 && rows != 1)
                    count--;
                update();
            }

        });

        gauth.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                model.EditPage();
            }
        });

        logout.setOnAction(event-> System.out.println("clickedaaa"));
        pesquisaEspe.setOnAction(event -> {
            model.SearchPage();
        });

        comboBox.setOnAction(event-> {
            count = 0;
            update();
        });

        reserva.setOnAction(event->{
            model.setfila(comboBox.getValue().toString());
            for(int i=0;i<10;++i)
                if(buttons[i].getId().equals(BSid)) {
                    if(buttons[i].getText().charAt(4) == ' ') {
                        //TODO::selLugar
                        model.setlugar(String.valueOf(buttons[i].getText().charAt(2) +""+ buttons[i].getText().charAt(3)));
                    }else
                        model.setlugar(String.valueOf(buttons[i].getText().charAt(2)));
                    break;
                }
            model.setPageState(PagesEnum.RESERVE);
        });

        buttons[0].setOnAction(event->{BSid = buttons[0].getId();});
        buttons[1].setOnAction(event->{BSid = buttons[1].getId();});
        buttons[2].setOnAction(event->{BSid = buttons[2].getId();});
        buttons[3].setOnAction(event->{BSid = buttons[3].getId();});
        buttons[4].setOnAction(event->{BSid = buttons[4].getId();});
        buttons[5].setOnAction(event->{BSid = buttons[5].getId();});
        buttons[6].setOnAction(event->{BSid = buttons[6].getId();});
        buttons[7].setOnAction(event->{BSid = buttons[7].getId();});
        buttons[8].setOnAction(event->{BSid = buttons[8].getId();});
        buttons[9].setOnAction(event->{BSid = buttons[9].getId();});

    }

    private void update() {
        if (model.getState() != TicketPdState.ORDERSSTATE) {
            this.setVisible(false);
            return;
        }
        this.setVisible(true);

        espetaculos = model.getEspetaculos();

        if(espetaculos.size() > 0) {
            Platform.runLater(() -> {

            show.setText("SHOW - " + espetaculos.get(ind).getDescricao());
            category.setText("Categoria: " + espetaculos.get(ind).getTipo());
            date.setText("Data: " + espetaculos.get(ind).getData());
            hour.setText("Hora: " + espetaculos.get(ind).getHora());
            duration.setText("Duração: " + espetaculos.get(ind).getDuracao());
            local.setText("Local: " + espetaculos.get(ind).getLocal());
            localidade.setText("Localidade: " + espetaculos.get(ind).getLocalidade());
            pais.setText("País: " + espetaculos.get(ind).getPais());
            rating.setText("Classificação: " + espetaculos.get(ind).getClassificacao_etaria());

            if(espetaculos.get(ind).getLugares().size() == 0){
                comboBox.getItems().clear();
                for(int t=0;t<10;++t) {
                    buttons[t].setText("---");
                    buttons[t].setDisable(true);
                }

            }else {
                if (another) {
                    comboBox.getItems().clear();
                    boolean first = true;
                    for (String i : espetaculos.get(ind).getLugares().keySet()) {
                        if (first) {
                            comboBox.setValue(i);
                            first = false;
                        }
                        comboBox.getItems().add(i);
                    }
                    another = false;
                }

                for (int t = 0; t < 10; ++t) {
                    buttons[t].setText("---");
                    buttons[t].setDisable(false);
                }

                
                lugaresSize = espetaculos.get(ind).getLugares().get(comboBox.getValue().toString()).size();
                System.out.println("size"+lugaresSize);
                if(lugaresSize <= 10){
                    rows = 1;
                    arr_b = new String[rows][10];
                    int i=0;
                    for(Lugar l : espetaculos.get(ind).getLugares().get(comboBox.getValue().toString())) {
                        if(l.isOcupado()){
                            arr_b[0][i] = "Ocupado";   
                            ++i;
                        }else {
                            if (i >= lugaresSize) {
                                arr_b[0][i] = "---";
                            } else {
                                arr_b[0][i] = "L-" + l.getAssento() + " : " + l.getPreco() + "€";
                                ++i;
                            }
                        }
                    }

                    for(int k=i;k<10;++k){
                        arr_b[0][k] = "---";
                    }

                    for(int k =0;k<1;++k){
                        for(int j=0;j<10;++j) {
                            System.out.println(" "+arr_b[k][j]);
                        }
                    }


                }else {
                    if (lugaresSize % 10 != 0) {
                        rows = (lugaresSize / 10) + 1;
                    } else {
                        rows = lugaresSize / 10;
                    }
                    arr_b = new String[rows][10];

                    int o=0;
                    int p=0;
                    for(Lugar l : espetaculos.get(ind).getLugares().get(comboBox.getValue().toString())) {
                        if(l.isOcupado()){
                            arr_b[o][p] = "Ocupado";
                            if (p >= 9) {
                                p = 0;
                                o += 1;
                            } else {
                                p += 1;
                            }
                        }else {
                            arr_b[o][p] = "L-" + l.getAssento() + " : " + l.getPreco() + "€";
                            if (p >= 9) {
                                p = 0;
                                o += 1;
                            } else {
                                p += 1;
                            }
                        }

                    }
                    for(int e = o;o<rows;++o){
                        for(int y=p;y<10;++y){
                            arr_b[e][y] = "---";
                        }
                    }

                }


                for(int t=0;t<10;++t){
                    buttons[t].setText(arr_b[count][t]);
                }



                for (int t = 0; t < 10; ++t)
                    if (buttons[t].getText().equals("---") || buttons[t].getText().equals("Ocupado"))
                        buttons[t].setDisable(true);
            }

            });
        }



    }


}

