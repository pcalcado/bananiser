package com.soundcloud.bananiser.utilities.test;

import org.apache.hadoop.mapreduce.Job;

import com.soundcloud.bananiser.utilities.BananaUtility;

public class TestUtility extends BananaUtility {

    public TestUtility(String[] args) {
        super(args);
    }

    @Override
    public void addMapperAndReducerTo(Job job) {
        job.setMapperClass(TestMapper.class);
        job.setReducerClass(TestReducer.class);
    }
}
