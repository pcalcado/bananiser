package com.soundcloud.bananiser.utilities.sedish;

import static com.soundcloud.bananiser.utilities.BananaUtility.toParameterListString;
import static com.soundcloud.bananiser.utilities.sedish.SedishMapper.REPLACE_WITH_PARAMETER;
import static com.soundcloud.bananiser.utilities.sedish.SedishMapper.TO_REPLACE_PARAMETER;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.mapreduce.Mapper.Context;
import org.junit.Test;

import com.soundcloud.bananiser.mr.MapperTestCase;

public class SedishMapperTest extends MapperTestCase {

    @Test
    @SuppressWarnings({ "rawtypes" })
    public void shouldReplacePatternWithDesiredString() {
        String sentence = "1332795134246        Mar 26 20:52:11 nyc.example.net nginx: api.soundcloud.com 10.120.13.52 - - [26/Mar/2012:20:52:11 +0000] \"POST /check/me?client_id=dbb8a8f&secret=558285f0077b&fruit_type=apple&username=alonso1x.x%40example.org&sex=m@l3 HTTP/1.0\" 401 392 \"-\" \"-\" \"-\" \"41.125.195.222, 10.120.13.52\" \"https\"";
        String modifiedSentence = "1332795134246        Mar 26 20:52:11 nyc.example.net nginx: api.soundcloud.com 10.120.13.52 - - [26/Mar/2012:20:52:11 +0000] \"POST /check/me?client_id=dbb8a8f&secret=558285f0077b&fruit_type=apple&BANANA&sex=m@l3 HTTP/1.0\" 401 392 \"-\" \"-\" \"-\" \"BANANA, 10.120.13.52\" \"https\"";

        Configuration configuration = new Configuration();

        Context context = setupContext(configuration);

        String patterns = toParameterListString("41.125.195.222",
                "username=(\\S(?!&))+.");
        configuration.set(TO_REPLACE_PARAMETER, patterns);
        configuration.set(REPLACE_WITH_PARAMETER, "BANANA");
        SedishMapper mapper = new SedishMapper();
        invokeMapOperation(mapper, sentence, context);

        verifyWroteTo(context, SOME_IRRELEVANT_KEY, modifiedSentence);
    }

    @Test
    @SuppressWarnings({ "rawtypes" })
    public void shouldNotDoAnythingIfNoMatch() {

        String sentence = "1332795134246        Mar 26 20:52:11 nyc.example.net nginx: api.soundcloud.com 10.120.13.52 - - [26/Mar/2012:20:52:11 +0000] \"POST /check/me?client_id=dbb8a8f&secret=558285f0077b&fruit_type=apple&username=alonso1x.x%40example.org&sex=m@l3 HTTP/1.0\" 401 392 \"-\" \"-\" \"-\" \"41.125.195.222, 10.120.13.52\" \"https\"";

        Configuration configuration = new Configuration();

        Context context = setupContext(configuration);

        String patterns = toParameterListString("not1there", "(lalala)+");
        configuration.set(TO_REPLACE_PARAMETER, patterns);
        configuration.set(REPLACE_WITH_PARAMETER, "BANANA");
        SedishMapper mapper = new SedishMapper();
        invokeMapOperation(mapper, sentence, context);

        verifyWroteTo(context, SOME_IRRELEVANT_KEY, sentence);
    }
}
