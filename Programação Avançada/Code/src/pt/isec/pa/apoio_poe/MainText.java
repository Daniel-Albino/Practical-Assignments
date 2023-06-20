package pt.isec.pa.apoio_poe;


import pt.isec.pa.apoio_poe.model.fsm.GPEContext;
import pt.isec.pa.apoio_poe.ui.text.UI;

public class MainText {
    public static void main(String []args){
        GPEContext fsm = new GPEContext();
        UI ui = new UI(fsm);
        ui.start();
    }
}