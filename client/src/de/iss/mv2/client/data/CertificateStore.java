package de.iss.mv2.client.data;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Properties;
import java.util.Set;

import de.iss.mv2.data.Exportable;
import de.iss.mv2.data.PropertiesExportable;
import de.iss.mv2.security.CertificateNameReader;
import de.iss.mv2.security.PEMFileIO;

/**
 * Represents a store for server certificates.
 * 
 * @author Marcel Singer
 *
 */
public class CertificateStore extends Exportable implements
		Set<X509Certificate> {

	/**
	 * Internal store for the certificates.
	 */
	private final Set<X509Certificate> internalStore = new HashSet<X509Certificate>();

	/**
	 * Creates a new instance of {@link CertificateStore}.
	 */
	public CertificateStore() {

	}

	@Override
	public int size() {
		return internalStore.size();
	}

	@Override
	public boolean isEmpty() {
		return internalStore.isEmpty();
	}

	@Override
	public boolean contains(Object o) {
		return internalStore.contains(o);
	}

	@Override
	public Iterator<X509Certificate> iterator() {
		return internalStore.iterator();
	}

	@Override
	public Object[] toArray() {
		return internalStore.toArray();
	}

	@Override
	public <T> T[] toArray(T[] a) {
		return internalStore.toArray(a);
	}

	/**
	 * Adds the given certificate to this store.
	 * @param e The certificate to add.
	 * @return {@code true} if this set did not already contain the specified certificate.
	 * @throws IllegalArgumentException Is thrown if the given certificate is null.
	 */
	@Override
	public boolean add(X509Certificate e) throws IllegalArgumentException {
		if(e == null) throw new IllegalArgumentException("The certificate to add may not be null.");
		return internalStore.add(e);
	}

	@Override
	public boolean remove(Object o) {
		return internalStore.remove(o);
	}

	@Override
	public boolean containsAll(Collection<?> c) {
		return internalStore.containsAll(c);
	}

	@Override
	public boolean addAll(Collection<? extends X509Certificate> c) {
		return internalStore.addAll(c);
	}

	@Override
	public boolean retainAll(Collection<?> c) {
		return internalStore.retainAll(c);
	}

	@Override
	public boolean removeAll(Collection<?> c) {
		return internalStore.removeAll(c);
	}

	@Override
	public void clear() {
		internalStore.clear();
	}

	/**
	 * Returns the first occurring certificate with the given common name.
	 * @param commonName The common name of the certificate to 
	 * @return The first occurring certificate with the given common name or {@code null} if there is none.
	 */
	public X509Certificate getCertificate(String commonName){
		for(X509Certificate cert : this){
			if(new CertificateNameReader(cert.getSubjectX500Principal()).getCommonName().equals(commonName)) return cert;
		}
		return null;
	}
	
	/**
	 * Tests if there is a certificate with the given common name.
	 * @param commonName The common name to search for.
	 * @return {@code true} if there is a certificate with the given common name.
	 */
	public boolean hasCertificate(String commonName){
		return getCertificate(commonName) != null;
	}
	
	/**
	 * Tests if the given certificate is present in this certificate store.
	 * @param certificate The certificate to check.
	 * @return {@code true} if this certificate store contains the given certificate.
	 */
	public boolean hasCertificate(X509Certificate certificate){
		for(X509Certificate c : this) if(c.equals(certificate)) return true;
		return false;
	}
	
	@Override
	protected void exportContent(OutputStream out) throws IOException {
		Encoder enc = Base64.getEncoder();
		int i = 0;
		PropertiesExportable propExp = new PropertiesExportable();
		Properties props = propExp.getProperties();
		try {
			for (X509Certificate cer : internalStore) {
				props.put("" + i, enc.encodeToString(cer.getEncoded()));
				i++;
			}
		} catch (CertificateEncodingException e) {
			throw new IOException(e);
		}
		propExp.export(out);
	}

	@Override
	protected void importContent(InputStream in) throws IOException {
		internalStore.clear();
		PropertiesExportable propExp = new PropertiesExportable();
		propExp.importData(in);
		Decoder dec = Base64.getDecoder();
		ByteArrayInputStream bin;
		PEMFileIO pemIO = new PEMFileIO();
		String enc;
		try {
			for (Object o : propExp.getProperties().values()) {
				if (!String.class.isAssignableFrom(o.getClass()))
					continue;
				enc = (String) o;
				bin = new ByteArrayInputStream(dec.decode(enc));
				internalStore.add(pemIO.readCertificate(bin));
				bin.close();
			}
		} catch (CertificateException e) {
			throw new IOException();
		}
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == null) return false;
		if(CertificateStore.class.isAssignableFrom(obj.getClass())){
			CertificateStore cs = (CertificateStore) obj;
			return internalStore.equals(cs.internalStore);
		}
		return super.equals(obj);
	}
	
	
	
}
