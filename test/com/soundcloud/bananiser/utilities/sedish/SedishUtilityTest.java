package com.soundcloud.bananiser.utilities.sedish;

import static com.soundcloud.bananiser.test.BananaMatchers.sameClassAs;
import static com.soundcloud.bananiser.utilities.sedish.SedishMapper.REPLACE_WITH_PARAMETER;
import static com.soundcloud.bananiser.utilities.sedish.SedishMapper.TO_REPLACE_PARAMETER;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapreduce.Job;
import org.junit.Assert;
import org.junit.Test;

import com.beust.jcommander.ParameterException;
import com.soundcloud.bananiser.NoOpReducer;

public class SedishUtilityTest {
    @Test
    public void shouldConfigureParametersAndUseSedishMapper()
            throws ClassNotFoundException {
        String[] args = new String[] { "Sedish", "--input", "some/in/a",
                "--output", "some/out/c", "--replace", "original", "--with",
                "new" };

        Job job = new SedishUtility(args).createJob(new Configuration());

        assertThat(job.getMapperClass(), sameClassAs(SedishMapper.class));
        assertThat(job.getReducerClass(), sameClassAs(NoOpReducer.class));
        Configuration jobConfig = job.getConfiguration();
        assertThat(jobConfig.get(TO_REPLACE_PARAMETER), is("original"));
        assertThat(jobConfig.get(REPLACE_WITH_PARAMETER), is("new"));
    }

    @Test
    public void shouldRequireReplaceAndWithArgs() throws ClassNotFoundException {
        assertParameterException(new String[] { "Sedish", "--input", "some/in/a",
                "--output", "some/out/c", "--with", "new" });
        assertParameterException(new String[] { "Sedish", "--input", "some/in/a",
                "--output", "some/out/c", "--replace", "original" });
    }

    private void assertParameterException(String[] args) {
        try {
            new SedishUtility(args);
            Assert.fail("No exception");
        } catch (ParameterException e) {
            // success
        }
    }
}
