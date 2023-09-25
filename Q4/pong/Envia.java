package Q4.pong;

import java.util.Random;

public class Envia implements Runnable{
    private ServerPong server;

    public Envia(ServerPong srv) {
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
                Thread.sleep(rdm.nextInt(300));
            } catch (InterruptedException e) {}
        }

        server.put("fim");
    }
}
