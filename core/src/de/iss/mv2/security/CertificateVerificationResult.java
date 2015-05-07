package de.iss.mv2.security;

import java.security.cert.PKIXCertPathBuilderResult;

/**
 * Contains the result of a certificate verification.
 * @author Marcel Singer
 *
 */
public class CertificateVerificationResult {
	
	/**
	 * Indicates if the certificate was valid.
	 */
    private boolean valid;
    /**
     * Contains the path builder result.
     */
    private PKIXCertPathBuilderResult result;
    /**
     * May contains an exception that failed the verification.
     */
    private Throwable exception;
     
    /**
     * Constructs a certificate verification result for valid
     * certificate by given certification path.
     * @param result The path builder result.
     */
    public CertificateVerificationResult(
            PKIXCertPathBuilderResult result) {
        this.valid = true;
        this.result = result;
    }
 
    /**
     * Constructs a certificate verification result for invalid
     * certificate by given exception that keeps the problem
     * occurred during the verification process.
     * @param exception The exception that failed the verification.
     */
    public CertificateVerificationResult(Throwable exception) {
        this.valid = false;
        this.exception = exception;
    }
 
    /**
     * Returns the result of the verification.
     * @return {@code true}, if the certificate seems to be valid.
     */
    public boolean isValid() {
        return valid;
    }

    /**
     * Returns the path builder result.
     * @return The path builder result.
     */
    public PKIXCertPathBuilderResult getResult() {
        return result;
    }
 
    /**
     * Returns the exception that failed the verification.
     * @return The exception that failed the verification or {@code null} if no exception was thrown.
     */
    public Throwable getException() {
        return exception;
    }   
}
 