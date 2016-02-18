package com.stijndepestel.thesis.capturereplay;

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
     * The current state of the replay object.
     */
    private State currentState;

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
    }

    public void startReplay() {
        if (this.currentState != State.CREATED) {
            throw new IllegalArgumentException(
                    "Object is not in correct state to start the replay.");
        }
        this.currentState = State.REPLAYING;
        // Get the json
        final JSONArray json = this.loader.get().getJSONArray(
                JSONNames.JSON_EVENTS);
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
