package com.soundcloud.bananiser.utilities.hash;

import static com.soundcloud.bananiser.test.BananaMatchers.sameClassAs;
import static com.soundcloud.bananiser.test.BananaMatchers.throwsParameterExceptionFor;
import static com.soundcloud.bananiser.utilities.BananaUtility.toParameterListString;
import static com.soundcloud.bananiser.utilities.hash.HashMapper.TO_REPLACE_PARAMETER;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapreduce.Job;
import org.junit.Test;

import com.soundcloud.bananiser.mr.NoKeyReducer;

public class HashUtilityTest {

    @Test
    public void shouldConfigureParametersAndMapper()
            throws ClassNotFoundException {
        String[] args = new String[] { "Sedish", "--input", "some/in/a",
                "--output", "some/out/c", "--hash", "original1", "--hash",
                "original2" };

        Job job = new HashUtility(args).createJob(new Configuration());

        assertThat(job.getMapperClass(), sameClassAs(HashMapper.class));
        assertThat(job.getReducerClass(), sameClassAs(NoKeyReducer.class));
        Configuration jobConfig = job.getConfiguration();
        assertThat(jobConfig.get(TO_REPLACE_PARAMETER),
                is(toParameterListString("original1", "original2")));
    }

    @Test
    public void shouldRequireARegularExpression() {
        String[] noReplaceStringArgs = new String[] { "Hash", "--input",
                "some/in/a", "--output", "some/out/c" };
        assertThat(HashUtility.class,
                throwsParameterExceptionFor(noReplaceStringArgs));
    }
}
