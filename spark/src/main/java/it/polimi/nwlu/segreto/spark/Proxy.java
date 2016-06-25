package it.polimi.nwlu.segreto.spark;

import java.io.IOException;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Riccardo on 24/06/16.
 */
public class Proxy implements Runnable {

    private final int port;
    private final String[][] play;
    private final Object lock;
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private boolean ready = false;


    public Proxy(int port, String[] play, Object o) throws IOException {
        this.port = port;
        this.play = new String[play.length][2];
        for (int i = 0; i < play.length; i++) {
            this.play[i] = play[i].split(" ");
        }
        this.serverSocket = new ServerSocket(port);
        this.lock = o;
    }

    public void run() {
        synchronized (lock) {
            System.out.println("Proxy Started on port [" + serverSocket.getLocalPort() + "]");
            try {

                clientSocket = serverSocket.accept();
                System.out.println("Just connected to "
                        + clientSocket.getRemoteSocketAddress());

                PrintStream out = new PrintStream(clientSocket.getOutputStream());
                long time = 0;
                for (String[] s : play) {
                    out.println("(" + (time = System.currentTimeMillis()) + "  " + s[1] + ")");
                    System.out.println("Sent [" + s[0] + " " + s[1] + "] at [" + time + "]");
                    Thread.sleep(1 * 1000);
                }

                out.println("Thank you for connecting to "
                        + clientSocket.getLocalSocketAddress() + "\nGoodbye!");

                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


        }
    }

}
