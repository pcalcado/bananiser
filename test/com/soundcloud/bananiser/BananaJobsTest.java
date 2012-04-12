package com.soundcloud.bananiser;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapreduce.Job;
import org.junit.Test;

import com.soundcloud.bananiser.mr.utilities.test.TestMapper;
import com.soundcloud.bananiser.mr.utilities.test.TestReducer;

public class BananaJobsTest {
    @Test
    public void shouldInstantiateExistingUtility()
            throws ClassNotFoundException {
        String[] args = new String[] { "test", "--input", "value", "--output",
                "value2" };

        Configuration config = new Configuration();

        BananaJobs jobs = new BananaJobs(config);
        Job job = jobs.from(args);
        assertThat((Object) job.getMapperClass(),
                equalTo((Object) TestMapper.class));
        assertThat((Object) job.getReducerClass(),
                equalTo((Object) TestReducer.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldComplainIfNoExistingUtility() {
        String[] args = new String[] { "doestExist", "--input", "value",
                "--output", "value2" };

        Configuration config = new Configuration();

        BananaJobs jobs = new BananaJobs(config);
        jobs.from(args);
    }
}
