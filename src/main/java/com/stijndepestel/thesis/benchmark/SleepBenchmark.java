package com.stijndepestel.thesis.benchmark;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Map;
import java.util.TreeMap;

/**
 * Benchmark to check the error range of the Thread.sleep() method.
 * 
 * @author sjdpeste
 *
 */
public class SleepBenchmark {

	/**
	 * The sleep interval in ms.
	 */
	private static final long SLEEP_INTERVAL = 100;

	/**
	 * Number of times to run the loop.
	 */
	private static final int NUMBER_OF_LOOPS = 10000;

	/**
	 * Array to store the raw results.
	 */
	private static final double[] RAW_RESULTS = new double[NUMBER_OF_LOOPS];

	/**
	 * Distribution of the results.
	 */
	private static final Map<Double, Integer> DISTRIBUTION = new TreeMap<>();

	/**
	 * Execute the benchmark.
	 * @param args Ignored.
	 * @throws InterruptedException On Thread.sleep() problem.
	 */
	public static void main(String... args) throws InterruptedException {
		for (int i = 0; i < NUMBER_OF_LOOPS; i++) {
			long before = System.nanoTime();
			Thread.sleep(SLEEP_INTERVAL);
			// SLEEP_INTERVAl is in ms and all other in nanoseconds.
			// Store result in ms rounded at two decimals.
			double result = round(
					((double) (System.nanoTime() - before) - (SLEEP_INTERVAL * 1000000)) / 1000000D,
					2);
			RAW_RESULTS[i] = result;
			if (DISTRIBUTION.containsKey(result)) {
				DISTRIBUTION.put(result, DISTRIBUTION.get(result) + 1);
			} else {
				DISTRIBUTION.put(result, 1);
			}
		}
		// System.out.println(Arrays.toString(RAW_RESULTS));
		System.out.println(DISTRIBUTION);
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
	public static double round(double value, int decimals) {
		if (decimals < 0) {
			throw new IllegalArgumentException(
					"Number of decimal places should be bigger than 0.");
		}
		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(decimals, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

}
