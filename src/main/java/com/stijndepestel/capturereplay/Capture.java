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

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

import org.json.JSONObject;

/**
 * Defines the methods to capture and store events.
 *
 * @author sjdpeste
 *
 * @param <T>
 *            The type of events to capture.
 */
public final class Capture<T> {

    /**
     * The error message for the IllegalStateExceptions thrown in this object.
     */
    private static final String ERROR_MESSAGE = "Object is not in the right state.";

    /**
     * List containing the serialized events that are not yet persisted.
     */
    private final List<Wrapper<T>> serializedEvents;

    /**
     * The current state of the Capture.
     */
    private State currentState;

    /**
     * Timestamp of when capturing started.
     */
    private long captureStart;

    /**
     * Serializer for the events.
     */
    private final Function<T, JSONObject> serializer;

    /**
     * Consumer to persist the captured events.
     *
     * @see Persister
     */
    private final Consumer<JSONObject> persister;

    /**
     * Create new capture object.
     *
     * @param serializer
     *            Function to serialize the event to a JSON object.
     * @param persister
     *            Consumer to persist the created JSON object.
     */
    public Capture(final Function<T, JSONObject> serializer,
            final Consumer<JSONObject> persister) {
        this.serializedEvents = new ArrayList<>();
        this.currentState = State.CREATED;
        this.serializer = serializer;
        this.persister = persister;
    }

    /**
     * Start capturing mode. Events will be logged relative to the timestamp of
     * calling this method.
     */
    public void startCapture() {
        if (this.currentState != State.CREATED) {
            throw new IllegalStateException(Capture.ERROR_MESSAGE);
        }
        this.currentState = State.CAPTURING;
        this.captureStart = System.currentTimeMillis();
    }

    /**
     * Stop the capture.
     *
     * @throws IllegalStateException
     *             When this is called before the capture was started.
     */
    public void stopCapture() {
        if (this.currentState != State.CAPTURING) {
            throw new IllegalStateException(Capture.ERROR_MESSAGE);
        }
        this.currentState = State.STOPPED;
    }

    /**
     * Capture event.
     *
     * @param event
     *            The event to capture.
     * @throws IllegalStateException
     *             When trying to capture an event when the object is not in
     *             capture mode.
     */
    public void capture(final T event) {
        if (this.currentState != State.CAPTURING) {
            throw new IllegalStateException(Capture.ERROR_MESSAGE);
        }
        final long relTimestamp = System.currentTimeMillis()
                - this.captureStart;
        this.serializedEvents.add(new Wrapper<T>(event, relTimestamp));
    }

    /**
     * Initiate the persisting of the captured events using the persister
     * provided when creating the object.
     */
    public void saveEvents() {
        if (this.currentState != State.STOPPED) {
            throw new IllegalStateException(Capture.ERROR_MESSAGE);
        }
        this.persister.accept(this.eventsToJSON());
    }

    /**
     * Get the number of captured events.
     *
     * @return The number of captured events.
     */
    public int getNumberOfCapturedEvents() {
        return this.serializedEvents.size();
    }

    /**
     * Convert the events stored in the map to a single JSONObject for
     * persisting.
     *
     * @return The JSONObject containing all events and their relative
     *         timestamps.
     */
    private JSONObject eventsToJSON() {
        final JSONObject json = new JSONObject();
        this.serializedEvents.forEach(wrap -> json.append(JSONNames.JSON_EVENTS,
                wrap.toJSON(this.serializer)));
        return json;
    }

    /**
     * Definitions of the possible states of a Capture object.
     *
     * @author sjdpeste
     *
     */
    private enum State {
        /**
         * The object has been created but has not yet started the capture.
         */
        CREATED,
        /**
         * The object is in capture mode, it is accepting new events.
         */
        CAPTURING,
        /**
         * The object has stopped accepting new events.
         */
        STOPPED;

    }
}
