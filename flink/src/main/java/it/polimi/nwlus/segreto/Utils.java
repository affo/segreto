package it.polimi.nwlus.segreto;

import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.streaming.api.functions.TimestampExtractor;
import org.apache.flink.streaming.api.windowing.windows.TimeWindow;

/**
 * Created by affo on 10/02/16.
 */
public class Utils {
    public static <T> String windowToString(TimeWindow window, Iterable<T> values) {
        String res = Thread.currentThread().getId() + " - ";

        res += "(" + window.getStart() + ") [";

        for (T v : values) {
            res += v.toString() + ", ";
        }

        res = res.substring(0, res.length() - 2);

        res += "] (" + window.getEnd() + ")";
        return res;
    }

    public static TimestampExtractor<Tuple2<Integer, Integer>> getTSExtractor() {
        return new TimestampExtractor<Tuple2<Integer, Integer>>() {
            long ts;
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
                    if (noTupleInvocations % 5 == 0) {
                        lastWM += MAX_DELAY;
                        System.out.println(">>> No incoming tuple, new WM: " + lastWM);
                    }
                }

                return lastWM;
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
}
