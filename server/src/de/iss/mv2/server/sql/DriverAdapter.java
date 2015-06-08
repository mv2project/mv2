package de.iss.mv2.server.sql;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverPropertyInfo;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Properties;
import java.util.logging.Logger;

/**
 * An adapter for drivers that are not part of the classpath.
 * @author Marcel Singer
 *
 */
public class DriverAdapter implements Driver {

	/**
	 * Holds the driver to wrap.
	 */
	private final Driver driver;
	
	/**
	 * Creates a new instance of {@link DriverAdapter} with the given driver to wrapp.
	 * @param driver The driver to wrapp.
	 */
	public DriverAdapter(Driver driver) {
		this.driver = driver;
	}
	
	@Override
	public Connection connect(String url, Properties info) throws SQLException {
		return driver.connect(url, info);
	}

	@Override
	public boolean acceptsURL(String url) throws SQLException {
		return driver.acceptsURL(url);
	}

	@Override
	public DriverPropertyInfo[] getPropertyInfo(String url, Properties info)
			throws SQLException {
		return driver.getPropertyInfo(url, info);
	}

	@Override
	public int getMajorVersion() {
		return driver.getMajorVersion();
	}

	@Override
	public int getMinorVersion() {
		return driver.getMinorVersion();
	}

	@Override
	public boolean jdbcCompliant() {
		return driver.jdbcCompliant();
	}

	@Override
	public Logger getParentLogger() throws SQLFeatureNotSupportedException {
		return driver.getParentLogger();
	}
	
	
	

	

}
