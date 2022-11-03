package com.example.test;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;

@RunWith(RobolectricTestRunner.class)
public class UnitTest {
    @Before
    public void setup() throws Exception {
        activity = Robolectric.buildActivity(MainActivity.class).create().resume().get();
    }
}
