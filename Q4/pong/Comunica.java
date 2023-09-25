package Q4.pong;

public class Comunica {
    private static ServerPong srv = new ServerPong();

    public static void main(String[] args) {
        (new Thread(new Envia(srv))).start();
        (new Thread(new Recebe(srv))).start();
    }
}

