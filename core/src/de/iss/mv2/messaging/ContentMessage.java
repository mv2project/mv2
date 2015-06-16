package de.iss.mv2.messaging;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import de.iss.mv2.security.PEMFileIO;
import de.iss.mv2.security.SHA256WithRSASignatureProvider;
import de.iss.mv2.security.SignatureProvider;

/**
 * An encrypted content message.
 * 
 * @author Marcel Singer
 *
 */
public class ContentMessage extends MV2Message {

	/**
	 * Creates a new instance of {@link ContentMessage}.
	 */
	public ContentMessage() {
		super(STD_MESSAGE.CONTENT_MESSAGE);
	}

	/**
	 * Returns the subject of this message.
	 * 
	 * @return The subject of this message.
	 */
	public String getSubject() {
		return getFieldStringValue(DEF_MESSAGE_FIELD.SUBJECT, "");
	}

	/**
	 * Sets the subject of this message.
	 * 
	 * @param subject
	 *            The subject of this message.
	 */
	public void setSubject(String subject) {
		setMessageField(new MessageField(DEF_MESSAGE_FIELD.SUBJECT, subject),
				true);
	}

	/**
	 * Returns the receivers of this message.
	 * 
	 * @return The receivers of this message.
	 */
	public String[] getReceivers() {
		String resultVal = getFieldStringValue(DEF_MESSAGE_FIELD.RECEIVER, "")
				.trim();
		if (!resultVal.contains(";"))
			return new String[] { resultVal };
		String[] res = resultVal.split(";");
		for (int i = 0; i < res.length; i++) {
			res[i] = res[i].trim();
		}
		return res;
	}

