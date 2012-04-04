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

    class StubMapper<T> extends SeqFileCompatibleMapper<T> {

        private LongWritable key;
        private Text value;
        private Context context;

        @Override
        public void mapText(LongWritable key, Text value, Context context) {
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
    public void shouldConvertFromBinaryFormatsToText() throws IOException,
            InterruptedException {
        String text = "Someone send a runner through the weather that I'm under for the feeling that I lost today";
        Context context = mock(Context.class);
        LongWritable key = new LongWritable(666);
        BytesWritable value = new BytesWritable(text.getBytes());

        StubMapper<BytesWritable> mapper = new StubMapper<BytesWritable>();
        mapper.map(key, value, context);

        assertThat(mapper.getKey(), is(key));
        assertThat(mapper.getValue().toString(), equalTo(text));
        assertThat(mapper.getContext(), is(context));
    }

    @Test
    public void shouldMapText() throws IOException, InterruptedException {
        String text = "You must be somewhere in London/You must be loving your life in the rain";
        Context context = mock(Context.class);
        LongWritable key = new LongWritable(667);
        Text value = new Text(text);

        StubMapper<Text> mapper = new StubMapper<Text>();
        mapper.mapText(key, value, context);

        assertThat(mapper.getKey(), is(key));
        assertThat(mapper.getValue().toString(), equalTo(text));
        assertThat(mapper.getContext(), is(context));
    }
}
