package com.soundcloud.bananiser.utilities.sedish;

import static com.soundcloud.bananiser.test.BananaMatchers.sameAs;
import static com.soundcloud.bananiser.utilities.sedish.SedishMapper.REGEXP_SEPARATOR;
import static com.soundcloud.bananiser.utilities.sedish.SedishMapper.REPLACE_WITH_PARAMETER;
import static com.soundcloud.bananiser.utilities.sedish.SedishMapper.TO_REPLACE_PARAMETER;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper.Context;
import org.junit.Test;

public class SedishMapperTest {

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Test
    public void shouldReplacePatternWithDesiredString() throws IOException,
            InterruptedException {
        LongWritable ignored = new LongWritable(666);

        String sentence = "1332795134246        Mar 26 20:52:11 nyc.example.net nginx: api.soundcloud.com 10.120.13.52 - - [26/Mar/2012:20:52:11 +0000] \"POST /check/me?client_id=dbb8a8f&secret=558285f0077b&fruit_type=apple&username=alonso1x.x%40example.org&sex=m@l3 HTTP/1.0\" 401 392 \"-\" \"-\" \"-\" \"41.125.195.222, 10.120.13.52\" \"https\"";
        String modifiedSentence = "1332795134246        Mar 26 20:52:11 nyc.example.net nginx: api.soundcloud.com 10.120.13.52 - - [26/Mar/2012:20:52:11 +0000] \"POST /check/me?client_id=dbb8a8f&secret=558285f0077b&fruit_type=apple&BANANA&sex=m@l3 HTTP/1.0\" 401 392 \"-\" \"-\" \"-\" \"BANANA, 10.120.13.52\" \"https\"";

        Configuration configuration = new Configuration();

        Context context = mock(Context.class);
        when(context.getConfiguration()).thenReturn(configuration);

        String patterns = "41.125.195.222" + REGEXP_SEPARATOR
                + "username=(\\S(?!&))+." + REGEXP_SEPARATOR;
        configuration.set(TO_REPLACE_PARAMETER, patterns);
        configuration.set(REPLACE_WITH_PARAMETER, "BANANA");
        SedishMapper mapper = new SedishMapper();
        mapper.setup(context);
        mapper.mapText(ignored, new Text(sentence), context);

        verify(context, times(1)).write(eq(ignored),
                argThat(sameAs(new Text(modifiedSentence))));
    }
}
