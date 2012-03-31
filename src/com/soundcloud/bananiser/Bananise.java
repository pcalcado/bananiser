package com.soundcloud.bananiser;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;


public class Bananise extends Configured implements Tool {

    private final Configuration config;

    public Bananise(Configuration configuration) {
        this.config = configuration;
    }

    @Override
    public int run(String[] args) throws Exception {
        BananaJobs jobs = new BananaJobs(config);
        Job job = jobs.from(args);
        job.submit();

        System.err.println("Tracking URL: " + job.getTrackingURL());

        return job.waitForCompletion(true) ? 0 : -1;
    }

    public static void main(String[] args) throws Exception {
        Configuration configuration = new GenericOptionsParser(args)
                .getConfiguration();
        Bananise bananise = new Bananise(configuration);
        int res = ToolRunner.run(configuration, bananise, args);
        System.exit(res);
    }
}
