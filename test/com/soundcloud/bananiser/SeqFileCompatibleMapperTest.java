package com.soundcloud.bananiser;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.mock;

import java.io.IOException;

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper.Context;
import org.junit.Test;

public class SeqFileCompatibleMapperTest {

    class StubMapper extends SeqFileCompatibleMapper {

        private LongWritable key;
        private Text value;
        private Context context;

        @Override
        public void map(LongWritable key, Text value, Context context) {
            this.key = key;
            this.value = value;
            this.context = context;
        }

        public LongWritable getKey() {
            return key;
        }

        public Text getValue() {
            return value;
        }

        public Context getContext() {
            return context;
        }

    }

    @Test
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void shouldConvertFromBinaryFormatsToText() throws IOException,
            InterruptedException {
        String text = "Someone send a runner through the weather that I'm under for the feeling that I lost today";
        Context context = mock(Context.class);
        LongWritable key = new LongWritable(666);
        BytesWritable value = new BytesWritable(text.getBytes());

        StubMapper mapper = new StubMapper();
        mapper.map(key, value, context);

        assertThat(mapper.getKey(), is(key));
        assertThat(mapper.getValue().toString(), equalTo(text));
        assertThat(mapper.getContext(), is(context));
    }
}
