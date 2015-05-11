package de.iss.mv2.messaging;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Base64;

import de.iss.mv2.security.PEMFileIO;

/**
 * A message containing the answer to a {@link ClientCertificateRequest}.
 * 
 * @author Marcel Singer
 *
 */
public class ClientCertificateResponse extends MV2Message {

	/**
	 * Creates a new instance of {@link ClientCertificateResponse}.
	 */
	public ClientCertificateResponse() {
		super(STD_MESSAGE.CLIENT_CERTIFICATE_RESPONSE);
	}

	/**
	 * Sets the requested client certificate.
	 * 
	 * @param certificate
	 *            The certificate to set.
	 * @throws IllegalArgumentException
	 *             Is thrown if the given certificate is {@code null}.
	 * @throws CertificateEncodingException
	 *             Is thrown if the given certificate can not be encoded.
	 */
	public void setCertificate(X509Certificate certificate)
			throws IllegalArgumentException, CertificateEncodingException {
		if (certificate == null)
			throw new IllegalArgumentException(
					"The certificate must not be null.");
		byte[] certData = certificate.getEncoded();
		setMessageField(new MessageField(DEF_MESSAGE_FIELD.CONTENT_BASE64,
				Base64.getEncoder().encodeToString(certData)), true);
	}

	/**
	 * Returns the requested client certificate.
	 * 
	 * @return The requested client certificate.
	 * @throws CertificateException
	 *             If the certificate can not be decoded.
	 * @throws IOException
	 *             If an I/O error occurs.
	 */
	public X509Certificate getCertificate() throws CertificateException,
			IOException {
		String value = getFieldValue(DEF_MESSAGE_FIELD.CONTENT_BASE64, null);
		if (value == null)
			return null;
		PEMFileIO pemIO = new PEMFileIO();
		X509Certificate cert = pemIO.readCertificate(new ByteArrayInputStream(
				Base64.getDecoder().decode(value)));
		return cert;
	}

}
