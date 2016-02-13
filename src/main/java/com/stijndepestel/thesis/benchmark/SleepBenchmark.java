package com.stijndepestel.thesis.benchmark;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Map;
import java.util.TreeMap;

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
//		System.out.println(Arrays.toString(RAW_RESULTS));
		System.out.println(DISTRIBUTION);
	}

	/**
	 * Copied from
	 * http://stackoverflow.com/questions/2808535/round-a-double-to-2
	 * -decimal-places
	 * 
	 * @param value
	 * @param places
	 * @return
	 */
	public static double round(double value, int places) {
		if (places < 0)
			throw new IllegalArgumentException();

		BigDecimal bd = new BigDecimal(value);
		bd = bd.setScale(places, RoundingMode.HALF_UP);
		return bd.doubleValue();
	}

}
