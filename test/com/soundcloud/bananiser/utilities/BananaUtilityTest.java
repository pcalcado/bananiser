package com.soundcloud.bananiser.utilities;

import static com.soundcloud.bananiser.test.BananaMatchers.sameClassAs;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.text.StringContains.containsString;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.junit.Test;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.soundcloud.bananiser.NoOpMapper;
import com.soundcloud.bananiser.NoOpReducer;

public class BananaUtilityTest {

    private static final String INPUT_PATH_1 = "some/in/a";
    private static final String INPUT_PATH_2 = "some/in/b";
    private static final String SOME_OUTPUT_PATH = "some/out/c";
    public static final String MAPRED_INPUT_DIR = "mapred.input.dir";
    public static final String MAPRED_OUTPUT_DIR = "mapred.output.dir";

    class StubBananaUtility extends BananaUtility {
        @Parameter(names = "--e", required = false)
        public String exclusive;

        public StubBananaUtility(String[] args) {
            super(args);
        }

    }

    @Test(expected = ParameterException.class)
    public void shouldComplainIfNotAllRequiredParametersAsArgs() {
        String[] argsMissingUtilityName = new String[] { "--input",
                INPUT_PATH_1, "--input", INPUT_PATH_2, "--output",
                SOME_OUTPUT_PATH };
        new StubBananaUtility(argsMissingUtilityName);
    }

    @Test
    public void shouldConfigureCommonJobAttricutesBasedOnArgs() {
        String[] args = new String[] { "Main", "--input", INPUT_PATH_1,
                "--input", INPUT_PATH_2, "--output", SOME_OUTPUT_PATH };
        Configuration config = new Configuration();

        StubBananaUtility stubBananaUtility = new StubBananaUtility(args);
        Job job = stubBananaUtility.createJob(config);

        String expectedJobName = "Bananiser: Running [Main] for ["
                + System.getenv().get("USER") + "]";
        assertThat(job.getJobName(), is(expectedJobName));

        String input = job.getConfiguration().get(MAPRED_INPUT_DIR);
        assertThat(input, containsString(INPUT_PATH_1));
        assertThat(input, containsString(INPUT_PATH_2));

        String output = job.getConfiguration().get(MAPRED_OUTPUT_DIR);
        assertThat(output, equalTo(SOME_OUTPUT_PATH));
    }

    @Test
    public void shouldConfigureUtilitySpecificAttributsFromArgs() {
        String utilitySpecificAttribute = "this is unique";
        String[] args = new String[] { "Main", "--e", utilitySpecificAttribute,
                "--input", INPUT_PATH_1, "--output", SOME_OUTPUT_PATH };
        Configuration config = new Configuration();

        StubBananaUtility stubBananaUtility = new StubBananaUtility(args);
        stubBananaUtility.createJob(config);
        assertThat(stubBananaUtility.exclusive, is(utilitySpecificAttribute));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldMakeInputAndUncompressedByDefault()
            throws ClassNotFoundException {
        String[] args = new String[] { "Main", "--input", INPUT_PATH_1,
                "--output", SOME_OUTPUT_PATH };
        Configuration config = new Configuration();

        StubBananaUtility stubBananaUtility = new StubBananaUtility(args);
        Job job = stubBananaUtility.createJob(config);

        assertThat(job.getInputFormatClass(),
                sameClassAs(TextInputFormat.class));
        assertThat(job.getOutputFormatClass(),
                sameClassAs(TextOutputFormat.class));
        assertThat(job.getMapperClass(), sameClassAs(NoOpMapper.class));
        assertThat(job.getReducerClass(), sameClassAs(NoOpReducer.class));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldMakeInputCompressedIfArgs() throws ClassNotFoundException {
        String[] args = new String[] { "Main", "--input", INPUT_PATH_1,
                "--output", SOME_OUTPUT_PATH, "--compressedInput" };
        Configuration config = new Configuration();

        StubBananaUtility stubBananaUtility = new StubBananaUtility(args);
        Job job = stubBananaUtility.createJob(config);

        assertThat(job.getInputFormatClass(),
                sameClassAs(SequenceFileInputFormat.class));
        assertThat(job.getOutputFormatClass(),
                sameClassAs(TextOutputFormat.class));
        assertThat(job.getMapperClass(), sameClassAs(NoOpMapper.class));
        assertThat(job.getReducerClass(), sameClassAs(NoOpReducer.class));
    }

    @SuppressWarnings("unchecked")
    @Test
    public void shouldMakeOutputCompressedIfArgs()
            throws ClassNotFoundException {
        String[] args = new String[] { "Main", "--input", INPUT_PATH_1,
                "--output", SOME_OUTPUT_PATH, "--compressedOutput" };
        Configuration config = new Configuration();

        StubBananaUtility stubBananaUtility = new StubBananaUtility(args);
        Job job = stubBananaUtility.createJob(config);

        assertThat(job.getInputFormatClass(),
                sameClassAs(TextInputFormat.class));
        assertThat(job.getOutputFormatClass(),
                sameClassAs(SequenceFileOutputFormat.class));
        assertThat(job.getMapperClass(), sameClassAs(NoOpMapper.class));
        assertThat(job.getReducerClass(), sameClassAs(Reducer.class));
    }
}
