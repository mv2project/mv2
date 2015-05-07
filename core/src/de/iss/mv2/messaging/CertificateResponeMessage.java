package de.iss.mv2.messaging;

import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.Base64;

/**
 * A message containing the response to a certificate request.
 * @author Marcel Singer
 *
 */
public class CertificateResponeMessage extends MV2Message {

	/**
	 * Holds the certificate to send.
	 */
	private X509Certificate cert;

	/**
	 * Creates a new instance of {@link CertificateResponeMessage}.
	 * @param cert The certificate to respond.
	 * @throws CertificateEncodingException If there was an exception during the encoding of the given certificate.
	 */
	public CertificateResponeMessage(X509Certificate cert)
			throws CertificateEncodingException {
		super(STD_MESSAGE.CERT_RESPONSE);
		setCertificate(cert);
	}

	/**
	 * Sets the certificate to respond with.
	 * @param cert The certificate to set.
	 * @throws CertificateEncodingException If there was an exception during the encoding of the given certificate.
	 */
	public void setCertificate(X509Certificate cert)
			throws CertificateEncodingException {
		this.cert = cert;
		String encoded = Base64.getEncoder().encodeToString(cert.getEncoded());
		setMessageField(new MessageField(STD_MESSAGE_FIELD.CONTENT_BASE64, encoded), true);
	}
	
	@Override
	protected void doCleanUp() {
		super.doCleanUp();
		try {
			setCertificate(cert);
		} catch (CertificateEncodingException e) {
			
		}
	}

}
