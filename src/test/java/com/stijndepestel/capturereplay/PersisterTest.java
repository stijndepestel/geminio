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
     * Combined persist and load test.
     *
     * @throws IOException
     *             When an IO error occurs during persisting.
     */
    @Test
    public void combinedTest() throws IOException {
        final String json = "{\"ping\":\"pong\"}";
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(
                json.getBytes());
        final Persister persister = new Persister(outputStream, inputStream);
        persister.persist(new JSONObject(json));
        Assert.assertEquals("JSON strings should be equal", json,
                outputStream.toString());
        final JSONObject jsonObject = persister.load();
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
