package com.stijndepestel.thesis.capturereplay;

import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.jayway.awaitility.Awaitility;

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

    private JSONObject provideJSONForFailedReplay() {
        return new JSONObject("{events : ["
                + "{relative_time:0,event:{random:0,time:0}},"
                + "{relative_time:1000,event:{random:1,time:1}},"
                + "{relative_time:2000,event:{random:2,time:2}}]}");
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
        final TestReplayListener listener = new TestReplayListener();
        this.replay.addReplayListener(listener);
        this.replay.load().startReplay();
        Awaitility.await().atMost(1, TimeUnit.SECONDS)
                .until(this.hasReplayEnded(listener));
        Assert.assertEquals("All events were replayed", this.fakeEvents.length,
                listener.getLastEndedEventsCount());
    }

    private void eventsReplayedTestHelper(final TestEvent event) {
        Assert.assertTrue("Events are not equal",
                event.equals(this.fakeEvents[this.countHelper]));
        this.countHelper++;
    }

    /**
     * Test that the replay is stopped gracefully.
     */
    @Test
    public void eventsReplayedFailedTest() {
        this.replay = new Replay<>(TestHelper::deserialize,
                this::provideJSONForFailedReplay, event -> {/* ignore */
                });
        final TestReplayListener listener = new TestReplayListener();
        this.replay.addReplayListener(listener);
        this.replay.load().startReplay();
        Assert.assertTrue(this.replay.stopReplay());
        Awaitility.await().atMost(1, TimeUnit.SECONDS)
                .until(this.hasReplayFailed(listener));
        Assert.assertEquals("Failed counter is 1", 1,
                listener.getFailedCounter());
        System.out.println(listener.getLastFailedEventsCount());
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

    private Callable<Boolean> hasReplayFailed(final TestReplayListener listener) {
        return new Callable<Boolean>() {
            public Boolean call() throws Exception {
                return listener.hasReplayFailed();
            }
        };
    }

    private Callable<Boolean> hasReplayEnded(final TestReplayListener listener) {
        return new Callable<Boolean>() {
            public Boolean call() throws Exception {
                return listener.hasReplayEnded();
            }
        };
    }

}
