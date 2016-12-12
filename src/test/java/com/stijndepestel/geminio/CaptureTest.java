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

import org.json.JSONArray;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for the capture functionality.
 *
 * @author sjdpeste
 *
 */
public class CaptureTest {

    /**
     * The capture object.
     */
    private Capture<TestEvent> capture;

    /**
     * Create new capture object before each test.
     */
    @Before
    public void before() {
        this.capture = new Capture<>(TestHelper::serialize, x -> {
        });
    }

    /**
     * Test to see if a captured event is serialized and stored.
     *
     */
    @Test
    public void eventsCapturedTest() {
        this.capture.startCapture();
        Assert.assertEquals("No events should have been captured yet.", 0,
                this.capture.getNumberOfCapturedEvents());
        this.capture.capture(new TestEvent());
        Assert.assertEquals("One event should have been captured.", 1,
                this.capture.getNumberOfCapturedEvents());
    }

    /**
     * Test to see if a captured event is rejected if the capture has not been
     * started.
     */
    @Test(expected = IllegalStateException.class)
    public void exceptionOnCaptureOnInvalidStateWhenNotStartedTest() {
        this.capture.capture(new TestEvent());
    }

    /**
     * Test to see if a captured event is rejected if the capture has already
     * been stopped.
     */
    @Test(expected = IllegalStateException.class)
    public void exceptionOnCaptureOnInvalidStateWhenStoppedTest() {
        this.capture.startCapture();
        this.capture.stopCapture();
        this.capture.capture(new TestEvent());
    }

    /**
     * Test to see if a stop request is rejected if the capture has not yet
     * started.
     */
    @Test(expected = IllegalStateException.class)
    public void exceptionOnStoppingCaptureOnInvalidStateTest() {
        this.capture.stopCapture();
    }

    /**
     * Test to see if a stopped capture cannot be restarted
     */
    @Test(expected = IllegalStateException.class)
    public void exceptionOnRestartingCaptureTest() {
        this.capture.startCapture();
        this.capture.stopCapture();
        this.capture.startCapture();
    }

    /**
     * Test to see if persisting is rejected on invalid created state.
     */
    @Test(expected = IllegalStateException.class)
    public void exceptionOnPersistingOnInvalidCreatedStateTest() {
        this.capture.saveEvents();
    }

    /**
     * Test to see if persisting is rejected on invalid capturing state.
     */
    @Test(expected = IllegalStateException.class)
    public void exceptionOnPersistingOnInvalidCapturingStateTest() {
        this.capture.startCapture();
        this.capture.saveEvents();
    }

    /**
     * Test to see if persister is provided with all events.
     */
    @Test
    public void persisterGetsJSONOfAllEvents() {
        final int numberOfEventsToGenerate = 5;
        this.capture = new Capture<>(TestHelper::serialize, x -> {
            final JSONArray jsonArray = x.getJSONArray(JSONNames.JSON_EVENTS);
            Assert.assertEquals(
                    "Same number of events should be stored as the number of events that were thrown.",
                    numberOfEventsToGenerate, jsonArray.length());
        });
        this.capture.startCapture();
        for (int i = 0; i < numberOfEventsToGenerate; i++) {
            this.capture.capture(new TestEvent());
        }
        this.capture.stopCapture();
        this.capture.saveEvents();
    }

}
