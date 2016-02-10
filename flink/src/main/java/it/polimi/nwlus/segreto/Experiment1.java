package it.polimi.nwlus.segreto;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.AllWindowFunction;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.util.concurrent.TimeUnit;

/**
 * Time-based Window Experiment 1
 * <p>
 * Input:
 * InStream(Time, Val) = {(10,10),(11,20),(12,30),(13,40),(14,50),(15,60),(16,70), ...}
 * <p>
 * Query:
 * Continuously compute the average value of the tuples
 * in the input stream using a time-based tumbling window of 3 seconds.
 */
public class Experiment1 implements Experiment {
    public void main(StreamExecutionEnvironment env) throws Exception {
        DataStream<Tuple2<Integer, Integer>> input = env.fromElements(
                new Tuple2<>(10, 10),
                new Tuple2<>(11, 20),
                new Tuple2<>(12, 30),
                new Tuple2<>(13, 40),
                new Tuple2<>(14, 50),
                new Tuple2<>(15, 60),
                new Tuple2<>(16, 70)
        );

        input
                .assignTimestamps(Utils.getTSExtractor())
                .timeWindowAll(Time.of(3, TimeUnit.MILLISECONDS))
                /*
                .apply(new AllWindowFunction<Tuple2<Integer, Integer>, Double, TimeWindow>() {
                    @Override
                    public void apply(TimeWindow window, Iterable<Tuple2<Integer, Integer>> values, Collector<Double> out) throws Exception {
                        int count = 0;
                        double sum = 0;

                        for (Tuple2<Integer, Integer> t : values) {
                            sum += t.f1;
                            count++;
                        }

                        System.out.println(getWindowRepr(window, values));

                        if (count > 0) {
                            out.collect(sum / count);
                        }
                    }
                })
                */
                .apply(new AllWindowFunction<Tuple2<Integer, Integer>, String, TimeWindow>() {
                    @Override
                    public void apply(
                            TimeWindow window,
                            Iterable<Tuple2<Integer, Integer>> values,
                            Collector<String> out) throws Exception {
                        String res = Utils.windowToString(window, values);
                        out.collect(res);
                    }
                })
                .print();
    }

    @Override
    public String getName() {
        return "Esperimento SEGRETO 1";
    }
}
