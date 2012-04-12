package com.soundcloud.bananiser.mr.utilities.hash;

import static com.soundcloud.bananiser.mr.utilities.BananaUtility.toParameterListString;
import static com.soundcloud.bananiser.mr.utilities.hash.HashMapper.TO_REPLACE_PARAMETER;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapreduce.Mapper.Context;
import org.junit.Test;

import com.soundcloud.bananiser.mr.MapperTestCase;
import com.soundcloud.bananiser.mr.utilities.hash.HashMapper;

public class HashMapperTest extends MapperTestCase {

    @SuppressWarnings("rawtypes")
    @Test
    public void shouldReplaceMatchesWithTheirHash() {
        String sentence = "Finally, Lula told the Ambassador that he is planning a  visit to Iran in May, and that his goal in engaging is to \"lower the temperature\" on the Iran issue.";
        String modifiedSentence = "Finally, r2tj3rhmqr7e3i72upta told the Ambassador that he is planning a  visit to 8i43s2aaht1u74r8249o in May, and that his goal in engaging is to \"lower the temperature\" on the 8i43s2aaht1u74r8249o issue.";

        Configuration configuration = new Configuration();
        Context context = setupContext(configuration);

        String patterns = toParameterListString("Lula", "I.an");
        configuration.set(TO_REPLACE_PARAMETER, patterns);
        HashMapper mapper = new HashMapper();
        invokeMapOperation(mapper, sentence, context);

        verifyWroteTo(context, SOME_IRRELEVANT_KEY, modifiedSentence);
    }

    @Test
    @SuppressWarnings("rawtypes")
    public void shouldNotDoAnythingWhenNoMatches() {
        String sentence = "Finally, Lula told the Ambassador that he is planning a  visit to Iran in May, and that his goal in engaging is to \"lower the temperature\" on the Iran issue.";

        Configuration configuration = new Configuration();
        Context context = setupContext(configuration);

        String patterns = toParameterListString("Brazil", "Iraq");
        configuration.set(TO_REPLACE_PARAMETER, patterns);
        HashMapper mapper = new HashMapper();
        invokeMapOperation(mapper, sentence, context);

        verifyWroteTo(context, SOME_IRRELEVANT_KEY, sentence);
    }
}
