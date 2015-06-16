package de.iss.mv2.io;

import java.io.IOException;
import java.io.InputStream;

/**
 * A class to manage variable data amounts.
 * @author Marcel Singer
 *
 */
public abstract class DataSource {

	/**
	 * Stores the length of the contained data.
	 */
	private int length;

	
	/**
	 * Creates a new DataSource.
	 * @param length The length of the contained data in bytes.
	 */
	protected DataSource(int length){
		this.length = length;
	}
	
	/**
	 * Returns the length of the contained data.
	 * @return The length in bytes.
	 */
	public final int getLength(){
		return length;
	}
	
	/**
	 * Returns the binary content as a binary array.
	 * @return The content as a binary array.
	 * @throws IOException If an I/O error occurs.
	 */
	public abstract byte[] getBytes() throws IOException;
	
	/**
	 * Returns the binary content as an input stream.
	 * @return The binary content as an input stream.
	 * @throws IOException If an I/O error occurs.
	 */
	public abstract InputStream getStream() throws IOException;
	
	
	/**
	 * Sets the length of the contained data.
	 * @param length The length in bytes to set.
	 */
	protected final void setLength(int length){
		this.length = length;
	}
	
	
	/**
	 * Imports the data from the given stream.
	 * @param in The input stream to read.
	 * @throws IOException If an I/O error occurs.
	 */
	public abstract void importData(InputStream in) throws IOException;
	
	/**
	 * Disposes this data source and releases all associated memory.
	 * @throws IOException Is thrown if an I/O error occurs.
	 */
	public abstract void dispose() throws IOException;

	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		dispose();
	}

}
