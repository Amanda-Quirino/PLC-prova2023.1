package Q4.ping;

public class ServerPing {
    private String[] mensagem = new String[0];
    private boolean fim;

    public synchronized String get() {
        // Loop para esperar a proxima mensagem ou o final das mensagens
        while (mensagem.length == 0 && !fim) {
            try {
                System.out.println("Esperando mensagem ...");
                wait();
            } catch (InterruptedException e) {}
        }
        notifyAll();
        String retorno = mensagem[0];
        String[] auxMensagem = mensagem;
        mensagem = new String[mensagem.length - 1];
        System.arraycopy(auxMensagem, 1, mensagem, 0, auxMensagem.length - 1);
        return retorno;
    }

    public synchronized void set(String mensagem) {
        if (mensagem == "fim") {
            fim = true;
        }
        String[] auxMensagem = this.mensagem;
        this.mensagem = new String[this.mensagem.length + 1];
        System.arraycopy(auxMensagem, 0, this.mensagem, 0, auxMensagem.length);
        this.mensagem[this.mensagem.length - 1] = mensagem;

        notifyAll();
    }
}
