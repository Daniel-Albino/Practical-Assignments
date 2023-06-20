package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.DataManager;
import pt.isec.pa.apoio_poe.model.data.Docentes;
import pt.isec.pa.apoio_poe.model.data.Erros;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class GestaoDocentes extends GPEStateAdapter implements Serializable {

    protected GestaoDocentes(GPEContext context, DataManager data) {
        super(context, data);
    }

    @Override
    public Erros addDocente(List<String> list) {
        return data.addDocente(list);
    }

    @Override
    public Erros removeDocente(String email) {
        return data.removeDocente(email);
    }

    @Override
    public Erros setNomeDocente(String email, String novoNome) {
        return data.setNomeDocente(email, novoNome);
    }

    @Override
    public String getDocente(String emailDoc) {
        return data.getDocente(emailDoc);
    }

    @Override
    public List<Docentes> getDocentes() {
        return data.getDocentes();
    }

    @Override
    public Docentes getDocenteEmail(String email) {
        return data.getDocenteEmail(email);
    }

    @Override
    public String getTodosDocentes() {
        return data.getTodosDocentes();
    }

    @Override
    public boolean importFile(File f){
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                List<String> info = Arrays.asList(linha.split(","));
                data.addDocente(info);
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

            for (Docentes a : data.getDocentes())
                pw.println(a.toString());

            pw.flush();
            fw.close();
        } catch (Exception e) {
            return false;
        }
        return true;
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
        return GPEState.GESTAO_DOCENTES;
    }
}
