package de.iss.mv2.messaging;

import static de.iss.mv2.data.BinaryTools.readInt;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import de.iss.mv2.io.DataSource;

/**
 * A message that can be sent to a communication partner.
 * 
 * @author Marcel Singer
 *
 */
public class MV2Message extends MV2CommunicationElement {

	/**
	 * Holds all fields of this message.
	 */
	private final Map<Integer, MessageField> fields = new HashMap<Integer, MessageField>();

	/**
	 * Creates a new instance of {@link MV2Message}.
	 * 
	 * @param messageIdentifier
	 *            The identifier of this message.
	 */
	public MV2Message(int messageIdentifier) {
		super(messageIdentifier);
	}

	/**
	 * Creates a new instance of {@link MV2Message}.
	 * 
	 * @param message
	 *            The message type of this message.
	 */
	public MV2Message(STD_MESSAGE message) {
		this(message.getIdentifier());
	}

	/**
	 * Sets up all fields before serialization.
	 */
	public final void cleanUp() {
		setMessageField(new ContentEncodingField(getEncoding()), true);

		doCleanUp();
	}

	/**
	 * Performs a clean up an returns the fields of this message.
	 * 
	 * @return The cleaned fields of this message.
	 */
	protected Iterable<MessageField> getCleanedFields() {
		cleanUp();
		return fields.values();
	}

	/**
	 * Returns all currently present fields.
	 * 
	 * @return All currently present fields.
	 */
	protected Collection<MessageField> getFields() {
		return fields.values();
	}

	/**
	 * Performs the cleaning before the serialization.
	 */
	protected void doCleanUp() {

	}

	/**
	 * Adds a field to this message.
	 * 
	 * @param field
	 *            The field to add.
	 * @param overwrite
	 *            {@code true} if an existing field should be overwritten.
	 */
	public void setMessageField(MessageField field, boolean overwrite) {
		if (field == null)
			return;
		if (!overwrite && fields.containsKey(field.getFieldIdentifier()))
			return;
		fields.put(field.getFieldIdentifier(), field);
	}

	/**
	 * Returns the field with the given identifier.
	 * 
	 * @param fieldIdentifier
	 *            The identifier of the field to return.
	 * @return The field with the given identifier or {@code null} if no field
	 *         was found.
	 */
	public MessageField getField(int fieldIdentifier) {
		if (!fields.containsKey(fieldIdentifier))
			return null;
		return fields.get(fieldIdentifier);
	}

	/**
	 * Returns the field with the given identifier or throws an exception if no
	 * field was found.
	 * 
	 * @param fieldIdentifier
	 *            The identifier of the field to return.
	 * @return The field with the given identifier.
	 * @throws IOException
	 *             Is thrown if no field was found.
	 */
	protected MessageField getFieldOrThrow(int fieldIdentifier)
			throws IOException {
		MessageField mf = getField(fieldIdentifier);
		if (mf == null)
			throw new IOException();
		return mf;
	}

	/**
	 * Returns the field with the given type or throws an exception if no field
	 * was found.
	 * 
	 * @param field
	 *            The type of the field to return.
	 * @return The field with the given identifier.
	 * @throws IOException
	 *             Is thrown if no field was found.
	 */
	protected MessageField getFieldOrThrow(DEF_MESSAGE_FIELD field)
			throws IOException {
		return getFieldOrThrow(field.getIdentifier());
	}

	/**
	 * Returns the string value of the field with the given type.
	 * 
	 * @param field
	 *            The type of the field thats content should be returned.
	 * @param defaultValue
	 *            The value that should be returned if no field with the given
	 *            type war found.
	 * @return The value of the field with the given type.
	 */
	public String getFieldStringValue(DEF_MESSAGE_FIELD field,
			String defaultValue) {
		if (!fields.containsKey(field.getIdentifier()))
			return defaultValue;
		return fields.get(field.getIdentifier()).getContent();
	}

