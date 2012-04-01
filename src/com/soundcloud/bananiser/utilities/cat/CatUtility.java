package com.soundcloud.bananiser.utilities.cat;

import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Reducer;

import com.soundcloud.bananiser.NoOpReducer;
import com.soundcloud.bananiser.utilities.BananaUtility;

public class CatUtility extends BananaUtility {
    public CatUtility(String[] args) {
        super(args);
    }

    @Override
    protected void addMapperAndReducerTo(Job job) {
        job.setMapperClass(CatMapper.class);
        if (isCompressedOutput()) {
            job.setReducerClass(Reducer.class);
        } else {
            job.setReducerClass(NoOpReducer.class);
        }
    }
}
