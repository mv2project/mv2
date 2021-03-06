package de.iss.mv2.data;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides general input/output functionalities.
 * @author Marcel Singer
 *
 */
public class BinaryTools {

	/**
	 * Prevent this class from being instantiated.
	 */
	private BinaryTools() {

	}

	/**
	 * Returns the value of the given number encoded in {@link ByteOrder#BIG_ENDIAN}-format.
	 * @param number The number to convert.
	 * @return A byte array containing the {@link ByteOrder#BIG_ENDIAN}-encoded value.
	 */
	public static byte[] toByteArray(int number) {
		return ByteBuffer.allocate(4).putInt(number).array();
	}
	
	/**
	 * Returns the value of the given number encoded in {@link ByteOrder#BIG_ENDIAN}-format.
	 * @param number The number to convert.
	 * @return A byte array containing the {@link ByteOrder#BIG_ENDIAN}-encoded value.
	 */
	public static byte[] toByteArray(long number){
		return ByteBuffer.allocate(8).putLong(number).array();
	}
	
	/**
	 * Returns an array containing the binary encoded values. See {@link BinaryTools#toByteArray(long)}.
	 * @param numbers The numbers to convert.
	 * @return An array containing the binary encoded values.
	 */
	public static byte[] toByteArray(long... numbers){
		ByteBuffer bb = ByteBuffer.allocate(numbers.length * 8);
		for(int i=0; i<numbers.length; i++){
			bb.put(toByteArray(numbers[i]));
		}
		return bb.array();
	}
	
	/**
	 * Converts the given data to the represented long-values.
	 * @param data The data to convert.
	 * @return The resulting long-Array.
	 */
	public static long[] toLongs(byte[] data){
		long[] result = new long[data.length / 8];
		ByteBuffer bb = ByteBuffer.wrap(data);
		byte[] tmp = new byte[8];
		for(int i=0; i<data.length; i+= 8){
			bb.get(tmp);
			result[i / 8] = toLong(tmp);
		}
		return result;
	}
	
	/**
	 * Returns the long-value of the given {@link ByteOrder#BIG_ENDIAN} byte array.
	 * @param data The byte array to convert.
	 * @return The retrieved long value.
	 */
	public static long toLong(byte[] data){
		return ByteBuffer.wrap(data).getLong();
	}

	/**
	 * Reads the given amount of bytes from the input-stream.
	 * @param in The stream to read.
	 * @param length The length of bytes to read from the input.
	 * @return A byte-array containing the read bytes.
	 * @throws IOException if an I/O error occurs.
	 * @throws EOFException Is thrown when the end of the steam is reached before all bytes are read.
	 */
	public static byte[] readBytes(InputStream in, int length)
			throws IOException, EOFException {
		if(length < 0) throw new IOException("The length must not be negative.");
		int read;
		byte[] result = new byte[length];
		for (int i = 0; i < result.length; i++) {
			read = in.read();
			if (read == -1)
				throw new EOFException();
			result[i] = (byte) read;
		}
		return result;
	}

	/**
	 * Reads a {@link ByteOrder#BIG_ENDIAN} encoded integer value.
	 * @param in The stream to read.
	 * @return The read integer.
	 * @throws IOException if an I/O error occurs. 
	 */
	public static int readInt(InputStream in) throws IOException {
		byte[] data = readBytes(in, 4);
		return ByteBuffer.wrap(data).getInt();
	}

	/**
	 * Reads the given amount of bytes from the given input-stream and wraps them within a new {@link InputStream}.
	 * @param in The input to read.
	 * @param length The amount of bytes to read.
	 * @return A new {@link InputStream} containing the read data.
	 * @throws IOException if an I/O error occurs.
	 */
	public static InputStream readBuffer(InputStream in, int length)
			throws IOException {
		return new ByteArrayInputStream(readBytes(in, length));
	}

	/**
	 * Attempts to read all bytes until the end of the stream is reached.
	 * @param in The input to read.
	 * @return The read data.
	 * @throws IOException if an I/O error occurs.
	 */
	public static byte[] readAll(InputStream in) throws IOException {
		List<Byte> buffer = new ArrayList<Byte>();
		int read;
		while ((read = in.read()) != -1) {
			buffer.add((byte) read);
		}
		byte[] result = new byte[buffer.size()];
		for (int i = 0; i < buffer.size(); i++)
			result[i] = buffer.get(i);
		return result;
	}
	
	/**
	 * Defines the characters used for hex-encoding.
	 */
	private static final char[] hexArray = "0123456789ABCDEF".toCharArray();
	
	/**
	 * Returns the given byte-array as hex-encoded string.
	 * @param bytes The data to encode.
	 * @return The encoded data as string.
	 */
	public static String toHexString(byte[] bytes) {
	    char[] hexChars = new char[bytes.length * 2];
	    for ( int j = 0; j < bytes.length; j++ ) {
	        int v = bytes[j] & 0xFF;
	        hexChars[j * 2] = hexArray[v >>> 4];
	        hexChars[j * 2 + 1] = hexArray[v & 0x0F];
	    }
	    return new String(hexChars);
	}
	
	/**
	 * Returns the decoded data of a hex-string.
	 * @param s The string to decode.
	 * @return The decoded data.
	 */
	public static byte[] fromHexString(String s) {
	    int len = s.length();
	    byte[] data = new byte[len / 2];
	    for (int i = 0; i < len; i += 2) {
	        data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
	                             + Character.digit(s.charAt(i+1), 16));
	    }
	    return data;
	}
	
	/**
	 * Inserts a spacing after every specified amount of characters.
	 * @param txt The text to process.
	 * @param spacing The spacing to insert.
	 * @param blockLength The amount of characters.
	 * @return The processed text.
	 */
	public static String insertSpacing(String txt, String spacing, int blockLength){
		StringBuilder sb = new StringBuilder();
		char[] chars = txt.toCharArray();
		for(int i=0; i<chars.length; i++){
			if(i%blockLength == 0 && i!=0){
				sb.append(spacing);
			}
			sb.append(chars[i]);
		}
		return sb.toString();
	}
	
	/**
	 * Copies the content of a stream to another.
	 * @param source The stream to copy. 
	 * @param target The stream to copy to.
	 * @param closeSource {@code true} if the source stream should be closed afterwards.
	 * @param closeTarget {@code true} if the target stream should be closed afterwards.
	 * @throws IOException if an I/O error occurs.
	 */
	public static void copy(InputStream source, OutputStream target, boolean closeSource, boolean closeTarget) throws IOException{
		int read = -1;
		while((read = source.read()) != -1){
			target.write((byte) read);
		}
		target.flush();
		if(closeSource) source.close();
		if(closeTarget) target.close();
	}
	
}
