package de.iss.mv2.server;

import java.security.PrivateKey;
import java.security.cert.X509Certificate;

import de.iss.mv2.security.CertificateNameReader;


/**
 * A server binding containing the address and relevant certificate information.
 * @author Marcel Singer
 *
 */
public class ServerBinding {

	/**
	 * Holds the address of this binding.
	 */
	private final String address;
	/**
	 * Holds the certificate of this binding.
	 */
	private final X509Certificate certificate;
	/**
	 * Holds the private key of this binding.
	 */
	private final PrivateKey privateKey;
	
	/**
	 * Creates a new {@link ServerBinding}.
	 * @param address The address of this server binding.
	 * @param cert The certificate of this server binding.
	 * @param privateKey The private key of this server binding.
	 * @throws IllegalArgumentException This exception is thrown if at least one of the following conditions is met:
	 * <ul>
	 * <li>at least one of the given parameters is {@code null}</li>
	 * <li>the given address is empty</li>
	 * <li>the certificates common name doesn't match the given address.</li>
	 * </ul>
	 * 
	 */
	public ServerBinding(String address, X509Certificate cert, PrivateKey privateKey) throws IllegalArgumentException {
		if(address == null || cert == null || privateKey == null) throw new IllegalArgumentException("All parameters may not be null.");
		if(address.isEmpty()) throw new IllegalArgumentException("The address may not be empty.");
		CertificateNameReader cnr = new CertificateNameReader(cert.getSubjectX500Principal());
		String commonName = cnr.getCommonName();
		if(!commonName.equalsIgnoreCase(address)) throw new IllegalArgumentException("The given address is not equal to the certificates common name.");
		this.address = address;
		this.certificate = cert;
		this.privateKey = privateKey;
		
	}
	
	/**
	 * Returns the address of this binding.
	 * @return The address of this binding.
	 */
	public String getAddress(){
		return address;
	}
	
	/**
	 * Returns the certificate of this binding.
	 * @return The certificate of this binding.
	 */
	public X509Certificate getCertificate(){
		return certificate;
	}
	
	/**
	 * Returns the private key of this binding.
	 * @return The private key of this binding.
	 */
	public PrivateKey getPrivateKey(){
		return privateKey;
	}

}
