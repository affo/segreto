package it.polimi.nwlu.segreto;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Riccardo on 24/06/16.
 */
public class Proxy implements Runnable {

    private final int port;
    private String[] play;
    private ServerSocket serverSocket;
    private Socket clientSocket;
    private boolean ready = false;
    private int wait_time;


    public Proxy(int port, String experiment) throws IOException {
        this.port = port;
        
        File f = new File(experiment);
        BufferedReader br = new BufferedReader(new FileReader(f));
        String line = br.readLine();
        System.out.println(line);
        play = line.split(" ");

        wait_time = 0;

        for (int i = 0; i < play.length - 1; i += 2) {

        }
        this.serverSocket = new ServerSocket(port);
    }

    public void run() {
        System.out.println("Proxy Started on port [" + serverSocket.getLocalPort() + "]");
        try {

            clientSocket = serverSocket.accept();
            System.out.println("Socket connected to " + clientSocket.getRemoteSocketAddress());

            long time = 0;
            PrintStream out = new PrintStream(clientSocket.getOutputStream());

            for (int i = 0; i < play.length - 1; i += 2) {
                System.out.println("Wait before starting " + (Integer.parseInt(play[i]) - wait_time) + " Seconds");
                Thread.sleep((Integer.parseInt(play[i]) - wait_time) * 1000);

                wait_time = Integer.parseInt(play[i]);

                out.println("(" + (time = System.currentTimeMillis()) + "  " + play[i] + "  " + play[1 + i] + ")");
                System.out.println("Sent [" + play[i] + " " + play[1 + i] + "] at [" + time + "]");
            }

            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
