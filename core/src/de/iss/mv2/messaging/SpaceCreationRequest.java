package de.iss.mv2.messaging;

import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Base64.Decoder;

/**
 * The request to create a user space.
 * 
 * @author Marcel Singer
 *
 */
public class SpaceCreationRequest extends MV2Message {

	/**
	 * Creates a new instance of {@link SpaceCreationRequest}.
	 */
	public SpaceCreationRequest() {
		super(STD_MESSAGE.SPACE_CREATION_REQUEST);
	}

	/**
	 * Returns the identifier of the message space to create.
	 * 
	 * @return The identifier of the message space to create.
	 */
	public String getSpaceIdentifier() {
		return getFieldValue(DEF_MESSAGE_FIELD.SPACE_IDENTIFIER, "");
	}

	/**
	 * Sets the identifier of the message space to create.
	 * 
	 * @param spaceIdentifier
	 *            The identifier to set.
	 * @throws IllegalArgumentException
	 *             Is thrown if the given identifier is {@code null} or empty.
	 */
	public void setSpaceIdentifier(String spaceIdentifier)
			throws IllegalArgumentException {
		if (spaceIdentifier == null || spaceIdentifier.isEmpty())
			throw new IllegalArgumentException(
					"The identifier may not be null or empty.");
		setMessageField(new MessageField(DEF_MESSAGE_FIELD.SPACE_IDENTIFIER,
				spaceIdentifier), true);
	}

	/**
	 * Returns the state of the requesting instance.
	 * 
	 * @return The state of the requesting instance.
	 */
	public String getState() {
		return getFieldValue(DEF_MESSAGE_FIELD.STATE, "");
	}

	/**
	 * Sets the state of the requesting instance.
	 * 
	 * @param state
	 *            The state to set.
	 * @throws IllegalArgumentException
	 *             If the given state is {@code null}.
	 */
	public void setState(String state) throws IllegalArgumentException {
		if (state == null)
			throw new IllegalArgumentException("The state may not be null.");
		setMessageField(new MessageField(DEF_MESSAGE_FIELD.STATE, state), true);
	}

	/**
	 * Returns the location of the requesting instance.
	 * 
	 * @return The location of the requesting instance.
	 */
	public String getLocation() {
		return getFieldValue(DEF_MESSAGE_FIELD.LOCATION, "");
	}

	/**
	 * Sets the location of the requesting instance.
	 * 
	 * @param location
	 *            The location to set.
	 * @throws IllegalArgumentException
	 *             Is thrown if the given location is {@code null}.
	 */
	public void setLocation(String location) throws IllegalArgumentException {
		if (location == null)
			throw new IllegalArgumentException("The location may not be null.");
		setMessageField(new MessageField(DEF_MESSAGE_FIELD.LOCATION, location),
				true);
	}

	/**
	 * Returns the country code of the requesting instance.
	 * 
	 * @return The country code of the requesting instance.
	 */
	public String getCountry() {
		return getFieldValue(DEF_MESSAGE_FIELD.COUNTRY, "");
	}

	/**
	 * Sets the county code of the requesting instance.
	 * 
	 * @param country
	 *            The country code to set.
	 * @throws IllegalArgumentException
	 *             Is thrown if the given country code is {@code null} or it's
	 *             length is not zero or two.
	 */
	public void setCountry(String country) throws IllegalArgumentException {
		if (country == null || (country.length() != 0 && country.length() != 2))
			throw new IllegalArgumentException(
					"The country code may not be null and must contain zero or two characters.");
		setMessageField(new MessageField(DEF_MESSAGE_FIELD.COUNTRY, country),
				true);
	}

	/**
	 * Returns the organization of the requesting instance.
	 * 
	 * @return The organization of the requesting instance.
	 */
	public String getOrganization() {
		return getFieldValue(DEF_MESSAGE_FIELD.ORGANIZATION, "");
	}

