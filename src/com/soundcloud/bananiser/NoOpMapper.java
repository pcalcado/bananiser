package com.soundcloud.bananiser;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;

public class NoOpMapper extends SeqFileCompatibleMapper<Text> {
    @Override
    public void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException {
        context.write(key, value);
    }
}