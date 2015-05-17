package de.iss.mv2.security;

import java.security.NoSuchAlgorithmException;
import java.security.Signature;

/**
 * A object to create a SHA256 with RSA {@link Signature}.
 * @author Marcel Singer
 *
 */
public class SHA256WithRSASignatureProvider implements SignatureProvider{

	/**
	 * Creates a new instance of {@link SHA256WithRSASignatureProvider}.
	 */
	public SHA256WithRSASignatureProvider() {
		
	}

	@Override
	public Signature createSignature() {
		try {
			return Signature.getInstance("SHA256withRSA");
		} catch (NoSuchAlgorithmException e) {
			return null;
		}
	}

}
