package de.iss.mv2.security;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.SecureRandom;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Base64;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openssl.PEMDecryptorProvider;
import org.bouncycastle.openssl.PEMEncryptedKeyPair;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.openssl.PKCS8Generator;
import org.bouncycastle.openssl.jcajce.JcaPEMKeyConverter;
import org.bouncycastle.openssl.jcajce.JcaPKCS8Generator;
import org.bouncycastle.openssl.jcajce.JceOpenSSLPKCS8DecryptorProviderBuilder;
import org.bouncycastle.openssl.jcajce.JceOpenSSLPKCS8EncryptorBuilder;
import org.bouncycastle.openssl.jcajce.JcePEMDecryptorProviderBuilder;
import org.bouncycastle.operator.InputDecryptorProvider;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.OutputEncryptor;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.PKCS8EncryptedPrivateKeyInfo;
import org.bouncycastle.pkcs.PKCSException;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemReader;
import org.bouncycastle.util.io.pem.PemWriter;

import sun.misc.BASE64Encoder;
import sun.security.provider.X509Factory;
import de.iss.mv2.data.BinaryTools;

/**
 * A class to read and write different PKCS entities from or to a stream.
 * 
 * @author Marcel Singer
 *
 */
public class PEMFileIO {

	/**
	 * Creates a new instance of {@link PEMFileIO}.
	 */
	public PEMFileIO() {

	}

	/**
	 * Reads an encrypted key pair from an input stream.<br />
	 * <br />
	 * This method is able to read key files created by OpenSSL.
	 * 
	 * @param in
	 *            The input stream to read.
	 * @param password
	 *            The password to use.
	 * @return The read key pair.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	public KeyPair readEncryptedKeyFile(InputStream in, String password)
			throws IOException {
		PEMParser parser = new PEMParser(new InputStreamReader(in));
		Object o = parser.readObject();
		PEMDecryptorProvider decProv = new JcePEMDecryptorProviderBuilder()
				.build(password.toCharArray());
		JcaPEMKeyConverter converter = new JcaPEMKeyConverter()
				.setProvider(new BouncyCastleProvider());
		KeyPair kp = null;
		if (o instanceof PEMEncryptedKeyPair) {
			kp = converter.getKeyPair(((PEMEncryptedKeyPair) o)
					.decryptKeyPair(decProv));
		}
		parser.close();
		return kp;
	}

	/**
	 * Reads a certificate from the given file.
	 * 
	 * @param file
	 *            The path of the file to read.
	 * @return The read certificate.
	 * @throws CertificateException
	 *             If the certificate can not be read.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	public X509Certificate readCertificate(String file)
			throws CertificateException, IOException {
		FileInputStream in = new FileInputStream(new File(file));
		X509Certificate cert = readCertificate(in);
		in.close();
		return cert;
	}

	/**
	 * Reads a certificate form the given input stream.
	 * 
	 * @param in
	 *            The input stream to read.
	 * @return The read certificate.
	 * @throws IOException
	 *             If an I/O error occurs.
	 * @throws CertificateException
	 *             If the certificate can not be read.
	 */
	public X509Certificate readCertificate(InputStream in) throws IOException,
			CertificateException {
		X509Certificate cert = (X509Certificate) CertificateFactory
				.getInstance("X.509").generateCertificate(in);
		return cert;
	}

	/**
	 * Writes a certificate to the given output stream.
	 * 
	 * @param out
	 *            The output stream to write.
	 * @param cert
	 *            The certificate to export.
	 * @throws CertificateEncodingException
	 *             If the certificate can not be encoded.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	public void writeCertificate(OutputStream out, X509Certificate cert)
			throws CertificateEncodingException, IOException {
		PrintWriter pw = new PrintWriter(out);

		BASE64Encoder encoder = new BASE64Encoder();
		pw.println(X509Factory.BEGIN_CERT);
		pw.flush();
		encoder.encodeBuffer(cert.getEncoded(), out);
		out.flush();
		pw.println(X509Factory.END_CERT);
		pw.flush();
	}

	/**
	 * Constant with the opening line of a CSR.
	 */
	private static final String BEGIN_CSR = "-----BEGIN CERTIFICATE REQUEST-----";
	/**
	 * Constant with the closing line of a CSR.
	 */
	private static final String END_CSR = "-----END CERTIFICATE REQUEST-----";

