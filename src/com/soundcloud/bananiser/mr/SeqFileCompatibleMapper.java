package com.soundcloud.bananiser.mr;

import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public abstract class SeqFileCompatibleMapper<TInput> extends
        Mapper<LongWritable, TInput, LongWritable, Text> {
    private static final Charset UTF8 = Charset.forName("UTF-8");

    public void map(LongWritable key, TInput value, Context context)
            throws IOException, InterruptedException {

        Text valueAsText = null;
        if (value instanceof Text) {
            valueAsText = (Text) value;
        } else {
            valueAsText = convertToText(value);
        }
        mapText(key, valueAsText, context);
    }

    private Text convertToText(TInput value) {
        BytesWritable valueAsByteWriteable = (BytesWritable) value;
        String valueAsString = new String(valueAsByteWriteable.getBytes(), 0,
                valueAsByteWriteable.getLength(), UTF8);
        return new Text(valueAsString);
    };

    public abstract void mapText(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException;
}
