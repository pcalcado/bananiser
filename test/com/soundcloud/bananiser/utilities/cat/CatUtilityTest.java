package com.soundcloud.bananiser.utilities.cat;

import static com.soundcloud.bananiser.test.BananaMatchers.sameClassAs;
import static org.hamcrest.MatcherAssert.assertThat;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapreduce.Job;
import org.junit.Test;

import com.soundcloud.bananiser.mr.NoOpMapper;
import com.soundcloud.bananiser.mr.NoOpReducer;

public class CatUtilityTest {

    @SuppressWarnings({ "unchecked" })
    @Test
    public void shouldUseNoOpMapperAndReducer() throws ClassNotFoundException {
        String[] args = new String[] { "Cat", "--input", "some/in/a",
                "--output", "some/out/c" };
        Configuration config = new Configuration();

        CatUtility catUtility = new CatUtility(args);
        Job job = catUtility.createJob(config);
        assertThat(job.getMapperClass(), sameClassAs(NoOpMapper.class));
        assertThat(job.getReducerClass(), sameClassAs(NoOpReducer.class));
    }
}
