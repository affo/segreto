package it.polimi.nwlu.segreto.spark;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;

import java.io.IOException;

/**
 * Created by Riccardo on 16/06/16.
 */
public class Main {
    private static final String host = "localhost";
    private static final int port = 9999;
    private static final Object lock = new Object();

    public static void main(String[] args) {


        Integer experimentNumber = Integer.parseInt(args[0]);

        System.out.println("Experiment " + experimentNumber + " is running [" + host + "][" + port + "]");
        SparkConf conf = new SparkConf().setAppName("SparkExperiment" + experimentNumber);//.setMaster("local[*]");

        Logger.getRootLogger().setLevel(Level.ERROR);

        Proxy p = null;
        Experiment e = null;
        try {
            new Thread(p = new Proxy(port, new String[]{"10 10", "11 20", "12 30", "13 40",
                    "14 50", "15 60", "16 70", "17 80", "18 90", "19 100"}, lock)).start();
        } catch (IOException e1) {
            e1.printStackTrace();
        }

        // The six time window experiments of SECRET
        switch (experimentNumber) {
            case 0:
                e = new TumblingExperiment("Experiment 0", 3, host, port, conf);
                break;
            case 1:
                e = new TumblingExperiment("Experiment 1", 3, host, port, conf);
                break;
            case 2:
                e = new SlidingExperiment("Experiment 2", 5, 1, host, port, conf);
                break;
            case 3:
                e = new SlidingExperiment("Experiment 3", 4, 1, host, port, conf);
                break;
            case 4:
                e = new SlidingExperiment("Experiment 4", 4, 1, host, port, conf);
                break;
            case 5:
                e = new TumblingExperiment("Experiment 5", 3, host, port, conf);
                break;
            case 6:
                e = new TumblingExperiment("Experiment 6", 3, host, port, conf);
                break;
            default:
                e = null;
                break;
        }


        try {
            e.start(p, lock);
        } catch (InterruptedException e1) {
            System.out.println("Experiment " + experimentNumber + " Terminated");
        }

    }
}
