package it.polimi.nwlu.segreto.spark;

import org.apache.spark.SparkConf;

/**
 * Created by Riccardo on 19/06/16.
 */
public interface Experiment {

    public void main(SparkConf context) throws InterruptedException;
}
