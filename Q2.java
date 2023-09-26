import java.util.*;
import java.util.concurrent.*;

public class ThreadPoolTarefas {
    public static void processarTarefas(List<Tarefa> filaTarefas, ExecutorService executor) {
        List<Tarefa> filaReserva = new ArrayList<>(filaTarefas);

        while (!filaTarefas.isEmpty()) {
            Tarefa tarefaAtual = filaTarefas.get(0);
            filaTarefas.remove(0);

            if (podeIniciarTarefa(tarefaAtual, filaReserva)) {
                executor.submit(() -> {
                    try {
                        Thread.sleep(tarefaAtual.tempo);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Tarefa " + tarefaAtual.id + " concluída");
                    filaReserva.remove(tarefaAtual);
                });
            } else {
                filaTarefas.add(tarefaAtual);
            }
        }
    }

    private static boolean podeIniciarTarefa(Tarefa tarefa, List<Tarefa> listaTarefas) {
        return tarefa.tarefasPendentes.stream().noneMatch(id -> listaTarefas.stream().anyMatch(t -> t.id == id));
    }

    public static void main(String[] args) {
        List<Tarefa> filaTarefas = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);

        System.out.print("Indique o número de operadores e de tarefas: ");
        int nOperadores = scanner.nextInt();
        int nTarefas = scanner.nextInt();

        for (int i = 0; i < nTarefas; i++) {
            System.out.print("Indique as informações da tarefa " + (i + 1) + ": ");

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

        ExecutorService executor = Executors.newFixedThreadPool(nOperadores);

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
