package com.rio.helper;

public class Sleeper {

	private static final int PAUSE = 400;
	private static final int MINIPAUSE = 200;

	/**
	 * Sleeps the current thread for a default pause length.
	 */

	public static void sleep() {
        sleep(PAUSE);
	}


	/**
	 * Sleeps the current thread for a default mini pause length.
	 */

	public static void sleepMini() {
        sleep(MINIPAUSE);
	}


	/**
	 * Sleeps the current thread for <code>time</code> milliseconds.
	 *
	 * @param time the length of the sleep in milliseconds
	 */

	public static void sleep(int time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException ignored) {}
	}

}
