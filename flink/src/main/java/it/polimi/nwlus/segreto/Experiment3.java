package it.polimi.nwlus.segreto;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.datastream.DataStreamSource;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.windowing.AllWindowFunction;
import org.apache.flink.streaming.api.windowing.time.Time;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

import java.util.concurrent.TimeUnit;

/**
 * Time-based Window Experiment 3
 * <p>
 * Input:
 * <p>
 * InStream(Time, Val) = {(3,10),(5,20),(5,30),(5,40),(5,50),(7,60), ...}
 * <p>
 * We configured input stream in three different ways:
 * STuple (one tuple per batch),
 * STime (all simutaneous tuples in the same batch),
 * SBatch (two simultaneous tuples per batch).
 * As discussed in Setup of Coral8, STime and SBatch are derived streams obtained by using joins.
 * Details can be found in query files of the experiment.
 * <p>
 * Query:
 * <p>
 * Continuously compute the sum value of the tuples in the input stream using a time-based window of 4 seconds.
 * <p>
 * Notably, Slide of 1 Second
 */
public abstract class Experiment3 implements Experiment {
    @Override
    public void main(StreamExecutionEnvironment env, String ip, int port) throws Exception {

        DataStreamSource<String> socketStream = env.socketTextStream(ip, port);

        DataStream<Tuple2<Integer, Integer>> input = socketStream.flatMap(new FlatMapFunction<String, Tuple2<Integer, Integer>>() {
            @Override
            public void flatMap(String s, Collector<Tuple2<Integer, Integer>> out) throws Exception {
                out.collect(Utils.parseTuple(s));
            }
        });


        DataStream<Tuple2<Integer, Tuple2<Integer, Integer>>> coral8Stream = Utils.toCoral8Stream(env, input, getBatchSize());


        coral8Stream
                .timeWindowAll(Time.of(4, TimeUnit.SECONDS), Time.of(1, TimeUnit.SECONDS))
                .apply(new AllWindowFunction<Tuple2<Integer, Tuple2<Integer, Integer>>, String, TimeWindow>() {
                    @Override
                    public void apply(
                            TimeWindow window,
                            Iterable<Tuple2<Integer, Tuple2<Integer, Integer>>> values,
                            Collector<String> out) throws Exception {
                        String res = Utils.windowToString(window, values);
                        out.collect(res);
                    }
                })
                .print();
    }

    public abstract int getBatchSize();
}
