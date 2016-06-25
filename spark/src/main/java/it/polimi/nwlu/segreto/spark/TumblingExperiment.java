package it.polimi.nwlu.segreto.spark;

import org.apache.spark.SparkConf;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;

import java.io.Serializable;

/**
 * Created by Riccardo on 19/06/16.
 */
public class TumblingExperiment implements Experiment, Serializable {

    protected final String host;
    protected final int port;
    private String name;
    protected int omega;

    protected JavaStreamingContext context;

    public TumblingExperiment(String name, int beta, String host, int port, SparkConf conf) {
        this.name = name;
        this.omega = beta;
        this.host = host;
        this.port = port;
        context = new JavaStreamingContext(conf, Durations.milliseconds(1));
        JavaReceiverInputDStream<String> stringJavaReceiverInputDStream = context.socketTextStream(host, port);
        stringJavaReceiverInputDStream.window(Durations.seconds(omega), Durations.seconds(omega)).print();

    }


    public void start(Proxy p, Object lock) throws InterruptedException {
        context.start();
        Thread.sleep(100000);
        context.stop(true, true);
    }

}


