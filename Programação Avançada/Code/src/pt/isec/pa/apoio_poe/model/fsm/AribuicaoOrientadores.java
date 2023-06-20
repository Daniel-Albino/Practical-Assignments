package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.DataManager;
import pt.isec.pa.apoio_poe.model.data.Docentes;
import pt.isec.pa.apoio_poe.model.data.Erros;
import pt.isec.pa.apoio_poe.model.data.Proposta;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class AribuicaoOrientadores extends GPEStateAdapter implements Serializable {

    protected AribuicaoOrientadores(GPEContext context, DataManager data) {
        super(context, data);
    }

    @Override
    public Erros addAtOrientadores(){return data.addAtOrientadores();}
    @Override
    public String mostraPropostasOrientadas(){return data.mostraPropostasOrientadas();}

    @Override
    public List<String> mostraPropostasOrientadasList() {
        return data.mostraPropostasOrientadasList();
    }

    @Override
    public Docentes getDocenteEmail(String email) {
        return data.getDocenteEmail(email);
    }

    @Override
    public Proposta getProp(String idProposta) {
        return data.getProp(idProposta);
    }

    @Override
    public Erros addManOrientadores(String docente, String idPropostas) {
        return data.addManOrientadores(docente, idPropostas);
    }

    @Override
    public Erros removeOrientadorProposta(String idProposta) {
        return data.removeOrientadorProposta(idProposta);
    }

    @Override
    public Erros removeOrientadorTodasProposta(String emailDocente){
        return data.removeOrientadorTodasProposta(emailDocente);
    }

    @Override
    public Erros editAtOrientadores(String idProposta, String novoDocente) {
        return data.editAtOrientadores(idProposta, novoDocente);
    }

    @Override
    public boolean voltar() {
        changeState(GPEState.ATRIBUICAO_PROPOSTAS);
        return true;
    }

    @Override
    public void undoAtOrientadores() {
        data.undoAtOrientadores();
    }

    @Override
    public void redoAtOrientadores() {
        data.redoAtOrientadores();
    }

    @Override
    public boolean exportFile(File f){

        FileWriter fw = null;
        try {
            fw = new FileWriter(f);
            BufferedWriter bw = new BufferedWriter(fw);
            PrintWriter pw = new PrintWriter(bw);

            pw.println(data.mostraPropostasOrientadas());

            pw.flush();
            fw.close();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public Erros fecharFase() {
        Erros erros = data.fecharFase(getState());
        if(erros.equals(Erros.NONE))
            changeState(GPEState.CONSULTA);
        return erros;
    }

    @Override
    public boolean isFechado() {
        return data.isFechado(getState());
    }

    @Override
    public GPEState getState() {
        return GPEState.ATRIBUICAO_ORIENTADORES;
    }
}
