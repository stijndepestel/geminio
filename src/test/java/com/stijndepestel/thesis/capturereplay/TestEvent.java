package com.stijndepestel.thesis.capturereplay;

import java.util.Random;

/**
 * Dummy event for testing purposes.
 * 
 * @author sjdpeste
 *
 */
public class TestEvent {

	/**
	 * Time when the event occurred.
	 */
	private final long timestamp;
	/**
	 * Random information for the event.
	 */
	private final long random;

	/**
	 * Create new test event with current time and random information.
	 */
	public TestEvent() {
		timestamp = System.currentTimeMillis();
		random = new Random().nextLong();
	}

	/**
	 * Create test event with given data.
	 * 
	 * @param timestamp
	 *            The time when the event occurred.
	 * @param random
	 *            Random information contained in the event.
	 */
	public TestEvent(long timestamp, long random) {
		this.timestamp = timestamp;
		this.random = random;
	}

	/**
	 * @return the timestamp.
	 */
	public long getTimestamp() {
		return timestamp;
	}

	/**
	 * @return the random.
	 */
	public long getRandom() {
		return random;
	}

}
