package com.stijndepestel.thesis.capturereplay;

/**
 * Listener for events from the Replay.
 *
 * @author sjdpeste
 *
 */
public interface ReplayListener {

    /**
     * Called when replay ends normally.
     *
     * @param event
     *            The ReplayEvent.
     */
    void replayEnded(ReplayEvent event);

    /**
     * Called when the replay ended without completing.
     *
     * @param event
     *            The ReplayEvent.
     */
    void replayFailed(ReplayEvent event);

    /**
     * A ReplayEvent contains information concerning the Replay and the state of
     * the replay when a specific event occurs.
     *
     * @author sjdpeste
     *
     */
    class ReplayEvent {

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

}
