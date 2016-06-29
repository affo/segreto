package it.polimi.nwlus.segreto.kafka.streams;

import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.kstream.KStream;
import org.apache.kafka.streams.kstream.KStreamBuilder;
import org.apache.kafka.streams.kstream.TimeWindows;

import java.util.Properties;

public class Experiment {

    public static void main(String[] args) throws Exception {
        Properties streamsConfiguration = new Properties();
        // Give the Streams application a unique name.  The name must be unique in the Kafka cluster
        // against which the application is run.
        streamsConfiguration.put(StreamsConfig.APPLICATION_ID_CONFIG, "segreto-experiments");
        // Where to find Kafka broker(s).
        streamsConfiguration.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "kafka:9092");
        // Where to find the corresponding ZooKeeper ensemble.
        streamsConfiguration.put(StreamsConfig.ZOOKEEPER_CONNECT_CONFIG, "zookeeper:2181");
        // default (de)serializers
        streamsConfiguration.put(StreamsConfig.KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        streamsConfiguration.put(StreamsConfig.VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass().getName());
        // timestamp extractor
        streamsConfiguration.put(StreamsConfig.TIMESTAMP_EXTRACTOR_CLASS_CONFIG, MyExtractor.class);

        if (args.length < 2) {
            throw new Exception("Pass windowSize and windowSlide in seconds, please.");
        }

        long windowSize = Integer.parseInt(args[0]) * 1000L;
        long windowSlide = Integer.parseInt(args[1]) * 1000L;

        System.out.println(">>> Set up with window size " + windowSize
                + " and slide " + windowSlide);

        KStreamBuilder builder = new KStreamBuilder();

        KStream<String, String> input = builder.stream("InTopic");

        KStream<String, String> output = input
                // use the same key for every tuple in the way that they end
                // up in the very same partition
                .map((key, tup) -> {
                    String[] values = tup.trim().split(",");
                    String val = values[0] + "-" + values[1];
                    return new KeyValue<>("key", val);
                })
                .reduceByKey((acc, val) ->
                                acc + ", " + val,
                        TimeWindows.of("WordWindow", windowSize).advanceBy(windowSlide))
                .toStream((wordWindow, value) -> "[" + wordWindow.window().start() + " - "
                        + wordWindow.window().end() + "]");

        output.to("OutTopic");

        KafkaStreams streams = new KafkaStreams(builder, streamsConfiguration);
        streams.start();
    }

}