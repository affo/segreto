package it.polimi.nwlus.segreto;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.java.tuple.Tuple;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.apache.flink.streaming.api.functions.AscendingTimestampExtractor;
import org.apache.flink.streaming.api.functions.TimestampExtractor;
import org.apache.flink.streaming.api.functions.windowing.AllWindowFunction;
import org.apache.flink.streaming.api.windowing.windows.GlobalWindow;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;
import org.apache.flink.util.Collector;

/**
 * Created by affo on 10/02/16.
 */
public class Utils {
    public static <T> AllWindowFunction<T, String, GlobalWindow> countWindowFunction() {
        return new AllWindowFunction<T, String, GlobalWindow>() {
            @Override
            public void apply(
                    GlobalWindow window,
                    Iterable<T> values,
                    Collector<String> out) throws Exception {
                String res = Thread.currentThread().getId() + " - ";

                res += "[";

                for (T v : values) {
                    res += v.toString() + ", ";
                }

                res = res.substring(0, res.length() - 2);

                res += "]";

                out.collect(res);
            }
        };
    }

    public static <T> AllWindowFunction<T, String, TimeWindow> timeWindowFunction() {
        return new AllWindowFunction<T, String, TimeWindow>() {
            @Override
            public void apply(
                    TimeWindow window,
                    Iterable<T> values,
                    Collector<String> out) throws Exception {
                String res = Thread.currentThread().getId() + " - ";

                res += "(" + window.getStart() + ") [";

                for (T v : values) {
                    res += v.toString() + ", ";
                }

                res = res.substring(0, res.length() - 2);

                res += "] (" + window.getEnd() + ")";

                out.collect(res);
            }
        };
    }

    public static <T extends Tuple> TimestampExtractor<T> getTSExtractor() {
        return new TimestampExtractor<T>() {
            long ts;
            long lastWM;

            @Override
            public long extractTimestamp(T element, long currentTimestamp) {
                ts = (int) element.getField(0) * 1000;
                return ts;
            }

            @Override
            public long extractWatermark(T element, long currentTimestamp) {
                return Long.MIN_VALUE;
            }

            @Override
            public long getCurrentWatermark() {
                long wm = ts - 1000;
                if (wm > lastWM) {
                    System.out.println(">>> New Watermark Emitted: " + wm);
                    lastWM = wm;
                }

                return wm;
            }
        };
    }

    public static TimestampExtractor<Tuple2<Integer, Integer>> getTimePassingExtractor() {
        return new TimestampExtractor<Tuple2<Integer, Integer>>() {
            int noTupleInvocations = 0;
            long MAX_DELAY = 2000;
            long ts = Long.MIN_VALUE;
            long lastWM;

            @Override
            public long extractTimestamp(Tuple2<Integer, Integer> element, long currentTimestamp) {
                ts = element.f0 * 1000;
                return ts;
            }

            @Override
            public long extractWatermark(Tuple2<Integer, Integer> element, long currentTimestamp) {
                return Long.MIN_VALUE;
            }

            @Override
            public long getCurrentWatermark() {
                if (ts == Long.MIN_VALUE) {
                    return Long.MIN_VALUE;
                }

                long wm = ts - MAX_DELAY;

                if (wm > lastWM) {
                    System.out.println(">>> New Watermark Emitted: " + wm);
                    lastWM = wm;
                } else {
                    noTupleInvocations++;
                    // TODO fix this according to getAutoWatermarkInterval
                    if (noTupleInvocations % 5 == 0) {
                        lastWM += MAX_DELAY;
                        System.out.println(">>> No incoming tuple, new WM: " + lastWM);
                    }
                }

                return lastWM;
            }
        };
    }

    public static TimestampExtractor<Tuple2<Integer, Integer>> getAscendingExtractor() {
        return new AscendingTimestampExtractor<Tuple2<Integer, Integer>>() {
            @Override
            public long extractAscendingTimestamp(Tuple2<Integer, Integer> element, long previousElementTimestamp) {
                return element.f0 * 1000;
            }
        };
    }

    public static Tuple2<Integer, Integer> parseTuple(String s) {

          /*(Integer, Integer) */

        String[] values = s.trim().replace(")", "").replace("(", "").split(",");
        int timestamp = Integer.parseInt(values[0]);
        int value = Integer.parseInt(values[1]);
        return new Tuple2<>(timestamp, value);
    }

    public static <T extends Tuple> DataStream<Tuple2<Integer, T>> toCoral8Stream(
            final StreamExecutionEnvironment env,
            final DataStream<T> ds,
            final int BATCH_SIZE) {
        if (env.getStreamTimeCharacteristic() != TimeCharacteristic.EventTime) {
            throw new RuntimeException("Set time to EventTime, please...");
        }

        return ds
                .map(new MapFunction<T, Tuple2<Integer, T>>() {
                    long lastTs = Long.MIN_VALUE;
                    int bid = 0;
                    int count = 0;

                    @Override
                    public Tuple2<Integer, T> map(T record) throws Exception {
                        // we extract the timestamp manually
                        int ts = (int) record.getField(0) * 1000;

                        if (count >= BATCH_SIZE || ts > lastTs) {
                            bid++; // be consistent with seconds, please
                            count = 1;
                        } else if (ts == lastTs) {
                            // ok, we are at the same event time
                            count++;
                        } else {
                            // out of order event
                            // should never happen
                            throw new RuntimeException("Out of order event in Coral8 converter");
                        }

                        lastTs = ts;
                        return new Tuple2<>(bid, record);
                    }
                })
                .assignTimestamps(Utils.<Tuple2<Integer, T>>getTSExtractor());
    }
}
