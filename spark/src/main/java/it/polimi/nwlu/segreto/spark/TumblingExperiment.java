package it.polimi.nwlu.segreto.spark;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.or.ThreadGroupRenderer;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;

import java.io.Serializable;
import java.util.Arrays;

/**
 * Created by Riccardo on 19/06/16.
 */
public class TumblingExperiment implements Experiment, Serializable {

    protected final String host;
    protected final int port;
    private String name;
    protected int beta;

    public TumblingExperiment(String name, int beta, String host, int port) {
        this.name = name;
        this.beta = beta;
        this.host = host;
        this.port = port;
    }

    public void main(SparkConf conf) throws InterruptedException {

        System.out.println(name);
        JavaStreamingContext context = new JavaStreamingContext(conf, Durations.seconds(beta));

        JavaReceiverInputDStream<String> stringJavaReceiverInputDStream = context.socketTextStream(host, port);
        stringJavaReceiverInputDStream.window(Durations.seconds(beta)).print();


        context.start();

        System.out.println("Sleep 30 sec");
        Thread.sleep(30000);
        System.out.println("Wake up");
        context.stop();
    }
}