	/**
	 * Sets the organization of the requesting instance.
	 * 
	 * @param organization
	 *            The organization to set.
	 * @throws IllegalArgumentException
	 *             Is thrown if the given organization is {@code null}.
	 */
	public void setOrganization(String organization)
			throws IllegalArgumentException {
		if (organization == null)
			throw new IllegalArgumentException(
					"The organization may not be null.");
		setMessageField(new MessageField(DEF_MESSAGE_FIELD.ORGANIZATION,
				organization), true);
	}

	/**
	 * Returns the organization unit of the requesting instance.
	 * 
	 * @return The organization unit of the requesting instance.
	 */
	public String getOrganizationUnit() {
		return getFieldValue(DEF_MESSAGE_FIELD.ORGANIZATION_UNIT, "");
	}

	/**
	 * Sets the organization unit of the requesting instance.
	 * 
	 * @param organizationUnit
	 *            The organization unit to set.
	 * @throws IllegalArgumentException
	 *             Is thrown if the given organization unit is {@code null}.
	 */
	public void setOrganizationUnit(String organizationUnit)
			throws IllegalArgumentException {
		if (organizationUnit == null)
			throw new IllegalArgumentException(
					"The organization unit may not be null");
		setMessageField(new MessageField(DEF_MESSAGE_FIELD.ORGANIZATION_UNIT,
				organizationUnit), true);
	}

	/**
	 * Returns the algorithm name of the public key.
	 * 
	 * @return The algorithm name of the public key.
	 */
	public String getKeyAlgorithmName() {
		return getFieldValue(DEF_MESSAGE_FIELD.ASYMETRIC_ALGORITHM, "RSA");
	}

	/**
	 * Sets the algorithm name of the public key.
	 * 
	 * @param algorithm
	 *            The algorithm name to set.
	 * @throws IllegalArgumentException
	 *             If the given algorithm name is {@code null} or empty.
	 */
	public void setKeyAlgorithmName(String algorithm)
			throws IllegalArgumentException {
		if (algorithm == null || algorithm.isEmpty())
			throw new IllegalArgumentException(
					"The algorithm name may not be null or empty.");
		setMessageField(new MessageField(DEF_MESSAGE_FIELD.ASYMETRIC_ALGORITHM,
				algorithm), true);
	}

	/**
	 * Returns the public key of the requesting instance.
	 * 
	 * @return The public key of the requesting instance.
	 * @throws InvalidKeySpecException
	 *             Is thrown if the public key is invalid and can not be
	 *             retrieved from its encoding.
	 * @throws NoSuchAlgorithmException
	 *             Is thrown if the algorithm of the public key was not found.
	 */
	public PublicKey getPublicKey() throws InvalidKeySpecException,
			NoSuchAlgorithmException {
		String keyS = getFieldValue(DEF_MESSAGE_FIELD.ENCRYPTION_KEY, "");
		if (keyS == null)
			return null;
		Decoder dec = Base64.getDecoder();
		byte[] data = dec.decode(keyS);
		return KeyFactory.getInstance(getKeyAlgorithmName()).generatePublic(
				new X509EncodedKeySpec(data));
	}

	/**
	 * Sets the public key of the requesting instance. The key algorithm is
	 * adjusted according to the algorithm of the given key.
	 * 
	 * @param publicKey
	 *            The key to set.
	 * @throws IllegalArgumentException
	 *             Is thrown if the given key is {@code null}.
	 */
	public void setPublicKey(PublicKey publicKey)
			throws IllegalArgumentException {
		if (publicKey == null)
			throw new IllegalArgumentException(
					"The public key may not be null.");
		setKeyAlgorithmName(publicKey.getAlgorithm());
		byte[] keyData = publicKey.getEncoded();
		String keyS = Base64.getEncoder().encodeToString(keyData);
		setMessageField(
				new MessageField(DEF_MESSAGE_FIELD.ENCRYPTION_KEY, keyS), true);
	}

}
