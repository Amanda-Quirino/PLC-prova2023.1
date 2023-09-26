import java.util.Collections;
import java.util.List;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.concurrent.locks.*;

public class Q1 {
    // Listas com os horarios dos avioes
    private static List<Integer> horarioSaidas = new ArrayList<Integer>();
    private static List<Integer> horarioEntradas = new ArrayList<Integer>();
    // Listas com os horarios em que cada pista estara livre
    private static List<Integer> terminoUsoPista = new ArrayList<Integer>();
    // Lista com as threads que representam as pistas
    private static List<Thread> thPistas = new ArrayList<Thread>();
    private static Integer numPistas = 0;

    // Lock e Condition para que as listas com os horarios possam ser acessadas por mais de 1 pista ao mesmo tempo
    private static Lock aLock = new ReentrantLock();
    private static Condition varCond = aLock.newCondition();

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
        
        // Loop para criar as threads que irao representar as pistas que os avioes vao usar
        for (int i=0; i<numPistas; i++) {
            // Como todas as pistas comecam livres, dizemos que no horario 0 elas estao liberadas
            terminoUsoPista.add(0);
            thPistas.add(new Thread(new Pista(i)));
            thPistas.get(i).start();
        }

        // Loop para esperar que todas as threads terminem
        for (int i=0; i<numPistas; i++) {
            try {
                thPistas.get(i).join();
            } catch (InterruptedException e) { }
        }
        
        // Fechando o scanner ja que nao vamos ler mais nada
        leitura.close();
    }

    private static class Pista implements Runnable{
        // Numero da pista que essa instancia representa
        private Integer numPista;
        // Atraso do aviao que vai usar essa pista
        private Integer atraso;
        // Horario que esse aviao deveria ter aterrisado ou decolado
        private Integer horario;
        // Horario que o aviao realmente aterrissou ou decolou
        private Integer saidaReal;

        // Construtor para salvarmos o nome da pista que essa instancia representa
        public Pista(int num) {
            this.numPista = num;
        }

        public void run() {
            // Loop para executarmos o run dessa instancia ate que todos os aviões tenham aterrissado ou decolado
            while (horarioSaidas.size() != 0 || horarioEntradas.size() != 0) {
                // Lock para evitar que outra pista acesse as listas
                aLock.lock();
                try {
                    // Caso essa instancia nao seja que a que sera liberada mais cedo e ainda tenhamos avioes
                    while(terminoUsoPista.get(numPista) != Collections.min(terminoUsoPista) && (!horarioSaidas.isEmpty() || !horarioEntradas.isEmpty())) {
                        // Botamos essa instancia para esperar por 2 milisegundos
                        try {
                            varCond.await();
                        } catch (InterruptedException e) {}
                    }

                    // Caso quando essa instancia sair do await, ja nao haja avioes
                    if (horarioSaidas.isEmpty() && horarioEntradas.isEmpty()) {
                        break;
                    }

                    // Caso nao tenhamos mais avioes saindo, iremos printar o proximo aviao a aterrissar
                    if (horarioSaidas.size() == 0) {
                        horario = horarioEntradas.remove(0);
                    }
                    // Caso nao tennhamos mais avioes aterrisando, printamos o proximo aviao a sair
                    else if (horarioEntradas.size() == 0) {
                        horario = horarioSaidas.remove(0);
                    }
                    // Caso o horario do proximo aviao a sair seja mais cedo do que o proximo a aterrissar
                    else if (horarioSaidas.getFirst() < horarioEntradas.getFirst()){
                        horario = horarioSaidas.remove(0);
                    }
                    // Caso contrario
                    else {
                        horario = horarioEntradas.remove(0);
                    }

                    atraso = horario < terminoUsoPista.get(numPista) ? terminoUsoPista.get(numPista) - horario : 0;
                    terminoUsoPista.set(numPista, horario + atraso + 500);

                    // Avisa para as outras instancias que essa pista terminou de liberar um aviao
                    varCond.signalAll();
                } finally {
                    // Liberando o lock
                    aLock.unlock();

                    // Printando as informacoes pedidas na questao
                    saidaReal = horario + atraso;
        
                    System.out.println(Thread.currentThread().getName() +
                                    System.lineSeparator() +
                                    "Aterrissagem esperada: " + horario +
                                    System.lineSeparator() +
                                    "Aterrissagem real: " + saidaReal +
                                    System.lineSeparator() +
                                    "Atraso: " + atraso);
                }
            }

        }
    }
}
