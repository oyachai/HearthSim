package com.hearthsim.test

import org.hamcrest.BaseMatcher
import org.hamcrest.Description
import org.hamcrest.Matcher

class IsByteEqual<Number> extends BaseMatcher<Number> {

    private final Byte object;

    public IsByteEqual(Number object) {
        this.object = (byte) object;
    }

    @Override
    boolean matches(Object arg) {
        return arg == object;
    }

    @Override
    void describeTo(Description description) {
        description.appendText("equal after byte cast")
    }

    @org.hamcrest.Factory
    public static <T> Matcher<T> isByteEqual(T target) {
        return new IsByteEqual<T>(target);
    }
}
