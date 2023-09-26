package Q4;

import Q4.ServerPing;
import Q4.ServerPong;

public class Comunica {
    private static ServerPing srv = new ServerPing();

    public static void main(String[] args) {
        (new Thread(new Envia(srv))).start();
        (new Thread(new Recebe(srv))).start();
    }
}
