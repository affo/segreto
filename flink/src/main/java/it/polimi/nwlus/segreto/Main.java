package it.polimi.nwlus.segreto;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.util.Collector;

import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws Exception {
        if (args.length < 3) {
            throw new Exception("Pass count, windowSize and windowSlide, please.");
        }

        boolean count = Integer.parseInt(args[0]) != 0;
        int windowSize = Integer.parseInt(args[1]);
        int windowSlide = Integer.parseInt(args[2]);

        final StreamExecutionEnvironment env = StreamExecutionEnvironment
                .getExecutionEnvironment();
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

        DataStreamSource<String> socketStream = env.socketTextStream("proxy", 9999);

        DataStream<Tuple2<Integer, Integer>> input = socketStream.flatMap(
                new FlatMapFunction<String, Tuple2<Integer, Integer>>() {
                    @Override
                    public void flatMap(String s, Collector<Tuple2<Integer, Integer>> out) throws Exception {
                        out.collect(Utils.parseTuple(s));
                    }
                }
        );

        String windowType = count ? "Count" : "Time";
        System.out.println(">>> " + windowType + " window -> size: " + windowSize
                + ", slide: " + windowSlide);

        input = input.assignTimestamps(Utils.<Tuple2<Integer, Integer>>getTSExtractor());

        if (count) {
            input
                    .countWindowAll(windowSize, windowSlide)
                    .apply(Utils.<Tuple2<Integer, Integer>>countWindowFunction())
                    .print();
        } else {
            input
                    .timeWindowAll(
                            Time.of(windowSize, TimeUnit.SECONDS),
                            Time.of(windowSlide, TimeUnit.SECONDS))
                    .apply(Utils.<Tuple2<Integer, Integer>>timeWindowFunction())
                    .print();
        }

        env.execute();
    }
}
