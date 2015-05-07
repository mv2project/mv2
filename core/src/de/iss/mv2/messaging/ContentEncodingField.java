package de.iss.mv2.messaging;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * A field of a message containing the encoding.
 * @author Marcel Singer
 *
 */
public class ContentEncodingField extends MessageField {

	/**
	 * Creates a new instance of {@link ContentEncodingField}.
	 * @param encoding The encoding to be set.
	 */
	public ContentEncodingField(Charset encoding) {
		super(DEF_MESSAGE_FIELD.CONTENT_ENCODING);
	}

	
	@Override
	public String getContent() {
		return getEncoding().name();
	}
	
	@Override
	protected void doSerialize(OutputStream out, Charset encoding)
			throws IOException {
		super.doSerialize(out, StandardCharsets.US_ASCII);
	}
	
	@Override
	protected void doDeserialize(InputStream in, Charset encdoding) throws IOException {
		super.doDeserialize(in, StandardCharsets.US_ASCII);
	}
	
	
}
