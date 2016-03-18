package com.stijndepestel.thesis.capturereplay;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.Optional;
import java.util.Scanner;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides functionality for persisting a JSON object.
 *
 * @author sjdpeste
 *
 */
public final class Persister {

    /**
     * Logger for the class.
     */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(Persister.class);

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
        } catch (final IOException exception) {
            Persister.LOGGER.error(
                    "Something went wrong when trying to persist the json.",
                    exception);
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
        try (final Scanner scanner = new Scanner(this.inputStream.get(), "UTF8")) {
            final StringBuilder sb = new StringBuilder(1024);
            while (scanner.hasNextLine()) {
                sb.append(scanner.nextLine());
            }
            return new JSONObject(sb.toString());
        }
    }
}
