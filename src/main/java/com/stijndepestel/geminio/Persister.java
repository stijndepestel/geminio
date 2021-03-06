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
package com.stijndepestel.geminio;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Optional;
import java.util.Scanner;

import org.json.JSONObject;
import org.slf4j.LoggerFactory;

/**
 * Provides functionality for persisting a JSON object.
 *
 * @author sjdpeste
 *
 */
public final class Persister {

    /**
     * The output stream to write the JSON to.
     */
    private final Optional<OutputStream> outputStream;

    /**
     * The input stream to read the JSON from.
     */
    private final Optional<InputStream> inputStream;

    /**
     * Constructs a Persister which will write the JSON to the given output
     * stream.
     *
     * @param outputStream
     *            The OutputStream to write to.
     */
    public Persister(final OutputStream outputStream) {
        this.outputStream = Optional.of(outputStream);
        this.inputStream = Optional.empty();
    }

    /**
     * Constructs a Persister which will read the JSON from the given input
     * stream.
     *
     * @param inputStream
     *            The input stream to read from.
     */
    public Persister(final InputStream inputStream) {
        this.inputStream = Optional.of(inputStream);
        this.outputStream = Optional.empty();
    }

    /**
     * Constructs a Persister which will read from or write to the given
     * streams.
     *
     * @param outputStream
     *            The output stream to write to.
     * @param inputStream
     *            The input stream to read from.
     */
    public Persister(final OutputStream outputStream,
            final InputStream inputStream) {
        this.outputStream = Optional.of(outputStream);
        this.inputStream = Optional.of(inputStream);

    }

    /**
     * The function that can act as a Consumer to Capture.
     *
     * @param json
     *            The json to persist.
     */
    public void persist(final JSONObject json) {
        if (!this.outputStream.isPresent()) {
            throw new IllegalStateException("Output stream not present.");
        }
        try (final BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(this.outputStream.get(), "UTF8"))) {
            writer.append(json.toString());
            writer.flush();
        } catch (final IOException e) {
            LoggerFactory.getLogger(Persister.class.getName())
                    .error("IO Exception during persisting.", e);
        }
    }

    /**
     * The function that can act as a Supplier to Replay.
     *
     * @return The loaded json.
     */
    public JSONObject load() {
        if (!this.inputStream.isPresent()) {
            throw new IllegalStateException("Input stream not present.");
        }
        try (final Scanner scanner = new Scanner(this.inputStream.get(),
                "UTF8")) {
            final StringBuilder sb = new StringBuilder(1024);
            while (scanner.hasNextLine()) {
                sb.append(scanner.nextLine());
            }
            return new JSONObject(sb.toString());
        }
    }
}
