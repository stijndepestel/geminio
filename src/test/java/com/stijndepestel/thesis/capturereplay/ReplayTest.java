package com.stijndepestel.thesis.capturereplay;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

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
     * The fake events, corresponding with the JSON from the provideJSON
     * function.
     */
    private final TestEvent[] fakeEvents = new TestEvent[] {
            new TestEvent(0, 0), new TestEvent(1, 1), new TestEvent(2, 2),
            new TestEvent(3, 3), new TestEvent(4, 4) };

    /**
     * A counter that can be used during the unit testing inside the higher
     * order functions. Will be reset to zero before every test.
     */
    private int countHelper;

    /**
     * Helper function to provide fake JSON events for the unit test.
     *
     * @return The created JSONObject with the fake events.
     */
    private JSONObject provideJSON() {
        return new JSONObject("{events : ["
                + "{relative_time:0,event:{random:0,time:0}},"
                + "{relative_time:10,event:{random:1,time:1}},"
                + "{relative_time:20,event:{random:2,time:2}},"
                + "{relative_time:30,event:{random:3,time:3}},"
                + "{relative_time:40,event:{random:4,time:4}}]}");
    }

    /**
     * Create new capture object before each test.
     */
    @Before
    public void before() {
        this.replay = new Replay<>(TestHelper::deserialize, this::provideJSON,
                event -> {
                });
        this.countHelper = 0;
    }

    /**
     * Test that the replayed events are equal to the original events.
     */
    @Test
    public void eventsReplayedTest() {
        this.replay = new Replay<>(TestHelper::deserialize, this::provideJSON,
                this::eventsReplayedTestHelper);
        this.replay.load().startReplay();
    }

    private void eventsReplayedTestHelper(final TestEvent event) {
        Assert.assertTrue("Events are not equal",
                event.equals(this.fakeEvents[this.countHelper]));
        this.countHelper++;
    }

    /**
     * Test that an exception is thrown when starting with an invalid state.
     */
    @Test(expected = IllegalStateException.class)
    public void exceptionOnStartWithInvalidStateTest() {
        // no load
        this.replay.startReplay();
    }

    /**
     * Test that an exception is thrown when trying to load with an invalid
     * state.
     */
    @Test(expected = IllegalStateException.class)
    public void exceptionOnLoadWithInvalidStateTest() {
        this.replay.load().startReplay();
        this.replay.load();
    }

}
