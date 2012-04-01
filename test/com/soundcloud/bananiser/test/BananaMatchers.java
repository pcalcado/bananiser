package com.soundcloud.bananiser.test;

import static org.hamcrest.CoreMatchers.equalTo;

import org.hamcrest.Matcher;

public class BananaMatchers {

    @SuppressWarnings("rawtypes")
    public static Matcher sameClassAs(Class someClass) {
        return equalTo(someClass);
    }
}
