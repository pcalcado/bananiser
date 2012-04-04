package com.soundcloud.bananiser.utilities.sedish;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;

import com.soundcloud.bananiser.SeqFileCompatibleMapper;

public class SedishMapper extends SeqFileCompatibleMapper<Text> {

    public static final String REPLACE_WITH_PARAMETER = SedishMapper.class
            .getName() + ".replaceWith";
    public static final String TO_REPLACE_PARAMETER = SedishMapper.class
            .getName() + ".toReplace";
    private String pattern;
    private String replaceWith;

    @Override
    protected void setup(Context context) throws IOException,
            InterruptedException {
        super.setup(context);
        Configuration configuration = context.getConfiguration();
        this.pattern = configuration.get(TO_REPLACE_PARAMETER);
        this.replaceWith = configuration.get(REPLACE_WITH_PARAMETER);
    }

    @Override
    public void map(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException {
        checkIfConfiguredCorrectly();
        String original = value.toString();
        String replaced = original.replaceAll(pattern, replaceWith);
        Text modified = new Text(replaced);
        context.write(key, modified);
    }

    private void checkIfConfiguredCorrectly() {
        if (pattern == null || replaceWith == null)
            throw new IllegalStateException("Pattern [" + pattern
                    + "] and replace string [" + replaceWith + "]");
    }
}
