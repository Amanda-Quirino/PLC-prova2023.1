import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;

public class aviao {
    private static List<Integer> horarioSaidas = new ArrayList<Integer>();
    private static List<Integer> horarioEntradas = new ArrayList<Integer>();
    private static Integer numPistas = 0;
    private static Integer atrasoSaida = 0;
    private static Integer atrasoEntrada = 0;
    private static List<Thread> thList = new ArrayList<Thread>();

    public static void main(String[] args) {
        // Scanner para pegar os inputs dado pelo usuario
        Scanner leitura = new Scanner(System.in);
        
        // Pegando a quantidade de avioes que irao sair
        System.out.println("Digite n: ");
        Integer n = leitura.nextInt();

        // Pegando o horario de cada um desses avioes
        for (int i=0; i<n; i++) {
            System.out.print("Digite a saida do aviao " + i + " : ");
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
        Integer atraso;

        public Pista (Integer numero) {
            this.terminoUsoPista = numero;
        }

        @Override
        public void run() {
            Integer horario;
            while (horarioSaidas.size() != 0 || horarioEntradas.size() != 0) {
                synchronized(this) {
                    // Calculando o possivel atraso do proximo aviao a sair
                    if (horarioSaidas.size() != 0) {
                        atrasoSaida = horarioSaidas.getFirst() < terminoUsoPista ? terminoUsoPista - horarioSaidas.getFirst() : 0;
                    }
                    // Calculando o possivel atraso do proximo aviao a aterrisar
                    if (horarioEntradas.size() != 0) {
                        atrasoEntrada = horarioEntradas.getFirst() < terminoUsoPista ? terminoUsoPista - horarioEntradas.getFirst() : 0;
                    }
                    // Caso nao tenhamos mais avioes saindo, iremos printar o proximo aviao a aterrissar
                    if (horarioSaidas.size() == 0) {
                        horario = horarioEntradas.remove(0);
                        atraso = atrasoEntrada;
                    }
                    // Caso nao tennhamos mais avioes aterrisando, printamos o proximo aviao a sair
                    else if (horarioEntradas.size() == 0) {
                        horario = horarioSaidas.remove(0);
                        atraso = atrasoSaida;
                    }
                    // Caso o horario do proximo aviao a sair seja mais cedo do que o proximo a aterrissar
                    else if (horarioSaidas.getFirst() < horarioEntradas.getFirst()){
                        horario = horarioSaidas.remove(0);
                        atraso = atrasoSaida;
                    }
                    // Caso contrario
                    else {
                        horario = horarioEntradas.remove(0);
                        atraso = atrasoEntrada;
                    }
                }

                    terminoUsoPista = horario + atraso + 500;
                    saidaReal = horario + atraso;

                    System.out.println(Thread.currentThread().getName() +
                                    System.lineSeparator() +
                                    "Aterrissagem esperada: " + horario +
                                    System.lineSeparator() +
                                    "Aterrissagem real: " + saidaReal +
                                    System.lineSeparator() +
                                    "Atraso: " + atrasoEntrada);
            }
        }
    }
}
