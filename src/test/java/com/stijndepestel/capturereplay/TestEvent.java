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
package com.stijndepestel.capturereplay;

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
