import basic.Query;
import basic.Stream;
import basic.Tuple;
import engine.*;
import params.TickParam;

import java.io.*;
import java.util.StringTokenizer;
import java.util.Vector;


/**
 * @author dindarn
 */

/**
 * @author dindarn
 */
public class SecretMainExecuter {

    private static int tAttrNum = 4;
    private static Stream myStream = null;
    private static Engine engine;
    private static Query query;

    // SET PROJECT DIRECTORY
    private static String projectDirectory = "";

    /**
     * @param args
     */
    public static void main(String[] args) {


        if (args.length == 0) {
            /*
             * is no args are specified simulator prints results of VLDB journal experiments
			 * */
            vldbJournalTimeBasedExperiments();
            vldbJournalTupleBasedExperiments();
        } else if (args.length == 6) {
            /*
             * input specifications data file's path and
			 * timestamp of the first tuple needed for time-based 
			 * windows, value is ignored for tuple-based windows 
			 * 
			 * */
            String inputFilename = args[0];
            int tstart = Integer.parseInt(args[1]);

			/*
             * engine name
			 */
            String engineName = args[2];

			/*
             * query parameters: size, slide, type
			 * */
            int size = Integer.parseInt(args[3]);
            int slide = Integer.parseInt(args[4]);
            /* qtype=0 is for time-based windows
			 * qtype=1 is for tuple-based windows*/
            int qtype = Integer.parseInt(args[5]);

            String result = run(inputFilename, tstart, engineName, size, slide, qtype);
            System.out.println(result);
        } else {
            throw new IllegalArgumentException
                    ("Incorrect number of command line arguments: "
                            + args.length + "\nShould be five");

        }
    }

