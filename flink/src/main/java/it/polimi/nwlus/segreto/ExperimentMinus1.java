package it.polimi.nwlus.segreto;

import org.apache.flink.api.common.functions.FlatMapFunction;
import org.apache.flink.api.common.functions.MapFunction;
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

/**
 * Created by affo on 23/02/16.
 */
public class ExperimentMinus1 implements Experiment {
    @Override
    public void main(StreamExecutionEnvironment env, String ip, int port) throws Exception {
        DataStreamSource<String> socketStream = env.socketTextStream(ip, port);

        DataStream<Integer> input = socketStream.map(new MapFunction<String, Integer>() {
            @Override
            public Integer map(String s) throws Exception {
                return Utils.parseTuple(s).f1;
            }
        });

        env.setStreamTimeCharacteristic(TimeCharacteristic.ProcessingTime);

        input
                .timeWindowAll(Time.of(3, TimeUnit.SECONDS))
                .apply(new AllWindowFunction<Integer, String, TimeWindow>() {
                    @Override
                    public void apply(
                            TimeWindow window,
                            Iterable<Integer> values,
                            Collector<String> out) throws Exception {
                        String res = Utils.windowToString(window, values);
                        out.collect(res);
                    }
                })
                .print();
    }

    @Override
    public String getName() {
        return "Esperimento SEGRETO -1";
    }
}
