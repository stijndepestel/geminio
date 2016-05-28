package com.stijndepestel.benchmark;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.LoggerFactory;

/**
 * Benchmark to check the error range of the Thread.sleep() method.
 *
 * @author sjdpeste
 *
 */
public final class SleepBenchmark {

    /**
     * The sleep interval in ms.
     */
    private static final long SLEEP_INTERVAL = 100;

    /**
     * Number of times to run the loop.
     */
    private static final int NUMBER_OF_LOOPS = 10000;

    /**
     * Number to use for calculating the conversion from nano to milli.
     */
    private static final double CONVERSION_CALC = 1000000D;

    /**
     * The number of decimals to round to.
     */
    private static final int ROUND_DECIMALS = 2;

    /**
     * Array to store the raw results.
     */
    private final double[] rawResults;

    /**
     * Distribution of the results.
     */
    private final Map<Double, Integer> distribution;

    /**
     * Default constructor.
     */
    private SleepBenchmark() {
        this.distribution = new ConcurrentHashMap<>();
        this.rawResults = new double[SleepBenchmark.NUMBER_OF_LOOPS];
    }

    /**
     * Execute the benchmark.
     *
     */
    public void execute() {
        for (int i = 0; i < SleepBenchmark.NUMBER_OF_LOOPS; i++) {
            final long before = System.nanoTime();
            try {
                Thread.sleep(SleepBenchmark.SLEEP_INTERVAL);
            } catch (final InterruptedException e) {
                // Ignore interrupted exception.
            }
            // SLEEP_INTERVAl is in ms and all other in nanoseconds.
            // Store result in ms rounded at two decimals.
            final double result = this.round(
                    (System.nanoTime() - before - SleepBenchmark.SLEEP_INTERVAL
                            * SleepBenchmark.CONVERSION_CALC)
                            / SleepBenchmark.CONVERSION_CALC,
                    SleepBenchmark.ROUND_DECIMALS);
            this.rawResults[i] = result;
            if (this.distribution.containsKey(result)) {
                this.distribution
                        .put(result, this.distribution.get(result) + 1);
            } else {
                this.distribution.put(result, 1);
            }
        }
        // Outpur the distribution to the logger.
        LoggerFactory.getLogger(SleepBenchmark.class).info("{}",
                this.distribution);
    }

    /**
     * Round the double value to a certain number of decimals.
     *
     * @param value
     *            The value to round.
     * @param decimals
     *            The number of decimal places to round to.
     * @return The rounded value.
     */
    public double round(final double value, final int decimals) {
        if (decimals < 0) {
            throw new IllegalArgumentException(
                    "Number of decimal places should be bigger than 0.");
        }
        BigDecimal bd = BigDecimal.valueOf(value);
        bd = bd.setScale(decimals, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    /**
     * Execute the benchmark.
     *
     * @param args
     *            Ignored.
     */
    public static void main(final String... args) {
        new SleepBenchmark().execute();
    }

}
