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

import java.util.function.Function;

import org.json.JSONObject;

/**
 * Wrapper object for the events (addition of a relative timestamp).
 *
 * @author sjdpeste
 *
 * @param <T>
 *            The type of events to wrap.
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
        this.event = deserializer
                .apply(json.getJSONObject(JSONNames.JSON_EVENT));
    }

    /**
     * Create JSON object of this wrapped event. The JSON consists of the
     * relative timestamp and the event which will be serialized by the provided
     * serializer in the parent Capture object.
     *
     * @param serializer
     *            The serializer for the event that is being wrapped.
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

    @Override
    public boolean equals(final Object obj) {
        if (obj != null && obj.getClass() == this.getClass()) {
            final Wrapper<?> wrapper = (Wrapper<?>) obj;
            return this.relativeTimestamp == wrapper.relativeTimestamp
                    && this.event.equals(wrapper.event);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return 13 * (int) this.relativeTimestamp + 17 * this.event.hashCode();
    }
}
