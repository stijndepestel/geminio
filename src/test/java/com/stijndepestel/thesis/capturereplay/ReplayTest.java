package com.stijndepestel.thesis.capturereplay;

import org.json.JSONObject;
import org.junit.Before;

/**
 * Test for the replay functionality.
 *
 * @author sjdpeste
 *
 */
public class ReplayTest {

    /**
     * The replay object.
     */
    private Replay<TestEvent> replay;

    /**
     * Create new capture object before each test.
     */
    @Before
    public void before() {
        this.replay = new Replay<>(TestHelper::deserialize,
                () -> new JSONObject("{}"), event -> {
                    return null;
                });
    }
    
    

    // @Test
    // public void eventsCapturedTest() throws InterruptedException
    // @Test(expected = IllegalStateException.class)
    // public void exceptionOnCaptureOnInvalidStateWhenNotStartedTest()
    // @Test(expected = IllegalStateException.class)
    // public void exceptionOnCaptureOnInvalidStateWhenStoppedTest()
    // @Test(expected = IllegalStateException.class)
    // public void exceptionOnStoppingCaptureOnInvalidStateTest()
    // @Test(expected = IllegalStateException.class)
    // public void exceptionOnPersistingOnInvalidCreatedStateTest()
    // @Test(expected = IllegalStateException.class)
    // public void exceptionOnPersistingOnInvalidCapturingStateTest()

}
