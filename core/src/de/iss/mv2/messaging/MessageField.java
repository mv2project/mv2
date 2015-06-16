package de.iss.mv2.messaging;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.Base64;

import de.iss.mv2.io.ByteArrayDataSource;
import de.iss.mv2.io.DataSource;

/**
 * Represents a content field of a {@link MV2Message}.
 * 
 * @author Marcel Singer
 *
 */
public class MessageField extends MV2CommunicationElement {

	/**
	 * Holds the content of this field.
	 */
	private String content;

	/**
	 * Holds the content data.
	 */
	private DataSource contentData;

	/**
	 * Creates a new instance of {@link MessageField}.
	 * 
	 * @param fieldIdentifier
	 *            The identifier of this field.
	 * @param content
	 *            The content of this field.
	 */
	public MessageField(int fieldIdentifier, String content) {
		super(fieldIdentifier);
		this.content = content;
	}

	/**
	 * Creates a new instance of {@link MessageField}.
	 * 
	 * @param field
	 *            The field type.
	 * @param content
	 *            The content of this field.
	 */
	public MessageField(DEF_MESSAGE_FIELD field, String content) {
		this(field.getIdentifier(), content);
	}

	/**
	 * Creates a new instance of {@link MessageField}.
	 * 
	 * @param field
	 *            The field type.
	 * @param content
	 *            The binary content of this field.
	 */
	public MessageField(DEF_MESSAGE_FIELD field, byte[] content) {
		super(field.getIdentifier());
		contentData = new ByteArrayDataSource(content);
	}

	/**
	 * Creates a new instance of {@link MessageField}.
	 * 
	 * @param field
	 *            The field type.
	 */
	public MessageField(DEF_MESSAGE_FIELD field) {
		this(field.getIdentifier());
	}

	/**
	 * Creates a new instance of {@link MessageField}.
	 * 
	 * @param fieldIdentifier
	 *            The identifier of this field.
	 */
	public MessageField(int fieldIdentifier) {
		super(fieldIdentifier);
		this.content = "";
	}

	/**
	 * Returns the identifier of this field.
	 * 
	 * @return The identifier of this field.
	 */
	public int getFieldIdentifier() {
		return getElementIdentifier();
	}

	/**
	 * Returns the content of this field.
	 * 
	 * @return The content of this field.
	 */
	public String getContent() {
		return content;
	}

	/**
	 * Returns the containing binary data.
	 * 
	 * @return The stream with the binary content data or {@code null} if there
	 *         is none.
	 * @throws IOException
	 *             If an I/O error occurs.
	 */
	public InputStream getDataContent() throws IOException {
		return contentData.getStream();
	}

	/**
	 * Returns a copy of the containing binary data.
	 * 
	 * @return A copy of the containing binary data or {@code null} if there is
	 *         none.
	 * @throws IOException
	 *             If an I/O error occurs.
	 */
	@Deprecated
	public byte[] getDataArrayContent() throws IOException {
		return contentData.getBytes();
	}

	/**
	 * Sets the content of this field.
	 * 
	 * @param content
	 *            The content to be set.
	 */
	protected void setContent(String content) {
		this.content = content;
	}

	@Override
	protected void doSerialize(OutputStream out, Charset encoding)
			throws IOException {
		if (contentData != null) {
			contentData.export(out);
			return;
		}
		OutputStreamWriter writer = new OutputStreamWriter(out, encoding);
		writer.write(getContent());
		writer.flush();
	}

	/**
	 * Deserializes this message field.
	 * 
	 * @param dataSource
	 *            The data to read.
	 */
	@Override
	public final void deserialize(DataSource dataSource) {
		this.contentData = dataSource;
	}

	/**
	 * Completes the deserialization.
	 * 
	 * @param encoding
	 *            The encoding to use.
	 * @throws IOException
	 *             If an I/O error occurs.
	 */
	public void completeDeserialize(Charset encoding) throws IOException {
		if (contentData == null)
			return;
		doDeserialize(contentData, encoding);
	}

	/**
	 * Deserializes this element.
	 * 
	 * @param in
	 *            The input data to read from.
	 * @param encoding
	 *            The encoding to be used.
	 * @throws IOException
	 *             If an I/O error occurs.
	 */
	@Override
	protected void doDeserialize(DataSource in, Charset encoding)
			throws IOException {
		DEF_MESSAGE_FIELD dmf = DEF_MESSAGE_FIELD.find(getFieldIdentifier());
		if (dmf.getContentType() == CONTENT_TYPE.BINARY) {
			this.contentData = in;
		} else {
			doDeserializeString(in.getStream(), encoding);
		}
	}

	/**
	 * Deserializes an input stream to a string.
	 * 
	 * @param in
	 *            The input stream to deserialize.
	 * @param encoding
	 *            The encoding to use.
	 * @throws IOException
	 *             If an I/O error occurs.
	 */
	private void doDeserializeString(InputStream in, Charset encoding)
			throws IOException {
		InputStreamReader reader = new InputStreamReader(in, encoding);
		StringBuilder sb = new StringBuilder();
		int read;
		while ((read = reader.read()) != -1) {
			sb.append((char) read);
		}
		setContent(sb.toString());
	}

	@Override
	public String toString() {
		DEF_MESSAGE_FIELD fieldType = DEF_MESSAGE_FIELD
				.find(getFieldIdentifier());
		if (fieldType != DEF_MESSAGE_FIELD.UNKNOWN
				&& fieldType.getContentType() == CONTENT_TYPE.BINARY) {
			try {
				return getFieldIdentifier()
						+ " ("
						+ fieldType
						+ "): "
						+ Base64.getEncoder().encodeToString(
								getDataArrayContent());
			} catch (IOException e) {
				return getFieldIdentifier() + " (" + fieldType
						+ "): <CAN'T EVALUATE>";
			}
		}
		return getFieldIdentifier() + " (" + fieldType + "): " + getContent();
	}

}
