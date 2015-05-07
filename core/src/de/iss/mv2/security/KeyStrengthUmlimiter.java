package de.iss.mv2.security;

import java.lang.reflect.Field;
import java.security.Permission;
import java.security.PermissionCollection;
import java.util.Map;

/**
 * Provides a method to disable the key size restriction of the current java
 * runtime.
 * 
 * @author Marcel Singer
 * @deprecated The functionality of this class may violate oracles terms of use
 *             and should not be used. However it is included to enable
 *             development on machines without the possibility to install the
 *             unlimited strength jurisdiction policy files.<br />
 * 				<br />
 *             If you can modify your java installation consider installing the
 *             unlimited strength jurisdiction policy files. They can be
 *             obtained at: <br />
 *             <ul>
 *             <li><a href=
 *             "http://www.oracle.com/technetwork/java/javase/downloads/jce8-download-2133166.html"
 *             >Java 8</a></li>
 *             <li><a href=
 *             "http://www.oracle.com/technetwork/java/javase/downloads/jce-7-download-432124.html"
 *             >Java 7</a></li>
 *             <li><a href=
 *             "http://www.oracle.com/technetwork/java/javase/downloads/jce-6-download-429243.html"
 *             >Java 6</a></li>
 *             </ul>
 */
@Deprecated
public class KeyStrengthUmlimiter {

	/**
	 * Prevent this class from being instantiated.
	 */
	private KeyStrengthUmlimiter() {

	}

	/**
	 * Removes the key size restrictions of the temporarily running java environment.
	 * @throws Exception If the restriction could not be removed.
	 */
	public static void removeCryptographyRestrictions() throws Exception {
		if (!isRestrictedCryptography()) {
			return;
		}

		final Class<?> jceSecurity = Class.forName("javax.crypto.JceSecurity");
		final Class<?> cryptoPermissions = Class
				.forName("javax.crypto.CryptoPermissions");
		final Class<?> cryptoAllPermission = Class
				.forName("javax.crypto.CryptoAllPermission");

		final Field isRestrictedField = jceSecurity
				.getDeclaredField("isRestricted");
		isRestrictedField.setAccessible(true);
		isRestrictedField.set(null, false);

		final Field defaultPolicyField = jceSecurity
				.getDeclaredField("defaultPolicy");
		defaultPolicyField.setAccessible(true);
		final PermissionCollection defaultPolicy = (PermissionCollection) defaultPolicyField
				.get(null);

		final Field perms = cryptoPermissions.getDeclaredField("perms");
		perms.setAccessible(true);
		((Map<?, ?>) perms.get(defaultPolicy)).clear();

		final Field instance = cryptoAllPermission.getDeclaredField("INSTANCE");
		instance.setAccessible(true);
		defaultPolicy.add((Permission) instance.get(null));
	}

	/**
	 * Checks if the current runtime is restricted.
	 * @return {@code true} if the current runtime is restricted.
	 */
	private static boolean isRestrictedCryptography() {
		return "Java(TM) SE Runtime Environment".equals(System
				.getProperty("java.runtime.name"));
	}

}
