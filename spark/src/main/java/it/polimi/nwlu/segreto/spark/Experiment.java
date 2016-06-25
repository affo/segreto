package it.polimi.nwlu.segreto.spark;

/**
 * Created by Riccardo on 19/06/16.
 */
public interface Experiment {


    public void start(Proxy p, Object lock) throws InterruptedException;

}
