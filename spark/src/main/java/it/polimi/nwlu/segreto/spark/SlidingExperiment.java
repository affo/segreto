package it.polimi.nwlu.segreto.spark;

import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaStreamingContext;

/**
 * Created by Riccardo on 19/06/16.
 */
public class SlidingExperiment extends SecretExperiment {

    public SlidingExperiment(String name, int omega, int beta, String host, int port, JavaStreamingContext context) {
        super(name, omega, beta, host, port, context);
    }

    @Override
    protected void setupStream() {
        context.socketTextStream(host, port).window(Durations.seconds(omega), Durations.seconds(beta)).print();
    }


}


