package it.polimi.nwlu.segreto.spark;

import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaStreamingContext;

/**
 * Created by Riccardo on 19/06/16.
 */
public class SlidingExperiment extends TumblingExperiment {

    private int omega;

    public SlidingExperiment(String name, int omega, int beta, String host, int port, SparkConf conf) {
        super(name, beta, host, port, conf);
        this.omega = omega;
    }

    public void main(SparkConf conf) {
        JavaStreamingContext context = new JavaStreamingContext(conf, Durations.seconds(this.omega));

        context.socketTextStream(host, port)
                .window(Durations.seconds(this.omega), Durations.seconds(omega))
                .print();

        //stringJavaDStream.print();

        context.start();
        context.awaitTermination();
    }
}
