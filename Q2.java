import java.util.*;
import java.util.concurrent.*;

public class ThreadPoolTarefas {
    public static void processarTarefas(List<Tarefa> filaTarefas, ExecutorService executor) {
        Iterator<Tarefa> iterador = filaTarefas.iterator();

        while (iterador.hasNext()) {
            Tarefa tarefaAtual = iterador.next();
            int idParaRemover = tarefaAtual.id;

            if (tarefaAtual.tarefasPendentes.isEmpty()) {
                executor.submit(() -> {
                    try {
                        Thread.sleep(tarefaAtual.tempo);
                        System.out.println("tarefa " + tarefaAtual.id + " feita");
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                });

                // Remove o ID da lista de pendências de todas as tarefas
                for (Tarefa tarefa : filaTarefas) {
                    tarefa.tarefasPendentes.removeIf(pendencia -> pendencia == idParaRemover);
                }

                // Remove a tarefa da fila
                iterador.remove();
            }
        }
        // Trata as pendências
        if (!filaTarefas.isEmpty()){
            processarTarefas(filaTarefas, executor);
        }
    }

    public static void main(String[] args) {
        List<Tarefa> filaTarefas = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);

        System.out.print("Indique o número de operadores e de tarefas: ");
        int nOpereradores = scanner.nextInt();
        int nTarefas = scanner.nextInt();

        for (int i = 0; i < nTarefas; i++) {
            System.out.print("Indique as informações da tarefa " + (i + 1) + " (indique -1 para fim da lista de tarefas pendentes): ");

            int id = scanner.nextInt();
            int tempo = scanner.nextInt();
            List<Integer> tarefasPendentes = new ArrayList<>();
            boolean continuar = true;

            while (continuar && scanner.hasNextInt()) {
                int idTarefa = scanner.nextInt();

                if (idTarefa == -1) {
                    continuar = false;
                } else {
                    tarefasPendentes.add(idTarefa);
                }
            }
            filaTarefas.add(new Tarefa(id, tempo, tarefasPendentes));
        }

        ExecutorService executor = Executors.newFixedThreadPool(nOpereradores);

        processarTarefas(filaTarefas, executor);

        executor.shutdown();
        scanner.close();
    }
}

class Tarefa {
    int id;
    int tempo;
    List<Integer> tarefasPendentes;

    public Tarefa(int id, int tempo, List<Integer> tarefasPendentes) {
        this.id = id;
        this.tempo = tempo;
        this.tarefasPendentes = tarefasPendentes;
    }
}
