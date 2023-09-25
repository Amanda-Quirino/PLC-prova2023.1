package prova;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

public class aviao {
    static List<Integer> horarioSaidas = new ArrayList<Integer>();
    static List<Integer> horarioEntradas = new ArrayList<Integer>();
    static Integer numPistas = 0;
    static Integer atrasoSaida = 0;
    static Integer atrasoEntrada = 0;
    static List<Thread> thList = new ArrayList<Thread>();

    public static void main(String[] args) {
        Pista pis = new Pista(0);
        // Scanner para pegar os inputs dado pelo usuario
        Scanner leitura = new Scanner(System.in);
        
        // Pegando a quantidade de avioes que irao sair
        System.out.println("Digite n: ");
        Integer n = leitura.nextInt();

        // Pegando o horario de cada um desses avioes
        for (int i=0; i<n; i++) {
            System.out.println("Digite a saida do aviao " + i + " : ");
            horarioSaidas.add(leitura.nextInt());
        }

        // Ordenando a saida dos avioes com base no seu horario
        Collections.sort(horarioSaidas);

        // Pegando a quantidade de avioes que vao aterrisar
        System.out.println("Digite m: ");
        n = leitura.nextInt();

        // Pegando o horario de aterrissagem desses avioes
        for (int i=0; i<n; i++) {
            System.out.print("Digite a entrada do aviao " + i + " : ");
            horarioEntradas.add(leitura.nextInt());
        }

        // Ordenando a aterrissagem deles com base no seu horario
        Collections.sort(horarioEntradas);

        // Pegando a quantidade de pistas disponiveis
        System.out.println("Digite k: ");
        numPistas = leitura.nextInt();
        
        // Fechando o scanner ja que nao vamos ler mais nada
        leitura.close();

        // Criando uma thread para cada pista
        for (int i=0; i<numPistas; i++) {
            thList.add((new Thread(new Pista(0))));
            thList.get(i).start();
        }

        // Loop para que o codigo so terminede rodar quando todas as threads terminarem
        for (int i=0; i<thList.size(); i++) {
            try {
                thList.get(i).join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    // Classe Runnable para podermos ver quais avioes decolam e saem
    public static class Pista implements Runnable {
        Integer terminoUsoPista;
        Integer saidaReal;

        public Pista (Integer numero) {
            this.terminoUsoPista = numero;
        }

        @Override
        public void run() {
            Integer horario;
            while (horarioSaidas.size() != 0 && horarioEntradas.size() != 0) {
                synchronized(this){
                    // Printando nome da thread atual
                    System.out.println(Thread.currentThread().getName());
                    // Calculando o possivel atraso do proximo aviao a sair
                    if (horarioSaidas.size() != 0) {
                        atrasoSaida = horarioSaidas.get(0) < terminoUsoPista ? terminoUsoPista - horarioSaidas.get(0) : 0;
                    }
                    // Calculando o possivel atraso do proximo aviao a aterrisar
                    if (horarioEntradas.size() != 0) {
                        atrasoEntrada = horarioEntradas.get(0) < terminoUsoPista ? terminoUsoPista - horarioEntradas.get(0) : 0;
                    }
                    
                    // Caso nao tenhamos mais avioes saindo, iremos printar o proximo aviao a aterrissar
                    if (horarioSaidas.size() == 0) {
                        horario = horarioEntradas.remove(0);
                        terminoUsoPista = horario + atrasoEntrada + 500;
                        saidaReal = horario + atrasoEntrada;
                        System.out.println("Aterrissagem esperada: " + horario);
                        System.out.println("Aterrissagem real: " + saidaReal);
                        System.out.println("Atraso: " + atrasoEntrada);
                    }
                    // Caso nao tennhamos mais avioes aterrisando, printamos o proximo aviao a sair
                    else if (horarioEntradas.size() == 0) {
                        horario = horarioSaidas.remove(0);
                        terminoUsoPista = horario + atrasoSaida + 500;
                        saidaReal = horario + atrasoSaida;
                        System.out.println("Saida esperada: " + horario);
                        System.out.println("Saida real: " + saidaReal);
                        System.out.println("Atraso: " + atrasoSaida);
                    }
                    // Caso tenhamos avioes para sair e para aterrissar
                    else {
                        // Caso o horario do proximo aviao a sair seja mais cedo do que o proximo a aterrissar
                        if (horarioSaidas.get(0) < horarioEntradas.get(0)) {
                            horario = horarioSaidas.remove(0);

                            terminoUsoPista = horario + atrasoSaida + 500;
                            saidaReal = horario + atrasoSaida;
                            System.out.println("Saida esperada: " + horario);
                            System.out.println("Saida real: " + saidaReal);
                            System.out.println("Atraso: " + atrasoSaida);
                        }

                        // Caso contrario
                        else {
                            horario = horarioEntradas.remove(0);
                            
                            terminoUsoPista = horario + atrasoEntrada + 500;
                            saidaReal = horario + atrasoEntrada;
                            System.out.println("Aterrissagem esperada: " + horario);
                            System.out.println("Aterrissagem real: " + saidaReal);
                            System.out.println("Atraso: " + atrasoEntrada);
                        }
                    }
                }
            }
        }
    }
}
