package de.iss.mv2.messaging;

import static de.iss.mv2.data.BinaryTools.toByteArray;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * The base class for all communication elements.
 * @author Marcel Singer
 *
 */
public abstract class MV2CommunicationElement {

	/**
	 * Holds the value that identifies this element.
	 */
	private int elementIdentifier;
	/**
	 * Holds the content encoding.
	 */
	private Charset encoding = StandardCharsets.UTF_8;

	/**
	 * Creates a new instanc of {@link MV2CommunicationElement}.
	 * @param elementIdentifier The identifier of this element.
	 */
	public MV2CommunicationElement(int elementIdentifier) {
		this.elementIdentifier = elementIdentifier;
	}

	/**
	 * Returns the identifier of this element.
	 * @return The identifier of this element.
	 */
	protected final int getElementIdentifier() {
		return elementIdentifier;
	}

	/**
	 * Returns the content encoding of this element.
	 * @return The content encoding of this element.
	 */
	public Charset getEncoding() {
		return encoding;
	}

	/**
	 * Sets the content encoding of this element.
	 * @param encoding The encoding to set.
	 */
	public void setEncoding(Charset encoding) {
		this.encoding = encoding;
	}


	/**
	 * Serializes the content element.
	 * @param out The output stream to write.
	 * @param encoding The encoding to use.
	 * @throws IOException if an I/O error occurs.
	 */
	protected abstract void doSerialize(OutputStream out, Charset encoding)
			throws IOException;

	/**
	 * Serializes this element.
	 * @param out The output stream to write.
	 * @throws IOException if an I/O error occurs.
	 */
	public void serialize(OutputStream out) throws IOException {
		ByteArrayOutputStream buffer = new ByteArrayOutputStream();
		doSerialize(buffer, encoding);
		int length = buffer.size();
		out.write(toByteArray(elementIdentifier));
		out.write(toByteArray(length));
		buffer.writeTo(out);
		out.flush();
	}

	/**
	 * Deserializes this element.
	 * @param in The input stream to read.
	 * @throws IOException if an I/O error occurs.
	 */
	public void deserialize(InputStream in) throws IOException {
		doDeserialize(in, getEncoding());
	}

	/**
	 * Deserializes the content of this element.
	 * @param in The input stream to read.
	 * @param encdoding The encoding to use.
	 * @throws IOException if an I/O error occurs.
	 */
	protected abstract void doDeserialize(InputStream in, Charset encdoding) throws IOException;

}
