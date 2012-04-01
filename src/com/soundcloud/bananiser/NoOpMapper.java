package com.soundcloud.bananiser;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;

import com.soundcloud.bananiser.SeqFileCompatibleMapper;

@SuppressWarnings("rawtypes")
public class NoOpMapper extends SeqFileCompatibleMapper {
    @Override
    @SuppressWarnings("unchecked")
    public void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException {
        context.write(key, value);
    }
}