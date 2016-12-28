package it.polimi.nwlu.segreto;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaStreamingContext;

import java.io.IOException;

/**
 * Created by Riccardo on 16/06/16.
 */
public class Main {

    private static final String host = "localhost";
    private static final int port = 9999;

    public static void main(String[] args) throws IOException {

        Logger.getRootLogger().setLevel(Level.ERROR);
        Integer experimentNumber = Integer.parseInt(args[0]);

        SparkConf conf = new SparkConf().setAppName("SparkExperiment" + experimentNumber);//.setMaster("local[*]");
        JavaStreamingContext context = new JavaStreamingContext(conf, Durations.milliseconds(1));

        System.out.println("Starting Experiment [" + experimentNumber +"]");
        Proxy p = new Proxy(port, "./experiments/exp" + experimentNumber);
        new Thread(p).start();

        SecretExperiment e = null;

        // The six time window experiments of SECRET
        switch (experimentNumber) {
            case 1:
                e = new SecretExperiment("Experiment 1", 3, host, port, context);
                break;
            case 2:
                e = new SecretExperiment("Experiment 2", 5, 1, host, port, context);
                break;
            case 4:
                e = new SecretExperiment("Experiment 4", 4, 1, host, port, context);
                break;
            case 5:
                e = new SecretExperiment("Experiment 5", 3, host, port, context);
                break;
            case 6:
                e = new SecretExperiment("Experiment 6", 3, host, port, context);
                break;
            case 7:
                /*Custom Experiment to understand tick and report*/
                e = new SecretExperiment("Experiment Custom", Integer.parseInt(args[1]), Integer.parseInt(args[2]), host, port, context);
                break;
            default:
                throw new RuntimeException("Use case do not exists!");
        }

        try {
            e.start();
        } catch (InterruptedException e1) {
            System.out.println("Experiment " + experimentNumber + " Terminated");
        }
    }
}