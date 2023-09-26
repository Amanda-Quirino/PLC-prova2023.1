package Q4;

public class ServerPing {
    private String [] mensagem = new String[0];
    // Variavel para dizer se a mensagem foi recebida e passada para o outro usuario
    private boolean recebi;
    private boolean fim;

    public synchronized String get() {
        while (mensagem.length == 0 && !fim) {
            try {
                System.out.println("Esperando mensagem ...");
                wait();
            } catch (InterruptedException e) { }
        }
        String retorno = mensagem[0];
        String[] auxMensagem = mensagem;
        mensagem = new String[mensagem.length - 1];
        System.arraycopy(auxMensagem, 1, mensagem, 0, auxMensagem.length - 1);
        notifyAll();
        return retorno;
    }

    public synchronized void put(String msg) {
        if (msg == "fim") {
            fim = true;
        }
        String[] auxMensagem = this.mensagem;
        this.mensagem = new String[this.mensagem.length + 1];
        System.arraycopy(auxMensagem, 0, this.mensagem, 0, auxMensagem.length);
        this.mensagem[this.mensagem.length - 1] = msg;
        notifyAll();
    }
}
