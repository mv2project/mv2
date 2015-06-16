package de.iss.mv2.data;

import java.io.EOFException;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel.MapMode;

import de.iss.mv2.io.ByteBufferInputStream;
import de.iss.mv2.io.DataSource;

/**
 * A data source that uses memory mapped buffers.
 * 
 * @author Marcel Singer
 *
 */
public class MemoryMappedDataSource extends DataSource {

	/**
	 * The buffer containing the data.
	 */
	private final MappedByteBuffer buffer;
	/**
	 * The memory mapped file.
	 */
	private final RandomAccessFile file;

	/**
	 * The file inside the file system.
	 */
	private final File mappedFile;

	/**
	 * Creates a new instance of {@link MemoryMappedDataSource}.
	 * 
	 * @param length
	 *            The length (in bytes) of the data to be stored.
	 * @throws IOException
	 *             If an I/O error occurs.
	 */
	public MemoryMappedDataSource(int length) throws IOException {
		super(length);
		mappedFile = File.createTempFile("mappedMemory", null);
		mappedFile.deleteOnExit();
		file = new RandomAccessFile(mappedFile, "rw");
		buffer = file.getChannel().map(MapMode.READ_WRITE, 0, length);

	}
	
	/**
	 * Returns the mapped file inside the file system.
	 * @return The mapped file inside the file system.
	 */
	public File getMappedFile(){
		return mappedFile;
	}

	@Override
	public byte[] getBytes() throws IOException {
		return buffer.array();
	}

	@Override
	public InputStream getStream() throws IOException {
		buffer.position(0);
		return new ByteBufferInputStream(buffer);
	}

	@Override
	public void importData(InputStream in) throws IOException {
		buffer.position(0);
		int read = -1;
		int length = getLength();
		for (int i = 0; i < length; i++) {
			read = in.read();
			if (read == -1)
				throw new EOFException();
			buffer.put((byte) read);
		}
	}

	@Override
	public void dispose() throws IOException {
		file.close();
		if (mappedFile.exists())
			mappedFile.delete();
	}

}
