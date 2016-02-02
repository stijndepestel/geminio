package com.stijndepestel.thesis.capturereplay;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Test for the capture/replay functionality.
 * @author sjdpeste
 *
 */
public class CaptureReplayTest {
	
	/**
	 * The capture object.
	 */
	private Capture<TestEvent> capture;
	
	/**
	 * Method to serialize test events.
	 * @param event The testevent to serialize.
	 * @return The serialized event.
	 */
	private String serialize(TestEvent event){
		return "{" + "\"time\":\"" + event.getTimestamp() + "\", \"random\" : \"" + event.getRandom() + "\"}";
	}
	
	/**
	 * Create new capture object before each test.
	 */
	@Before
	public void before(){
		capture = new Capture<>();
	}
	
	/**
	 * Test to see if a captured event is serialized and stored in the non-persisted list.
	 */
	@Test
	public void serializeSavedInListTest(){
		Assert.assertEquals(0, capture.getSerializedEvents().size());
		capture.capture(this::serialize, new TestEvent());
		Assert.assertEquals(1, capture.getSerializedEvents().size());
		System.out.println(capture.getSerializedEvents());
	}

}
