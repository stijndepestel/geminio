package com.stijndepestel.thesis.capturereplay;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Optional;
import java.util.Scanner;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Provides functionality for persisting a JSON object to a file.
 *
 * @author sjdpeste
 *
 */
public final class FilePersister {

    /**
     * Logger for the class.
     */
    private static final Logger LOGGER = LoggerFactory
            .getLogger(FilePersister.class);

    /**
     * The file to write the JSON to.
     */
    private final Optional<File> file;

    /**
     * Constructs a FilePersister which will write the JSON to the given file.
     *
     * @param file
     *            The file to write to.
     */
    public FilePersister(final File file) {
        this.file = Optional.of(file);
    }

    /**
     * The function that can act as a Consumer to Capture.
     *
     * @param json
     *            The json to persist.
     */
    public void persist(final JSONObject json) {
        if (!(this.file.isPresent() && this.file.get().isFile())) {
            throw new IllegalStateException(
                    "No valid file was provided when creating the FilePersister");
        }
        try (final BufferedWriter writer = new BufferedWriter(
                new OutputStreamWriter(new FileOutputStream(this.file.get()),
                        "UTF8"))) {
            writer.append(json.toString());
            writer.flush();
        } catch (final IOException exception) {
            FilePersister.LOGGER.error(
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
        if (!(this.file.isPresent() && this.file.get().isFile() && this.file
                .get().exists())) {
            throw new IllegalStateException(
                    "No valid file was provided when creating the FilePersister");
        }
        try (final Scanner scanner = new Scanner(this.file.get(), "UTF8")) {
            final StringBuilder sb = new StringBuilder(1024);
            while (scanner.hasNextLine()) {
                sb.append(scanner.nextLine());
            }
            return new JSONObject(sb.toString());
        } catch (final FileNotFoundException exception) {
            FilePersister.LOGGER.error(
                    "Something went wrong when trying to persist the json.",
                    exception);
            // return empty JSON
            return new JSONObject();
        }
    }
}
