package com.example.omgandroid.test;

import android.test.InstrumentationTestCase;

public class ExampleTest extends InstrumentationTestCase {
    public void test() throws Exception {
        final int expected = 1;
        final int reality = 5;
        assertEquals(expected, reality);
    }
}