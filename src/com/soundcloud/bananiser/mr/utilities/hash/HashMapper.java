package com.soundcloud.bananiser.mr.utilities.hash;

import static com.soundcloud.bananiser.mr.utilities.BananaUtility.asParameterList;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;

import com.soundcloud.bananiser.mr.SeqFileCompatibleMapper;

public class HashMapper extends SeqFileCompatibleMapper<Text> {
    public static final String TO_REPLACE_PARAMETER = HashMapper.class
            .getName() + ".replace";
    private List<Pattern> patterns = new ArrayList<Pattern>();
    private MessageDigest hasher;

    protected void setup(Context context) throws IOException,
            InterruptedException {
        super.setup(context);
        Configuration configuration = context.getConfiguration();
        String[] patternStrings = asParameterList(configuration
                .get(TO_REPLACE_PARAMETER));
        for (String patternString : patternStrings) {
            patterns.add(Pattern.compile(patternString));
        }
        hasher = newHasher();
    };

    @Override
    public void mapText(LongWritable key, Text value, Context context)
            throws IOException, InterruptedException {
        String valueAsString = value.toString();
        for (Pattern pattern : patterns) {
            Matcher matcher = pattern.matcher(valueAsString);
            StringBuffer buffer = new StringBuffer();
            while (matcher.find()) {
                String hashed = hashNextMatch(matcher);
                matcher.appendReplacement(buffer, hashed);
            }
            matcher.appendTail(buffer);
            valueAsString = buffer.toString();
        }
        context.write(key, new Text(valueAsString));
    }

    private String hashNextMatch(Matcher matcher) {
        hasher.reset();
        String found = matcher.toMatchResult().group();
        try {
            hasher.update(found.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }

        StringBuffer sb = new StringBuffer("");
        for (byte b : hasher.digest()) {
            sb.append(Integer.toString((b & 0xff) + 0x100, 32).substring(1));
        }
        String hashed = sb.toString();
        return hashed;
    }

    private MessageDigest newHasher() {
        MessageDigest hasher;
        try {
            hasher = MessageDigest.getInstance("SHA1");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        return hasher;
    }
}
