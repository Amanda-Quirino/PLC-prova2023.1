package Q4;

import java.util.Random;

import Q4.ServerPing;
import Q4.ServerPong;

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
            server.put(msg);
            try {
                Thread.sleep(rdm.nextInt(1000));
            } catch (InterruptedException e) {}
        }

        server.put("fim");
    }
}
