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
