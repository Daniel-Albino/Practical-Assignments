package tests;/*
package tests;


import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import java.io.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import pt.isec.tp_gps.model.data.*;


class JUnitTest {

    @Test
    public void testMedicamentoEquals() {
        Calendar dataValidade = Calendar.getInstance();
        Medicamento m1 = new Medicamento("AB12", "Medicamento_A", "ORAL", dataValidade, "nosei", "lab", "bula");
        Medicamento m2 = new Medicamento("AB123", "Medicamento_B", "ORAL", dataValidade, "nosei", "lab", "bula");
        assertNotEquals(true, m1.equals(m2));
    }

    @Test
    public void principalMedSize() {
        Principal principal = new Principal();
        Calendar dataValidade = Calendar.getInstance();
        List<String> dadosMedicamento = new ArrayList<>();
        dadosMedicamento.add("AB123"); dadosMedicamento.add("Medicamento_B");dadosMedicamento.add("ORAL");
        dadosMedicamento.add("nosei");dadosMedicamento.add("lab");dadosMedicamento.add("bula");
        principal.adicionarMedicamento(dadosMedicamento,dataValidade);
        List<String> dadosMedicamento1 = new ArrayList<>();
        dadosMedicamento1.add("AB12"); dadosMedicamento1.add("Medicamento_A");dadosMedicamento1.add("ORAL");
        dadosMedicamento1.add("nosei");dadosMedicamento1.add("lab");dadosMedicamento1.add("bula");
        principal.adicionarMedicamento(dadosMedicamento1,dataValidade);
        assertEquals(2, principal.getMedicamentos().size());
    }

    @Test
    public void removeMedicamento() {
        Principal principal = new Principal();
        Calendar dataValidade = Calendar.getInstance();
        List<String> dadosMedicamento = new ArrayList<>();
        dadosMedicamento.add("AB123"); dadosMedicamento.add("Medicamento_B");dadosMedicamento.add("ORAL");
        dadosMedicamento.add("nosei");dadosMedicamento.add("lab");dadosMedicamento.add("bula");
        principal.adicionarMedicamento(dadosMedicamento,dataValidade);
        List<String> dadosMedicamento1 = new ArrayList<>();
        dadosMedicamento1.add("AB12"); dadosMedicamento1.add("Medicamento_A");dadosMedicamento1.add("ORAL");
        dadosMedicamento1.add("nosei");dadosMedicamento1.add("lab");dadosMedicamento1.add("bula");
        principal.adicionarMedicamento(dadosMedicamento1,dataValidade);

        principal.removerMedicamento("AB123");
        assertEquals(1, principal.getMedicamentos().size());
    }

    @Test
    public void testSerializaObjetoAndLoad() {
        Principal principal = new Principal();
        Calendar dataValidade = Calendar.getInstance();
        Medicamento m1 = new Medicamento("AB123", "Medicamento_B", "ORAL", dataValidade, "nosei", "lab", "bula");

        List<String> dadosMedicamento = new ArrayList<>();
        dadosMedicamento.add("AB123"); dadosMedicamento.add("Medicamento_B");dadosMedicamento.add("ORAL");
        dadosMedicamento.add("nosei");dadosMedicamento.add("lab");dadosMedicamento.add("bula");
        principal.adicionarMedicamento(dadosMedicamento,dataValidade);

        List<String> dadosMedicamento1 = new ArrayList<>();
        dadosMedicamento1.add("AB12"); dadosMedicamento1.add("Medicamento_A");dadosMedicamento1.add("ORAL");
        dadosMedicamento1.add("nosei");dadosMedicamento1.add("lab");dadosMedicamento1.add("bula");
        principal.adicionarMedicamento(dadosMedicamento1,dataValidade);

        //String quantidade,Medicamento medicamento
        principal.adicionarEncomendas("4",m1);

        */
/* serializaObjeto *//*

        try {
            FileOutputStream fileOut =
                    new FileOutputStream("./serializeFiles/Dados.ser");
            ObjectOutputStream out = new ObjectOutputStream(fileOut);
            out.writeObject(principal);
            out.close();
            fileOut.close();
            //System.out.printf("Objeto serializado com sucesso na diretoria ./serializeFiles/Dados.ser");
        } catch (IOException i) {
            i.printStackTrace();
        }

        */
/* load *//*

        principal = null;
        try {
            FileInputStream fileIn = new FileInputStream("./serializeFiles/Dados.ser");
            ObjectInputStream in = new ObjectInputStream(fileIn);
            principal = (Principal) in.readObject();
            in.close();
            fileIn.close();
            //System.out.println("Leitura de objeto serializado");
        } catch (FileNotFoundException e){
            System.out.println("Sem ficheiro!");
        } catch (IOException i) {
            i.printStackTrace();
        } catch (ClassNotFoundException c) {
            System.out.println("Sem objetos serializados");
            c.printStackTrace();
        }

        //assert principal != null;
        assertEquals(2, principal.getMedicamentos().size());
        assertEquals(1, principal.getEncomendas().size());
        assertEquals(0, principal.getEncomendas().get(0).getIDEncomendas());
    }

    @Test
    public void testeAddUtente() {
        Calendar dataValidade = Calendar.getInstance();
        Medicamento m1 = new Medicamento("AB123", "Medicamento_B", "ORAL", dataValidade, "nosei", "lab", "bula");
        List<String> alergias = new ArrayList<>();
        alergias.add("Tomate");
        alergias.add("Alface");
        List<String> doencas = new ArrayList<>();
        doencas.add("Asma");
        doencas.add("Covid");
        List<Calendar> calendars = new ArrayList<>();
        calendars.add(Calendar.getInstance());
        List<ListaMedicamentosUtente> list = new ArrayList<>();
        list.add(new ListaMedicamentosUtente(m1,calendars));
        List<String> dados = new ArrayList<>();
        dados.add("Nome");
        dados.add("CC");
        dados.add("Email@isec.pt");
        dados.add("961925758");
        dados.add("ocupação");
        dados.add("Sexo");
        System.out.println(dados.size());
        Calendar calendar = Calendar.getInstance();
        Principal principal = new Principal();
        principal.adicionarUtente(dados,calendar,alergias,doencas,list);

        System.out.println(principal.getUtentes());

        assertEquals(1, principal.getUtentes().size());
    }
}


*/
