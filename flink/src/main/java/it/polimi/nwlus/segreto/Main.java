package it.polimi.nwlus.segreto;

import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class Main {
    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment
                .getExecutionEnvironment();
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

        int noExperiment = Integer.parseInt(args[0]);
        Experiment experiment = new Experiment1();

        switch (noExperiment) {
            case 2:
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
            case 6:
                break;
        }

        experiment.main(env);
        System.out.println("Running " + experiment.getName());
        env.execute(experiment.getName());
    }
}
