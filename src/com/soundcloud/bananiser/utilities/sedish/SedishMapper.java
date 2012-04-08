package com.soundcloud.bananiser.utilities.sedish;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;

import com.soundcloud.bananiser.mr.SeqFileCompatibleMapper;

public class SedishMapper extends SeqFileCompatibleMapper<Text> {

    public static final String REPLACE_WITH_PARAMETER = SedishMapper.class
            .getName() + ".replaceWith";
    public static final String TO_REPLACE_PARAMETER = SedishMapper.class
            .getName() + ".toReplace";
    public static final String REGEXP_SEPARATOR = "::,::";

    private String replaceWith;
    private String[] patterns;

    @Override
    protected void setup(Context context) throws IOException,
            InterruptedException {
        super.setup(context);
        Configuration configuration = context.getConfiguration();
        this.patterns = configuration.get(TO_REPLACE_PARAMETER).split(
                REGEXP_SEPARATOR);
        this.replaceWith = configuration.get(REPLACE_WITH_PARAMETER);
    }

    @Override
    public void mapText(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException {
        checkIfConfiguredCorrectly();
        String original = value.toString();

        String modified = original;
        for (String pattern : patterns) {
            modified = modified.replaceAll(pattern, replaceWith);
        }
        context.write(key, new Text(modified));
    }

    private void checkIfConfiguredCorrectly() {
        if (patterns == null || patterns.length == 0 || replaceWith == null)
            throw new IllegalStateException("Patterns [" + patterns
                    + "] and replace string [" + replaceWith + "]");
    }
}
