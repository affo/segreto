package it.polimi.nwlu.segreto.spark;

import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaStreamingContext;

/**
 * Created by Riccardo on 19/06/16.
 */
public class TumblingExperiment extends SlidingExperiment {

    public TumblingExperiment(String name, int omega, String host, int port, JavaStreamingContext context) {
        super(name, omega, omega, host, port, context);
    }

    @Override
    public void setupStream() {
        context.socketTextStream(host, port).window(Durations.seconds(omega), Durations.seconds(omega)).print();
    }


}


