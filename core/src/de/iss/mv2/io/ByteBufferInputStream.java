package de.iss.mv2.io;

import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

/**
 * An InputStream to access the data of a {@link ByteBuffer}.
 * @author Marcel Singer
 *
 */
public class ByteBufferInputStream extends InputStream {

	/**
	 * Holds the buffer to read from.
	 */
	private final ByteBuffer buffer;
	
	/**
	 * Creates a new instance of {@link ByteBufferInputStream} to read from the given buffer.
	 * @param buffer The buffer to read from.
	 */
	public ByteBufferInputStream(ByteBuffer buffer) {
		this.buffer = buffer;
	}

	@Override
	public int read() throws IOException {
		if(buffer.remaining() == 0) return -1;
		return buffer.get();
	}

}
