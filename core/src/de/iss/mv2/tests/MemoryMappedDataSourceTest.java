package de.iss.mv2.tests;

import static org.junit.Assert.*;

import java.io.File;
import java.io.InputStream;
import java.security.MessageDigest;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import de.iss.mv2.io.MemoryMappedDataSource;

/**
 * A test for the {@link MemoryMappedDataSource}-class.
 * 
 * @author Marcel Singer
 *
 */
public class MemoryMappedDataSourceTest {

	/**
	 * The size of the data source to create.
	 */
	private static final int TEST_SIZE = 5000;

	/**
	 * Holds the data source.
	 */
	private MemoryMappedDataSource ds;
	
	/**
	 * Holds the datas hash.
	 */
	private byte[] hash;

	/**
	 * Sets up the test.
	 * 
	 * @throws Exception
	 *             If anything goes wrong.
	 */
	@Before
	public void setup() throws Exception {
		ds = new MemoryMappedDataSource(TEST_SIZE);
		InfiniteInputStream iis = new InfiniteInputStream();
		ds.importData(iis);
		hash = iis.getDigest();
		assertTrue(ds.getMappedFile().exists());
		assertTrue("The hash value of the data is empty!", hash.length != 0);
	}

	/**
	 * Runs the test.
	 * 
	 * @throws Exception
	 *             If anything goes wrong.
	 */
	@Test
	public void testReadComplete() throws Exception {
		MessageDigest md =MessageDigest.getInstance("SHA-256");
		InputStream in = ds.getStream();
		for(int i=0; i<ds.getLength(); i++){
			md.update((byte)in.read()); 
		}
		byte[] hash = md.digest();
		assertArrayEquals(this.hash, hash);
	}

	/**
	 * 
	 * @throws Exception
	 */
	@After
	public void close() throws Exception {
		File f = ds.getMappedFile();
		ds.dispose();
		assertFalse(f.exists());
	}

}
