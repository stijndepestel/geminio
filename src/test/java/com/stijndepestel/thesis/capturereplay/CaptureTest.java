package com.stijndepestel.thesis.capturereplay;

import java.util.Random;

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
     * Test to see if a captured event is serialized and stored in the
     * non-persisted list.
     *
     * @throws InterruptedException
     *             When something goes wrong during sleep.
     */
    @Test
    public void eventsCapturedTest() throws InterruptedException {
        this.capture.startCapture();
        // sleep before posting event
        Thread.sleep(new Random().nextInt(1000));
        Assert.assertEquals(0, this.capture.getNumberOfCapturedEvents());
        this.capture.capture(new TestEvent());
        Assert.assertEquals(1, this.capture.getNumberOfCapturedEvents());
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

}
