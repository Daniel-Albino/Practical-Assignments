package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.DataManager;
import pt.isec.pa.apoio_poe.model.data.Erros;

import java.io.*;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Consulta extends GPEStateAdapter implements Serializable {

    protected Consulta(GPEContext context, DataManager data) {
        super(context, data);
    }

    @Override
    public String alunosAtribuidos() {
        return data.alunosAtribuidos();
    }

    @Override
    public String alunosNAtribuidos() {
        return data.alunosNAtribuidos();
    }

    @Override
    public LinkedHashMap<String, Integer> getTop5Docentes() {
        return data.getTop5Docentes();
    }

    @Override
    public LinkedHashMap<String, Integer> getTop5EmpEstagio(){
        return data.getTop5EmpEstagio();
    }

    @Override
    public int nPropAtNAt(String s) {
        return data.nPropAtNAt(s);
    }

    @Override
    public String propDisponiveis() {
        return data.propDisponiveis();
    }
    @Override
    public String orientacoesPorDocente(){return data.orientacoesPorDocente();}
    @Override
    public float mediaOrientacoes(){return data.mediaOrientacoes();}
    @Override
    public int minimoOrientacoes(){return data.minimoOrientacoes();}
    @Override
    public int maximoOrientacoes(){return data.maximoOrientacoes();}
    @Override
    public int numeroOrientacoesDocente(String docente){return data.numeroOrientacoesDocente(docente);}

    @Override
    public boolean exportFile(File f){

        FileWriter fw = null;
        try {
            fw = new FileWriter(f);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);

            pw.println(data.alunosAtribuidos());

            pw.println(data.alunosNAtribuidos());

            pw.println(data.propDisponiveis());

            pw.println(data.propAtribuidas());

            pw.println(data.orientacoesPorDocente());

            float auxM = data.mediaOrientacoes();

            if(auxM > -1)
                pw.printf("Media de orientações: %f\n",auxM);

            int aux;

            aux = data.minimoOrientacoes();

            if(aux > -1)
                pw.printf("Minimo de orientações: %d\n",aux);

            aux = data.maximoOrientacoes();

            if(aux > -1)
                pw.printf("Máximo de orientações: %d\n",aux);

            pw.flush();
            fw.close();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean terminar() {
        changeState(GPEState.INICIO);
        return true;
    }

    @Override
    public GPEState getState() {
        return GPEState.CONSULTA;
    }
}
