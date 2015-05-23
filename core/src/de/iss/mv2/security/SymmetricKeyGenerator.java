package de.iss.mv2.security;

/**
 * Used to generate symmetric keys for the encrypted communication.
 * {@link AESKeyGenerator} is the default implementation of this interface.
 * @author Marcel Singer
 *
 */
public interface SymmetricKeyGenerator extends Cloneable<SymmetricKeyGenerator> {

	/**
	 * Returns a random key using the default size.<br />
	 * <b>Note:</b> Once a fixed key or iv is set the result of this method should be the fixed one.
	 * @return The generated or set random key.
	 */
	public byte[] getRandomKey();
	/**
	 * Creates and returns a random key with the given strength.
	 * @param length The length of the key to create in bytes.
	 * @return The generated random key.
	 */
	public byte[] getRandomKey(int length);
	
	/**
	 * Returns a random IV using the default size.<br />
	 * <b>Note:</b> Once a fixed key or iv is set the result of this method should be the fixed IV.
	 * @return The generated or set IV.
	 */
	public byte[] getRandomIV();
	
	/**
	 * Sets the fixed key to return for all subsequent calls of {@link SymmetricKeyGenerator#getRandomKey()}.
	 * @param key The key to set.
	 */
	public void setFixedKey(byte[] key);
	
	/**
	 * Sets the fixed IV to return for all subsequent calls of {@link SymmetricKeyGenerator#getRandomIV()}.
	 * @param iv
	 */
	public void setFixedIV(byte[] iv);
	
	/**
	 * Returns {@code true} if the key and IV is fixed.
	 * @return {@code true} if the key and IV is fixed.
	 */
	public boolean hasFixedKeyAndIV();
	
	
}
