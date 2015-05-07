package de.iss.mv2.security;

import java.security.cert.X509Certificate;

/**
 * An exception that is thrown if the verification of a {@link X509Certificate} fails.
 * @author Marcel Singer
 *
 */
public class CertificateVerificationException extends Exception {
	/**
	 * Serial
	 */
    private static final long serialVersionUID = 1L;
 
    /**
     * Creates a new instance of {@link CertificateVerificationException}.
     * @param message The message.
     * @param cause The cause.
     */
    public CertificateVerificationException(String message, Throwable cause) {
        super(message, cause);
    }
 
    /**
     * Creates a new instance of {@link CertificateVerificationException}.
     * @param message The message.
     */
    public CertificateVerificationException(String message) {
        super(message);
    }
}