package com.stijndepestel.thesis.capturereplay;

import org.junit.Assert;
import org.junit.Test;

/**
 * Unit tests for the Wrapper class.
 *
 * @author sjdpeste
 *
 */
public class WrapperTest {

    /**
     * First test event object.
     */
    private final TestEvent testEvent1 = new TestEvent(0, 0);
    /**
     * Second test event object.
     */
    private final TestEvent testEvent2 = new TestEvent(0, 0);
    /**
     * Third test event object.
     */
    private final TestEvent testEvent3 = new TestEvent(1, 0);
    /**
     * Fourth test event object.
     */
    private final TestEvent testEvent4 = new TestEvent(0, 1);
    /**
     * Fifth test event object.
     */
    private final TestEvent testEvent5 = new TestEvent(1, 1);

    /**
     * Test if objects are equal.
     */
    @Test
    public void objectsAreEqualTest() {
        Assert.assertTrue("Event 1 and event 1 are equal",
                this.testEvent1.equals(this.testEvent1));
        Assert.assertTrue("Event 1 and event 2 are equal",
                this.testEvent1.equals(this.testEvent2));
        Assert.assertTrue("Event 2 and event 1 are equal",
                this.testEvent2.equals(this.testEvent1));

        // test if Wrapper objects are equal
        final Wrapper<TestEvent> wrapper1 = new Wrapper<TestEvent>(
                this.testEvent1, this.testEvent1.getTimestamp());
        final Wrapper<TestEvent> wrapper2 = new Wrapper<TestEvent>(
                this.testEvent1, this.testEvent1.getTimestamp());
        final Wrapper<TestEvent> wrapper3 = new Wrapper<TestEvent>(
                this.testEvent1, this.testEvent1.getTimestamp() + 1);
        Assert.assertTrue("Wrapper 1 and wrapper 1 are equal",
                wrapper1.equals(wrapper2));
        Assert.assertFalse("Wrapper 1 and wrapper 3 are not equal",
                wrapper1.equals(wrapper3));
    }

    /**
     * Test if objects are not equal.
     */
    @Test
    public void objectsAreNotEqualTest() {
        Assert.assertFalse("Event 1 and event 3 are not equal",
                this.testEvent1.equals(this.testEvent3));
        Assert.assertFalse("Event 1 and event 4 are not equal",
                this.testEvent1.equals(this.testEvent4));
        Assert.assertFalse("Event 1 and event 5 are not equal",
                this.testEvent1.equals(this.testEvent5));
    }

    /**
     * Test object is not equal to null.
     */
    @Test
    public void objectIsNullTest() {
        Assert.assertFalse("Event and null are not equal",
                this.testEvent1.equals(null));
    }
}
