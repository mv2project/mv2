package de.iss.mv2.messaging;

import java.io.IOException;
import java.io.InputStream;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import de.iss.mv2.security.PEMFileIO;

/**
 * A message containing the response to a certificate request.
 * 
 * @author Marcel Singer
 *
 */
public class CertificateResponeMessage extends MV2Message {

	

	/**
	 * Creates a new instance of {@link CertificateResponeMessage}.
	 */
	public CertificateResponeMessage(){
		super(STD_MESSAGE.CERT_RESPONSE);
	}

	/**
	 * Sets the certificate to respond with.
	 * 
	 * @param cert
	 *            The certificate to set.
	 * @throws CertificateEncodingException
	 *             If there was an exception during the encoding of the given
	 *             certificate.
	 */
	public void setCertificate(X509Certificate cert)
			throws CertificateEncodingException {
		setMessageField(
				new MessageField(DEF_MESSAGE_FIELD.CONTENT_BINARY,
						cert.getEncoded()), true);
	}
	
	/**
	 * Returns the certificate.
	 * @return The certificate.
	 * @throws IOException If an I/O error occurs.
	 * @throws CertificateException If the certificate could not be decoded.
	 */
	public X509Certificate getCertificate() throws IOException, CertificateException{
		InputStream in = getFieldData(DEF_MESSAGE_FIELD.CONTENT_BINARY, null);
		if(in == null) return null;
		PEMFileIO pemIO = new PEMFileIO();
		X509Certificate cer = pemIO.readCertificate(in);
		in.close();
		return cer;
	}


}
