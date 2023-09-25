package Q4.pong;

import java.util.Random;

public class Recebe implements Runnable{
    private ServerPong server;

    public Recebe(ServerPong srv) {
        this.server = srv;
    }

    public void run() {
        Random rdm = new Random();
        String msg = server.get();

        for (; msg != "fim"; msg = server.get()) {
            System.out.println(msg);
            try {
                Thread.sleep(rdm.nextInt(500));
            } catch (InterruptedException e) {}
        }
        System.out.println(msg);
    }
}