    /**
     * Runs SECRET simulator for vldb journal time-based window experiments
     */
    public static void vldbJournalTimeBasedExperiments() {
        System.out.println("TIME-BASED WINDOW EXPERIMENTS\n");
        // Experiment # 1
        System.out.println("\nEXPERIMENT # 1\n");

        String result = run(projectDirectory + "data/VLDBJournalTimeBasedExp/ex1.csv", 10, "OracleCEP", 3, 3, 0);
        System.out.println(result);

        result = run(projectDirectory + "data/VLDBJournalTimeBasedExp/ex1.csv", 10, "StreamBase", 3, 3, 0);
        System.out.println(result);

        result = run(projectDirectory + "data/VLDBJournalTimeBasedExp/ex1.csv", 10, "Flink", 3, 3, 0);
        System.out.println(result);

        result = run(projectDirectory + "data/VLDBJournalTimeBasedExp/ex1.csv", 10, "Storm", 3, 3, 0);
        System.out.println(result);

        result = run(projectDirectory + "data/VLDBJournalTimeBasedExp/ex1.csv", 10, "KafkaStreams", 3, 3, 0);
        System.out.println(result);

        result = run(projectDirectory + "data/VLDBJournalTimeBasedExp/ex1.csv", 10, "Spark16", 3, 3, 0);
        System.out.println(result);

        // Experiment # 2
        System.out.println("\nEXPERIMENT # 2\n");

        result = run(projectDirectory + "data/VLDBJournalTimeBasedExp/ex2.csv", 30, "Coral8", 5, 1, 0);
        System.out.println(result);

        result = run(projectDirectory + "data/VLDBJournalTimeBasedExp/ex2.csv", 30, "OracleCEP", 5, 1, 0);
        System.out.println(result);

        result = run(projectDirectory + "data/VLDBJournalTimeBasedExp/ex2.csv", 30, "STREAM", 5, 1, 0);
        System.out.println(result);

        result = run(projectDirectory + "data/VLDBJournalTimeBasedExp/ex2.csv", 30, "StreamBase", 5, 1, 0);
        System.out.println(result);

        result = run(projectDirectory + "data/VLDBJournalTimeBasedExp/ex2.csv", 30, "Flink", 5, 1, 0);
        System.out.println(result);

        result = run(projectDirectory + "data/VLDBJournalTimeBasedExp/ex2.csv", 30, "Storm", 5, 1, 0);
        System.out.println(result);

        result = run(projectDirectory + "data/VLDBJournalTimeBasedExp/ex2.csv", 30, "KafkaStreams", 5, 1, 0);
        System.out.println(result);

        result = run(projectDirectory + "data/VLDBJournalTimeBasedExp/ex2.csv", 30, "Spark16", 5, 1, 0);
        System.out.println(result);

        // Experiment # 3
        System.out.println("\nEXPERIMENT # 3\n");

        result = run(projectDirectory + "data/VLDBJournalTimeBasedExp/ex3.csv", 3, "Coral8", 4, 1, 0);
        System.out.println(result);

        result = run(projectDirectory + "data/VLDBJournalTimeBasedExp/ex3-time.csv", 3, "Coral8", 4, 1, 0);
        System.out.println(result);

        result = run(projectDirectory + "data/VLDBJournalTimeBasedExp/ex3-batch.csv", 3, "Coral8", 4, 1, 0);
        System.out.println(result);

        // Experiment # 4
        System.out.println("\nEXPERIMENT # 4\n");

        result = run(projectDirectory + "data/VLDBJournalTimeBasedExp/ex3.csv", 3, "OracleCEP", 4, 1, 0);
        System.out.println(result);

        result = run(projectDirectory + "data/VLDBJournalTimeBasedExp/ex3.csv", 3, "STREAM", 4, 1, 0);
        System.out.println(result);

        result = run(projectDirectory + "data/VLDBJournalTimeBasedExp/ex3.csv", 3, "StreamBase", 4, 1, 0);
        System.out.println(result);

        result = run(projectDirectory + "data/VLDBJournalTimeBasedExp/ex3.csv", 3, "Flink", 4, 1, 0);
        System.out.println(result);

        result = run(projectDirectory + "data/VLDBJournalTimeBasedExp/ex3.csv", 3, "Storm", 4, 1, 0);
        System.out.println(result);

        result = run(projectDirectory + "data/VLDBJournalTimeBasedExp/ex3.csv", 3, "KafkaStreams", 4, 1, 0);
        System.out.println(result);

        result = run(projectDirectory + "data/VLDBJournalTimeBasedExp/ex3.csv", 3, "Spark16", 4, 1, 0);
        System.out.println(result);

        // Experiment # 5
        System.out.println("\nEXPERIMENT # 5\n");

        result = run(projectDirectory + "data/VLDBJournalTimeBasedExp/ex5.csv", 11, "Coral8", 3, 3, 0);
        System.out.println(result);

        result = run(projectDirectory + "data/VLDBJournalTimeBasedExp/ex5.csv", 11, "OracleCEP", 3, 3, 0);
        System.out.println(result);

        result = run(projectDirectory + "data/VLDBJournalTimeBasedExp/ex5.csv", 11, "StreamBase", 3, 3, 0);
        System.out.println(result);

        result = run(projectDirectory + "data/VLDBJournalTimeBasedExp/ex5.csv", 11, "Flink", 3, 3, 0);
        System.out.println(result);

        result = run(projectDirectory + "data/VLDBJournalTimeBasedExp/ex5.csv", 11, "Storm", 3, 3, 0);
        System.out.println(result);

        result = run(projectDirectory + "data/VLDBJournalTimeBasedExp/ex5.csv", 11, "KafkaStreams", 3, 3, 0);
        System.out.println(result);

        result = run(projectDirectory + "data/VLDBJournalTimeBasedExp/ex5.csv", 11, "Spark16", 3, 3, 0);
        System.out.println(result);

        // Experiment # 6
        System.out.println("\nEXPERIMENT # 6\n");

        result = run(projectDirectory + "data/VLDBJournalTimeBasedExp/ex6.csv", 3, "Coral8", 3, 3, 0);
        System.out.println(result);

        result = run(projectDirectory + "data/VLDBJournalTimeBasedExp/ex6-time.csv", 3, "Coral8", 3, 3, 0);
        System.out.println(result);

        result = run(projectDirectory + "data/VLDBJournalTimeBasedExp/ex6-batch.csv", 3, "Coral8", 3, 3, 0);
        System.out.println(result);

        result = run(projectDirectory + "data/VLDBJournalTimeBasedExp/ex6.csv", 3, "OracleCEP", 3, 3, 0);
        System.out.println(result);

        result = run(projectDirectory + "data/VLDBJournalTimeBasedExp/ex6.csv", 3, "StreamBase", 3, 3, 0);
        System.out.println(result);

        result = run(projectDirectory + "data/VLDBJournalTimeBasedExp/ex6.csv", 3, "Flink", 3, 3, 0);
        System.out.println(result);

        result = run(projectDirectory + "data/VLDBJournalTimeBasedExp/ex6.csv", 3, "Storm", 3, 3, 0);
        System.out.println(result);

        result = run(projectDirectory + "data/VLDBJournalTimeBasedExp/ex6.csv", 3, "KafkaStreams", 3, 3, 0);
        System.out.println(result);

        result = run(projectDirectory + "data/VLDBJournalTimeBasedExp/ex6.csv", 3, "Spark16", 3, 3, 0);
        System.out.println(result);

    }