	/**
	 * Returns the {@link DataSource} for the given field.
	 * 
	 * @param field
	 *            The field thats {@link DataSource} should be returned.
	 * @param defaultValue
	 *            Is returned if there is no {@link DataSource} for the given
	 *            field.
	 * @return The {@link DataSource} for the given field.
	 */
	public DataSource getFieldData(DEF_MESSAGE_FIELD field, byte[] defaultValue) {
		if (!fields.containsKey(field.getIdentifier())) {
			if (defaultValue == null)
				return null;
			try {
				return DataSource.getDataSource(defaultValue);
			} catch (IOException ex) {
				return null;
			}
		}
		return fields.get(field.getIdentifier()).getData();
	}

	/**
	 * Returns the binary content of the field with the given type.
	 * 
	 * @param field
	 *            The type of the field thats content data should be returned.
	 * @param defaultValue
	 *            The value that should be returned is no field with the given
	 *            type was found.
	 * @return The binary content of the specified field.
	 */
	public byte[] getFieldArrayValue(DEF_MESSAGE_FIELD field,
			byte[] defaultValue) {
		if (!fields.containsKey(field.getIdentifier()))
			return defaultValue;
		try {
			return fields.get(field.getIdentifier()).getData().getBytes();
		} catch (IOException e) {
			e.printStackTrace();
			return new byte[0];
		}
	}

	/**
	 * Returns the binary content stream of the field with the given type.
	 * 
	 * @param field
	 *            The type of the field thats content data should be returned.
	 * @param defaultValue
	 *            The value that should be returned is no field with the given
	 *            type was found.
	 * @return The binary content stream of the specified field.
	 * @throws IOException
	 *             If an I/O error occurs.
	 */
	public InputStream getFieldContentData(DEF_MESSAGE_FIELD field,
			InputStream defaultValue) throws IOException {
		if (!fields.containsKey(field.getIdentifier()))
			return defaultValue;
		return fields.get(field.getIdentifier()).getDataContent();
	}

	@Override
	protected void doSerialize(OutputStream out, Charset encoding)
			throws IOException {
		cleanUp();
		for (MessageField mf : fields.values()) {
			mf.setEncoding(getEncoding());
			mf.serialize(out);
		}
	}

	/**
	 * Returns the identifier of this message.
	 * 
	 * @return The identifier of this message.
	 */
	public int getMessageIdentifier() {
		return getElementIdentifier();
	}

	@Override
	protected void doDeserialize(DataSource in, Charset encdoding) {

	}

	/**
	 * Clears all fields.
	 */
	protected void clearFields() {
		fields.clear();
	}

	/**
	 * Merges the fields of the given source to the target.
	 * 
	 * @param target
	 *            The target message.
	 * @param source
	 *            The source message.
	 */
	public static void merge(MV2Message target, MV2Message source) {
		for (MessageField mf : source.fields.values()) {
			target.setMessageField(mf, true);
		}
	}

	@Override
	public void deserialize(DataSource dataSource) throws IOException {
		super.deserialize(dataSource);
		fields.clear();
		int identifier;
		int length;
		InputStream in = dataSource.getStream();
		DataSource fieldData;
		try {
			while (true) {
				identifier = readInt(in);
				length = readInt(in);
				fieldData = DataSource.getDataSource(length);
				fieldData.importData(in);
				MessageField mf = new MessageField(identifier);
				mf.deserialize(fieldData);
				fields.put(mf.getFieldIdentifier(), mf);
			}
		} catch (EOFException ex) {
		}
		MessageField encodingField = getField(DEF_MESSAGE_FIELD.CONTENT_ENCODING
				.getIdentifier());
		Charset encoding = StandardCharsets.UTF_8;
		if (encodingField != null) {
			encodingField.completeDeserialize(StandardCharsets.US_ASCII);
			encoding = Charset.forName(encodingField.getContent());
		}
		setEncoding(encoding);
		for (MessageField mf : fields.values()) {
			mf.setEncoding(encoding);
			mf.completeDeserialize(encoding);
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Message-ID: " + getMessageIdentifier() + " ("
				+ STD_MESSAGE.find(getMessageIdentifier()) + ")\n");
		for (MessageField mf : fields.values()) {
			sb.append("\t" + mf.toString() + "\n");
		}
		return sb.toString();
	}

}
