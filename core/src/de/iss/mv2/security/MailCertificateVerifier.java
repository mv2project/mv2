package de.iss.mv2.security;

import java.security.cert.PKIXCertPathBuilderResult;
import java.security.cert.X509Certificate;
import java.util.HashSet;
import java.util.Set;

/**
 * A class to verify a clients certificate. The verification of the trust chain
 * is performed step by step.
 * 
 * @author Marcel Singer
 *
 */
public class MailCertificateVerifier {

	/**
	 * Holds the trusted CA certificates.
	 */
	private final Set<X509Certificate> caCertificates;
	/**
	 * Holds the trusted server certificates.
	 */
	private final Set<X509Certificate> serverCertificates;

	/**
	 * Creates a new instance of {@link MailCertificateVerifier}.
	 * 
	 * @param caCerts
	 *            A set containing the trusted CA certificates.
	 * @param serverCerts
	 *            A set containing the trusted server certificates.
	 */
	public MailCertificateVerifier(Set<X509Certificate> caCerts,
			Set<X509Certificate> serverCerts) {
		this.caCertificates = caCerts;
		this.serverCertificates = serverCerts;
	}

	/**
	 * Returns the certificate of the server with the given domain name.
	 * 
	 * @param domain
	 *            The domain of the server thats certificate should be returned.
	 * @return The certificate of the server with the given domain name.
	 *         {@code null} if there is none.
	 */
	public X509Certificate getServerCertificate(String domain) {
		for (X509Certificate cert : serverCertificates) {
			if (new CertificateNameReader(cert.getSubjectX500Principal())
					.getCommonName().equals(domain))
				return cert;
		}
		return null;
	}

	/**
	 * Verifies the given clients certificate.
	 * 
	 * @param clientCertificate
	 *            The client certificate to verify.
	 * @return The resulting trust chain.
	 * @throws CertificateVerificationException
	 *             Is thrown if the verification fails for any reason.
	 */
	public PKIXCertPathBuilderResult verifyCertificate(
			X509Certificate clientCertificate)
			throws CertificateVerificationException {
		String address = new CertificateNameReader(
				clientCertificate.getSubjectX500Principal()).getCommonName();
		if (address == null || address.isEmpty() || !address.contains("@"))
			throw new CertificateVerificationException(
					"The common name of the given client certificate is invalid.");
		String[] parts = address.split("@");
		if (parts.length != 2)
			throw new CertificateVerificationException(
					"The common name of the given client certificate is invalid.");
		String domain = parts[0].trim();
		X509Certificate serverCertificate = getServerCertificate(domain);
		if (serverCertificate == null)
			throw new CertificateVerificationException(
					"The certificate of the receivers server is not trusted.");
		// Validate servers certificate
		PKIXCertPathBuilderResult cvr = null;
		try {
			cvr = CertificateVerifier.verifyCertificate(serverCertificate,
					caCertificates);
		} catch (CertificateVerificationException ex) {
			throw new CertificateVerificationException(
					"The certificate of the receivers server is invalid.", ex);
		}
		X509Certificate caCert = (X509Certificate) cvr.getCertPath()
				.getCertificates().get(0);
		// Verify full trust chain
		Set<X509Certificate> fullChain = new HashSet<X509Certificate>();
		fullChain.add(caCert);
		fullChain.add(serverCertificate);
		return CertificateVerifier.verifyCertificate(clientCertificate, fullChain);
	}

}
