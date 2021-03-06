package com.soundcloud.bananiser.mr;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import java.io.IOException;
import java.util.Arrays;

import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer.Context;
import org.junit.Test;

import com.soundcloud.bananiser.mr.NoKeyReducer;

public class NoKeyReducerTest {
    @Test
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void shouldNotChangeInput() throws IOException, InterruptedException {
        LongWritable key = new LongWritable(666);
        Text text1 = new Text("1");
        Text text2 = new Text("2");
        Context context = mock(Context.class);

        Iterable<Text> values = Arrays.asList(text1, text2);

        NoKeyReducer reducer = new NoKeyReducer();
        reducer.reduce(key, values, context);

        verify(context).write(null, text1);
        verify(context).write(null, text2);
        verifyNoMoreInteractions(context);
    }
}
