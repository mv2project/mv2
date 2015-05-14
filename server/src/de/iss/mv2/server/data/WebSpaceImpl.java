package de.iss.mv2.server.data;

import de.iss.mv2.data.Certificate;

/**
 * A default implementation of {@link WebSpace}.
 * 
 * @author Marcel Singer
 *
 */
public class WebSpaceImpl implements WebSpace {

	/**
	 * Holds the certificate.
	 */
	private Certificate certificate;
	/**
	 * Holds the identifier.
	 */
	private String identifier;

	/**
	 * Creates a new instance of {@link WebSpaceImpl}.
	 * 
	 * @param identifier
	 *            The identifier of this web space.
	 * @param certificate
	 *            The certificate of this web space.
	 * @throws IllegalArgumentException
	 *             Is thrown if either the identifier or the certificate is
	 *             {@code null}. The identifier may not be empty.
	 */
	public WebSpaceImpl(String identifier, Certificate certificate)
			throws IllegalArgumentException {
		if (certificate == null)
			throw new IllegalArgumentException(
					"The certificate may no be null.");
		if (identifier == null || identifier.isEmpty())
			throw new IllegalArgumentException(
					"The identifier may not be null or empty.");
		this.identifier = identifier;
		this.certificate = certificate;
	}

	@Override
	public Certificate getCertificate() {
		return certificate;
	}

	/**
	 * Sets the certificate of this web space.
	 * 
	 * @param certificate
	 *            The certificate to set.
	 * @throws IllegalArgumentException
	 *             Is thrown if the given certificate is {@code null}.
	 */
	@Override
	public void setCertificate(Certificate certificate)
			throws IllegalArgumentException {
		if (certificate == null)
			throw new IllegalArgumentException(
					"The certificate may not be null.");
		this.certificate = certificate;
	}

	@Override
	public String getIdentifier() {
		return identifier;
	}


}
