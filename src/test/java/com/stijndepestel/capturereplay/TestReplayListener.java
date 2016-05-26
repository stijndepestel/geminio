package com.stijndepestel.capturereplay;

import com.stijndepestel.capturereplay.ReplayEvent;
import com.stijndepestel.capturereplay.ReplayListener;

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