	/**
	 * Writes a certificate signing request to the given output stream (PKCS10).
	 * 
	 * @param out
	 *            The output stream to write.
	 * @param request
	 *            The request to export.
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	public void writeCertificateSigningRequest(OutputStream out,
			PKCS10CertificationRequest request) throws IOException {
		PrintWriter pw = new PrintWriter(out);
		pw.println(BEGIN_CSR);
		String base64 = BinaryTools.insertSpacing(Base64.getEncoder()
				.encodeToString(request.getEncoded()), "\n", 64);
		pw.println(base64);
		pw.println(END_CSR);
		pw.flush();
	}

	/**
	 * Reads a certificate signing request (PKCS10) from the given input stream.
	 * 
	 * @param in
	 *            The input stream to read.
	 * @return The read certificate signing request.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	public PKCS10CertificationRequest readCertificateSigningRequest(
			InputStream in) throws IOException {
		PemObject pO = readPEM(in);
		return new PKCS10CertificationRequest(pO.getContent());
	}

	/**
	 * Reads a certificate signing request (PKCS10) from the given file.
	 * 
	 * @param filePath
	 *            The path of the file to read.
	 * @return The read certificate signing request.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	public PKCS10CertificationRequest readCertificateSigningRequest(
			String filePath) throws IOException {
		FileInputStream fin = new FileInputStream(new File(filePath));
		PKCS10CertificationRequest result = readCertificateSigningRequest(fin);
		fin.close();
		return result;
	}

	/**
	 * Reads the first {@link PemObject} from the given input stream.
	 * 
	 * @param in
	 *            The stream to read.
	 * @return The read {@link PemObject}.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	private PemObject readPEM(InputStream in) throws IOException {
		@SuppressWarnings("resource")
		PemReader reader = new PemReader(new InputStreamReader(in));
		PemObject o = reader.readPemObject();
		return o;
	}

	/**
	 * Encrypts the given private key and writes it to the given output stream
	 * (PKCS8). <br />
	 * <br />
	 * The encryption is done using the AES-256-algorithm in cipher block
	 * chaining mode.
	 * 
	 * @param out
	 *            The output stream to write.
	 * @param key
	 *            The key to export.
	 * @param password
	 *            The password used to encrypt the key.
	 * @throws OperatorCreationException
	 *             If there was an exception initializing the cipher.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	@SuppressWarnings("resource")
	public void writePKCS8EncryptedPrivateKey(OutputStream out, PrivateKey key,
			String password) throws OperatorCreationException, IOException {
		JceOpenSSLPKCS8EncryptorBuilder encryptorBuilder = new JceOpenSSLPKCS8EncryptorBuilder(
				PKCS8Generator.AES_256_CBC).setProvider(new BouncyCastleProvider());
		encryptorBuilder.setRandom(new SecureRandom());
		encryptorBuilder.setPasssword(password.toCharArray());
		OutputEncryptor oe = encryptorBuilder.build();

		JcaPKCS8Generator gen = new JcaPKCS8Generator(key, oe);
		PemObject obj = gen.generate();
		PemWriter pemWrt = new PemWriter(new OutputStreamWriter(out));
		pemWrt.writeObject(obj);
		pemWrt.flush();
	}

	/**
	 * Reads an encrypted private key (PKCS8) from an input stream.
	 * 
	 * @param in
	 *            The input stream to read.
	 * @param password
	 *            The password to use.
	 * @return The read private key.
	 * @throws IOException
	 *             If an I/O error occurs.
	 * @throws OperatorCreationException
	 *             If the algorithm used for encryption could not be
	 *             initialized.
	 * @throws PKCSException
	 *             If the parsing of the input failed.
	 */
	public PrivateKey readEncryptedPrivateKey(InputStream in, String password)
			throws IOException, OperatorCreationException, PKCSException {
		PEMParser parser = new PEMParser(new InputStreamReader(in));
		Object o = parser.readObject();
		JcaPEMKeyConverter converter = new JcaPEMKeyConverter()
				.setProvider(new BouncyCastleProvider());
		PrivateKey kp = null;
		if (o instanceof PKCS8EncryptedPrivateKeyInfo) {
			PKCS8EncryptedPrivateKeyInfo pkKey = (PKCS8EncryptedPrivateKeyInfo) o;
			JceOpenSSLPKCS8DecryptorProviderBuilder builder = new JceOpenSSLPKCS8DecryptorProviderBuilder();
			builder.setProvider(new BouncyCastleProvider());
			InputDecryptorProvider pkcs8decoder = builder.build(password
					.toCharArray());
			kp = converter.getPrivateKey(pkKey
					.decryptPrivateKeyInfo(pkcs8decoder));
		}
		parser.close();
		return kp;
	}
}
