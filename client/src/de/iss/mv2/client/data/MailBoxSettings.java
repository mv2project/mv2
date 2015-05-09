package de.iss.mv2.client.data;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.security.PrivateKey;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Base64;

import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.pkcs.PKCSException;

import de.iss.mv2.data.PropertiesExportable;
import de.iss.mv2.security.PEMFileIO;

/**
 * Stores the settings for a mail box.
 * 
 * @author Marcel Singer
 *
 */
public class MailBoxSettings extends PropertiesExportable {

	/**
	 * The key of the address property.
	 */
	private static final String ADDRESS_KEY = "ADDRESS";

	/**
	 * The key of the server port property.
	 */
	private static final String SERVER_PORT_KEY = "PORT";

	/**
	 * The key of the server host property.
	 */
	private static final String SERVER_HOST_KEY = "HOST";

	/**
	 * The key of the client certificate property.
	 */
	private static final String CLIENT_CERT_KEY = "CLIENT_CERT";

	/**
	 * The key of the server certificate property.
	 */
	private static final String SERVER_CERT_KEY = "SERVER_CERT";

	/**
	 * The key of the clients private key property.
	 */
	private static final String CLIENT_KEY_KEY = "CLIENT_KEY";

	/**
	 * Creates a new instance of {@link MailBoxSettings}.
	 */
	public MailBoxSettings() {

	}

	/**
	 * Sets the address of this mail box.
	 * 
	 * @param address
	 *            The address to set.
	 */
	public void setAddress(String address) {
		getProperties().setProperty(ADDRESS_KEY, address);
	}

	/**
	 * Returns the address of this mail box.
	 * 
	 * @return The address of this mail box.
	 */
	public String getAddress() {
		return (String) getProperties().getProperty(ADDRESS_KEY, "");
	}

	/**
	 * Returns the port of the server.
	 * 
	 * @return The port of the server
	 */
	public int getServerPort() {
		return Integer.parseInt(getProperties().getProperty(SERVER_PORT_KEY,
				"9898"));
	}

	/**
	 * Sets the port of the server.
	 * 
	 * @param port
	 *            The port to set.
	 */
	public void setServerPort(int port) {
		getProperties().setProperty(SERVER_PORT_KEY, "" + port);
	}

	/**
	 * Returns the servers address.
	 * 
	 * @return The servers address.
	 */
	public String getHost() {
		return getProperties().getProperty(SERVER_HOST_KEY);
	}

	/**
	 * Sets the servers address.
	 * 
	 * @param host
	 *            The address to set.
	 */
	public void setHost(String host) {
		getProperties().setProperty(SERVER_HOST_KEY, host);
	}

	/**
	 * Sets the clients certificate.
	 * 
	 * @param certificate
	 *            The certificate to set.
	 * @throws IOException
	 *             If an I/O error occurs.
	 * @throws CertificateEncodingException
	 *             If the certificate can not be encoded.
	 */
	public void setClientCertificate(X509Certificate certificate)
			throws CertificateEncodingException, IOException {
		PEMFileIO pemIO = new PEMFileIO();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		pemIO.writeCertificate(baos, certificate);
		getProperties().setProperty(CLIENT_CERT_KEY,
				Base64.getEncoder().encodeToString(baos.toByteArray()));
	}

	/**
	 * Returns the clients certificate.
	 * 
	 * @return The clients certificate.
	 * @throws IOException
	 *             If an I/O error occurs.
	 * @throws CertificateException
	 *             If the certificate can not be retrieved.
	 */
	public X509Certificate getClientCertificate() throws CertificateException,
			IOException {
		String value = getProperties().getProperty(CLIENT_CERT_KEY);
		if (value == null || value.isEmpty())
			return null;
		ByteArrayInputStream bais = new ByteArrayInputStream(Base64
				.getDecoder().decode(value));
		PEMFileIO pemIO = new PEMFileIO();
		return pemIO.readCertificate(bais);
	}

	/**
	 * Sets the server certificate.
	 * 
	 * @param certificate
	 *            The certificate to set.
	 * @throws IOException
	 *             If an I/O error occurs.
	 * @throws CertificateEncodingException
	 *             If the certificate can not be encoded.
	 */
	public void setServerCertificate(X509Certificate certificate)
			throws CertificateEncodingException, IOException {
		PEMFileIO pemIO = new PEMFileIO();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		pemIO.writeCertificate(baos, certificate);
		getProperties().setProperty(SERVER_CERT_KEY,
				Base64.getEncoder().encodeToString(baos.toByteArray()));
	}

	/**
	 * Returns the server certificate.
	 * 
	 * @return The server certificate.
	 * @throws IOException
	 *             If an I/O error occurs.
	 * @throws CertificateException
	 *             If the certificate can not be retrieved.
	 */
	public X509Certificate getServerCertificate() throws CertificateException,
			IOException {
		String value = getProperties().getProperty(SERVER_CERT_KEY);
		if (value == null || value.isEmpty())
			return null;
		ByteArrayInputStream bais = new ByteArrayInputStream(Base64
				.getDecoder().decode(value));
		PEMFileIO pemIO = new PEMFileIO();
		return pemIO.readCertificate(bais);
	}

	/**
	 * Sets the clients private key.
	 * 
	 * @param key
	 *            The private key to set.
	 * @throws IOException
	 *             If an I/O error occurs.
	 * @throws IllegalStateException
	 *             If the settings are in an illegal state.
	 * @throws OperatorCreationException
	 *             If the operator to use for encryption can not be found or created.
	 */
	public void setClientKey(PrivateKey key) throws OperatorCreationException,
			IllegalStateException, IOException {
		PEMFileIO pemIO = new PEMFileIO();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		pemIO.writePKCS8EncryptedPrivateKey(baos, key, MV2ClientSettings
				.getRuntimeSettings().getPassphrase());
		getProperties().setProperty(CLIENT_KEY_KEY, Base64.getEncoder().encodeToString(baos.toByteArray()));
	}
	
	/**
	 * Returns the clients private key.
	 * @return The clients private key.
	 * @throws OperatorCreationException If the operator to use for encryption can not be found or created.
	 * @throws IllegalStateException If the setting are in an illegal state.
	 * @throws IOException If an I/O error occurs.
	 * @throws PKCSException If the key could not be revovered from its endcoding.
	 */
	public PrivateKey getClientKey() throws OperatorCreationException, IllegalStateException, IOException, PKCSException{
		String value = getProperties().getProperty(CLIENT_KEY_KEY);
		if(value == null || value.isEmpty()) return null;
		ByteArrayInputStream bais = new ByteArrayInputStream(Base64.getDecoder().decode(value));
		PEMFileIO pemIO = new PEMFileIO();
		PrivateKey pk = pemIO.readEncryptedPrivateKey(bais, MV2ClientSettings.getRuntimeSettings().getPassphrase());
		return pk;
	}

}
