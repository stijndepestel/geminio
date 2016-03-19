package com.stijndepestel.thesis.capturereplay;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.json.JSONObject;
import org.junit.Assert;
import org.junit.Test;

/**
 * Unit tests for the Persister class.
 *
 * @author sjdpeste
 *
 */
public class PersisterTest {

    /**
     * Test for illegal state if no output stream is available.
     *
     * @throws IOException
     *             When an IO error occurs during persisting.
     */
    @Test(expected = IllegalStateException.class)
    public void exceptionNoOutputStreamTest() throws IOException {
        new Persister(System.in).persist(new JSONObject());
    }

    /**
     * Test if a JSONObject is provided to the output stream as it should be.
     *
     * @throws IOException
     *             when an IO error occurs during persisting.
     */
    @Test
    public void persistTest() throws IOException {
        final String json = "{\"ping\":\"pong\"}";
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        new Persister(outputStream).persist(new JSONObject(json));
        Assert.assertEquals("JSON strings should be equal", json,
                outputStream.toString());
    }

    /**
     * Test if an inputstream is converted to a JSONObject as it should be.
     */
    @Test
    public void loadTest() {
        final String json = "{\"ping\":\"pong\"}";
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(
                json.getBytes());
        final JSONObject jsonObject = new Persister(inputStream).load();
        Assert.assertEquals("JSON strings should be equal", json,
                jsonObject.toString());
    }

    /**
     * Test for illegal state if no input stream is available.
     */
    @Test(expected = IllegalStateException.class)
    public void exceptionNoInputStreamTest() {
        new Persister(System.out).load();
    }

}
