package com.soundcloud.bananiser;

import java.io.IOException;
import java.nio.charset.Charset;

import org.apache.hadoop.io.BytesWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

public class BananaMapper extends
		Mapper<LongWritable, BytesWritable, LongWritable, Text> {

	private static final Charset UTF8 = Charset.forName("UTF-8");

	private static Bananiser bananiser = new Bananiser("password",
			"username");

	@Override
	public void map(LongWritable key, BytesWritable value, Context ctx)
			throws IOException, InterruptedException {

		String valueAsString = new String(value.getBytes(), 0,
				value.getLength(), UTF8);

		String bananised = bananiser.process(valueAsString);
		ctx.write(key, new Text(bananised));
	}
}