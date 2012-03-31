package com.soundcloud.bananiser.utilities.cat;

import java.io.IOException;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;

import com.soundcloud.bananiser.SeqFileCompatibleMapper;

@SuppressWarnings("rawtypes")
public class CatMapper extends SeqFileCompatibleMapper {
    @Override
    @SuppressWarnings("unchecked")
    public void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException {
        context.write(key, value);
    }
}