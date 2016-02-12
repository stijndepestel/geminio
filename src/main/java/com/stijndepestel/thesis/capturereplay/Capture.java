package com.stijndepestel.thesis.capturereplay;

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
	 * List containing the serialized events that are not yet persisted.
	 */
	private final List<Wrapper> serializedEvents;

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
	private Function<T, JSONObject> serializer;

	/**
	 * Consumer to persist the captured events.
	 */
	private Consumer<JSONObject> persister;

	/**
	 * Create new capture object.
	 * 
	 * @param serializer
	 *            Function to serialize the event to a JSON object.
	 * @param persister
	 *            Consumer to persist the created JSON object.
	 */
	public Capture(Function<T, JSONObject> serializer,
			Consumer<JSONObject> persister) {
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
		this.currentState = State.CAPTURING; 
		this.captureStart = System.nanoTime();
	}

	/**
	 * Stop the capture.
	 * 
	 * @throws IllegalStateException
	 *             When this is called before the capture was started.
	 */
	public void stopCapture() {
		if (this.currentState != State.CAPTURING) {
			throw new IllegalStateException("Object is not in capture mode.");
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
	public void capture(T event) {
		if (this.currentState != State.CAPTURING) {
			throw new IllegalStateException("Object is not in capture mode.");
		}
		final long relTimestamp = System.nanoTime() - this.captureStart;
		this.serializedEvents.add(new Wrapper(event, relTimestamp));
	}

	/**
	 * Initiate the persisting of the captured events using the persister
	 * provided when creating the object.
	 */
	public void saveEvents() {
		if (this.currentState != State.STOPPED) {
			throw new IllegalStateException(
					"Has the object stopped the capture?");
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
		JSONObject json = new JSONObject();
		this.serializedEvents.forEach(wrap -> json.append(JSONNames.JSON_EVENTS,
				wrap.toJSON()));
		return json;
	}

	/**
	 * Wrapper object for storing the events.
	 * 
	 * @author sjdpeste
	 *
	 */
	private final class Wrapper {
		/**
		 * The wrapped event.
		 */
		private T event;
		/**
		 * The relative timestamp.
		 */
		private long relativeTimestamp;

		/**
		 * Create a new wrapper.
		 * 
		 * @param event
		 *            Event to wrap.
		 * @param relativeTimestamp
		 *            Relative timestamp of when the event occurred.
		 */
		private Wrapper(T event, long relativeTimestamp) {
			super();
			this.event = event;
			this.relativeTimestamp = relativeTimestamp;
		}

		/**
		 * Create JSON object of this wrapped event. The JSON consists of the
		 * relative timestamp and the event which will be serialized by the
		 * provided serializer in the parent Capture object.
		 * 
		 * @return The JSON object containing the relative time and the event.
		 */
		private JSONObject toJSON() {
			JSONObject json = new JSONObject();
			json.put(JSONNames.JSON_REL_TIME, this.relativeTimestamp);
			json.put(JSONNames.JSON_EVENT, Capture.this.serializer.apply(this.event));
			return json;
		}

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
