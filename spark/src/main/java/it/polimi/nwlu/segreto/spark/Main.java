package it.polimi.nwlu.segreto.spark;

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
    private static final Object lock = new Object();

    public static void main(String[] args) throws IOException {

        Logger.getRootLogger().setLevel(Level.ERROR);
        Integer experimentNumber = Integer.parseInt(args[0]);

        SparkConf conf = new SparkConf().setAppName("SparkExperiment" + experimentNumber);//.setMaster("local[*]");
        JavaStreamingContext context = new JavaStreamingContext(conf, Durations.milliseconds(1));


        Proxy p = new Proxy(port, new String[]{"10 10", "11 20", "12 30", "13 40",
                "14 50", "15 60", "16 70", "17 80", "18 90", "19 100"}, lock);
        new Thread(p).start();

        SecretExperiment e = null;

        // The six time window experiments of SECRET
        switch (experimentNumber) {
            case 0:
                e = new TumblingExperiment("Experiment 0", 3, host, port, context);
                break;
            case 1:
                e = new TumblingExperiment("Experiment 1", 3, host, port, context);
                break;
            case 2:
                e = new SlidingExperiment("Experiment 2", 5, 1, host, port, context);
                break;
            case 3:
                e = new SlidingExperiment("Experiment 3", 4, 1, host, port, context);
                break;
            case 4:
                e = new SlidingExperiment("Experiment 4", 4, 1, host, port, context);
                break;
            case 5:
                e = new TumblingExperiment("Experiment 5", 3, host, port, context);
                break;
            case 6:
                e = new TumblingExperiment("Experiment 6", 3, host, port, context);
                break;
            default:
                e = new TumblingExperiment("Experiment 0 Default", 3, host, port, context);
                break;
        }


        try {
            e.start();
        } catch (InterruptedException e1) {
            System.out.println("Experiment " + experimentNumber + " Terminated");
        }

    }
}
