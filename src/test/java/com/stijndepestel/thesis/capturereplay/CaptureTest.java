package com.stijndepestel.thesis.capturereplay;

import java.util.Random;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for the capture/replay functionality.
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
	 * Method to serialize test events.
	 * 
	 * @param event
	 *            The testevent to serialize.
	 * @return The serialized event.
	 */
	private JSONObject serialize(TestEvent event) {
		return new JSONObject().put("time", event.getTimestamp()).put("random",
				event.getRandom());
	}

	/**
	 * Create new capture object before each test.
	 */
	@Before
	public void before() {
		capture = new Capture<>(this::serialize, x -> {});
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
		capture.startCapture();
		//sleep before posting event
		Thread.sleep(new Random().nextInt(1000));
		Assert.assertEquals(0, capture.getNumberOfCapturedEvents());
		capture.capture(new TestEvent());
		Assert.assertEquals(1, capture.getNumberOfCapturedEvents());
	}

	/**
	 * Test to see if a captured event is rejected if the capture has not been
	 * started.
	 */
	@Test(expected = IllegalStateException.class)
	public void exceptionOnCaptureOnInvalidStateWhenNotStartedTest() {
		capture.capture(new TestEvent());
	}

	/**
	 * Test to see if a captured event is rejected if the capture has already
	 * been stopped.
	 */
	@Test(expected = IllegalStateException.class)
	public void exceptionOnCaptureOnInvalidStateWhenStoppedTest() {
		capture.startCapture();
		capture.stopCapture();
		capture.capture(new TestEvent());
	}

	/**
	 * Test to see if a stop request is rejected if the capture has not yet
	 * started.
	 */
	@Test(expected = IllegalStateException.class)
	public void exceptionOnStoppingCaptureOnInvalidStateTest() {
		capture.stopCapture();
	}
	
	/**
	 * Test to see if persisting is rejected on invalid created state.
	 */
	@Test(expected = IllegalStateException.class)
	public void exceptionOnPersistingOnInvalidCreatedStateTest(){
		capture.saveEvents();
	}
	
	/**
	 * Test to see if persisting is rejected on invalid capturing state.
	 */
	@Test(expected = IllegalStateException.class)
	public void exceptionOnPersistingOnInvalidCapturingStateTest(){
		capture.startCapture();
		capture.saveEvents();
	}

}
