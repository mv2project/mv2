package de.iss.mv2.server.data;

import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
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
			return new MessageImpl(rs.getLong("idmessage") ,webSpace, new Date(rs.getTimestamp("timestamp").getTime()), content);
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * The SQL statement to request all messages for a timestamp later than the give one.
	 */
	private static final String GET_MESSAGES_WITH_DATE = "SELECT idmessage FROM message WHERE receiver=? AND timestamp > ?;";

	
	@Override
	public List<Long> getMessages(WebSpace webSpace, Date notBefore) {
		try{
			PreparedStatement ps = context.getConnection().prepareStatement(GET_MESSAGES_WITH_DATE);
			ps.setString(1, webSpace.getIdentifier());
			ps.setTimestamp(2, new Timestamp(notBefore.getTime()));
			ResultSet rs = ps.executeQuery();
			List<Long> result = new ArrayList<Long>();
			while(rs.next()){
				result.add(rs.getLong("idmessage"));
			}
			return result;
		}catch(SQLException e){
			throw new RuntimeException(e);
		}
	}

	/**
	 * The SQL statement to request a message by its identifier.
	 */
	private static final String GET_MESSAGE_WITH_IDENTIFIER = "SELECT * FROM message WHERE idmessage=? AND receiver=?;";
	
	@Override
	public Message getMessage(WebSpace webSpace, long identifier) {
		try{
			PreparedStatement ps = context.getConnection().prepareStatement(GET_MESSAGE_WITH_IDENTIFIER);
			ps.setLong(1, identifier);
			ps.setString(2, webSpace.getIdentifier());
			ResultSet rs = ps.executeQuery();
			if(!rs.next()) throw new NoSuchElementException("There is no mail with given identifier.");
			return new MessageImpl(identifier, webSpace, new Date(rs.getTimestamp("timestamp").getTime()), rs.getBytes("content"));
		}catch(SQLException ex){
			throw new RuntimeException(ex);
		}
	}

	/**
	 * The SQL statement to store the private key of a web space.
	 */
	private static final String STORE_KEY = "UPDATE webspace SET keypassphrase=?, key=? WEHERE identifier=?;";
	
	@Override
	public void setPrivateKey(WebSpace webSpace, byte[] passphrase,
			byte[] privateKey) {
		try{
			PreparedStatement ps = context.getConnection().prepareStatement(STORE_KEY);
			ps.setBytes(1, passphrase);
			ps.setBytes(2, privateKey);
			ps.setString(3, webSpace.getIdentifier());
			ps.executeUpdate();
		}catch(SQLException ex){
			throw new RuntimeException(ex);
		}
	}

	/**
	 * The SQL statement to request the private key of a web space.
	 */
	private static final String GET_KEY = "SELECT key, keypassphrase FROM webspace WHERE identifier=?;";
	
	@Override
	public byte[] getPrivateKey(WebSpace webSpace, byte[] passphrase)
			throws NoSuchElementException, IllegalArgumentException {
		try{
			PreparedStatement ps = context.getConnection().prepareStatement(GET_KEY);
			ps.setString(1, webSpace.getIdentifier());
			ResultSet rs = ps.executeQuery();
			if(!rs.next()) throw new RuntimeException("Can't find an entry for the given webspace.");
			byte[] phrase = rs.getBytes("keypassphrase");
			byte[] key = rs.getBytes("key");
			if(!Arrays.equals(phrase, passphrase)) throw new IllegalArgumentException("The given passphrase is invalid.");
			return key;
		}catch(SQLException ex){
			throw new IllegalArgumentException(ex);
		}
	}
	
	

}
