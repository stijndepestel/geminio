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

}
