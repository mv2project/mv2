package de.iss.mv2.messaging;

import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;
import java.util.Date;

/**
 * A message to request in-box messages of the current client.
 * 
 * @author Marcel Singer
 *
 */
public class MessageQueryRequestMessage extends MV2Message {

	/**
	 * Creates a new instance of {@link MessageQueryRequestMessage}.
	 */
	public MessageQueryRequestMessage() {
		super(STD_MESSAGE.MESSAGE_QUERY_REQUEST);
	}

	/**
	 * Sets the date of the earliest message to return.
	 * 
	 * @param date
	 *            The date of the earliest message to include.
	 */
	public void setNotBefore(Date date) {
		String value = "";
		if (date != null) {
			ZonedDateTime zdt = ZonedDateTime.ofInstant(date.toInstant(),
					ZoneId.systemDefault());
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern(
					"yyyy-MM-dd'T'HH:mmX").withZone(ZoneOffset.UTC);
			value = dtf.format(zdt);
		}
		setMessageField(new MessageField(DEF_MESSAGE_FIELD.NOT_BEFORE, value),
				true);
	}
	
	
	public Date getNotBefore(){
		String value = getFieldStringValue(DEF_MESSAGE_FIELD.NOT_BEFORE, null);
		if(value == null || value.isEmpty()) return null;
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern(
				"yyyy-MM-dd'T'HH:mmX").withZone(ZoneOffset.UTC);
		ZonedDateTime zdt = ZonedDateTime.parse(value, dtf);
		return Date.from(zdt.toLocalDateTime().get);
	}

}
