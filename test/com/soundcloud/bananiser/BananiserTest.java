package com.soundcloud.bananiser;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;

import com.soundcloud.bananiser.Bananiser;

public class BananiserTest {
	@Test
	public void shouldBananiseASingleArgument() {
		String sentence = "1332795134246	Mar 26 20:52:11 ams-mid006.int.s-cloud.net nginx: api.soundcloud.com 10.20.3.72 - - [26/Mar/2012:20:52:11 +0000] \"POST /oauth2/token?client_id=76ac93274bab1af225ace0b69dbb8a8f&client_secret=558cdd9dfe1608d1534df0a285f0077b&grant_type=password&username=neomatr1x%40example.org&password=magafogafo HTTP/1.0\" 401 392 \"-\" \"-\" \"-\" \"41.125.95.222, 10.20.0.11\" \"https\"";
		String redactedSentence = "1332795134246	Mar 26 20:52:11 ams-mid006.int.s-cloud.net nginx: api.soundcloud.com 10.20.3.72 - - [26/Mar/2012:20:52:11 +0000] \"POST /oauth2/token?client_id=76ac93274bab1af225ace0b69dbb8a8f&client_secret=558cdd9dfe1608d1534df0a285f0077b&grant_type=password&username=BANANA&password=BANANA HTTP/1.0\" 401 392 \"-\" \"-\" \"-\" \"41.125.95.222, 10.20.0.11\" \"https\"";

		Bananiser bananiser = new Bananiser("password", "username");
		assertThat(bananiser.process(sentence), is(redactedSentence));
	}
}
