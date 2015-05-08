package de.iss.mv2.messaging;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;

import org.bouncycastle.pkcs.PKCS10CertificationRequest;

import de.iss.mv2.security.PEMFileIO;


/**
 * The request to create a user space.
 * 
 * @author Marcel Singer
 *
 */
public class SpaceCreationRequest extends MV2Message {

	/**
	 * Creates a new instance of {@link SpaceCreationRequest}.
	 */
	public SpaceCreationRequest() {
		super(STD_MESSAGE.SPACE_CREATION_REQUEST);
	}

	/**
	 * Returns the certificate signing request.
	 * @return The certificate signing request.
	 * @throws IOException If an I/O error occurs.
	 */
	public PKCS10CertificationRequest getSigningRequest() throws IOException{
		String dataString = getFieldValue(DEF_MESSAGE_FIELD.CONTENT_BASE64, null);
		if(dataString == null) return null;
		byte[] data = Base64.getDecoder().decode(dataString);
		PEMFileIO pemIO = new PEMFileIO();
		PKCS10CertificationRequest cr = pemIO.readCertificateSigningRequest(new ByteArrayInputStream(data));
		return cr;
	}
	
	/**
	 * Sets the certificate signing request. 
	 * @param data The PKCS10 encoded certificate signing request to set.
	 * @throws IllegalArgumentException Is thrown if the given data is {@code null} or empty.
	 */
	public void setSigningRequest(byte[] data) throws IllegalArgumentException{
		if(data == null || data.length == 0) throw new IllegalArgumentException("The data may not be null or empty.");
		setMessageField(new MessageField(DEF_MESSAGE_FIELD.CONTENT_BASE64, Base64.getEncoder().encodeToString(data)), true);
	}
	
	/**
	 * Sets the certificate signing request.
	 * @param request The {@link PKCS10CertificationRequest} to set.
	 * @throws IllegalArgumentException Is thrown if the given certificate signing request is {@code null}.
	 * @throws IOException If the given request can not be encoded.
	 */
	public void setSigningRequest(PKCS10CertificationRequest request) throws IllegalArgumentException, IOException{
		if(request == null) throw new IllegalArgumentException("The request may not be null.");
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PEMFileIO pemIO = new PEMFileIO();
		pemIO.writeCertificateSigningRequest(baos, request);
		setSigningRequest(baos.toByteArray());
	}
	
}
