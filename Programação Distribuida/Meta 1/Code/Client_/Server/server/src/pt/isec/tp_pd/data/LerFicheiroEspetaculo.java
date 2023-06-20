package pt.isec.tp_pd.data;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class LerFicheiroEspetaculo {

    private String filemane;

    public LerFicheiroEspetaculo(String filemane) {
        this.filemane = filemane;
    }

    public Espetaculo lerEspetaculo(){
        System.out.println("entrei");

        File f = new File("C:\\Users\\ndomi\\Desktop\\PD_TP-Final(sortof)\\PD_TP - experimental\\TrabalhoPratico_PD\\TrabalhoPratico_PD\\espetaculos\\espetaculo.txt");
        if(!f.exists())
            System.out.println("noexists");
        FileReader fr = null;
        BufferedReader buffRead;
        try {
            fr = new FileReader(f);
            buffRead = new BufferedReader(fr);
        } catch (FileNotFoundException e) {
            return null;
        }
        System.out.println("here");
        String linha = "";
        Espetaculo espetaculo = new Espetaculo();
        while (true) {
            if (linha != null) {
                linha = removeAspasDuplas(linha);
                String[] linhaSplit = splitLinha(linha);
                if(linhaSplit != null)
                    if(!trataFicheiro(espetaculo,linhaSplit)){
                        System.out.println("trata");
                        return null;
                    }
            } else
                break;
            try {
                linha = buffRead.readLine();
            } catch (IOException e) {
            }
        }
        try {
            buffRead.close();
        } catch (IOException e) {
        }
        return espetaculo;
    }

    private static boolean trataFicheiro(Espetaculo espetaculo, String[] linha) {
        /*System.out.println(linha[1]);*/
        System.out.println(linha[0]);
        switch (linha[0].toUpperCase()){
            case "DESIGNAÇÃO" -> {
                System.out.println("ENTREI DESIG");espetaculo.setDescricao(linha[1]);}
            case "TIPO" -> espetaculo.setTipo(linha[1]);
            case "DATA", "HORA" -> {
                if(!espetaculo.setData_hora(linha)) {
                    System.out.println("setData exception");
                    return false;
                }
            }
            case "DURAÇÃO" -> {
                int i = -1;
                try {
                    i = Integer.parseInt(linha[1]);
                }catch (Exception e){
                    System.out.println("setData exception1");
                    return false;
                }

                espetaculo.setDuracao(i);
            }
            case "LOCAL" -> espetaculo.setLocal(linha[1]);
            case "LOCALIDADE" -> espetaculo.setLocalidade(linha[1]);
            case "PAÍS" -> espetaculo.setPais(linha[1]);
            case "CLASSIFICAÇÃO ETÁRIA" -> espetaculo.setClassificacao_etaria(linha[1]);
            default -> {
                linha = removeEspacos(linha);
                if(!linha[0].equalsIgnoreCase("Fila")) {
                    String fila = linha[0];
                    List<Lugar> lugares = new ArrayList<>();
                    for(int i = 1;i<linha.length;i++){
                        System.out.println("everithing ok");
                        System.out.println("linha:"+linha[i]);
                        String[] lugaresPreco = splitLinha(linha[i]);
                        System.out.println("primeiro:"+lugaresPreco[0]+"segundo:"+lugaresPreco[1]);
                        float j = -1f;
                        try {
                            j = Float.parseFloat(lugaresPreco[1]);
                            System.out.println("-->"+j);
                        }catch (Exception e){
                            System.out.println("setData exception2");
                            return false;
                        }
                        lugares.add(new Lugar(fila,lugaresPreco[0],j,1));

                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    }
                    espetaculo.addLugares(lugares);
                }
            }
        }
        return true;
    }

    static String[] splitLinha(String linha) {
        if(linha.contains(";"))
            return linha.split(";");
        else if (linha.contains(":"))
            return linha.split(":");
        return null;
    }

    private String removeAspasDuplas(String input){

        StringBuilder sb = new StringBuilder();

        char[] tab = input.toCharArray();
        for( char current : tab ){
            if( current != '"' )
                sb.append( current );
        }

        return sb.toString();
    }

    static String[] removeEspacos(String[] input){
        String[] inputAux = new String[input.length];
        for(int i = 0;i<input.length;i++){
            inputAux[i] = input[i].replaceAll("\\s+","");
        }
        return inputAux;
    }
}