	/**
	 * Sets the receivers of this message.
	 * 
	 * @param receivers
	 *            The receivers to set.
	 */
	public void setReceivers(String[] receivers) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < receivers.length; i++) {
			if (i > 0)
				sb.append(";");
			sb.append(receivers[i]);
		}
		setMessageField(
				new MessageField(DEF_MESSAGE_FIELD.RECEIVER, sb.toString()),
				true);
	}

	/**
	 * Sets the senders certificate.
	 * 
	 * @param senderCertificate
	 *            The certificate to set.
	 * @throws IOException
	 *             If an I/O error occurs.
	 * @throws CertificateEncodingException
	 *             If the certificate can not be encoded.
	 */
	public void setSender(X509Certificate senderCertificate)
			throws CertificateEncodingException, IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		PEMFileIO pemIO = new PEMFileIO();
		pemIO.writeCertificate(baos, senderCertificate);
		setMessageField(new MessageField(DEF_MESSAGE_FIELD.SENDER_CERTIFICATE,
				baos.toByteArray()), true);
		baos.close();
	}

	/**
	 * Returns the senders certificate.
	 * 
	 * @return The certificate of the sending instance. To get the address of
	 *         the sender see the common name (CN) of the certificate.
	 * @throws IOException
	 *             If an I/O error occurs.
	 * @throws CertificateException
	 *             If the certificate can not be obtained from its encoding.
	 */
	public X509Certificate getSender() throws CertificateException, IOException {
		InputStream in = getFieldData(DEF_MESSAGE_FIELD.SENDER_CERTIFICATE,
				null);
		if (in == null)
			return null;
		PEMFileIO pemIO = new PEMFileIO();
		X509Certificate cert = pemIO.readCertificate(in);
		in.close();
		return cert;
	}

	/**
	 * Returns the addresses of the carbon copy (CC) receivers.
	 * 
	 * @return The addresses of the carbon copy (CC) receivers.
	 */
	public String[] getCarbonCopyAddresses() {
		String resultVal = getFieldStringValue(DEF_MESSAGE_FIELD.CARBON_COPY,
				"").trim();
		if (!resultVal.contains(";"))
			return new String[] { resultVal };
		String[] res = resultVal.split(";");
		for (int i = 0; i < res.length; i++) {
			res[i] = res[i].trim();
		}
		return res;
	}

	/**
	 * Sets the addresses of the carbon copy (CC) receivers.
	 * 
	 * @param ccAddresses
	 *            The addresses to set.
	 */
	public void setCarbonCopyAddresses(String[] ccAddresses) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < ccAddresses.length; i++) {
			if (i > 0)
				sb.append(";");
			sb.append(ccAddresses[i]);
		}
		setMessageField(
				new MessageField(DEF_MESSAGE_FIELD.CARBON_COPY, sb.toString()),
				true);
	}

	/**
	 * Sets the content text of this message.
	 * 
	 * @param content
	 *            The content text of this message.
	 */
	public void setContent(String content) {
		setMessageField(new MessageField(DEF_MESSAGE_FIELD.CONTENT_PLAIN,
				content), true);
	}

	/**
	 * Returns the content text of this message.
	 * 
	 * @return The content text of this message.
	 */
	public String getContent() {
		return getFieldStringValue(DEF_MESSAGE_FIELD.CONTENT_PLAIN, "");
	}

	/**
	 * Signs this message.
	 * <p>
	 * <b>Remarks:</b> This must be done after all other fields have been set.
	 * Otherwise the signature will be invalid.
	 * 
	 * @param signatureProvider
	 *            The provider to use.
	 * @param key
	 *            The private key of the signing instance.
	 * @throws InvalidKeyException
	 *             Is thrown if the given key is invalid.
	 * @throws IOException
	 *             If an I/O error occurs.
	 * @throws SignatureException
	 *             If the signature object is not initialized properly.
	 */
	public void sign(SignatureProvider signatureProvider, PrivateKey key)
			throws InvalidKeyException, IOException, SignatureException {
		Signature signature = signatureProvider.createSignature();
		signature.initSign(key);
		signature.update(getContentToSign());
		byte[] sig = signature.sign();
		setMessageField(new MessageField(DEF_MESSAGE_FIELD.SIGNATURE, sig),
				true);
	}

	/**
	 * Returns the data of this message that should be signed.
	 * 
	 * @return The data of this message that should be signed.
	 * @throws IOException
	 *             If an I/O error occurs.
	 */
	private byte[] getContentToSign() throws IOException {
		List<MessageField> fields = new ArrayList<MessageField>();
		for (MessageField mf : getCleanedFields()) {
			if (mf.getFieldIdentifier() == DEF_MESSAGE_FIELD.SIGNATURE
					.getIdentifier())
				continue;
			fields.add(mf);
		}
		fields.sort(new Comparator<MessageField>() {

			@Override
			public int compare(MessageField o1, MessageField o2) {
				return ((Integer) o1.getFieldIdentifier())
						.compareTo(o2.getFieldIdentifier());
			}
		});

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		for (MessageField mf : fields)
			mf.serialize(baos);
		return baos.toByteArray();
	}

	/**
	 * Signs this message.
	 * <p>
	 * <b>Remarks:</b> This must be done after all other fields have been set.
	 * Otherwise the signature will be invalid.
	 * 
	 * @param key
	 *            The private key of the signing instance.
	 * @throws InvalidKeyException
	 *             Is thrown if the given key is invalid.
	 * @throws IOException
	 *             If an I/O error occurs.
	 * @throws SignatureException
	 *             If the signature object is not initialized properly.
	 */
	public void sign(PrivateKey key) throws InvalidKeyException, IOException,
			SignatureException {
		sign(new SHA256WithRSASignatureProvider(), key);
	}

	/**
	 * Returns the signature of this message.
	 * 
	 * @return The signature of this message.
	 */
	public byte[] getSignature() {
		return getFieldDataArrayValue(DEF_MESSAGE_FIELD.SIGNATURE, null);
	}

	/**
	 * Verifies the signature of this message.
	 * 
	 * @param signatureProvider
	 *            The {@link SignatureProvider} to use.
	 * @param key
	 *            The public key of the signing instance.
	 * @return {@code true} if the signature is valid.
	 * @throws InvalidKeyException
	 *             If the supplied public key is invalid.
	 * @throws IOException
	 *             If an I/O error occurs.
	 * @throws SignatureException
	 *             If the signature object is not initialized properly.
	 */
	public boolean verifySignature(SignatureProvider signatureProvider,
			PublicKey key) throws InvalidKeyException, IOException,
			SignatureException {
		Signature signature = signatureProvider.createSignature();
		signature.initVerify(key);
		signature.update(getContentToSign());
		return signature.verify(getSignature());
	}

	/**
	 * Verifies the signature of this message with the public key obtained from
	 * the senders certificate.
	 * 
	 * @param signatureProvider
	 *            The {@link SignatureProvider} to use.
	 * @return {@code true} if the signature is valid.
	 * @throws InvalidKeyException
	 *             Is thrown if the key from the issuers certificate is invalid.
	 * @throws SignatureException
	 *             If an the signature object is not initialized properly.
	 * @throws CertificateException
	 *             Is thrown if the certificate can no be decoded.
	 * @throws IOException
	 *             If an I/O error occurs.
	 */
	public boolean verifySignature(SignatureProvider signatureProvider)
			throws InvalidKeyException, SignatureException,
			CertificateException, IOException {
		return verifySignature(signatureProvider, getSender().getPublicKey());
	}

	/**
	 * Verifies the signature (SHA256 with RSA) of this message with the public
	 * key obtained from the senders certificate.
	 * 
	 * @return {@code true} if the signature is valid.
	 * @throws InvalidKeyException
	 *             Is thrown if the key from the issuers certificate is invalid.
	 * @throws SignatureException
	 *             If an the signature object is not initialized properly.
	 * @throws CertificateException
	 *             Is thrown if the certificate can no be decoded.
	 * @throws IOException
	 *             If an I/O error occurs.
	 */
	public boolean verifySignature() throws InvalidKeyException,
			SignatureException, CertificateException, IOException {
		return verifySignature(new SHA256WithRSASignatureProvider());
	}

}
