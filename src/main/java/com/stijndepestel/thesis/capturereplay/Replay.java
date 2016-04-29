package com.stijndepestel.thesis.capturereplay;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Defines the methods to replay captured events.
 *
 * @author sjdpeste
 *
 * @param <T>
 *            The type of events to replay.
 *
 */
public final class Replay<T> {

    /**
     * The error message for the IllegalStateExceptions thrown in this object.
     */
    private static final String ERROR_MESSAGE = "Object is not in the right state.";

    /**
     * Treshold defining the error margin of the sleep functionality.
     */
    private static final long TRESHOLD = 200L;

    /**
     * The list of ReplayListeners.
     */
    private final List<ReplayListener> listeners;

    /**
     * Deserializer for the events that will be replayed.
     */
    private final Function<JSONObject, T> deserializer;

    /**
     * Supplier of the JSONObject. Can f.e. be loaded from file.
     *
     * @see Persister
     */
    private final Supplier<JSONObject> loader;

    /**
     * Catcher of the events that will be replayed.
     */
    private final Consumer<T> eventCatcher;

    /**
     * Set that holds the events that were loaded.
     */
    private final Set<Wrapper<T>> loadedEvents;

    /**
     * Priority queue that holds the events that still have to be replayed.
     * Ordered by the natural ordering (Comparable) of Wrapper.
     */
    private PriorityQueue<Wrapper<T>> queue;

    /**
     * The current state of the replay object.
     */
    private State currentState;

    /**
     * Timestamp of when replay started.
     */
    private long replayStart;

    /**
     * Counter for the number of replayed events.
     */
    private int replayCounter;

    /**
     * Flag to indicate that a stop was requested.
     */
    private boolean stopRequested;

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
            final Supplier<JSONObject> loader, final Consumer<T> eventCatcher) {
        this.deserializer = deserializer;
        this.loader = loader;
        this.eventCatcher = eventCatcher;
        this.listeners = new ArrayList<>();
        this.loadedEvents = new HashSet<>();
        this.currentState = State.CREATED;
    }

    /**
     * Load and deserialize the events from their persisted state.
     *
     * @return Reference to this instance.
     */
    public Replay<T> load() {
        if (this.currentState != State.CREATED) {
            throw new IllegalStateException(Replay.ERROR_MESSAGE);
        }
        // Get the json
        final JSONArray jsonarr = this.loader.get().getJSONArray(
                JSONNames.JSON_EVENTS);
        jsonarr.forEach(json -> this.loadedEvents.add(new Wrapper<>(
                (JSONObject) json, this.deserializer)));
        this.currentState = State.LOADED;
        return this;
    }

    /**
     * Start the replay, will load all events into a queue and start replaying
     * them.
     */
    public void startReplay() {
        if (this.currentState != State.LOADED) {
            throw new IllegalStateException(Replay.ERROR_MESSAGE);
        }
        this.queue = new PriorityQueue<>(this.loadedEvents);
        this.currentState = State.REPLAYING;
        this.replayCounter = 0;
        this.replayStart = System.currentTimeMillis();
        // start replay in a thread
        new Thread(this::replay).start();
    }

    /**
     * Request a stop of the current replay. After this request maximum one more
     * event will be replayed.
     *
     * @return true if the stop was requested, false if the replay had already
     *         ended.
     */
    public boolean stopReplay() {
        if (this.currentState == State.STOPPED) {
            return false;
        }
        if (this.currentState == State.CREATED
                || this.currentState == State.LOADED) {
            throw new IllegalStateException(Replay.ERROR_MESSAGE);
        }
        this.stopRequested = true;
        return true;
    }

    /**
     * Recursive replay function.
     */
    private void replay() {
        if (this.currentState != State.REPLAYING) {
            throw new IllegalStateException(Replay.ERROR_MESSAGE);
        }
        if (this.queue.isEmpty()) {
            // end of replay
            this.currentState = State.STOPPED;
            this.throwEnded();
            return;
        }
        final long nextReplayTime = this.queue.peek().getRelativeTimestamp();
        final long timerunning = System.currentTimeMillis() - this.replayStart;
        final long toSleep = nextReplayTime - timerunning;
        // if within treshold
        if (toSleep < Replay.TRESHOLD) {
            // throw event
            this.eventCatcher.accept(this.queue.poll().getEvent());
            this.replayCounter++;
        } else {
            try {
                Thread.sleep(toSleep);
            } catch (final InterruptedException e) {
                // Ignore exception
            }
        }
        // Recursion
        if (this.stopRequested) {
            this.throwFailed();
        } else {
            this.replay();
        }

    }

    /**
     * Add a replay listener.
     *
     * @param listener
     *            The ReplayListener to add.
     */
    public void addReplayListener(final ReplayListener listener) {
        this.listeners.add(listener);
    }

    /**
     * Remove a replay listener.
     *
     * @param listener
     *            The ReplayListener to remove.
     */
    public void removeReplayListener(final ReplayListener listener) {
        this.listeners.remove(listener);
    }

    /**
     * Throw a ReplayEvent to the listeners to notify them of the end of the
     * replay.
     */
    private void throwEnded() {
        final ReplayEvent event = new ReplayEvent(this.replayCounter);
        this.listeners.forEach(l -> l.replayEnded(event));
    }

    /**
     * Throw a ReplayEvent to the listeners to notify them of the failing of the
     * replay.
     */
    private void throwFailed() {
        final ReplayEvent event = new ReplayEvent(this.replayCounter);
        this.listeners.forEach(l -> l.replayFailed(event));
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
         * The object has loaded the events for replaying.
         */
        LOADED,
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
