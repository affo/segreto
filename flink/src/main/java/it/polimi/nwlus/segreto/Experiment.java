package it.polimi.nwlus.segreto;

import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

/**
 * Created by affo on 10/02/16.
 */
public interface Experiment {
    void main(StreamExecutionEnvironment env) throws Exception;

    String getName();
}
