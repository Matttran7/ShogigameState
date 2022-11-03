package com.example.test;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import static org.junit.Assert.*;

@RunWith(RobolectricTestRunner.class)
public class ShogiTests {

    public MainActivity activity;

    @Before
    public void setup() throws Exception {
        activity = Robolectric.buildActivity(MainActivity.class).create().resume().get();
    }

    @Test
    public void copyConstruct_GameState(){
        GameState origGS = new GameState();
        GameState copyGS = new GameState(origGS);
        assertTrue("GameState Copy Constructor is not equal",origGS.equals(copyGS));
    }

}
