package de.iss.mv2.data;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * This is the base class for binary exportable and importable content.
 * @author Marcel Singer
 *
 */
public abstract class Exportable {

	/**
	 * Exports the content of this object to an output stream.
	 * @param out The output stream to use.
	 * @throws IOException if an I/O error occurs.
	 */
	protected abstract void exportContent(OutputStream out) throws IOException;
	
	/**
	 * Exports this object to an output stream.
	 * @param out The output stream to use.
	 * @throws IOException if an I/O error occurs.
	 */
	public void export(OutputStream out) throws IOException{
		exportContent(out);
		out.flush();
	}
	
	/**
	 * Imports the content of the given input stream.
	 * @param in The input stream to read.
	 * @throws IOException If an I/O error occurs.
	 */
	protected abstract void importContent(InputStream in) throws IOException;
	
	/**
	 * Imports from the given input stream.
	 * @param in The input stream to read.
	 * @throws IOException if an I/O error occurs.
	 */
	public void importData(InputStream in) throws IOException {
		importContent(in);
	}
	
}
