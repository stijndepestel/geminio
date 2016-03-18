package com.stijndepestel.thesis.capturereplay;

import org.json.JSONObject;

/**
 * Serializer and deserializer for the TestEvent.
 * 
 * @author sjdpeste
 *
 */
public final class TestHelper {

    private static final String JSON_NAME_TIME = "time";
    private static final String JSON_NAME_RANDOM = "random";

    /**
     * Method to serialize test events.
     *
     * @param event
     *            The testevent to serialize.
     * @return The serialized event.
     */
    public static JSONObject serialize(final TestEvent event) {
        return new JSONObject().put(TestHelper.JSON_NAME_TIME,
                event.getTimestamp()).put(TestHelper.JSON_NAME_RANDOM,
                event.getRandom());
    }

    /**
     * Method to deserialize test events.
     *
     * @param json
     *            The json to deserialize.
     * @return The deserialized event.
     */
    public static TestEvent deserialize(final JSONObject json) {
        return new TestEvent(json.getLong(TestHelper.JSON_NAME_TIME),
                json.getLong(TestHelper.JSON_NAME_RANDOM));
    }
}
