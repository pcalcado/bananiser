package com.soundcloud.bananiser.mr.utilities.sedish;

import java.io.IOException;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;

import com.soundcloud.bananiser.mr.SeqFileCompatibleMapper;
import com.soundcloud.bananiser.mr.utilities.BananaUtility;

public class SedishMapper extends SeqFileCompatibleMapper<Text> {

    public static final String REPLACE_WITH_PARAMETER = SedishMapper.class
            .getName() + ".replaceWith";
    public static final String TO_REPLACE_PARAMETER = SedishMapper.class
            .getName() + ".toReplace";
    private String replaceWith;
    private String[] patterns;

    @Override
    protected void setup(Context context) throws IOException,
            InterruptedException {
        super.setup(context);
        Configuration configuration = context.getConfiguration();
        this.patterns = BananaUtility.asParameterList(configuration
                .get(TO_REPLACE_PARAMETER));
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
