package de.iss.mv2.security;

import java.io.IOException;
import java.security.KeyPair;

import javax.security.auth.x500.X500Principal;

import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.pkcs.PKCS10CertificationRequest;
import org.bouncycastle.pkcs.PKCS10CertificationRequestBuilder;
import org.bouncycastle.pkcs.jcajce.JcaPKCS10CertificationRequestBuilder;

/**
 * Represents a certificate signing request.
 * 
 * @author Marcel Singer
 *
 */
public class CertificateSigningRequest {

	/**
	 * Holds the common name of the requesting instance.
	 */
	private String commonName;
	/**
	 * Holds the organization unit of the requesting instance.
	 */
	private String organizationUnit;
	/**
	 * Holds the organization of the requesting instance.
	 */
	private String organizationName;
	/**
	 * Holds the location of the requesting instance.
	 */
	private String location;
	/**
	 * Holds the country of the requesting instance.
	 */
	private String country;
	/**
	 * Holds the state of the requesting instance.
	 */
	private String state;

	/**
	 * Holds the mail address of the requesting instance.
	 */
	private String mailAddress;

	/**
	 * Holds the name of the requesting instance.
	 */
	private String name;

	/**
	 * Creates an empty instance of {@link CertificateSigningRequest}.
	 */
	public CertificateSigningRequest() {

	}

	/**
	 * Removes all German 'Umlaute' (ä, ö, ü) from the given string.
	 * 
	 * @param content
	 *            The string to clean.
	 * @return The cleaned string.
	 */
	private String cleanString(String content) {
		return content.trim().replace("ä", "ae").replace("ö", "ou")
				.replace("ü", "ue").replace("Ä", "Ae").replace("Ö", "Oe")
				.replace("Ü", "Ue").replace("ß", "ss");
	}

	/**
	 * Initializes this request with all needed values.
	 * 
	 * @param commonName
	 *            The common name of the requesting instance.
	 * @param country
	 *            The country of the requesting instance.
	 * @param state
	 *            The state of the requesting instance.
	 * @param location
	 *            The location of the requesting instance.
	 * @param organisation
	 *            The organization of the requesting instance.
	 * @param organisationUnit
	 *            The organization unit of the requesting instance.
	 */
	public void init(String commonName, String country, String state,
			String location, String organisation, String organisationUnit) {
		setCommonName(commonName);
		setCountry(country);
		setState(state);
		setLocation(location);
		setOrganizationName(organisation);
		setOrganizationUnit(organisationUnit);
	}

	/**
	 * Returns the common name part.
	 * 
	 * @return The common name part.
	 */
	public String getCommonName() {
		return commonName;
	}

	/**
	 * Sets the common name part of this request.
	 * 
	 * @param commonName
	 *            The common name part to set.
	 */
	public void setCommonName(String commonName) {
		this.commonName = cleanString(commonName);
	}

	/**
	 * Returns the name of the requesting instance.
	 * 
	 * @return The name of the requesting instance.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the requesting instance.
	 * 
	 * @param name
	 *            The name of the requesting instance.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns the organization unit part.
	 * 
	 * @return The organization unit part.
	 */
	public String getOrganizationUnit() {
		return organizationUnit;
	}

	/**
	 * Sets the organization unit part of this request.
	 * 
	 * @param organisationUnit
	 *            The organization unit part to set.
	 */
	public void setOrganizationUnit(String organisationUnit) {
		this.organizationUnit = cleanString(organisationUnit);
	}

	/**
	 * Returns the organization part of this request.
	 * 
	 * @return The organization part of this request.
	 */
	public String getOrganizationName() {
		return organizationName;
	}

	/**
	 * Sets the organization part of this request.
	 * 
	 * @param organisationName
	 *            The organization part to set.
	 */
	public void setOrganizationName(String organisationName) {
		this.organizationName = cleanString(organisationName);
	}

	/**
	 * Returns the location part of this request.
	 * 
	 * @return The location part of this request.
	 */
	public String getLocation() {
		return location;
	}

	/**
	 * Sets the location part of this request.
	 * 
	 * @param location
	 *            The location part to be set.
	 */
	public void setLocation(String location) {
		this.location = cleanString(location);
	}

	/**
	 * Returns the country part of this request.
	 * 
	 * @return The country part of this request.
	 */
	public String getCountry() {
		return country;
	}

	/**
	 * Sets the country part of this request.
	 * 
	 * @param country
	 *            The country part to be set.
	 */
	public void setCountry(String country) {
		this.country = cleanString(country);
	}

	/**
	 * Returns the state part of this request.
	 * 
	 * @return The state part of this request.
	 */
	public String getState() {
		return state;
	}

	/**
	 * Sets the state part of this request.
	 * 
	 * @param state
	 *            The state part to be set.
	 */
	public void setState(String state) {
		this.state = cleanString(state);
	}

	/**
	 * Sets the mail address of the requesting instance.
	 * 
	 * @param mailAddress
	 *            The mail address to set. If this parameter might be
	 *            {@code null}.
	 */
	public void setMailAddress(String mailAddress) {
		this.mailAddress = mailAddress;
	}

	/**
	 * Generates a {@link PKCS10CertificationRequest}.
	 * 
	 * @param kp
	 *            The key pair of the requesting instance.
	 * @return The generates certificate signing request.
	 * @throws IOException
	 *             if an I/O error occurs.
	 * @throws OperatorCreationException
	 *             If an error occurs creating the request.
	 */
	public PKCS10CertificationRequest generatePKCS10(KeyPair kp)
			throws IOException, OperatorCreationException {
		X500NameBuilder nb = new X500NameBuilder(BCStyle.INSTANCE);
		if (location != null && !location.isEmpty())
			nb.addRDN(BCStyle.L, location);
		if (state != null && !state.isEmpty())
			nb.addRDN(BCStyle.ST, state);
		if (country != null && !country.isEmpty())
			nb.addRDN(BCStyle.C, country);
		if (organizationName != null && !organizationName.isEmpty())
			nb.addRDN(BCStyle.O, organizationName);
		if (organizationUnit != null && !organizationUnit.isEmpty())
			nb.addRDN(BCStyle.OU, organizationUnit);
		nb.addRDN(BCStyle.CN, commonName);
		if (mailAddress != null && !mailAddress.isEmpty())
			nb.addRDN(BCStyle.EmailAddress, mailAddress);
		if(name != null && !name.isEmpty())
			nb.addRDN(BCStyle.NAME, name);
		X500Name x500Name = nb.build();
		X500Principal pr = new X500Principal(x500Name.getEncoded());
		PKCS10CertificationRequestBuilder p10Builder = new JcaPKCS10CertificationRequestBuilder(
				pr, kp.getPublic());

		JcaContentSignerBuilder csBuilder = new JcaContentSignerBuilder(
				"SHA256WithRSA");
		ContentSigner signer = csBuilder.build(kp.getPrivate());
		PKCS10CertificationRequest csr = p10Builder.build(signer);
		return csr;
	}

}
