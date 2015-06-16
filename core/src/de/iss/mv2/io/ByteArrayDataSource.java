package de.iss.mv2.io;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;

/**
 * A data source that stores the given data inside a byte array.
 * @author Marcel Singer
 *
 */
public class ByteArrayDataSource extends DataSource{
	
	/**
	 * Holds the data.
	 */
	private final byte[] data;

	/**
	 * Creates a new ByteArrayDataSource.
	 * @param length The length of the data.
	 */
	public ByteArrayDataSource(int length) {
		super(length);
		data = new byte[length];
	}
	
	/**
	 * Creates a new instance of {@link ByteArrayDataSource} with the given data.
	 * @param data The data to store.
	 */
	public ByteArrayDataSource(byte[] data){
		super(data.length);
		this.data = data;
	}

	@Override
	public byte[] getBytes() throws IOException {
		return data;
	}

	@Override
	public InputStream getStream() throws IOException {
		return new ByteArrayInputStream(data);
	}

	@Override
	public void importData(InputStream in) throws IOException {
		int read = -1;
		for(int i=0; i<data.length; i++){
			read = in.read();
			if(read == -1) throw new EOFException();
			data[i] = (byte) read;
		}
	}

	@Override
	public void dispose() throws IOException {
		
	}

}
