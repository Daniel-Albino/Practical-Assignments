package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.DataManager;
import pt.isec.pa.apoio_poe.model.data.Data;
import pt.isec.pa.apoio_poe.model.data.Erros;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class OpcoesCandidatura extends GPEStateAdapter implements Serializable {

    protected OpcoesCandidatura(GPEContext context, DataManager data) {
        super(context, data);
    }

    @Override
    public Erros addCandidatura(List<String> candidatura){return data.addCandidatura(candidatura);}
    @Override
    public String getTodasCandidaturas(){return data.getTodasCandidaturas();}

    @Override
    public Erros removeCandidatura(long nAluno){return data.removeCandidatura(nAluno);}

    @Override
    public Erros setCandidaturas(List<String> candidatura) {
        return data.setCandidaturas(candidatura);
    }

    @Override
    public String getInfoCandidaturas(String type, String filtro) {
        return data.getInfoCandidaturas(type, filtro);
    }

    @Override
    public List<String> getCandidaturasList() {
        return data.getCandidaturasList();
    }

    @Override
    public String getCandidaturasAlunoList(long nAluno) {
        return data.getCandidaturasAlunoList(nAluno);
    }

    @Override
    public List<String> getCandAutopropostasAlunos(){
        return data.getCandAutopropostasAlunos();
    }
    @Override
    public List<String> getCandRegistadaAlunos(){return data.getCandRegistadaAlunos();}
    @Override
    public List<String> getSemCandRegistadaAlunos(){return data.getSemCandRegistadaAlunos();}
    @Override
    public List<String> getCandAutopropostasProp(){return data.getCandAutopropostasProp();}
    @Override
    public List<String> getCandDocenteProp(){return data.getCandDocenteProp();}
    @Override
    public List<String> getCandProp(){return data.getCandProp();}
    @Override
    public List<String> getCandSemProp(){return data.getCandSemProp();}


    @Override
    public boolean importFile(File f){
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                List<String> info = Arrays.asList(linha.split(","));
                data.addCandidatura(info);
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

            pw.println(data.exportCandidaturas());

            pw.flush();
            fw.close();
        } catch (Exception e) {
            return false;
        }
        return true;
        //return data.exportFile(type, filename);
    }

    @Override
    public boolean voltar() {
        changeState(GPEState.CONFIGURACAO);
        return true;
    }

    @Override
    public Erros fecharFase() {
        Erros erros = data.fecharFase(getState());
        if(erros.equals(Erros.NONE))
            changeState(GPEState.ATRIBUICAO_PROPOSTAS);
        return erros;
    }

    @Override
    public boolean isFechado() {
        return data.isFechado(getState());
    }

    @Override
    public boolean seguinte() {
        changeState(GPEState.ATRIBUICAO_PROPOSTAS);
        return true;
    }

    @Override
    public GPEState getState() {
        return GPEState.OPCOES_CANDIDATURA;
    }
}
