/*
 * MIT License
 *
 * Copyright (c) 2016 Stijn De Pestel
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package com.stijndepestel.geminio;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

import com.jayway.awaitility.Awaitility;

/**
 * Integration test for the complete capture/replay functionality.
 *
 * @author sjdpeste
 *
 */
public final class CaptureReplayIT {

    /**
     * The number of events to throw during the test.
     */
    private static final int NUMBER_OF_EVENTS_TO_THROW = 100;
    /**
     * The interval between events, in ms.
     */
    private static final long INTERVAL_BETWEEN_EVENTS = 1000;

    /**
     * Acceptable error margin in ms of the replayed vs. intended time between
     * events. Important to note that the deviation can occur twice: once while
     * sleeping during the capture and the second time during the replay.
     */
    private static final long ACCEPTABLE_ERROR_MARGIN = 500;

    /**
     * String to store the result of the capture.
     */
    private String persistedJSON = "{}";

    /**
     * Place holder for the previous timestamp to check the error margin.
     */
    private long previousTimestamp;

    /**
     * Capture/Replay integration test.
     */
    @Test
    public void captureReplayIT() {
        // Create the events to throw.
        final TestEvent[] eventsToThrow = new TestEvent[CaptureReplayIT.NUMBER_OF_EVENTS_TO_THROW];
        for (int i = 0; i < eventsToThrow.length; i++) {
            eventsToThrow[i] = new TestEvent();
        }
        // Create the arraylist to store the thrown events during replay.
        final ArrayList<TestEvent> catchedEvents = new ArrayList<>(
                CaptureReplayIT.NUMBER_OF_EVENTS_TO_THROW);
        // Function that serves as an event catcher.
        final Consumer<TestEvent> eventCatcher = event -> {
            // Deviation of intended time.
            final long deviation = System.currentTimeMillis()
                    - this.previousTimestamp
                    - CaptureReplayIT.INTERVAL_BETWEEN_EVENTS;
            // Assert deviation is inside the error margin.
            Assert.assertTrue("Error margin to big: " + deviation,
                    deviation < CaptureReplayIT.ACCEPTABLE_ERROR_MARGIN);
            // Add the caught event to the arraylist.
            catchedEvents.add(event);
            // Set the previous time stamp for the next iteration.
            this.previousTimestamp = System.currentTimeMillis();
        };
        // The capture object to use during the test.
        final Capture<TestEvent> capture = new Capture<>(TestHelper::serialize,
                json -> this.persistedJSON = json.toString());
        // The replay object to use during the test.
        final Replay<TestEvent> replay = new Replay<>(TestHelper::deserialize,
                () -> new JSONObject(this.persistedJSON), eventCatcher);

        // Start the capture
        capture.startCapture();

        final Stream<TestEvent> stream = Arrays.stream(eventsToThrow);
        stream.forEach(event -> {
            try {
                Thread.sleep(CaptureReplayIT.INTERVAL_BETWEEN_EVENTS);
            } catch (final Exception e) {
                // ignore
            }
            capture.capture(event);
        });
        stream.close();
        // Stop the capture
        capture.stopCapture();
        // Persist the events (using the consumer passed during construction).
        capture.saveEvents();
        // Load the persisted events in the replayer (using the supplier passed
        // during construction).
        replay.load();
        // Set the previoustimestamp for the first iteration.
        this.previousTimestamp = System.currentTimeMillis();
        // set listener
        final TestReplayListener listener = new TestReplayListener();
        replay.addReplayListener(listener);
        // Start the replay.
        replay.startReplay();
        // Await till replay has finished
        Awaitility.await().atMost(3, TimeUnit.MINUTES)
                .until(this.hasReplayEnded(listener));
        // Assert that the catched events are equal to the replayed events (deep
        // equals).
        Assert.assertArrayEquals(eventsToThrow, catchedEvents.toArray());
        Assert.assertTrue("Replay is in stopped state.", replay.hasEnded());
    }

    private Callable<Boolean> hasReplayEnded(
            final TestReplayListener listener) {
        return new Callable<Boolean>() {
            public Boolean call() throws Exception {
                return listener.hasReplayEnded();
            }
        };
    }
}
