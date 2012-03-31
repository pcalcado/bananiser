package com.soundcloud.bananiser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.FileStatus;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Reducer;
import org.apache.hadoop.mapreduce.lib.input.SequenceFileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.SequenceFileOutputFormat;
import org.apache.hadoop.util.GenericOptionsParser;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

public class OldBananise extends Configured implements Tool {


	public static void main(String... args) throws Exception {
		int res = ToolRunner.run(
				new GenericOptionsParser(args).getConfiguration(),
				new OldBananise(), args);
		System.exit(res);
	}

	public int run(String[] args) throws Exception {

		JCommander cli = new JCommander(this);
		try {
			cli.parse(args);
		} catch (Exception e) {
			for (String arg : args) {
				System.out.println("arg:" + arg);
			}
			e.printStackTrace();
			cli.usage();
			ToolRunner.printGenericCommandUsage(System.out);
			return -1;
		}

		Job job = setupJob();

		FileSystem fs = FileSystem.get(job.getConfiguration());
//		for (String input : inputs) {
//			FileStatus[] files = fs.globStatus(new Path(input));
//			if (files != null) {
//				for (FileStatus file : files) {
//					SequenceFileInputFormat.addInputPath(job, file.getPath());
//				}
//			} else {
//				SequenceFileInputFormat.addInputPath(job, new Path(input));
//			}
//		}

		//FileOutputFormat.setOutputPath(job, new Path(output));

		job.submit();

		System.err.println("Tracking URL: " + job.getTrackingURL());

		return job.waitForCompletion(true) ? 0 : -1;
	}

	private Job setupJob() throws IOException {
		Job job = new Job(getConf());
		job.setJarByClass(this.getClass());
		job.setInputFormatClass(SequenceFileInputFormat.class);
		job.setOutputFormatClass(SequenceFileOutputFormat.class);

		job.setMapperClass(BananaMapper.class);
		job.setMapOutputKeyClass(LongWritable.class);
		job.setMapOutputValueClass(Text.class);

		job.setReducerClass(Reducer.class);
		job.setNumReduceTasks(1);
		job.setOutputKeyClass(LongWritable.class);
		job.setOutputValueClass(Text.class);

		job.setJobName("bananising for " + System.getenv().get("USER"));
		return job;
	}
}