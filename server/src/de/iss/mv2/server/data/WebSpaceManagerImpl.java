package de.iss.mv2.server.data;

import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.NoSuchElementException;

import org.bouncycastle.pkcs.PKCS10CertificationRequest;

import de.iss.mv2.data.Certificate;
import de.iss.mv2.data.CertificateManager;
import de.iss.mv2.sql.SQLContext;

/**
 * The default implementation of {@link WebSpaceManager}.
 * 
 * @author Marcel Singer
 *
 */
public class WebSpaceManagerImpl implements WebSpaceManager {

	/**
	 * Holds the database context.
	 */
	private SQLContext context;

	/**
	 * Holds the certificate manager.
	 */
	private CertificateManager certManager;

	/**
	 * The create statement for a web space.
	 */
	private static final String CREATE_WEB_SPACE = "INSERT INTO webspace (identifier, certificate) VALUES (?, ?);";

	/**
	 * The SQL select statement to load a web space.
	 */
	private static final String LOAD_WEB_SPACE = "SELECT * FROM webspace WHERE identifier=?;";

	/**
	 * Creates a new instance of {@link WebSpaceManagerImpl}.
	 * 
	 * @param context
	 *            The context with the connection to the database.
	 * @param certManager
	 *            The certificate manager to use.
	 */
	public WebSpaceManagerImpl(SQLContext context,
			CertificateManager certManager) {
		this.context = context;
		this.certManager = certManager;
	}

	@Override
	public boolean isUnambiguously(String value) {
		try {
			getWebSpace(value);
		} catch (NoSuchElementException ex) {
			return true;
		}
		return false;
	}

	@Override
	public WebSpace createWebSpace(String identifier, Certificate cert) {
		try {
			PreparedStatement ps = context.getConnection().prepareStatement(
					CREATE_WEB_SPACE);
			ps.setString(1, identifier);
			ps.setBytes(2, cert.getCertificate().getSerialNumber()
					.toByteArray());
			ps.execute();
			WebSpaceImpl wsi = new WebSpaceImpl(identifier, cert);
			return wsi;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public WebSpace getWebSpace(String identifier)
			throws NoSuchElementException {
		try {
			PreparedStatement ps = context.getConnection().prepareStatement(
					LOAD_WEB_SPACE);
			ps.setString(1, identifier);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				Certificate cert = certManager.load(new BigInteger(rs
						.getBytes("certificate")));
				return new WebSpaceImpl(rs.getString("identifier"), cert);
			}
			throw new NoSuchElementException(
					"No web space found for the given identifier ("
							+ identifier + ").");
		} catch (SQLException ex) {
			throw new RuntimeException(ex);
		}
	}

	@Override
	public boolean canCreate(PKCS10CertificationRequest request)
			throws IllegalArgumentException {
		return true;
	}

	/**
	 * The SQL command to persist an incoming message.
	 */
	private static final String CREATE_MESSAGE = "INSERT INTO message (receiver, content) VALUES (?, ?); SELECT * FROM message WHERE idmessage = currval('message_idmessage_seq');";

	@Override
	public Message storeMessage(WebSpace webSpace, byte[] content) {
		try {
			PreparedStatement ps = context.getConnection().prepareStatement(
					CREATE_MESSAGE);
			ps.setString(1, webSpace.getIdentifier());
			ps.setBytes(2, content);
			ps.execute();
			ps.getMoreResults();
			ResultSet rs = ps.getResultSet();
			rs.next();
			return new MessageImpl(webSpace, new Date(rs.getTimestamp("timestamp").getTime()), content);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

}
