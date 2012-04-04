package com.soundcloud.bananiser;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;

public class NoOpMapper<TInput> extends SeqFileCompatibleMapper<TInput> {
    @Override
    public void mapText(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException {
        context.write(key, value);
    }
}