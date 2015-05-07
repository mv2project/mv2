package de.iss.mv2.security;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

/**
 * A class for reading a PKCS encoded {@link X509Certificate} from different sources.
 * @author Marcel Singer
 *
 */
public class CertificateLoader {

	/**
	 * Creates a new instance of {@link CertificateLoader}.
	 */
	public CertificateLoader() {
		
	}
	
	/**
	 * Loads a certificate from the given input.
	 * @param in The input stream to read.
	 * @return The read certificate.
	 * @throws CertificateException If the input doesn't contain a valid certificate.
	 */
	public static X509Certificate load(InputStream in) throws CertificateException{
		X509Certificate cert  = (X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(in);
		return cert;
		
	}
	
	/**
	 * Try's to load a certificate from the resource with the given name.
	 * @param resourceName The name of the resource to load.
	 * @return The loaded certificate.
	 * @throws CertificateException If the input doesn't contain a valid certificate.
	 */
	public static X509Certificate loadFromResource(String resourceName) throws CertificateException{
		return load(new CertificateLoader().getClass().getClassLoader().getResourceAsStream(resourceName));
	}
	
	/**
	 * Try's to load a certificate from the given byte-array.
	 * @param buffer The byte-array containing the data to read.
	 * @return The read certificate.
	 * @throws CertificateException If the input doesn't contain a valid certificate.
	 */
	public static X509Certificate load(byte[] buffer) throws CertificateException{
		return load(new ByteArrayInputStream(buffer));
	}

}
