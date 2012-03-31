package com.soundcloud.bananiser.utilities;

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

public class TestUtility extends BananaUtility {

    public TestUtility(String[] args) {
        super(args);
    }

    public abstract class TestReducer extends
            Reducer<LongWritable, BytesWritable, LongWritable, Text> {

    }

    public abstract class TestMapper extends
            Mapper<LongWritable, BytesWritable, LongWritable, Text> {
    }

    @Override
    public void addMapperAndReducerTo(Job job) {
        job.setMapperClass(TestMapper.class);
        job.setReducerClass(TestReducer.class);
    }
}
