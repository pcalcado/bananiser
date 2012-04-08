package com.soundcloud.bananiser.mr;

import static com.soundcloud.bananiser.test.BananaMatchers.sameAs;
import static org.mockito.Matchers.argThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper.Context;

import com.soundcloud.bananiser.mr.SeqFileCompatibleMapper;

public class MapperTestCase {

    protected static final LongWritable SOME_IRRELEVANT_KEY = new LongWritable(
            666);

    public MapperTestCase() {
        super();
    }

    @SuppressWarnings("rawtypes")
    protected Context setupContext(Configuration configuration) {
        Context context = mock(Context.class);
        when(context.getConfiguration()).thenReturn(configuration);
        return context;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected void invokeMapOperation(SeqFileCompatibleMapper<Text> mapper,
            String input, Context context) {
        try {
            mapper.setup(context);
            mapper.mapText(SOME_IRRELEVANT_KEY, new Text(input), context);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    protected void verifyWroteTo(Context context, LongWritable keyUsed,
            String modifiedSentence) {
        try {
            verify(context, times(1)).write(eq(keyUsed),
                    argThat(sameAs(new Text(modifiedSentence))));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}