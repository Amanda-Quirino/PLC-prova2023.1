package Q4;

import java.util.concurrent.locks.*;

public class ServerPong {
    private Lock lck = new ReentrantLock();
    private Condition varCond = lck.newCondition();

    private String mensagem = "";

    private boolean fim;

    public String get() {
        String retorna;
        lck.lock();
        try {
            while (!fim && mensagem == "") {
                try {
                    varCond.await();
                } catch (InterruptedException e) { }
            }
            retorna = mensagem;
            mensagem = "";
            varCond.signalAll();
            return retorna;
        } finally {
            lck.unlock();
        }
    }

    public void put(String msg) {
        lck.lock();
        try {
            while (mensagem != "") {
                try {
                    varCond.await();
                } catch (InterruptedException e) { }
            }
            mensagem = msg;
            varCond.signalAll();
        } finally {
            lck.unlock();
        }
    }
}
