package com.soundcloud.bananiser.test;

import static org.hamcrest.CoreMatchers.equalTo;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;

import org.apache.hadoop.io.Text;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import com.beust.jcommander.ParameterException;
import com.soundcloud.bananiser.utilities.BananaUtility;

public class BananaMatchers {

    public static Matcher<Class<? extends BananaUtility>> throwsParameterExceptionFor(
            final String[] args) {
        return new TypeSafeMatcher<Class<? extends BananaUtility>>() {

            private Class<? extends BananaUtility> utilityClass;

            @Override
            public boolean matchesSafely(
                    Class<? extends BananaUtility> utilityClass) {
                this.utilityClass = utilityClass;
                return invokeExpectingParameterException(
                        constructor(utilityClass), args);
            }

            @SuppressWarnings("rawtypes")
            private Constructor<? extends BananaUtility> constructor(
                    Class<? extends BananaUtility> utilityClass) {
                Constructor<? extends BananaUtility> constructor = null;
                try {
                    Class[] signature = new Class[] { String[].class };
                    constructor = utilityClass.getConstructor(signature);
                } catch (Exception e) {
                    new RuntimeException(e);
                }
                return constructor;
            }

            private boolean invokeExpectingParameterException(
                    Constructor<? extends BananaUtility> constructor,
                    final String[] args) {
                boolean thereWasAParameterException = false;
                try {
                    constructor.newInstance(new Object[] { args });
                    thereWasAParameterException = false;
                } catch (InvocationTargetException e) {
                    if (e.getCause() instanceof ParameterException) {
                        thereWasAParameterException = true;
                    }
                } catch (Exception e) {
                    new RuntimeException(e);
                }
                return thereWasAParameterException;
            }

            @Override
            public void describeTo(Description description) {
                String msg = String.format(
                        "No ParameterException when instantiating %s with &s",
                        utilityClass, Arrays.toString(args));
                description.appendText(msg);
            }
        };
    }

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
