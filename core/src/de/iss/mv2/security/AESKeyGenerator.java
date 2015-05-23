package de.iss.mv2.security;

import java.security.SecureRandom;

/**
 * A class for generating random AES-keys.
 * @author Marcel Singer
 *
 */
public class AESKeyGenerator implements SymmetricKeyGenerator{

	/**
	 * A field to hold a fixed key.
	 */
	private byte[] fixedKey;
	/**
	 * A field to hold a fixed iv.
	 */
	private byte[] fixedIV;
	
	/**
	 * Creates a new instance of {@link AESKeyGenerator}.
	 */
	public AESKeyGenerator() {
	}

	
	
	@Override
	public byte[] getRandomKey(int length) {
		if(fixedKey != null && fixedKey.length == length) return fixedKey;
		SecureRandom sr = new SecureRandom();
		byte[] result = new byte[length];
		sr.nextBytes(result);
		return result;
	}

	@Override
	public byte[] getRandomIV() {
		if(fixedIV != null) return fixedIV;
		return getRandomKey(16);
	}

	@Override
	public void setFixedKey(byte[] key) {
		this.fixedKey = key;
	}

	@Override
	public void setFixedIV(byte[] iv) {
		this.fixedIV = iv;
	}

	@Override
	public boolean hasFixedKeyAndIV() {
		return (fixedKey != null && fixedIV != null);
	}



	@Override
	public byte[] getRandomKey() {
		if(fixedKey != null) return fixedKey;
		return getRandomKey(16);
	}



	@Override
	public SymmetricKeyGenerator doClone() {
		AESKeyGenerator cl = new AESKeyGenerator();
		return cl;
	}

}
