package com.soundcloud.bananiser.utilities.sedish;

import static com.soundcloud.bananiser.utilities.sedish.SedishMapper.REPLACE_WITH_PARAMETER;
import static com.soundcloud.bananiser.utilities.sedish.SedishMapper.TO_REPLACE_PARAMETER;

import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;

import com.beust.jcommander.Parameter;
import com.soundcloud.bananiser.utilities.BananaUtility;

public class SedishUtility extends BananaUtility {

    public static final int DEFAULT_LINES = 1000;

    @Parameter(names = "--replace", description = "regexp to replace", required = true)
    private String replace;

    @Parameter(names = "--with", description = "what to put in place", required = true)
    private String with;

    public SedishUtility(String[] args) {
        super(args);
    }

    @Override
    protected void configure(Job job) {
        super.configure(job);
        job.getConfiguration().set(TO_REPLACE_PARAMETER, replace);
        job.getConfiguration().set(REPLACE_WITH_PARAMETER, with);
    }

    @SuppressWarnings("rawtypes")
    @Override
    protected Class<? extends Mapper> getMapperToUse() {
        return SedishMapper.class;
    }
}
