package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.DataManager;
import pt.isec.pa.apoio_poe.model.data.Erros;
import pt.isec.pa.apoio_poe.model.data.Proposta;

import java.io.*;
import java.util.Arrays;
import java.util.List;

public class GestaoPropostas extends GPEStateAdapter implements Serializable {

    protected GestaoPropostas(GPEContext context, DataManager data) {
        super(context, data);
    }

    @Override
    public Erros addProposta(List<String> values) {
        return data.addProposta(values);
    }

    @Override
    public Erros removeProposta(String idProposta) {
        return data.removeProposta(idProposta);
    }

    @Override
    public Erros setRamo(String idProposta, String novoRamo){
        return data.setRamo(idProposta, novoRamo);
    }

    @Override
    public Erros setTitulo(String idProposta, String novoTitulo){
        return data.setTitulo(idProposta, novoTitulo);
    }

    @Override
    public String getTodasPropostas() {
        return data.getTodasPropostas();
    }

    @Override
    public String getProposta(String idProposta) {
        return data.getProposta(idProposta);
    }

    @Override
    public List<Proposta> getPropostas() {
        return data.getPropostas();
    }

    @Override
    public Proposta getProp(String idProposta) {
        return data.getProp(idProposta);
    }

    @Override
    public boolean importFile(File f){
       try (BufferedReader br = new BufferedReader(new FileReader(f))) {
           String linha;
           while ((linha = br.readLine()) != null) {
               List<String> info = Arrays.asList(linha.split(","));
               data.addProposta(info);
           }
       } catch (Exception e) {
           System.err.println("Error" );
           return false;
       }
       return true;
    }

    @Override
    public boolean exportFile(File f){

        FileWriter fw = null;
        try {
            fw = new FileWriter(f);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);

            for (Proposta a : data.getPropostas())
                pw.println(a.toString());

            pw.flush();
            fw.close();
        } catch (Exception e) {
            return false;
        }
        return true;
        //return data.exportFile(type,filename);
    }

    @Override
    public boolean isFechado() {
        return data.isFechado(GPEState.CONFIGURACAO);
    }

    @Override
    public boolean voltar() {
        changeState(GPEState.CONFIGURACAO);
        return true;
    }



    @Override
    public GPEState getState() {
        return GPEState.GESTAO_PROPOSTAS;
    }
}