    /**
     * Runs SECRET simulator for vldb journal tuple-based window experiments
     */
    public static void vldbJournalTupleBasedExperiments() {
        System.out.println("\n\nTUPLE-BASED WINDOW EXPERIMENTS\n");
        // Experiment # 1
        System.out.println("\nEXPERIMENT # 1\n");

        String result = run(projectDirectory + "data/VLDBJournalTupleBasedExp/ex1.csv", 10, "OracleCEP", 3, 2, 1);
        System.out.println(result);

        result = run(projectDirectory + "data/VLDBJournalTupleBasedExp/ex1.csv", 10, "StreamBase", 3, 2, 1);
        System.out.println(result);

        // Experiment # 2
        System.out.println("\nEXPERIMENT # 2\n");

        result = run(projectDirectory + "data/VLDBJournalTupleBasedExp/ex1.csv", 10, "Coral8", 3, 3, 1);
        System.out.println(result);

        result = run(projectDirectory + "data/VLDBJournalTupleBasedExp/ex1.csv", 10, "OracleCEP", 3, 3, 1);
        System.out.println(result);

        result = run(projectDirectory + "data/VLDBJournalTupleBasedExp/ex1.csv", 10, "StreamBase", 3, 3, 1);
        System.out.println(result);


        // Experiment # 3
        System.out.println("\nEXPERIMENT # 3\n");

        result = run(projectDirectory + "data/VLDBJournalTupleBasedExp/ex3-tuple.csv", 10, "Coral8", 1, 1, 1);
        System.out.println(result);

        result = run(projectDirectory + "data/VLDBJournalTupleBasedExp/ex3-tuple.csv", 10, "OracleCEP", 1, 1, 1);
        System.out.println(result);

        result = run(projectDirectory + "data/VLDBJournalTupleBasedExp/ex3-tuple.csv", 10, "STREAM", 1, 1, 1);
        System.out.println(result);

        result = run(projectDirectory + "data/VLDBJournalTupleBasedExp/ex3-tuple.csv", 10, "StreamBase", 1, 1, 1);
        System.out.println(result);


        // Experiment # 4
        System.out.println("\nEXPERIMENT # 4\n");

        result = run(projectDirectory + "data/VLDBJournalTupleBasedExp/ex3-tuple.csv", 10, "OracleCEP", 3, 2, 1);
        System.out.println(result);

        result = run(projectDirectory + "data/VLDBJournalTupleBasedExp/ex3-tuple.csv", 10, "StreamBase", 3, 2, 1);
        System.out.println(result);


        // Experiment # 5
        System.out.println("\nEXPERIMENT # 5\n");

        result = run(projectDirectory + "data/VLDBJournalTupleBasedExp/ex3-tuple.csv", 10, "Coral8", 2, 2, 1);
        System.out.println(result);

        result = run(projectDirectory + "data/VLDBJournalTupleBasedExp/ex3-time.csv", 10, "Coral8", 2, 2, 1);
        System.out.println(result);

        result = run(projectDirectory + "data/VLDBJournalTupleBasedExp/ex3-tuple.csv", 10, "OracleCEP", 2, 2, 1);
        System.out.println(result);

        result = run(projectDirectory + "data/VLDBJournalTupleBasedExp/ex3-tuple.csv", 10, "StreamBase", 2, 2, 1);
        System.out.println(result);

    }

