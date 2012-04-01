package com.soundcloud.bananiser.utilities;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.util.ToolRunner;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;

public abstract class BananaUtility {

    @Parameter(required = true)
    private List<String> utility = null;

    @Parameter(names = "-fs", required = false)
    @SuppressWarnings("unused")
    private String ignore = null;

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

        return job;
    }

    private void addJobName(Job job) {
        String name = "Bananiser: Running [" + utility.get(0) + "] for ["
                + System.getenv().get("USER") + "]";
        job.setJobName(name);
    }

    private void addInputAndOutputFormats(Job job) {
        if (compressedInput) {
            job.setInputFormatClass(SequenceFileInputFormat.class);
        }

        if (compressedOutput) {
            job.setOutputFormatClass(SequenceFileOutputFormat.class);
        }
    }

    private void loadArgs(String[] args) {
        JCommander cli = new JCommander(this);
        try {
            cli.parse(args);
        } catch (ParameterException e) {
            for (String arg : args) {
                System.out.println("arg:" + arg);
            }
            e.printStackTrace();
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

    protected boolean isCompressedInput() {
        return compressedInput;
    }

    protected boolean isCompressedOutput() {
        return compressedOutput;
    }

    protected abstract void addMapperAndReducerTo(Job job);
}
