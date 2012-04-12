package com.soundcloud.bananiser.mr.utilities.sedish;

import static com.soundcloud.bananiser.mr.utilities.sedish.SedishMapper.REPLACE_WITH_PARAMETER;
import static com.soundcloud.bananiser.mr.utilities.sedish.SedishMapper.TO_REPLACE_PARAMETER;

import java.util.List;

import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;

import com.beust.jcommander.Parameter;
import com.soundcloud.bananiser.mr.utilities.BananaUtility;

public class SedishUtility extends BananaUtility {

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
        job.getConfiguration().set(TO_REPLACE_PARAMETER,
                toParameterListString(replaceList));
        job.getConfiguration().set(REPLACE_WITH_PARAMETER, with);
    }

    @SuppressWarnings("rawtypes")
    @Override
    protected Class<? extends Mapper> getMapperToUse() {
        return SedishMapper.class;
    }
}
