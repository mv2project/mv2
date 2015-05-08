package de.iss.mv2.security;

import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import javax.security.auth.x500.X500Principal;

import org.bouncycastle.asn1.ASN1OctetString;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.AuthorityKeyIdentifier;
import org.bouncycastle.asn1.x509.BasicConstraints;
import org.bouncycastle.asn1.x509.Extension;
import org.bouncycastle.asn1.x509.SubjectKeyIdentifier;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cert.jcajce.JcaX509ExtensionUtils;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.crypto.params.RSAKeyParameters;
import org.bouncycastle.crypto.util.PublicKeyFactory;
import org.bouncycastle.jce.X509KeyUsage;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;

import de.iss.mv2.data.CertificateManager;

/**
 * A class to sign a given certificate signing request with an existing
 * certificate.
 * 
 * @author Marcel Singer
 *
 */
public class CertificateSigner {

	/**
	 * Stores the start date of validity.
	 */
	private final Date startDate = (new GregorianCalendar()).getTime();
	/**
	 * Contains the end date of validity.
	 */
	private Date endDate;
	/**
	 * Contains the certificate of the signing instance.
	 */
	private X509Certificate signerCert;
	/**
	 * Contains the manager to handle the signed certificates.
	 */
	private final CertificateManager certManager;
	/**
	 * Contains a randomizer used for creating the serial number for a signed
	 * certificate.
	 */
	private final SecureRandom random;

	/**
	 * Creates a new instance of {@link CertificateSigner}.
	 * 
	 * @param issuerCert
	 *            The certificate of the signing instance.
	 * @param manager
	 *            An instance of {@link CertificateManager} to handle the signed
	 *            certificates.
	 * @param random
	 *            An instance of {@link SecureRandom} to generate the serial
	 *            numbers.
	 */
	public CertificateSigner(X509Certificate issuerCert,
			CertificateManager manager, SecureRandom random) {
		setValidity(365);
		this.signerCert = issuerCert;
		this.random = random;
		this.certManager = manager;
	}

	/**
	 * Sets the duration of validity in days.
	 * 
	 * @param days
	 *            The duration of validity in days.
	 */
	public void setValidity(int days) {
		GregorianCalendar gc = new GregorianCalendar();
		gc.setTime(startDate);
		gc.add(Calendar.DAY_OF_MONTH, days);
		endDate = gc.getTime();
	}

	/**
	 * Returns the start date of validity.
	 * 
	 * @return The start date of validity.
	 */
	public Date getStartDate() {
		return startDate;
	}

	/**
	 * Returns the end date of validity.
	 * 
	 * @return The end date of validity.
	 */
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * Looks for an unique serial number.
	 * 
	 * @return The found serial number.
	 */
	private BigInteger getFreeSerial() {
		byte[] dat = new byte[20];
		BigInteger cVal;
		do {
			random.nextBytes(dat);
			cVal = new BigInteger(dat);
		} while (cVal.signum() != 1 || !certManager.isUnambiguously(cVal));
		return cVal;
	}

	/**
	 * Returns the key identifier of the signing instance.
	 * 
	 * @return The key identifier of the signing instance.
	 * @throws IOException
	 *             if an I/O error occurs.
	 */
	private AuthorityKeyIdentifier getSignerKeyIdentifier() throws IOException {
		ASN1Primitive p = JcaX509ExtensionUtils.parseExtensionValue(signerCert
				.getExtensionValue(Extension.subjectKeyIdentifier.getId()));
		ASN1OctetString octet = (ASN1OctetString) p;
		return new AuthorityKeyIdentifier(octet.getOctets());
	}

