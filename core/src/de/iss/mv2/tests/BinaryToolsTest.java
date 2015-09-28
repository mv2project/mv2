package de.iss.mv2.tests;

import static org.junit.Assert.*;

import java.util.Random;

import org.junit.Test;

import de.iss.mv2.data.BinaryTools;

/**
 * Provides test cases to test the functionality of {@link BinaryTools}.
 * 
 * @author Marcel Singer
 *
 */
public class BinaryToolsTest {

	/**
	 * Tests the methods {@link BinaryTools#toLongs(byte[])} and
	 * {@link BinaryTools#toByteArray(long...)}.
	 */
	@Test
	public void testGetSetLongValues() {
		Random r = new Random();
		long[] testArr = new long[20];
		for(int i=0; i<testArr.length; i++){
			testArr[i] = r.nextLong();
		}
		byte[] binary = BinaryTools.toByteArray(testArr);
		long[] reverseTestArr = BinaryTools.toLongs(binary);
		assertEquals(testArr.length, reverseTestArr.length);
		for(int i=0; i<testArr.length; i++){
			assertEquals(testArr[i], reverseTestArr[i]);
		}
	}

}
