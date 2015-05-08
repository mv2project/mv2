package de.iss.mv2.data;

import java.math.BigInteger;
import java.security.cert.X509Certificate;

import de.iss.mv2.security.UnambiguityPovider;

/**
 * Provides management methods for the lifetime cycle of a certificate. <br />
 * Known implementing classes: {@link LocalCertificateManager}
 * 
 * @author Marcel Singer
 *
 */
public interface CertificateManager extends UnambiguityPovider<BigInteger> {

	/**
	 * Loads the certificate with the given serial number from a persistent
	 * source and returns it.
	 * 
	 * @param serialNumber
	 *            The serial number of the certificate to load.
	 * @return The loaded certificate or {@code null} if there is no certificate
	 *         with the given serial number.
	 */
	public Certificate load(BigInteger serialNumber);

	/**
	 * Saves the given certificate to a persistent store.
	 * 
	 * @param cert
	 *            The certificate to store.
	 */
	public void create(X509Certificate cert);

	/**
	 * Removes the certificate from the persistent store.
	 * 
	 * @param cert
	 *            The certificate to remove.
	 * @param andRevoke
	 *            {@code true} if the removed certificate should be marked and
	 *            published as revoked.
	 */
	public void remove(X509Certificate cert, boolean andRevoke);

}
