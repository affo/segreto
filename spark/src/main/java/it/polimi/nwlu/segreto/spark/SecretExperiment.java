package it.polimi.nwlu.segreto.spark;

import org.apache.spark.streaming.Durations;
import org.apache.spark.streaming.api.java.JavaStreamingContext;

import java.io.Serializable;

/**
 * Created by Riccardo on 19/06/16.
 */
public  class SecretExperiment implements Experiment, Serializable {

    protected final String host;
    protected final int port;
    private String name;
    protected int omega, beta;

    protected JavaStreamingContext context;

    public SecretExperiment(String name, int omega, int beta, String host, int port, JavaStreamingContext context) {
        this.name = name;
        this.omega = omega;
        this.beta = beta;
        this.host = host;
        this.port = port;
        this.context = context;

        setupStream();
    }

    public SecretExperiment(String name, int omega, String host, int port, JavaStreamingContext context) {
        this(name, omega, omega, host, port, context);
    }

    public void start() throws InterruptedException {
        context.start();
        Thread.sleep(100000);
        context.stop(true, true);
    }

    protected  void setupStream(){
        context.socketTextStream(host, port).window(Durations.seconds(omega), Durations.seconds(beta)).print();
    }

}


