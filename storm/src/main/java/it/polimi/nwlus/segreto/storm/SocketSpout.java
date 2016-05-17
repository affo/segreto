package it.polimi.nwlus.segreto.storm;

import org.apache.storm.spout.SpoutOutputCollector;
import org.apache.storm.task.TopologyContext;
import org.apache.storm.topology.OutputFieldsDeclarer;
import org.apache.storm.topology.base.BaseRichSpout;
import org.apache.storm.tuple.Fields;
import org.apache.storm.tuple.Values;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.Map;

/**
 * Created by affo on 17/05/16.
 */
public class SocketSpout extends BaseRichSpout {
    private SpoutOutputCollector collector;
    private String host;
    private int port;

    private BufferedReader in;

    public SocketSpout(String host, int port) {
        this.host = host;
        this.port = port;
    }

    public void declareOutputFields(OutputFieldsDeclarer decl) {
        decl.declare(new Fields("ts", "val"));
    }

    public void open(Map map, TopologyContext topologyContext, SpoutOutputCollector collector) {
        this.collector = collector;
        try {
            Socket s = new Socket(host, port);
            in = new BufferedReader(
                    new InputStreamReader(s.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void nextTuple() {
        try {
            String raw = in.readLine();

            if (raw == null) {
                // make the topology die
                throw new SocketSpoutKO();
            }

            // parse
            raw = raw.replace("(", "");
            raw = raw.replace(")", "");
            String[] fields = raw.split(",");

            collector.emit(
                    new Values(
                            Long.parseLong(fields[0] + "000"),
                            Integer.parseInt(fields[1])
                    )
            );

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private class SocketSpoutKO extends RuntimeException {
    }
}
