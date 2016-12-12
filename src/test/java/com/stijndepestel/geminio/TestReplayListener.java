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
 * ReplayListener for testing purposes.
 *
 * @author sjdpeste
 *
 */
class TestReplayListener implements ReplayListener {
    private int endedCounter = 0, failedCounter = 0;

    private int lastEndedEventsCount = -1, lastFailedEventsCount = -1;

    @Override
    public void replayEnded(final ReplayEvent event) {
        this.endedCounter++;
        this.lastEndedEventsCount = event.getTotalEventsReplayed();
    }

    public boolean hasReplayEnded() {
        return this.endedCounter != 0;
    }

    @Override
    public void replayFailed(final ReplayEvent event) {
        this.failedCounter++;
        this.lastFailedEventsCount = event.getTotalEventsReplayed();
    }

    public boolean hasReplayFailed() {
        return this.failedCounter != 0;
    }

    public int getLastEndedEventsCount() {
        return this.lastEndedEventsCount;
    }

    public int getLastFailedEventsCount() {
        return this.lastFailedEventsCount;
    }

    public int getEndedCounter() {
        return this.endedCounter;
    }

    public int getFailedCounter() {
        return this.failedCounter;
    }
}