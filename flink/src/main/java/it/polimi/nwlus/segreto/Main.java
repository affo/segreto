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
                experiment = new Experiment2();
                break;
            case 3:
                //TODO experiment = new Experiment3();
                break;
            case 4:
                experiment = new Experiment4();
                break;
            case 5:
                experiment = new Experiment5();
                break;
            case 6:
                //TODO experiment = new Experiment6();
                break;
        }

        experiment.main(env, "proxy", 9999);
        System.out.println("Running " + experiment.getName());
        env.execute(experiment.getName());
    }
}
