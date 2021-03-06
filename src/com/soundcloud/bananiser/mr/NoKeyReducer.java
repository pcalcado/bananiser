package com.soundcloud.bananiser.mr;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class NoKeyReducer extends
        Reducer<LongWritable, Text, LongWritable, Text> {

    protected void reduce(LongWritable key, Iterable<Text> values,
            Reducer<LongWritable, Text, LongWritable, Text>.Context context)
            throws java.io.IOException, InterruptedException {
        for (Text value : values) {
            context.write(null, value);
        }
    };
}
