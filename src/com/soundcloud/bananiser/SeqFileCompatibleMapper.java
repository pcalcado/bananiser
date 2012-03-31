package com.soundcloud.bananiser;

import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public abstract class SeqFileCompatibleMapper<TInput> extends
        Mapper<LongWritable, TInput, LongWritable, Text> {
    private static final Charset UTF8 = Charset.forName("UTF-8");

    public void map(LongWritable key, BytesWritable value, Context context)
            throws IOException, InterruptedException {

        String valueAsString = new String(value.getBytes(), 0,
                value.getLength(), UTF8);
        Text valueAsText = new Text(valueAsString);
        map(key, valueAsText, context);
    };

    public abstract void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException;
}
