package com.soundcloud.bananiser.utilities.hash;

import static com.soundcloud.bananiser.utilities.hash.HashMapper.TO_REPLACE_PARAMETER;

import java.util.List;

import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;

import com.beust.jcommander.Parameter;
import com.soundcloud.bananiser.utilities.BananaUtility;

public class HashUtility extends BananaUtility {

    @Parameter(names = "--hash", description = "regexp to replace with its hash", required = true)
    private List<String> replaceList;

    public HashUtility(String[] args) {
        super(args);
    }

    @Override
    protected void configure(Job job) {
        super.configure(job);
        job.getConfiguration().set(TO_REPLACE_PARAMETER,
                toParameterListString(replaceList));
    }

    @SuppressWarnings("rawtypes")
    @Override
    protected Class<? extends Mapper> getMapperToUse() {
        return HashMapper.class;
    }
}