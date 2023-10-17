package br.ufpr.delt.termo;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;
//import android.app.AlertDialog;
import android.content.DialogInterface;

import java.io.InputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Random;
import java.util.Scanner;

public class MainActivity extends AppCompatActivity {
    Random random;
    Banco banco;

    private String palavrasegredo = "";
    private String termoSegredo = "";

    // linha e coluna atual do jogo
    private short linhaatual = 0;
    private short colunaatual = 0;


    // numero de linhas e colunas do termo
    private short maxlinhas = 6; //base zero
    private short maxcolunas = 5; //base zero

    // matriz de textview para exibição as letras

    TableLayout TableLayoutJogo;
    private TextView [][]letrasjogo = new TextView[maxlinhas][maxcolunas];

    private AlertDialog alerta;


    public void vocêganhou() {
        //Cria o gerador do AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //define o titulo
        builder.setTitle("VENCEDOR");
        //define a mensagem
        builder.setMessage("Parabéns você venceu, a palavra era: "+palavrasegredo);
        //define um botão como positivo
        builder.setPositiveButton("Nova Jogada", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                novajogada();
                sorteia();
            }
        });

        //cria o AlertDialog
        alerta = builder.create();
        //Exibe
        alerta.show();
    }

    public void vocêperdeu() {
        //Cria o gerador do AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //define o titulo
        builder.setTitle("Game Over");
        //define a mensagem
        builder.setMessage("Não foi desta vez, a palavra era: "+palavrasegredo);
        //define um botão como positivo
        builder.setPositiveButton("Nova Jogada", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                novajogada();
                sorteia();
            }
        });

        //cria o AlertDialog
        alerta = builder.create();
        //Exibe
        alerta.show();
    }

    public void sorteia(){
        random = new Random();
        int numerosorteado = random.nextInt(400);
        banco = Banco.getInstance(getApplicationContext());
        palavrasegredo = banco.getTermo(numerosorteado).getPalavra();
        termoSegredo = banco.getTermo(numerosorteado).getTermo();
        Toast.makeText(getApplicationContext(), "Termo: " + termoSegredo+"  Palavra Segredo: "+palavrasegredo, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sorteia();
        inicializatabela();
    }


    public void onclickteclado(View view){

        Button b = (Button)view;
        String text = b.getText().toString();
        //Toast.makeText(getApplicationContext(), text , Toast.LENGTH_LONG).show();

        //Toast.makeText(getApplicationContext(), String.valueOf(linhaatual)+','+String.valueOf(colunaatual) , Toast.LENGTH_LONG).show();
        letrasjogo[linhaatual][colunaatual].setText(text);
        if (colunaatual < maxcolunas-1) {
            colunaatual++;
        }

    }

    public String capturalinha(){
        StringBuilder jogada = new StringBuilder();
        for (int i = 0;i < maxcolunas; i++ ){
            //Toast.makeText(getApplicationContext(), letrasjogo[linhaatual][i].getText() , Toast.LENGTH_LONG).show();
            jogada.append( letrasjogo[linhaatual][i].getText());
        }
        return jogada.toString();
    }


    public void colorirresposta(String entrada){
        for (int i = 0; i < maxcolunas; i++) {
            if (entrada.charAt(i) == 'c'){
                letrasjogo[linhaatual][i].setBackgroundColor(Color.parseColor( "#8EC594"));
            } else if (entrada.charAt(i) == 'n') {
                letrasjogo[linhaatual][i].setBackgroundColor(Color.parseColor( "#DF8181"));
            } else if (entrada.charAt(i) == 'x') {
                letrasjogo[linhaatual][i].setBackgroundColor(Color.parseColor( "#E9F181"));
            }
        }
    }
    public String verificaJogada(String entrada) {
        StringBuilder saida = new StringBuilder();
        boolean[] usado = new boolean[maxcolunas];
        boolean achou = false;

        // inicializar o vetor de usado
        for (int i =0; i < maxcolunas; i++){
            usado[i]=false;
        }

        // trata cada letra
        for (int i = 0; i < entrada.length(); i++) {
            for (int j = 0; j < entrada.length(); j++){
                char c = entrada.charAt(i);

                if ((Objects.equals(c,termoSegredo.charAt(j)))&& (!achou)){
                    if ((i == j) && (usado[j]==false)){
                        saida.append('c');
                        usado[j] = true;
                        achou = true;
                    } else if ((i != j) && (usado[j]==false)){
                        saida.append('x');
                        usado[j]=true;
                        achou = true;
                    }
                }

                if ((j == maxcolunas-1) && (!achou)){
                    saida.append('n');
                }
            }
            achou = false;
        }
        return saida.toString();
    }

    public void onclicktecladoBackspace(View view){

        Button b = (Button)view;
        String text = "";
        //Toast.makeText(getApplicationContext(), text , Toast.LENGTH_LONG).show();

        //Toast.makeText(getApplicationContext(), String.valueOf(linhaatual)+','+String.valueOf(colunaatual) , Toast.LENGTH_LONG).show();

        for (int i = 0 ; i< maxcolunas; i++ ){
            letrasjogo[linhaatual][i].setText("");
        }
        colunaatual = 0;
    }

    public void novajogada(){
        for (int i = 0; i< maxcolunas;i++){
            for (int j = 0; j< maxlinhas; j++){
                letrasjogo[j][i].setText("");
                letrasjogo[j][i].setBackgroundColor(Color.parseColor("#C8CEEC"));
            }
        }
        linhaatual = 0;
        colunaatual = 0;
        pintatabelainicial();
    }
    public void onclicktecladoEnter(View view){

        Button b = (Button)view;
        String resposta = "";
        //Toast.makeText(getApplicationContext(), text , Toast.LENGTH_LONG).show();

        //Toast.makeText(getApplicationContext(), String.valueOf(linhaatual)+','+String.valueOf(colunaatual) , Toast.LENGTH_LONG).show();
        if (colunaatual == maxcolunas - 1) {
            //Toast.makeText(getApplicationContext(), capturalinha(), Toast.LENGTH_LONG).show();
            String linha = capturalinha();
            if (banco.existetermo(linha)){
                resposta = verificaJogada(linha);
                //Toast.makeText(getApplicationContext(), resposta.toString(), Toast.LENGTH_LONG).show();
                colorirresposta(resposta);
                linhaatual++;
                colunaatual = 0;

                if (Objects.equals(resposta,"ccccc")){
                    vocêganhou();
                }
                if (linhaatual == maxlinhas){
                    vocêperdeu();
                }

                if (linhaatual < maxlinhas) {
                    for (int i = 0; i < maxcolunas; i++) {
                        letrasjogo[linhaatual][i].setBackgroundColor(Color.parseColor("#C8CEEC"));
                    }
                }

            } else{
                Toast.makeText(getApplicationContext(), "Palavra não existe, use uma palavra conhecida", Toast.LENGTH_LONG).show();
            }


        } else {
            Toast.makeText(getApplicationContext(), "Preencha a linha inteira", Toast.LENGTH_LONG).show();
        }
    }


    private void pintatabelainicial(){
        for (int i = 1; i< maxlinhas; i++){
            for (int j = 0; j< maxcolunas; j++){
                letrasjogo[i][j].setBackgroundColor(Color.parseColor("#383940"));
            }
        }
    }
    private void inicializatabela(){
        TableRow LinhaTermo;

        TableLayoutJogo = findViewById(R.id.tableLayoutJogo);

        for (int i = 0; i < maxlinhas; i++){
            LinhaTermo =  (TableRow) TableLayoutJogo.getChildAt(i);
            for (int j = 0; j < maxcolunas; j++){
                letrasjogo[i][j] = (TextView) LinhaTermo.getChildAt(j);
                //Toast.makeText(getApplicationContext(), String.valueOf(i)+' '+String.valueOf(j), Toast.LENGTH_SHORT).show();
            }
        }
        pintatabelainicial();

    }


}