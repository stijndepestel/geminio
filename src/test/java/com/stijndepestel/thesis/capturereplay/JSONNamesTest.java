package com.stijndepestel.thesis.capturereplay;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit tests for the JSONNames class.
 *
 * @author sjdpeste
 *
 */
public class JSONNamesTest {

    /**
     * Test to see if the JSONNames class cannot be instantiated.
     *
     * @throws NoSuchMethodException
     * @throws SecurityException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    @Test(expected = Exception.class)
    public void jSONNamesNotConstructable() throws NoSuchMethodException,
            SecurityException, InstantiationException, IllegalAccessException,
            IllegalArgumentException, InvocationTargetException {
        final Constructor<JSONNames> constructor = JSONNames.class
                .getDeclaredConstructor();
        Assert.assertTrue("Constructor should be private",
                Modifier.isPrivate(constructor.getModifiers()));
        constructor.setAccessible(true);
        constructor.newInstance();
    }
}
