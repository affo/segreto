package it.polimi.nwlus.segreto;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.AllWindowFunction;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws Exception {
        if (args.length < 2) {
            throw new Exception("Pass windowSize and windowSlide in seconds, please.");
        }

        int windowSize = Integer.parseInt(args[0]);
        int windowSlide = Integer.parseInt(args[1]);

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

        System.out.println(">>> Set up with window size " + windowSize
                + " and slide " + windowSlide);

        input
                .assignTimestamps(Utils.<Tuple2<Integer, Integer>>getTSExtractor())
                .timeWindowAll(
                        Time.of(windowSize, TimeUnit.SECONDS),
                        Time.of(windowSlide, TimeUnit.SECONDS)
                )
                .apply(
                        new AllWindowFunction<Tuple2<Integer, Integer>, String, TimeWindow>() {
                            @Override
                            public void apply(
                                    TimeWindow window,
                                    Iterable<Tuple2<Integer, Integer>> values,
                                    Collector<String> out) throws Exception {
                                String res = Utils.windowToString(window, values);
                                out.collect(res);
                            }
                        }
                )
                .print();

        env.execute();
    }
}
