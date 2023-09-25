package Q4.ping;

import java.util.Random;

public class Envia implements Runnable{
    private ServerPing server;

    public Envia(ServerPing srv) {
        this.server = srv;
    }

    public void run() {
        String mensagemEnviadas[] = {
            "Ola",
            "Amigo,",
            "como",
            "voce",
            "esta",
            "nessa",
            "maravilhosa",
            "tarde"
        };

        Random rdm = new Random();

        for (String msg : mensagemEnviadas) {
            server.set(msg);
            try {
                Thread.sleep(rdm.nextInt(300));
            } catch (InterruptedException e) {}
        }

        server.set("fim");
    }
}
