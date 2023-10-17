package br.ufpr.delt.termo;

import android.widget.Toast;
import android.content.Context;


import java.io.InputStream;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Scanner;

public class Banco {

    private ArrayList<Termo> termos;
    private static Banco banco;


    // construtor privado (escondido), criando um SINGLETON
    private Banco(Context context) {
        termos = new ArrayList<Termo>();  // lista vazia


        readCSV(context);
    }

    public static Banco getInstance(Context context) {
        if (banco == null) {// não existe? então é primeira chamada
            // logo, criar o objeto, cria a lista e popula
            banco = new Banco(context);
        }
        return banco;
    }

    public int size(){
        return termos.size();
    }

    public Termo getTermo(int i){
        return termos.get(i);
    }

    public Boolean existetermo(String resposta){
        boolean existe = false;
        for (int i = 0; i< this.size(); i++){
            if (Objects.equals(this.getTermo(i).getTermo(),resposta )){
                existe = true;
            }
        }
        return existe;

    }
    private  void readCSV(Context context) {
        try {
            String palavra = null;
            InputStream inputStream = context.getResources().openRawResource(R.raw.palavras_termo);
            Scanner scanner = new Scanner(inputStream);
            for (int i = 1; i <= 400; i++) {
                palavra = scanner.nextLine();
                String[] termo = palavra.split("\\s*;\\s*");
                Termo  termoelemento= new Termo(termo[0],termo[1]);
                termos.add(termoelemento);
            }
           // Toast.makeText(context, "Sorteado: " +termos.get(399).getPalavra() + " - Termo: " + termos.get(399).getPalavra(), Toast.LENGTH_LONG).show();
            inputStream.close();
            scanner.close();
        } catch (Exception e) {
            Toast.makeText(context, "Algo deu errado: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }



}
