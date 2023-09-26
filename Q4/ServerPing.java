package Q4;

public class ServerPing {
    private String mensagem;
    // Variavel para dizer se a mensagem foi recebida e passada para o outro usuario
    private boolean recebi;
    private boolean fim;

    public synchronized String get() {
        while (!recebi && !fim) {
            try {
                System.out.println("Esperando mensagem ...");
                wait();
            } catch (InterruptedException e) { }
        }
        recebi = false;
        notifyAll();
        return mensagem;
    }

    public synchronized void put(String msg) {
        while (recebi) {
            try {
                wait();
            } catch (InterruptedException e) { }
        }
        mensagem = msg;
        recebi = true;
        notifyAll();
    }
}