    /**
     * Generates predicted result of SECRET for given specifications
     *
     * @param inputFilename directory of the input data file
     * @param tstart        application time of the first tuple in the input data
     * @param engineName    name of the engine
     * @param size          size of the window in query
     * @param slide         slide of the window in query
     * @param qtype         type of the window in query 0 time-based, 1 tuple-based
     * @return string having predicted result of the model for given specification
     */
    public static String run(String inputFilename, int tstart, String engineName, int size, int slide, int qtype) {
        int ratio = 1;

        File file = new File(inputFilename);
        FileInputStream fis = null;
        BufferedInputStream bis = null;

        // create stream
        myStream = new Stream();

        // create query object
        query = new Query(size, slide, qtype);

        // setup the engine: set known SECRET parameters of the engine
        engine = setup(engineName, tstart, query, ratio);

        // take the tick parameter as it is the start of the control loop
        TickParam tick = (TickParam) engine.getParams().get(3);

        try {

            // read the input file in
            fis = new FileInputStream(file);
            bis = new BufferedInputStream(fis);
            BufferedReader d = new BufferedReader(new InputStreamReader(bis));
            String line;

            while ((line = d.readLine()) != null) {

                //parse the string
                StringTokenizer tokens = new StringTokenizer(line, ",");
                int[] tupleParams = new int[tAttrNum];
                int i = 0;
                while (tokens.hasMoreTokens()) {
                    tupleParams[i] = Integer.parseInt(tokens.nextToken());
                    i++;
                }
				/* 
				 * tupleParams[0]: system-time
				 * tupleParams[1]: tuple-id
				 * tupleParams[2]: application-time
				 * tupleParams[3]: batch-id
				 * */
                Tuple tpl = new Tuple(tupleParams[0], tupleParams[1], tupleParams[2], tupleParams[3]);
                myStream.addTuple(tpl);

                if (Engine.Debug) {
                    System.out.println("New Tuple: " + tpl.print());
                    System.out.println("Result of " + engine.getName() + " at " + tupleParams[0] + " ");
                }

                // evaluate tick for the tuple, only tsys is sent for both time values
                String result = (String) tick.eval(tupleParams[0], tupleParams[0], myStream, query);

                // result is empty when the event does not trigger any reportings including empty window reporting
                if (!result.isEmpty()) {
                    engine.addResult(result);
                }

                if (Engine.Debug) {
                    System.out.println();
                    System.out.println("------------------------------------------------");
                }
            }

            // dispose all the resources after using them.
            fis.close();
            bis.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


        return engine.printResult();
    }


    /**
     * Setting up the engine, creating engine object, setting its fields, calculating t0, i0
     *
     * @param engineName engine name
     * @param tstart     time-stamp of the first tuple in the input stream, needed for t0 calculation of some engines
     * @param wQuery     window specification of the query
     * @param ratio      can be ignored, it is not used anymore
     * @return engine object
     */
    public static Engine setup(String engineName, int tstart, Query wQuery, int ratio) {

        if (engineName.equalsIgnoreCase("Coral8")) {
            return new Coral8Params(engineName, tstart, wQuery, ratio);
        } else if (engineName.equalsIgnoreCase("StreamBase")) {
            return new SBParams(engineName, tstart, wQuery, ratio);
        } else if (engineName.equalsIgnoreCase("STREAM")) {
            return new STREAMParams(engineName, tstart, wQuery, ratio);
        } else if (engineName.equalsIgnoreCase("OracleCEP")) {
            return new OracleCEPParams(engineName, tstart, wQuery, ratio);
        } else if (engineName.equalsIgnoreCase("Flink")) {
            return new FlinkParams(engineName, tstart, wQuery, ratio);
        } else if (engineName.equalsIgnoreCase("Storm")) {
            return new StormParams(engineName, tstart, wQuery, ratio);
        } else if (engineName.equalsIgnoreCase("KafkaStreams")) {
            return new KafkaStreamsParams(engineName, tstart, wQuery, ratio);
        } else if (engineName.equalsIgnoreCase("Spark16")) {
            return new Spark16Params(engineName, tstart, wQuery, ratio);
        }
        return null;

    }

    /**
     * Prints the given windowing result
     *
     * @param content content of the result given by engine
     * @return generates the content of the windows in the form of t+tuple-id
     */
    static String print(Vector<Tuple> content) {
        String res = "{";
        int n = content.size();
        for (int i = 0; i < n; i++) {
            res += content.get(i).print();
            if (i < n - 1)
                res += ",";
        }
        res += "}";
        return res;
    }

}
