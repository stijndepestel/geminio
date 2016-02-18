package com.stijndepestel.thesis.capturereplay;

import java.util.PriorityQueue;
import java.util.function.Function;
import java.util.function.Supplier;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Defines the methods to replay captured events.
 *
 * @author sjdpeste
 *
 */
public final class Replay<T> {

    /**
     * Treshold defining the error margin of the sleep functionality.
     */
    private final static long TRESHOLD = 500L;

    /**
     * Deserializer for the events that will be replayed.
     */
    private final Function<JSONObject, T> deserializer;

    /**
     * Supplier of the JSONObject. Can f.e. be loaded from file.
     *
     * @see FilePersister
     */
    private final Supplier<JSONObject> loader;

    /**
     * Catcher of the events that will be replayed.
     */
    private final Function<T, ?> eventCatcher;

    /**
     * Priority queue that holds the events that still have to be replayed.
     * Ordered by the natural ordering (Comparable) of Wrapper.
     */
    private final PriorityQueue<Wrapper<T>> queue;

    /**
     * The current state of the replay object.
     */
    private State currentState;

    /**
     * Timestamp of when replay started.
     */
    private long replayStart;

    /**
     * Create new Replay object.
     *
     * @param deserializer
     *            Deserializer for the events (converts from JSONObject to T (==
     *            event type)).
     * @param loader
     *            Supplies the JSONObject containing all the previously captured
     *            events.
     * @param eventCatcher
     *            Catcher of the event that will be thrown by the replay. Return
     *            type of this function can be anything (including Void) and
     *            will be ignored.
     */
    public Replay(final Function<JSONObject, T> deserializer,
            final Supplier<JSONObject> loader, final Function<T, ?> eventCatcher) {
        this.deserializer = deserializer;
        this.loader = loader;
        this.eventCatcher = eventCatcher;
        this.queue = new PriorityQueue<>();
    }

    /**
     * Start the replay, will load and deserialize all events and start
     * replaying them.
     */
    public void startReplay() {
        if (this.currentState != State.CREATED) {
            throw new IllegalArgumentException(
                    "Object is not in correct state to start the replay.");
        }
        this.currentState = State.REPLAYING;
        // Get the json
        final JSONArray jsonarr = this.loader.get().getJSONArray(
                JSONNames.JSON_EVENTS);
        jsonarr.forEach(json -> this.queue.add(new Wrapper<>((JSONObject) json,
                this.deserializer)));
        this.replayStart = System.currentTimeMillis();
        // start replay
        this.replay();
    }

    /**
     * Recursive replay function.
     */
    private void replay() {
        if (this.currentState != State.REPLAYING) {
            throw new IllegalArgumentException(
                    "Object is not in replaying state.");
        }
        if (this.queue.isEmpty()) {
            // end of replay
            this.currentState = State.STOPPED;
            return;
        }
        final long nextReplayTime = this.queue.peek().getRelativeTimestamp();
        final long timerunning = System.currentTimeMillis() - this.replayStart;
        final long toSleep = nextReplayTime - timerunning;
        // if within treshold
        if (toSleep < Replay.TRESHOLD) {
            // throw event
            this.eventCatcher.apply(this.queue.poll().getEvent());
        } else {
            try {
                Thread.sleep(toSleep);
            } catch (final InterruptedException e) {
                // Ignore exception
            }
        }
        // Recursion
        this.replay();
    }

    /**
     * Definitions of the possible states of a Replay object.
     *
     * @author sjdpeste
     *
     */
    private enum State {
        /**
         * The object has been created but has not yet started the replay.
         */
        CREATED,
        /**
         * The object is in replay mode, it is throwing.
         */
        REPLAYING,
        /**
         * The object has finished replaying the events or was interrupted.
         */
        STOPPED;

    }
}
