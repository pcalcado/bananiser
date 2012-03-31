package com.soundcloud.bananiser.utilities;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.text.StringContains.containsString;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapreduce.InputFormat;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.OutputFormat;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.TextOutputFormat;
import org.junit.Test;

import com.beust.jcommander.Parameter;

public class BananaUtilityTest {

    public static final String MAPRED_INPUT_DIR = "mapred.input.dir";
    public static final String MAPRED_OUTPUT_DIR = "mapred.output.dir";

    class StubBananaUtility extends BananaUtility {

        @SuppressWarnings("unused")
        @Parameter(names = "--subclass", required = true)
        private String boo;

        public StubBananaUtility(String[] args) {
            super(args);
        }

        @Override
        protected void addMapperAndReducerTo(Job job) {
        }

    }

    @Test
    public void shouldConfigureAttributesBasedOnArgs() {
        String inputPath1 = "some/in/a";
        String inputPath2 = "some/in/b";
        String outputPath = "some/out/c";
        String[] args = new String[] { "Main", "--subclass", "hehe", "--input",
                inputPath1, "--input", inputPath2, "--output", outputPath };
        Configuration config = new Configuration();

        StubBananaUtility stubBananaUtility = new StubBananaUtility(args);
        Job job = stubBananaUtility.createJob(config);

        String input = job.getConfiguration().get(MAPRED_INPUT_DIR);
        assertThat(input, containsString(inputPath1));
        assertThat(input, containsString(inputPath2));

        String output = job.getConfiguration().get(MAPRED_OUTPUT_DIR);
        assertThat(output, equalTo(outputPath));
    }

    @Test
    public void shouldMakeInputUncompressedByDefault()
            throws ClassNotFoundException {
        String[] args = new String[] { "Main","--subclass", "hehe", "--input",
                "some/in/a", "--output", "some/out/c" };
        Configuration config = new Configuration();

        StubBananaUtility stubBananaUtility = new StubBananaUtility(args);
        Job job = stubBananaUtility.createJob(config);

        Class<? extends InputFormat<?, ?>> inputFormatClass = job
                .getInputFormatClass();
        Class<? extends OutputFormat<?, ?>> outputFormatClass = job
                .getOutputFormatClass();
        assertThat(inputFormatClass, equalTo((Object) TextInputFormat.class));
        assertThat(outputFormatClass, equalTo((Object) TextOutputFormat.class));
    }

    @Test
    public void shouldMakeInputCompressedIfArgs() throws ClassNotFoundException {
        String[] args = new String[] {"Main", "--subclass", "hehe", "--input",
                "some/in/a", "--output", "some/out/c", "--compressedInput" };
        Configuration config = new Configuration();

        StubBananaUtility stubBananaUtility = new StubBananaUtility(args);
        Job job = stubBananaUtility.createJob(config);

        Class<? extends InputFormat<?, ?>> inputFormatClass = job
                .getInputFormatClass();
        Class<? extends OutputFormat<?, ?>> outputFormatClass = job
                .getOutputFormatClass();
        assertThat(inputFormatClass,
                equalTo((Object) SequenceFileInputFormat.class));
        assertThat(outputFormatClass, equalTo((Object) TextOutputFormat.class));
    }
}
