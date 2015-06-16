package de.iss.mv2.io;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

import de.iss.mv2.data.BinaryTools;

/**
 * A data source that stores its content inside a byte buffer.
 * 
 * @author Marcel Singer
 *
 */
public class ByteBufferDataSource extends DataSource {
	
	/**
	 * Holds the data.
	 */
	private final ByteBuffer buffer;

	/**
	 * Creates a new {@link ByteBufferDataSource} with the given size.
	 * 
	 * @param size
	 *            The size of the data source.
	 */
	public ByteBufferDataSource(int size) {
		super(size);
		buffer = ByteBuffer.allocate(size);
	}

	@Override
	public byte[] getBytes() throws IOException {
		if(buffer.hasArray()) return buffer.array();
		return BinaryTools.readAll(getStream());
	}

	@Override
	public InputStream getStream() throws IOException {
		return new ByteBufferInputStream(buffer);
	}

	@Override
	public void importData(InputStream in) throws IOException {
		int read = -1;
		buffer.rewind();
		for (int i = 0; i < getLength(); i++) {
			read = in.read();
			if (read == -1)
				throw new EOFException("EOF at: " + i + " / " + getLength());
			buffer.put((byte) read);
		}
	}

	@Override
	public void dispose() throws IOException {

	}

}
