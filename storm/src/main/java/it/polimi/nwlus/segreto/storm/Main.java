package it.polimi.nwlus.segreto.storm;

import org.apache.storm.Config;
import org.apache.storm.LocalCluster;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.TopologyBuilder;
import org.apache.storm.topology.base.BaseWindowedBolt;
import org.apache.storm.tuple.Tuple;
import org.apache.storm.windowing.TupleWindow;

import java.util.concurrent.TimeUnit;

public class Main {
    public static final String TOPOLOGY_NAME = "segreto-test";

    public static class WindowPrinter extends BaseWindowedBolt {
        public void declareOutputFields(OutputFieldsDeclarer declarer) {
        }

        public void execute(TupleWindow tupleWindow) {
            String res = "[";
            for (Tuple t : tupleWindow.get()) {
                res += "(";
                res += t.getLong(0);
                res += ",";
                res += t.getInteger(1);
                res += "), ";
            }
            res = res.substring(0, res.length() - 2);
            res += "]";
            System.out.println(res);
        }
    }

    public static void main(String[] args) throws Exception {

        if (args.length < 3) {
            throw new Exception("Pass count, windowSize and windowSlide, please.");
        }

        boolean count = Integer.parseInt(args[0]) != 0;
        int windowSize = Integer.parseInt(args[1]);
        int windowSlide = Integer.parseInt(args[2]);

        TopologyBuilder builder = new TopologyBuilder();

        builder.setSpout("spout", new SocketSpout("proxy", 9999), 1);

        WindowPrinter wp = new WindowPrinter();

        String windowType = count ? "Count" : "Time";
        System.out.println(">>> " + windowType + " window -> size: " + windowSize
                + ", slide: " + windowSlide);

        if (count) {
            wp.withWindow(
                    new BaseWindowedBolt.Count(windowSize),
                    new BaseWindowedBolt.Count(windowSlide)
            );

            // no watermarking machanism in this case
        } else {
            wp.withWindow(
                    new BaseWindowedBolt.Duration(windowSize, TimeUnit.SECONDS),
                    new BaseWindowedBolt.Duration(windowSlide, TimeUnit.SECONDS)
            );

            wp.withTimestampField("ts");
            wp.withLag(new BaseWindowedBolt.Duration(1, TimeUnit.SECONDS));
            // as in Flink
            wp.withWatermarkInterval(new BaseWindowedBolt.Duration(200, TimeUnit.MILLISECONDS));
        }

        builder.setBolt("windower", wp, 1).globalGrouping("spout");

        Config conf = new Config();
        conf.setDebug(false);
        // avoid org.apache.storm.topology.WindowedBoltExecutor.ensureDurationLessThanTimeout
        conf.setMessageTimeoutSecs(windowSize + windowSlide);

        LocalCluster cluster = new LocalCluster();
        cluster.submitTopology(TOPOLOGY_NAME, conf, builder.createTopology());
        Thread.sleep(30000);
        cluster.killTopology(TOPOLOGY_NAME);
        cluster.shutdown();
    }
}
