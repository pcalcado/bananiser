package com.soundcloud.bananiser.utilities.test;

import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Reducer;

import com.soundcloud.bananiser.utilities.BananaUtility;

public class TestUtility extends BananaUtility {

    public TestUtility(String[] args) {
        super(args);
    }

    @SuppressWarnings("rawtypes")
    @Override
    protected Class<? extends Mapper> getMapperToUse() {
        return TestMapper.class;
    }

    @SuppressWarnings("rawtypes")
    @Override
    protected Class<? extends Reducer> getReducerToUse() {
        return TestReducer.class;
    }
}
