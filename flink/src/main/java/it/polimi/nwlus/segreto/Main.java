package it.polimi.nwlus.segreto;

import org.apache.flink.streaming.api.TimeCharacteristic;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;

public class Main {
    public static void main(String[] args) throws Exception {
        final StreamExecutionEnvironment env = StreamExecutionEnvironment
                .getExecutionEnvironment();
        env.setStreamTimeCharacteristic(TimeCharacteristic.EventTime);

        int noExperiment = Integer.parseInt(args[0]);
        Experiment experiment = new ExperimentMinus1();

        switch (noExperiment) {
            case 0:
                experiment = new Experiment0();
                break;
            case 1:
                experiment = new Experiment1();
                break;
            case 2:
                experiment = new Experiment2();
                break;
            case 3:
                String mod = "stuple";

                if (args.length >= 2) {
                    mod = args[1];
                }

                switch (mod) {
                    case "stime":
                        experiment = new Experiment3() {
                            @Override
                            public String getName() {
                                return "Esperimento SEGRETO 3 - STIME";
                            }

                            @Override
                            public int getBatchSize() {
                                return Integer.MAX_VALUE;
                            }
                        };
                        break;
                    case "sbatch":
                        experiment = new Experiment3() {
                            @Override
                            public String getName() {
                                return "Esperimento SEGRETO 3 - SBATCH";
                            }

                            @Override
                            public int getBatchSize() {
                                return 2;
                            }
                        };
                        break;
                    default:
                        experiment = new Experiment3() {
                            @Override
                            public String getName() {
                                return "Esperimento SEGRETO 3 - STUPLE";
                            }

                            @Override
                            public int getBatchSize() {
                                return 1;
                            }
                        };
                }
                break;
            case 4:
                experiment = new Experiment4();
                break;
            case 5:
                experiment = new Experiment5();
                break;
            case 6:
                experiment = new Experiment6();
                break;
        }

        experiment.main(env, "proxy", 9999);
        System.out.println("Running " + experiment.getName());
        env.execute(experiment.getName());
    }
}
