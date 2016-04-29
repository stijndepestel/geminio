package com.stijndepestel.thesis.capturereplay;

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
