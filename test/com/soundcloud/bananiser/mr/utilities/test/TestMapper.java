package com.soundcloud.bananiser.mr.utilities.test;

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public abstract class TestMapper extends
        Mapper<LongWritable, BytesWritable, LongWritable, Text> {
}