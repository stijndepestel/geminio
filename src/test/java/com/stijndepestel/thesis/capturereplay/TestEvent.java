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
        this.timestamp = System.currentTimeMillis();
        this.random = new Random().nextLong();
    }

    /**
     * Create test event with given data.
     *
     * @param timestamp
     *            The time when the event occurred.
     * @param random
     *            Random information contained in the event.
     */
    public TestEvent(final long timestamp, final long random) {
        this.timestamp = timestamp;
        this.random = random;
    }

    /**
     * @return the timestamp.
     */
    public long getTimestamp() {
        return this.timestamp;
    }

    /**
     * @return the random.
     */
    public long getRandom() {
        return this.random;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj instanceof TestEvent) {
            final TestEvent other = (TestEvent) obj;
            return this.timestamp == other.timestamp
                    && this.random == other.random;
        }
        return false;
    }

}
