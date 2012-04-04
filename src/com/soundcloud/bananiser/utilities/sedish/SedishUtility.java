package com.soundcloud.bananiser.utilities.sedish;

import static com.soundcloud.bananiser.utilities.sedish.SedishMapper.REPLACE_WITH_PARAMETER;
import static com.soundcloud.bananiser.utilities.sedish.SedishMapper.TO_REPLACE_PARAMETER;

import java.util.List;

import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;

import com.beust.jcommander.Parameter;
import com.soundcloud.bananiser.utilities.BananaUtility;

public class SedishUtility extends BananaUtility {

    public static final int DEFAULT_LINES = 1000;

    @Parameter(names = "--replace", description = "regexp to replace", required = true)
    private List<String> replaceList;

    @Parameter(names = "--with", description = "what to put in place", required = true)
    private String with;

    public SedishUtility(String[] args) {
        super(args);
    }

    @Override
    protected void configure(Job job) {
        super.configure(job);
        StringBuilder replaces = new StringBuilder();
        for (String r : replaceList) {
            replaces.append(r).append(SedishMapper.REGEXP_SEPARATOR);
        }
        job.getConfiguration().set(TO_REPLACE_PARAMETER, replaces.toString());
        job.getConfiguration().set(REPLACE_WITH_PARAMETER, with);
    }

    @SuppressWarnings("rawtypes")
    @Override
    protected Class<? extends Mapper> getMapperToUse() {
        return SedishMapper.class;
    }
}
