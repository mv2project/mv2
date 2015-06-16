package de.iss.mv2.tests;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.util.Random;

/**
 * An input stream that produces an infinite amount of random data.
 * @author Marcel Singer
 *
 */
public class InfiniteInputStream extends InputStream {

	/**
	 * The random number generator.
	 */
	private final Random random = new Random();
	
	/**
	 * The digest to be used.
	 */
	private final MessageDigest messageDigest;
	
	/**
	 * Creates a new instance of {@link InfiniteInputStream}.
	 */
	public InfiniteInputStream() {
		MessageDigest d = null;
		try{
			d = MessageDigest.getInstance("SHA-256");
		}catch(Exception ex){
			
		}
		this.messageDigest = d;
	}

	
	@Override
	public int read() throws IOException {
		byte[] data = new byte[1];
		random.nextBytes(data);
		byte result = data[0];
		if(result < 0) result = (byte) (result * -1);
		data[0] = result;
		messageDigest.update(data, 0, 1);
		return result;
	}
	
	/**
	 * Returns the digest of the created data.
	 * @return The digest of the created data.
	 */
	public byte[] getDigest(){
		if(messageDigest == null) return new byte[0];
		return messageDigest.digest();
	}
	
	/**
	 * Returns the name of the used message digest algorithm.
	 * @return The name of the used message digest algorithm.
	 */
	public String getUsedDigestAlgorithm(){
		return "SHA-256";
	}

}
