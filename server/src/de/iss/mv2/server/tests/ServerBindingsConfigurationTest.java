package de.iss.mv2.server.tests;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import de.iss.mv2.server.io.ServerBindingsConfiguration;

/**
 * A test for the {@link ServerBindingsConfiguration} class.
 * 
 * @author Marcel Singer
 *
 */
public class ServerBindingsConfigurationTest {

	/**
	 * The instance to use.
	 */
	private final ServerBindingsConfiguration config = new ServerBindingsConfiguration();

	/**
	 * Test the get and set methods.
	 */
	@Before
	public void testGetSetBinding() {
		config.addBinding("localhost", "localhost.cert");
		Set<String> bindings = config.getBindings();
		assertEquals(1, bindings.size());
		assertEquals("localhost", bindings.iterator().next());
		assertEquals("localhost.cert", config.getCertificatePath("localhost"));
	}

	/**
	 * Test the ex- and import.
	 * 
	 * @throws Exception
	 *             If anything goes wrong.
	 */
	@Test
	public void testExportImport() throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		config.write(baos);
		ServerBindingsConfiguration config2 = new ServerBindingsConfiguration();
		config2.read(new ByteArrayInputStream(baos.toByteArray()));
		assertEquals(config.getCertificatePath("localhost"),
				config2.getCertificatePath("localhost"));
	}

}
