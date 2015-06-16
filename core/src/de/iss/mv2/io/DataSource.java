package de.iss.mv2.io;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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
	
	/**
	 * Exports the content of this data source to the specified stream.
	 * @param out The stream to export to.
	 * @throws IOException If an I/O error occurs.
	 */
	public void export(OutputStream out) throws IOException{
		int read = -1;
		InputStream in = getStream();
		byte[] data = new byte[1];
		for(int i=0; i<getLength(); i++){
			read = in.read();
			if(read == -1) throw new EOFException();
			data[0] = (byte) read;
			out.write(data);
		}
		out.flush();
	}
	
	/**
	 * Returns a DataSource for the specified amount.
	 * @param size The amount of data to store.
	 * @return A DataSource.
	 * @throws IOException If an I/O error occurs.
	 */
	public static DataSource getDataSource(int size) throws IOException{
		if(size <= 100){
			System.out.println("Using ByteArrayDataSource for " + size + " bytes...");
			return new ByteArrayDataSource(size);
		}
		if(size <= 1000000) {
			System.out.println("Using ByteBufferDataSource for " + size + " bytes...");
			return new ByteBufferDataSource(size);
		}
		System.out.println("Using MemoryMappedDataSource for " + size + " bytes...");
		return new MemoryMappedDataSource(size);
	}
	
	

}
