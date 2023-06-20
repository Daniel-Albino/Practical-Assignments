package pt.isec.pa.apoio_poe.model.fsm;

import pt.isec.pa.apoio_poe.model.DataManager;
import pt.isec.pa.apoio_poe.model.data.Alunos;
import pt.isec.pa.apoio_poe.model.data.Erros;

import java.io.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GestaoAlunos extends GPEStateAdapter implements Serializable {

    protected GestaoAlunos(GPEContext context, DataManager data) {
        super(context, data);
    }

    @Override
    public Erros addAlunos(List<String> values) {
        return data.addAluno(values);
    }
    @Override
    public Erros removeAlunos(long nAluno) {
        return data.removeAluno(nAluno);
    }
    @Override
    public Erros setNomeAluno(long n_aluno, String novoNome){return data.setNomeAluno(n_aluno, novoNome);}
    @Override
    public Erros setClassificacao(long n_aluno, String novaNota){return data.setClassificacao(n_aluno, novaNota);}
    @Override
    public Erros setAptoEstagio(long nAluno){return data.setAptoEstagio(nAluno);}
    @Override
    public String getAluno(long nAluno) {
        return data.getAluno(nAluno);
    }
    @Override
    public String getTodosAlunos(){return data.getTodosAlunos();}

    @Override
    public List<Alunos> getListAlunos() {
        return data.getListAlunos();
    }

    @Override
    public List<Alunos> getAlunos() {
        return data.getAlunos();
    }

    @Override
    public Alunos getAlunoNumber(long nAluno) {
        return data.getAlunoNumber(nAluno);
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
    public boolean importFile(File f){
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                List<String> info = Arrays.asList(linha.split(","));
                data.addAluno(info);
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

            for (Alunos a : data.getAlunos())
                pw.println(a);

            pw.flush();
            fw.close();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    @Override
    public GPEState getState() {
        return GPEState.GESTAO_ALUNOS;
    }
}
