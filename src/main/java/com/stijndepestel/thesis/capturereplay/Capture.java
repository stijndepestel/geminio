package com.stijndepestel.thesis.capturereplay;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * Defines the methods to capture and store events.
 * 
 * @author sjdpeste
 * 
 * @param <T>
 *            The type of events to capture.
 */
public class Capture<T> {

	/**
	 * List containing the serialized events that are not yet persisted.
	 */
	private final List<String> serializedEvents;

	/**
	 * Create new capture object.
	 */
	public Capture() {
		serializedEvents = new ArrayList<>();
	}

	/**
	 * Capture event.
	 * @param serializer The serializer to convert the event.
	 * @param event The event to capture.
	 */
	public void capture(Function<T, String> serializer, T event) {
		serializedEvents.add(serializer.apply(event));
	}

	/**
	 * Get all serialized events that have not been persisted.
	 * 
	 * @return the serializedEvents.
	 */
	public List<String> getSerializedEvents() {
		return serializedEvents;
	}

}
