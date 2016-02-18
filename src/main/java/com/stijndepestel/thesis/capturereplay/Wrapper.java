package com.stijndepestel.thesis.capturereplay;

import java.util.function.Function;

import org.json.JSONObject;

/**
 * Wrapper object for the events (addition of a relative timestamp).
 *
 * @author sjdpeste
 *
 */
public final class Wrapper<T> implements Comparable<Wrapper<T>> {
    /**
     * The wrapped event.
     */
    private final T event;
    /**
     * The relative timestamp.
     */
    private final long relativeTimestamp;

    /**
     * Create a new wrapper given the event and the relativetimestamp.
     *
     * @param event
     *            Event to wrap.
     * @param relativeTimestamp
     *            Relative timestamp of when the event occurred.
     */
    public Wrapper(final T event, final long relativeTimestamp) {
        this.event = event;
        this.relativeTimestamp = relativeTimestamp;
    }

    /**
     * Create new wrapper given the JSON representation of the wrapper and the
     * deserializer of the event.
     *
     * @param json
     *            The JSON representation of a wrapper object.
     * @param deserializer
     *            The deserializer for the event type of this wrapper.
     */
    public Wrapper(final JSONObject json,
            final Function<JSONObject, T> deserializer) {
        this.relativeTimestamp = json.getLong(JSONNames.JSON_REL_TIME);
        this.event = deserializer.apply(json
                .getJSONObject(JSONNames.JSON_EVENT));
    }

    /**
     * Create JSON object of this wrapped event. The JSON consists of the
     * relative timestamp and the event which will be serialized by the provided
     * serializer in the parent Capture object.
     *
     * @return The JSON object containing the relative time and the event.
     */
    public JSONObject toJSON(final Function<T, JSONObject> serializer) {
        final JSONObject json = new JSONObject();
        json.put(JSONNames.JSON_REL_TIME, this.relativeTimestamp);
        json.put(JSONNames.JSON_EVENT, serializer.apply(this.event));
        return json;
    }

    /**
     * Get the event contained in this wrapper.
     *
     * @return The wrapped event.
     */
    public T getEvent() {
        return this.event;
    }

    /**
     * The relative timestamp linked to this event.
     *
     * @return The relative timestamp.
     * @see Capture
     */
    public long getRelativeTimestamp() {
        return this.relativeTimestamp;
    }

    @Override
    public int compareTo(final Wrapper<T> wrapper) {
        return Long.compare(this.getRelativeTimestamp(),
                wrapper.getRelativeTimestamp());
    }
}