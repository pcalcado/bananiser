package com.soundcloud.bananiser;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.only;
import static org.mockito.Mockito.verify;

import java.io.IOException;

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper.Context;
import org.junit.Test;

public class NoOpMapperTest {
    @Test
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void shouldNotChangeContentsOfTextFile() throws IOException,
            InterruptedException {
        LongWritable key = new LongWritable(666);
        Text originalText = new Text(
                "If you kiss me, kiss me with your silver eyes");
        Context context = mock(Context.class);

        NoOpMapper cat = new NoOpMapper();
        cat.mapText(key, originalText, context);
        verify(context, only()).write(key, originalText);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Test
    public void shouldNotChangeContentsOfCompressedFile() throws IOException,
            InterruptedException {
        LongWritable key = new LongWritable(666);
        Text originalText = new Text(
                "how is the air of the wind?".getBytes("UTF-8"));
        BytesWritable compressedText = new BytesWritable(
                originalText.getBytes());
        Context context = mock(Context.class);

        NoOpMapper<BytesWritable> cat = new NoOpMapper<BytesWritable>();
        cat.map(key, compressedText, context);
        verify(context, only()).write(key, originalText);
    }
}
