package de.iss.mv2.data;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * A class to export a string.
 * @author Marcel Singer
 *
 */
public class StringExportable extends Exportable {

	/**
	 * Holds the current value.
	 */
	private String value;
	/**
	 * Holds the character set.
	 */
	private Charset charset = StandardCharsets.UTF_8;
	
	/**
	 * Creates a new Instance of {@link StringExportable}.
	 * @param value The string value to export.
	 */
	public StringExportable(String value) {
		this.value = value;
	}
	
	/**
	 * Creates a new Instance of {@link StringExportable} with an empty string.
	 */
	public StringExportable() {
		this("");
	}
	
	/**
	 * Returns the string content.
	 * @return The string content.
	 */
	public String getContent(){
		return value;
	}
	
	/**
	 * Returns the encoding to use.
	 * @return The encoding to use.
	 */
	public Charset getCharset(){
		return charset;
	}
	
	/**
	 * Sets the encoding to use.
	 * @param charset The encoding to use.
	 */
	public void setCharset(Charset charset){
		if(charset == null) charset = StandardCharsets.UTF_8;
		this.charset = charset;
	}
	
	/**
	 * Sets the string content.
	 * @param content The content to set.
	 */
	public void setContent(String content){ this.value = content; }
	
	@Override
	protected void exportContent(OutputStream out) throws IOException {
		out.write(value.getBytes(charset));
	}

	@Override
	protected void importContent(InputStream in) throws IOException {
		byte[] read = BinaryTools.readAll(in);
		value = new String(read, charset);
	}

}
