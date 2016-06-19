package it.polimi.spark.hellow;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaDStream;
import org.apache.spark.streaming.api.java.JavaReceiverInputDStream;
import org.apache.spark.streaming.api.java.JavaStreamingContext;

import java.util.Arrays;

/**
 * Created by Riccardo on 16/06/16.
 */
public class StreamSparkApp {
    private static final String host = "localhost";
    private static final int port = 9999;

    public static void main(String[] args) {

        SparkConf conf = new SparkConf().setMaster("local[*]").setAppName("StreamSparkApp");

        JavaStreamingContext context = new JavaStreamingContext(conf, Durations.seconds(5));
        Logger.getRootLogger().setLevel(Level.ERROR);

        JavaReceiverInputDStream<String> lines = context.socketTextStream(host, port);

        JavaDStream<String> words = lines.window(Durations.seconds(10));


        JavaDStream<String> stringJavaDStream = words.flatMap(
                new FlatMapFunction<String, String>() {
                    public Iterable<String> call(String x) {
                        return Arrays.asList(x.split(" "));
                    }
                });

        stringJavaDStream.print();

        context.start();
        context.awaitTermination();

    }
}
