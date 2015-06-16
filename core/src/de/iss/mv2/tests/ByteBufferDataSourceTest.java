package de.iss.mv2.tests;

import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Random;

import org.junit.Test;

import de.iss.mv2.io.ByteBufferDataSource;

/**
 * A test for the {@link ByteBufferDataSource}-class.
 * @author Marcel Singer
 *
 */
public class ByteBufferDataSourceTest {

	
	/**
	 * Performs the test.
	 * @throws Exception If anything goes wrong.
	 */
	@Test
	public void test() throws Exception {
		ByteBufferDataSource bbds = new ByteBufferDataSource(370);
		byte[] data = new byte[370];
		Random rnd = new Random();
		rnd.nextBytes(data);
		bbds.importData(new ByteArrayInputStream(data));
		byte[] arr = bbds.getBytes();
		assertArrayEquals(data, arr);
	}

}
