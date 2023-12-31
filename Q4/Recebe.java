package Q4;

import java.util.Random;

import Q4.ServerPing;
import Q4.ServerPong;

public class Recebe implements Runnable{
    private ServerPing server;

    public Recebe (ServerPing srv) {
        this.server = srv;
    }

    public void run () {
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