	/**
	 * Returns the key identifier of the requesting instance.
	 * 
	 * @param pk
	 *            The {@link SubjectPublicKeyInfo} of the requesting instance.
	 * @return The key identifier of the requesting instance.
	 * @throws NoSuchAlgorithmException
	 *             If there is no algorithm for the given
	 *             {@link SubjectPublicKeyInfo}.
	 */
	private SubjectKeyIdentifier getKeyIdentifier(SubjectPublicKeyInfo pk)
			throws NoSuchAlgorithmException {
		JcaX509ExtensionUtils utils = new JcaX509ExtensionUtils();
		return utils.createSubjectKeyIdentifier(pk);

	}
	
	/**
	 * Returns the certificate manager used by this instance.
	 * @return The certificate manager used by this instance. 
	 */
	public CertificateManager getCertificateManager(){
		return certManager;
	}

	/**
	 * Signs the given certificate signing request.
	 * 
	 * @param caKey
	 *            The private key of the signing instance. This key must belong
	 *            to the certificate given at
	 *            {@link CertificateSigner#CertificateSigner(X509Certificate, CertificateManager, SecureRandom)}
	 *            .
	 * @param signingRequest
	 *            The request to be signed.
	 * @param allowResign
	 *            {@code true} if the requesting instance should be allowed to
	 *            sign other request.
	 * @return The signed certificate.
	 * @throws OperatorCreationException
	 * @throws IOException
	 *             if an I/O error occurs.
	 * @throws CertificateException
	 *             If there was an exception creating the requesting instance's
	 *             certificate.
	 * @throws NoSuchAlgorithmException
	 *             If an required algorithm implementation was not found.
	 * @throws InvalidKeySpecException
	 *             If a required key was invalid.
	 */
	public X509Certificate sign(PrivateKey caKey,
			PKCS10CertificationRequest signingRequest, boolean allowResign)
			throws OperatorCreationException, IOException,
			CertificateException, NoSuchAlgorithmException,
			InvalidKeySpecException {
		SubjectPublicKeyInfo keyInfo = signingRequest.getSubjectPublicKeyInfo();
		RSAKeyParameters rsa = (RSAKeyParameters) PublicKeyFactory
				.createKey(keyInfo);
		RSAPublicKeySpec rsaSpec = new RSAPublicKeySpec(rsa.getModulus(),
				rsa.getExponent());
		KeyFactory kf = KeyFactory.getInstance("RSA");
		PublicKey rsaPub = kf.generatePublic(rsaSpec);

		X500Name subject = signingRequest.getSubject();
		X509KeyUsage keyuse;
		if (allowResign) {
			keyuse = new X509KeyUsage(X509KeyUsage.digitalSignature
					| X509KeyUsage.nonRepudiation
					| X509KeyUsage.keyEncipherment
					| X509KeyUsage.dataEncipherment | X509KeyUsage.keyCertSign);
		} else {
			keyuse = new X509KeyUsage(X509KeyUsage.digitalSignature
					| X509KeyUsage.nonRepudiation
					| X509KeyUsage.keyEncipherment
					| X509KeyUsage.dataEncipherment);
		}

		X509v3CertificateBuilder certGen = new JcaX509v3CertificateBuilder(
				signerCert, getFreeSerial(), startDate, endDate,
				new X500Principal(subject.getEncoded()), rsaPub);

		certGen.addExtension(Extension.keyUsage, false, keyuse.getEncoded());

		certGen.addExtension(Extension.authorityKeyIdentifier, false,
				getSignerKeyIdentifier());
		certGen.addExtension(Extension.subjectKeyIdentifier, false,
				getKeyIdentifier(signingRequest.getSubjectPublicKeyInfo()));
		certGen.addExtension(Extension.basicConstraints, false,
				new BasicConstraints(allowResign));
		ContentSigner cs = new JcaContentSignerBuilder("SHA256WithRSA")
				.setProvider("BC").build(caKey);
		X509CertificateHolder holder = certGen.build(cs);
		X509Certificate cert = new JcaX509CertificateConverter().setProvider(
				"BC").getCertificate(holder);
		certManager.create(cert);
		return cert;
	}

}
