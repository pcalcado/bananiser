package com.soundcloud.bananiser.utilities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.SequenceFile.CompressionType;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.util.ToolRunner;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.soundcloud.bananiser.mr.NoKeyReducer;
import com.soundcloud.bananiser.mr.NoOpMapper;

public abstract class BananaUtility {
    private static final String PARAM_SEPARATOR = "::,::";

    @Parameter(names = "-fs", required = false)
    @SuppressWarnings("unused")
    private List<String> ignoreFs = null;
    @Parameter(names = "-D", required = false)
    @SuppressWarnings("unused")
    private List<String> ignoredMinusD = null;

    @Parameter(required = true)
    private List<String> utility = null;

    @Parameter(names = "--input", description = "input path patterns", required = true)
    private List<String> inputs = new ArrayList<String>();

    @Parameter(names = "--output", description = "output path", required = true)
    private String output = null;

    @Parameter(names = "--compressedInput", description = "if input file was compressed", required = false)
    private boolean compressedInput = false;

    @Parameter(names = "--compressedOutput", description = "if ouput will becompressed", required = false)
    private boolean compressedOutput = false;

    public BananaUtility(String[] args) {
        loadArgs(args);
    }

    public Job createJob(Configuration configToUseAsPrototype) {
        Job job = instantiateJob(configToUseAsPrototype);
        addJobName(job);
        addInputAndOutputPathsTo(job);
        addMapperAndReducerTo(job);
        addInputAndOutputFormats(job);

        configure(job);
        return job;
    }

    private void addJobName(Job job) {
        String name = "Bananiser: Running [" + getUtilityName() + "] for ["
                + getUsername() + "]";
        job.setJobName(name);
    }

    private String getUtilityName() {
        return utility.get(0);
    }

    private String getUsername() {
        return System.getenv().get("USER");
    }

    private void addInputAndOutputFormats(Job job) {
        if (isCompressedInput()) {
            makeInputCompressed(job);
        }

        if (isCompressedOutput()) {
            makeOutputCompressed(job);
        }
    }

    private void makeInputCompressed(Job job) {
        job.setInputFormatClass(SequenceFileInputFormat.class);
    }

    private void makeOutputCompressed(Job job) {
        job.setOutputFormatClass(SequenceFileOutputFormat.class);
        SequenceFileOutputFormat.setCompressOutput(job, true);
        SequenceFileOutputFormat.setOutputCompressionType(job,
                CompressionType.BLOCK);
    }

    private void loadArgs(String[] args) {
        JCommander cli = new JCommander(this);
        try {
            cli.parse(args);
        } catch (ParameterException e) {
            for (String arg : args) {
                System.out.println("arg:" + arg);
            }
            e.getMessage();
            cli.usage();
            ToolRunner.printGenericCommandUsage(System.out);
            throw e;
        }
    }

    private void addInputAndOutputPathsTo(Job job) {
        try {
            FileSystem fs = FileSystem.get(job.getConfiguration());
            for (String input : inputs) {
                FileStatus[] files = fs.globStatus(new Path(input));
                if (files != null) {
                    for (FileStatus file : files) {
                        SequenceFileInputFormat.addInputPath(job,
                                file.getPath());
                    }
                } else {
                    SequenceFileInputFormat.addInputPath(job, new Path(input));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        FileOutputFormat.setOutputPath(job, new Path(output));
    }

    private Job instantiateJob(Configuration config) {
        try {
            return new Job(config);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void addMapperAndReducerTo(Job job) {
        job.setMapperClass(getMapperToUse());
        job.setReducerClass(getReducerToUse());
    }

    protected final boolean isCompressedInput() {
        return compressedInput;
    }

    protected final boolean isCompressedOutput() {
        return compressedOutput;
    }

    @SuppressWarnings("rawtypes")
    protected Class<? extends Reducer> getReducerToUse() {
        return isCompressedOutput() ? Reducer.class : NoKeyReducer.class;
    }

    @SuppressWarnings("rawtypes")
    protected Class<? extends Mapper> getMapperToUse() {
        return NoOpMapper.class;
    }

    protected void configure(Job job) {
    }

    public static String[] asParameterList(String parameterListString) {
        return parameterListString.split(PARAM_SEPARATOR);
    }

    public static String toParameterListString(String... paramList) {
        StringBuilder replaces = new StringBuilder();
        for (String r : paramList) {
            replaces.append(r).append(PARAM_SEPARATOR);
        }
        return replaces.toString();
    }

    public static String toParameterListString(List<String> paramList) {
        return toParameterListString(paramList.toArray(new String[] {}));
    }

}
