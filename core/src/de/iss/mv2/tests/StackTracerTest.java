package de.iss.mv2.tests;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

import de.iss.mv2.logging.StackTracer;

/**
 * A text for the {@link StackTracer}.
 * 
 * @author Marcel Singer
 *
 */
public class StackTracerTest {

	/**
	 * Tests the stack tracer.
	 */
	@Test
	public void testStackTracer() {
		StackTraceElement[] stackTrace = StackTracer.getStackTrace();
		assertTrue(stackTrace.length > 0);
		assertTrue(stackTrace[0].getClassName().equals(getClass().getName()));
		assertTrue(stackTrace[0].getMethodName().equals("testStackTracer"));
	}

}
