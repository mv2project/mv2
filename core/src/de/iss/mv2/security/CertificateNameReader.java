package de.iss.mv2.security;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.security.auth.x500.X500Principal;

import org.bouncycastle.asn1.x500.X500Name;

/**
 * Reader for parsing information from a given distinguished name.
 * 
 * @author Marcel Singer
 *
 */
public class CertificateNameReader {

	/**
	 * Stores all encoded values.
	 */
	private final Map<String, String> values = new HashMap<String, String>();

	/**
	 * A constant holding the identifier of the organization part.
	 */
	public static final String ORGANIZATION = "O";
	/**
	 * A constant holding the identifier of the state part.
	 */
	public static final String STATE = "ST";
	/**
	 * A constant holding the identifier of the country part.
	 */
	public static final String COUNTRY = "C";
	/**
	 * A constant holding the identifier of the organisation unit part.
	 */
	public static final String ORGANISATION_UNIT = "OU";
	/**
	 * A constant holding the identifier of the common name part.
	 */
	public static final String COMMON_NAME = "CN";
	/**
	 * A constant holding the identifier of the city part.
	 */
	public static final String CITY = "L";

	/**
	 * Creates a new instance of {@link CertificateNameReader} with the given
	 * principal.
	 * 
	 * @param p
	 *            The principal to read.
	 */
	public CertificateNameReader(X500Principal p) {
		String[] args = p.getName().split(",");
		for (String arg : args) {
			if (arg.contains("=")) {
				String[] kv = arg.split("=");
				if (kv.length == 2) {
					values.put(kv[0], kv[1]);
				}
			}
		}
	}

	/**
	 * Creates a new instance of {@link CertificateNameReader} with the given
	 * {@link X500Name}.
	 * 
	 * @param name
	 *            The {@link X500Name} to read.
	 * @throws IOException
	 *             If an I/O error occurs.
	 */
	public CertificateNameReader(X500Name name) throws IOException {
		this(new X500Principal(name.getEncoded()));
	}

	/**
	 * Returns the value for the given part. If no value was found the default
	 * is returned.
	 * 
	 * @param identifier
	 *            The identifier of the part to return. See the constants of
	 *            {@link CertificateNameReader} for available parts.
	 * @param defaultValue
	 *            The default that should be returned if the requested part was
	 *            not found.
	 * @return The value for the requested part or the given default if the part
	 *         was not found.
	 */
	public String getValue(String identifier, String defaultValue) {
		if (!values.containsKey(identifier))
			return defaultValue;
		return values.get(identifier);
	}

	/**
	 * Returns the value for the given part. If not value was found an empty
	 * string is returned.
	 * 
	 * @param identifier
	 *            The identifier of the part to return. See the constants of
	 *            {@link CertificateNameReader} for available parts.
	 * @return The value for the given part or an empty string if the specified
	 *         part was not found.
	 */
	public String getValue(String identifier) {
		return getValue(identifier, "");
	}

	/**
	 * Returns the organisation part.
	 * 
	 * @return The organisation part or an empty string if not found.
	 */
	public String getOrangisation() {
		return getValue(ORGANIZATION);
	}

	/**
	 * Returns the organisation unit part.
	 * 
	 * @return The organisation unit part or an empty string if not found.
	 */
	public String getOrganisationUnit() {
		return getValue(ORGANISATION_UNIT);
	}

	/**
	 * Returns the state part.
	 * 
	 * @return The state part or an empty string if not found.
	 */
	public String getState() {
		return getValue(STATE);
	}

	/**
	 * Returns the country part.
	 * 
	 * @return The country part or an empty string if not found.
	 */
	public String getCountry() {
		return getValue(COUNTRY);
	}

	/**
	 * Returns the common name part.
	 * 
	 * @return The common name part or an empty string if not found.
	 */
	public String getCommonName() {
		return getValue(COMMON_NAME);
	}

	/**
	 * Returns the city part.
	 * 
	 * @return The city part or an empty string if not found.
	 */
	public String getCity() {
		return getValue(CITY);
	}

}
