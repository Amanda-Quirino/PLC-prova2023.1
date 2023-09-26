import java.util.*;
import java.util.concurrent.*;

public class ThreadPoolTarefas {
    public static void processarTarefas(List<Tarefa> filaTarefas, ExecutorService executor) {
        List<Tarefa> filaReserva = new ArrayList<>(filaTarefas);

        while (!filaTarefas.isEmpty()) {
            Iterator<Tarefa> iterator = filaTarefas.iterator();
            while (iterator.hasNext()) {
                Tarefa tarefaAtual = iterator.next();

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
                    iterator.remove();
                }
            }
        }
    }

    public static boolean podeIniciarTarefa(Tarefa tarefa, List<Tarefa> listaTarefas) {
        return tarefa.tarefasPendentes.stream().noneMatch(id -> listaTarefas.stream().anyMatch(task -> task.id == id));
    }

    public static void main(String[] args) {
        List<Tarefa> filaTarefas = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);

        System.out.print("Indique o número de operadores e de tarefas: ");
        int nOperadores = scanner.nextInt();
        int nTarefas = scanner.nextInt();
        scanner.nextLine();

        for (int i = 0; i < nTarefas; i++) {
            System.out.print("Indique as informações da tarefa " + (i + 1) + ": ");
            String tarefaInfo = scanner.nextLine();

            String[] listaString = tarefaInfo.split(" ");
            int id = Integer.parseInt(listaString[0]);
            int tempo = Integer.parseInt(listaString[1]);

            List<Integer> tarefasPendentes = new ArrayList<>();
            for (int j = 2; j < listaString.length; j++) {
                if (!listaString[j].isEmpty()) {
                    tarefasPendentes.add(Integer.parseInt(listaString[j]));
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
