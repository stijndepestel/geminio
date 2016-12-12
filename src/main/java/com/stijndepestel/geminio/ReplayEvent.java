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

/**
 * A ReplayEvent contains information concerning the Replay and the state of the
 * replay when a specific event occurs.
 *
 * @author sjdpeste
 *
 */
public final class ReplayEvent {

    /**
     * The total events that were replayed during the replay.
     */
    private final int totalEventsReplayed;

    /**
     * Create a new ReplayEvent.
     *
     * @param totalEventsReplayed
     *            The total number of events that were replayed before this
     *            event occurred.
     */
    public ReplayEvent(final int totalEventsReplayed) {
        this.totalEventsReplayed = totalEventsReplayed;
    }

    /**
     * Get the total number of replayed events during the replay.
     *
     * @return The total number of events that were replayed.
     */
    public int getTotalEventsReplayed() {
        return this.totalEventsReplayed;
    }

}
