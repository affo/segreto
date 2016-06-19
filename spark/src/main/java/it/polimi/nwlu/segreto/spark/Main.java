package it.polimi.nwlu.segreto.spark;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;

/**
 * Created by Riccardo on 16/06/16.
 */
public class Main {
    private static final String host = "proxy";
    private static final int port = 9999;

    public static void main(String[] args) throws InterruptedException {

        Integer experimentNumber = Integer.parseInt(args[0]);
        SparkConf conf = new SparkConf().setAppName("SparkExperiment" + experimentNumber);//.setMaster("local[*]");

        Logger.getRootLogger().setLevel(Level.ERROR);

        Experiment e = null;


        // The six time window experiments of SECRET
        switch (experimentNumber) {
            case 0:
                e = new TumblingExperiment("Experiment 0", 3, host, port);
                break;
            case 1:
                e = new TumblingExperiment("Experiment 1", 3, host, port);
                break;
            case 2:
                e = new SlidingExperiment("Experiment 2", 5, 1, host, port);
                break;
            case 3:
                e = new SlidingExperiment("Experiment 3", 4, 1, host, port);
                break;
            case 4:
                e = new SlidingExperiment("Experiment 4", 4, 1, host, port);
                break;
            case 5:
                e = new TumblingExperiment("Experiment 5", 3, host, port);
                break;
            case 6:
                e = new TumblingExperiment("Experiment 6", 3, host, port);
                break;
            default:
                e = null;
                break;
        }

        e.main(conf);

    }
}
