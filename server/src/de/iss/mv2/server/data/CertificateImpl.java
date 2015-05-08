package de.iss.mv2.server.data;

import java.io.ByteArrayInputStream;
import java.math.BigInteger;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.sql.ResultSet;
import java.sql.SQLException;

import de.iss.mv2.data.Certificate;
import de.iss.mv2.server.io.sql.Commitable;
import de.iss.mv2.server.io.sql.ReferenceValueResolver;
import de.iss.mv2.server.io.sql.TableField;

/**
 * Represents a certificate that can be stored into a database.
 * @author Marcel Singer 
 *
 */
public class CertificateImpl extends Commitable implements Certificate {

	/**
	 * Holds the plain certificate.
	 */
	private final TableField<X509Certificate> certificate;
	/**
	 * Holds the serial of this certificate.
	 */
	private final TableField<BigInteger> serial;

	/**
	 * Creates a new instance from the given SQL result set.
	 * @param rs The result set to read.
	 * @throws CertificateException If the certificate can not be parsed from the result set.
	 * @throws SQLException if a SQL-error occurs.
	 */
	public CertificateImpl(ResultSet rs) throws CertificateException,
			SQLException {
		this(loadFromResult(rs));
	}

	/**
	 * Loads a {@link X509Certificate} from the given result set.
	 * @param rs The result set to read.
	 * @return The read {@link X509Certificate}.
	 * @throws SQLException if a SQL-error occurs.
	 * @throws CertificateException If the certificate can not be parsed from the given result set.
	 */
	private static X509Certificate loadFromResult(ResultSet rs)
			throws SQLException, CertificateException {
		byte[] cnt = rs.getBytes("content");
		CertificateFactory cf = CertificateFactory.getInstance("X.509");
		return (X509Certificate) cf
				.generateCertificate(new ByteArrayInputStream(cnt));
	}

	/**
	 * Creates an instance of {@link CertificateImpl} from the given certificate.
	 * @param cert The certificate.
	 */
	public CertificateImpl(X509Certificate cert) {
		super("certificate");
		serial = new TableField<BigInteger>("serialnumber",
				cert.getSerialNumber(), true,
				new ReferenceValueResolver<BigInteger>() {

					@Override
					public Object getResolvedValue(BigInteger origignal) {
						return origignal.toByteArray();
					}
				});
		certificate = new TableField<X509Certificate>("content", cert, false,
				new ReferenceValueResolver<X509Certificate>() {

					@Override
					public Object getResolvedValue(X509Certificate origignal) {
						try {
							return origignal.getEncoded();
						} catch (CertificateEncodingException e) {
							throw new RuntimeException(e);
						}
					}
				});
	}

	@Override
	public BigInteger getSerialNumber() {
		return serial.get();
	}

	@Override
	public X509Certificate getCertificate() {
		return certificate.get();
	}

}
