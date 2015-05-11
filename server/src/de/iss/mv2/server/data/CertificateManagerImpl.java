package de.iss.mv2.server.data;

import java.math.BigInteger;
import java.security.cert.X509Certificate;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import de.iss.mv2.data.Certificate;
import de.iss.mv2.data.CertificateManager;
import de.iss.mv2.sql.SQLContext;

/**
 * A {@link CertificateManager} that persists given certificates into a database.
 * @author Marcel Singer
 *
 */
public class CertificateManagerImpl implements CertificateManager {

	/**
	 * The SQL context to access the database.
	 */
	private final SQLContext context;

	/**
	 * Creates a new instance of {@link CertificateImpl}.
	 * @param context The given context to access the databse.
	 */
	public CertificateManagerImpl(SQLContext context) {
		this.context = context;
	}

	/**
	 * The SQL command to load a certificate with the given serial number from the database.
	 */
	private static final String LOAD_CERT = "SELECT * FROM certificate WHERE serialnumber = ?;";
	/**
	 * The SQL command to store a new certificate into the database.
	 */
	private static final String CREATE_CERT = "INSERT INTO certificate (serialnumber, content) VALUES (?, ?);";
	/**
	 * The SQL command to remove a certificate from the database.
	 */
	private static final String REMOVE_CERT = "DELETE FROM certificate WHERE serialnumber = ?;";

	@Override
	public boolean isUnambiguously(BigInteger value) {
		return load(value) == null;
	}

	@Override
	public Certificate load(BigInteger serialNumber) {
		try {
			PreparedStatement ps = context.getConnection().prepareStatement(
					LOAD_CERT);
			ps.setBytes(1, serialNumber.toByteArray());
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				return new CertificateImpl(rs);
			} else {
				return null;
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		}

	}

	@Override
	public void create(X509Certificate cert) {
		try {
			PreparedStatement ps = context.getConnection().prepareStatement(
					CREATE_CERT);
			ps.setBytes(1, cert.getSerialNumber().toByteArray());
			ps.setBytes(2, cert.getEncoded());
			ps.execute();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

	@Override
	public void remove(X509Certificate cert, boolean andRevoke) {
		// TODO Revoke umsetzen!
		try {
			PreparedStatement ps = context.getConnection().prepareStatement(
					REMOVE_CERT);
			ps.setBytes(1, cert.getSerialNumber().toByteArray());
			ps.execute();
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}
	}

}
