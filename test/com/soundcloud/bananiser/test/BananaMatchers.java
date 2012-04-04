package com.soundcloud.bananiser.test;

import static org.hamcrest.CoreMatchers.equalTo;

import org.apache.hadoop.io.Text;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class BananaMatchers {

    @SuppressWarnings("rawtypes")
    public static Matcher<Class> sameClassAs(Class someClass) {
        return equalTo(someClass);
    }

    public static Matcher<Text> sameAs(final Text expected) {
        return new TypeSafeMatcher<Text>() {

            private Text other;

            @Override
            public void describeTo(Description description) {
                description.appendText("Expected (1) but found (2):\n(1)"
                        + expected + "\n(2)" + other);

            }

            @Override
            public boolean matchesSafely(Text other) {
                this.other = other;
                return this.other.toString().equals(expected.toString());
            }
        };
    }
}
