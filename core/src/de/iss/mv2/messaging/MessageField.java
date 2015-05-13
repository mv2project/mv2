package de.iss.mv2.messaging;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.Arrays;

import de.iss.mv2.data.BinaryTools;

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
	 * Holds the binary content.
	 */
	private byte[] binaryContent = null;

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
	 * @param field The field type.
	 * @param content The binary content of this field.
	 */
	public MessageField(DEF_MESSAGE_FIELD field, byte[] content){
		super(field.getIdentifier());
		binaryContent = content;
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
	 * @return The stream with the binary content data or {@code null} if there is none.
	 */
	public InputStream getDataContent(){
		if(binaryContent == null) return null;
		return new ByteArrayInputStream(binaryContent);
	}
	
	/**
	 * Returns a copy of the containing binary data.
	 * @return A copy of the containing binary data or {@code null} if there is none.
	 */
	public byte[] getDataArrayContent(){
		if(binaryContent == null) return null;
		return Arrays.copyOf(binaryContent, binaryContent.length);
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
		if(binaryContent != null){
			out.write(binaryContent);
			return;
		}
		OutputStreamWriter writer = new OutputStreamWriter(out, encoding);
		writer.write(getContent());
		writer.flush();
	}

	/**
	 * Holds a stream to be read.
	 */
	private InputStream deserializeInput;

	@Override
	public final void deserialize(InputStream in) {
		this.deserializeInput = in;
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
		if (deserializeInput == null)
			return;
		doDeserialize(deserializeInput, encoding);
		deserializeInput = null;
	}

	@Override
	protected void doDeserialize(InputStream in, Charset encdoding)
			throws IOException {
		DEF_MESSAGE_FIELD dmf = DEF_MESSAGE_FIELD.find(getFieldIdentifier());
		if (dmf.getContentType() == CONTENT_TYPE.BINARY) {
			doDeserializeBinary(in);
		} else {
			doDeserializeString(in, encdoding);
		}
	}

	/**
	 * Copies the content of the given input steam to a internal store.
	 * 
	 * @param in
	 *            The input stream to copy.
	 * @throws IOException
	 *             If an I/O error occurs.
	 */
	private void doDeserializeBinary(InputStream in) throws IOException {
		binaryContent = BinaryTools.readAll(in);
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
		return getFieldIdentifier() + " ("
				+ DEF_MESSAGE_FIELD.find(getFieldIdentifier()) + "): "
				+ getContent();
	}

}
