package de.iss.mv2.messaging;

import static de.iss.mv2.data.BinaryTools.readBuffer;
import static de.iss.mv2.data.BinaryTools.readInt;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * A message that can be sent to a communication partner.
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
	 * @param messageIdentifier The identifier of this message.
	 */
	public MV2Message(int messageIdentifier) {
		super(messageIdentifier);
	}
	
	/**
	 * Creates a new instance of {@link MV2Message}.
	 * @param message The message type of this message.
	 */
	public MV2Message(STD_MESSAGE message){
		this(message.getIdentifier());
	}
	
	/**
	 * Sets up all fields before serialization.
	 */
	public final void cleanUp(){
		setMessageField(new ContentEncodingField(getEncoding()), true);
		
		doCleanUp();
	}
	
	/**
	 * Performs a clean up an returns the fields of this message.
	 * @return The cleaned fields of this message.
	 */
	protected Iterable<MessageField> getCleanedFields(){
		cleanUp();
		return fields.values();
	}
	
	/**
	 * Performs the cleaning before the serialization.
	 */
	protected void doCleanUp(){
		
	}
	
	/**
	 * Adds a field to this message.
	 * @param field The field to add.
	 * @param overwrite {@code true} if an existing field should be overwritten.
	 */
	public void setMessageField(MessageField field, boolean overwrite){
		if(field == null) return;
		if(!overwrite && fields.containsKey(field.getFieldIdentifier())) return;
		fields.put(field.getFieldIdentifier(), field);
	}
	
	/**
	 * Returns the field with the given identifier.
	 * @param fieldIdentifier The identifier of the field to return.
	 * @return The field with the given identifier or {@code null} if no field was found.
	 */
	public MessageField getField(int fieldIdentifier){
		if(!fields.containsKey(fieldIdentifier)) return null;
		return fields.get(fieldIdentifier);
	}
	
	/**
	 * Returns the field with the given identifier or throws an exception if no field was found.
	 * @param fieldIdentifier The identifier of the field to return.
	 * @return The field with the given identifier.
	 * @throws IOException Is thrown if no field was found.
	 */
	protected MessageField getFieldOrThrow(int fieldIdentifier) throws IOException{
		MessageField mf = getField(fieldIdentifier);
		if(mf == null) throw new IOException();
		return mf;
	}
	
	/**
	 * Returns the field with the given type or throws an exception if no field was found.
	 * @param field The type of the field to return.
	 * @return The field with the given identifier.
	 * @throws IOException Is thrown if no field was found.
	 */
	protected MessageField getFieldOrThrow(STD_MESSAGE_FIELD field) throws IOException{
		return getFieldOrThrow(field.getIdentifier());
	}
	
	/**
	 * Returns the value of the field with the given identifier.
	 * @param fieldIdentifier The identifier of the field thats content should be returned.
	 * @param defaultValue The value that should be returned if no field with the given identifier war found.
	 * @return The value of the field with the given identifier.
	 */
	public String getFieldValue(int fieldIdentifier, String defaultValue){
		if(!fields.containsKey(fieldIdentifier)) return defaultValue;
		return fields.get(fieldIdentifier).getContent();
	}
	
	/**
	 * Returns the value of the field with the given type.
	 * @param field The type of the field thats content should be returned.
	 * @param defaultValue The value that should be returned if no field with the given type war found.
	 * @return The value of the field with the given type.
	 */
	public String getFieldValue(STD_MESSAGE_FIELD field, String defaultValue){
		return getFieldValue(field.getIdentifier(), defaultValue);
	}
	
	@Override
	protected void doSerialize(OutputStream out, Charset encoding) throws IOException {
		cleanUp();
		for(MessageField mf : fields.values()){
			mf.setEncoding(getEncoding());
			mf.serialize(out);
		}
	}

	/**
	 * Returns the identifier of this message.
	 * @return The identifier of this message.
	 */
	public int getMessageIdentifier(){
		return getElementIdentifier();
	}

	@Override
	protected void doDeserialize(InputStream in, Charset encdoding) {
		
	}
	
	/**
	 * Clears all fields.
	 */
	protected void clearFields(){
		fields.clear();
	}
	
	
	
	@Override
	public void deserialize(InputStream in) throws IOException {
		super.deserialize( in);
		fields.clear();
		int identifier;
		int length;
		InputStream buffer;
		try{
			while(true){
				identifier = readInt(in);
				length = readInt(in);
				buffer = readBuffer(in, length);
				MessageField mf = new MessageField(identifier);
				mf.deserialize(buffer);
				fields.put(mf.getFieldIdentifier(), mf);
			}
		}catch(EOFException ex){
		}
		MessageField encodingField = getField(STD_MESSAGE_FIELD.CONTENT_ENCODING.getIdentifier());
		Charset encoding = StandardCharsets.UTF_8;
		if(encodingField != null){
			encodingField.completeDeserialize(StandardCharsets.US_ASCII);
			encoding = Charset.forName(encodingField.getContent());
		}
		setEncoding(encoding);
		for(MessageField mf : fields.values()){
			mf.setEncoding(encoding);
			mf.completeDeserialize(encoding);
		}
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Message-ID: " + getMessageIdentifier() + " (" + STD_MESSAGE.find(getMessageIdentifier()) + ")\n");
		for (MessageField mf : fields.values()) {
			sb.append("\t" + mf.toString() + "\n");
		}
		return sb.toString();
	}

}
