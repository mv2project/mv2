package de.iss.mv2.data;

import java.math.BigInteger;
import java.security.cert.X509Certificate;

/**
 * Provides the data relevant data of a certificate.
 * @author Marcel Singer
 *
 */
public interface Certificate {

	/**
	 * Returns the serial number of this certificate.
	 * @return The serial number of this certificate.
	 */
	public BigInteger getSerialNumber();
	
	/**
	 * Returns the {@link X509Certificate}.
	 * @return The {@link X509Certificate}.
	 */
	public X509Certificate getCertificate();
	
}
