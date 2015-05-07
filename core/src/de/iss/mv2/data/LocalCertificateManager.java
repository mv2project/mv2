package de.iss.mv2.data;

import java.math.BigInteger;
import java.security.cert.X509Certificate;

/**
 * Represents a certificate manager used for testing.<br />
 * <br />
 * It doesn't persist given certificates and only checks the unambiguously against the serial number of the own certificate. This class is intended to be use for testing purposes.
 * @author Marcel Singer
 *
 */
public class LocalCertificateManager implements CertificateManager {
	
	/**
	 * Holds the certificate of this instance.
	 */
	private final X509Certificate signingCert;

	/**
	 * Creates a new instance of {@link LocalCertificateManager}.
	 * @param signingCert The certificate of this instance.
	 */
	public LocalCertificateManager(X509Certificate signingCert) {
		this.signingCert = signingCert;
	}

	
	@Override
	public boolean isUnambiguously(BigInteger value) {
		return !value.equals(signingCert.getSerialNumber());
	}

	@Override
	public Certificate load(BigInteger serialNumber) {
		return null;
	}

	@Override
	public void create(X509Certificate cert) {
		
	}

	@Override
	public void remove(X509Certificate cert, boolean andRevoke) {
		
	}

}
