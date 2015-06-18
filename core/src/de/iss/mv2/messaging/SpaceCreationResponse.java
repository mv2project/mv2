package de.iss.mv2.messaging;

import java.io.IOException;
import java.io.InputStream;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

/**
 * Represents the response message to a {@link SpaceCreationRequest}.
 * 
 * @author Marcel Singer
 *
 */
public class SpaceCreationResponse extends MV2Message {

	/**
	 * Creates a new instance of {@link SpaceCreationResponse}.
	 */
	public SpaceCreationResponse() {
		super(STD_MESSAGE.SPACE_CREATION_RESPONSE);
	}

	/**
	 * Sets the created certificate.
	 * 
	 * @param certData
	 *            The encoded data of the certificate to set.
	 * @throws IllegalArgumentException
	 *             If the given data is {@code null} or empty.
	 */
	public void setCertificate(byte[] certData) throws IllegalArgumentException {
		if (certData == null || certData.length == 0)
			throw new IllegalArgumentException(
					"The given data may not be null or empty.");
		setMessageField(new MessageField(DEF_MESSAGE_FIELD.CONTENT_BINARY,
				certData), true);
	}

	/**
	 * Sets the created certificate.
	 * 
	 * @param cert
	 *            The created certificate.
	 * @throws IllegalArgumentException
	 *             Is thrown if the given certificate is {@code null}.
	 * @throws IOException
	 *             if an I/O error occurs.
	 * @throws CertificateEncodingException
	 *             Is thrown if the certificate can not be encoded.
	 */
	public void setCertificate(X509Certificate cert)
			throws IllegalArgumentException, CertificateEncodingException,
			IOException {
		if (cert == null)
			throw new IllegalArgumentException(
					"The certificate may not be null.");
		setCertificate(cert.getEncoded());
	}

	/**
	 * Returns the created certificate.
	 * 
	 * @return The created certificate.
	 * @throws IOException
	 *             If an I/O error occurs.
	 * @throws CertificateException
	 *             Is thrown if the certificate can not be decoded.
	 */
	public X509Certificate getCertificate() throws CertificateException,
			IOException {
		InputStream in = getFieldContentData(DEF_MESSAGE_FIELD.CONTENT_BINARY, null);
		if (in == null)
			return null;
		X509Certificate cert = (X509Certificate) CertificateFactory
				.getInstance("X.509").generateCertificate(in);
		in.close();
		return cert;
	}

}
