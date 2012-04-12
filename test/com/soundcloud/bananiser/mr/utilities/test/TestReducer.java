package com.soundcloud.bananiser.mr.utilities.test;

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public abstract class TestReducer extends
        Reducer<LongWritable, BytesWritable, LongWritable, Text> {

}