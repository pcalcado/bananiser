package com.soundcloud.bananiser.utilities;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Reducer;
import org.junit.Ignore;
import org.junit.Test;

import com.soundcloud.bananiser.utilities.cat.CatMapper;

public class CatUtilityTest {

    @SuppressWarnings("rawtypes")
    @Test
    @Ignore
    public void shouldUseCatMapperAndIdentityReducer()
            throws ClassNotFoundException {
        String[] args = new String[] { "Cat", "--input", "some/in/a",
                "--output", "some/out/c", "--compressed" };
        Configuration config = new Configuration();

        CatUtility catUtility = new CatUtility(args);
        Job job = catUtility.createJob(config);
        assertThat((Class) job.getMapperClass(),
                equalTo((Class) CatMapper.class));
        assertThat((Class) job.getReducerClass(),
                equalTo((Class) Reducer.class));
    }
}
