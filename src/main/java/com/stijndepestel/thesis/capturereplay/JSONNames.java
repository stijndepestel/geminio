package com.stijndepestel.thesis.capturereplay;

/**
 * Definitions of the keys for the persisted JSON.
 * 
 * @author sjdpeste
 *
 */
public final class JSONNames {

	/**
	 * JSON key for the attribute containing the list of events.
	 */
	public static final String JSON_EVENTS = "events";
	/**
	 * JSON key for a single event.
	 */
	public static final String JSON_EVENT = "event";
	/**
	 * JSON key for the relative time for an event.
	 */
	public static final String JSON_REL_TIME = "relative_time";

	/**
	 * Default private constructor.
	 * 
	 * @throws InstantiationError
	 *             Class cannot be instantiated.
	 */
	private JSONNames() {
		throw new InstantiationError("Class cannot be instantiated.");
	}

}
