package it.polimi.nwlus.segreto.dataflow;

import com.google.cloud.dataflow.sdk.Pipeline;
import com.google.cloud.dataflow.sdk.io.PubsubIO;
import com.google.cloud.dataflow.sdk.options.*;
import com.google.cloud.dataflow.sdk.transforms.Combine;
import com.google.cloud.dataflow.sdk.transforms.windowing.SlidingWindows;
import com.google.cloud.dataflow.sdk.transforms.windowing.Window;
import com.google.cloud.dataflow.sdk.values.PCollection;
import org.joda.time.Duration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;


public class Main {
    private static final Logger LOG = LoggerFactory.getLogger(Main.class);
    static final int WINDOW_SIZE = 3;  // Default window size in seconds
    static final int WINDOW_SLIDE = 3;  // Default window slide in seconds

    static class InputTopicFactory implements DefaultValueFactory<String> {
        @Override
        public String create(PipelineOptions options) {
            DataflowPipelineOptions dataflowPipelineOptions =
                    options.as(DataflowPipelineOptions.class);
            return "projects/" + dataflowPipelineOptions.getProject()
                    + "/topics/input";
        }
    }

    static class OutputTopicFactory implements DefaultValueFactory<String> {
        @Override
        public String create(PipelineOptions options) {
            DataflowPipelineOptions dataflowPipelineOptions =
                    options.as(DataflowPipelineOptions.class);
            return "projects/" + dataflowPipelineOptions.getProject()
                    + "/topics/output";
        }
    }

    public static interface Options
            extends StreamingOptions {

        @Description("Input topic")
        @Default.InstanceFactory(InputTopicFactory.class)
        String getInputTopic();

        void setInputTopic(String topic);

        @Description("Output topic")
        @Default.InstanceFactory(OutputTopicFactory.class)
        String getOutputTopic();

        void setOutputTopic(String topic);

        @Description("Window size")
        @Default.Integer(WINDOW_SIZE)
        Integer getWindowSize();

        void setWindowSize(Integer value);

        @Description("Window slide")
        @Default.Integer(WINDOW_SLIDE)
        Integer getWindowSlide();

        void setWindowSlide(Integer value);

        // Leave here, but it seems that Dataflow
        // does not provide tuple-based windows
        // by default...
        @Description("Count or time-based window")
        @Default.Integer(0)
        Integer getCount();

        void setCount(Integer value);
    }

    public static class WindowContentFn extends Combine.CombineFn<String, String, String> {

        @Override
        public String createAccumulator() {
            return "";
        }

        @Override
        public String addInput(String accum, String s) {
            accum += s;
            return accum;
        }

        @Override
        public String mergeAccumulators(Iterable<String> accums) {
            String merged = createAccumulator();
            for (String accum : accums) {
                merged += accum;
            }

            return merged;
        }

        @Override
        public String extractOutput(String accum) {
            return accum;
        }
    }

    public static void main(String[] args) throws IOException {
        Options options = PipelineOptionsFactory.fromArgs(args).withValidation().as(Options.class);
        options.setStreaming(true);
        Pipeline pipeline = Pipeline.create(options);

        int size = options.getWindowSize();
        int slide = options.getWindowSlide();
        boolean count = options.getCount() != 0;
        String prefix = count ? "Count" : "Time";
        LOG.info("\n\n>>> " + prefix + " window: " + size + " " + slide + "\n\n");

        PCollection<String> input = pipeline
                .apply(PubsubIO.Read.topic(options.getInputTopic()).timestampLabel("ts"));

        SlidingWindows window = SlidingWindows.of(Duration.standardSeconds(size))
                .every(Duration.standardSeconds(slide));

        PCollection<String> windowed = input
                .apply(Window.<String>into(window));

        PCollection<String> content = windowed
                .apply(Combine.globally(new WindowContentFn()).withoutDefaults());

        content.apply(PubsubIO.Write.topic(options.getOutputTopic()));

        pipeline.run();
    }
}
