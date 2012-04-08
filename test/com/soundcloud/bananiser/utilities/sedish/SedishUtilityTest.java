package com.soundcloud.bananiser.utilities.sedish;

import static com.soundcloud.bananiser.test.BananaMatchers.sameClassAs;
import static com.soundcloud.bananiser.test.BananaMatchers.throwsParameterExceptionFor;
import static com.soundcloud.bananiser.utilities.BananaUtility.toParameterListString;
import static com.soundcloud.bananiser.utilities.sedish.SedishMapper.REPLACE_WITH_PARAMETER;
import static com.soundcloud.bananiser.utilities.sedish.SedishMapper.TO_REPLACE_PARAMETER;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapreduce.Job;
import org.junit.Test;

import com.soundcloud.bananiser.mr.NoKeyReducer;

public class SedishUtilityTest {
    @Test
    public void shouldConfigureParametersAndUseSedishMapper()
            throws ClassNotFoundException {
        String[] args = new String[] { "Sedish", "--input", "some/in/a",
                "--output", "some/out/c", "--replace", "original1",
                "--replace", "original2", "--with", "new" };

        Job job = new SedishUtility(args).createJob(new Configuration());

        assertThat(job.getMapperClass(), sameClassAs(SedishMapper.class));
        assertThat(job.getReducerClass(), sameClassAs(NoKeyReducer.class));
        Configuration jobConfig = job.getConfiguration();
        assertThat(jobConfig.get(TO_REPLACE_PARAMETER),
                is(toParameterListString("original1", "original2")));
        assertThat(jobConfig.get(REPLACE_WITH_PARAMETER), is("new"));
    }

    @Test
    public void shouldRequireReplaceAndWithArgs() {
        String[] noReplaceStringArgs = new String[] { "Sedish", "--input",
                "some/in/a", "--output", "some/out/c", "--with", "new" };
        String[] noWithStringArgs = new String[] { "Sedish", "--input",
                "some/in/a", "--output", "some/out/c", "--replace", "original" };
        assertThat(SedishUtility.class,
                throwsParameterExceptionFor(noReplaceStringArgs));
        assertThat(SedishUtility.class,
                throwsParameterExceptionFor(noWithStringArgs));
    }
}
