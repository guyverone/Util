package com.shd.util.json;

import net.sf.ezmorph.MorphException;
import net.sf.ezmorph.object.AbstractObjectMorpher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;

/**
 * To convert the String type of datetime into Timestamp type of datetime
 */
public class TimestampMorpher extends AbstractObjectMorpher {

	private static Logger logger = LoggerFactory.getLogger(TimestampMorpher.class);
	/*** datetime pattern */
	private String[] formats;

	/**
	 *
	 * @param formats
     */
	public TimestampMorpher(String[] formats) {
		this.formats = formats;
	}

	/**
	 * To convert the ClassType of parameter into Timestamp
	 * @param value time value that need to be converted to Timestamp
	 * @return
     */
	@Override
	public Object morph(Object value) {
		if (Timestamp.class.isAssignableFrom(value.getClass())) {
			return  value;
		}

		if(!isQualifyObjectToMorph(value)) {
			return null;
		}

		String strValue = (String) value;
		SimpleDateFormat dateParser = null;
		
		for (int i = 0; i < formats.length; i++) {
			if (null == dateParser) {
				dateParser = new SimpleDateFormat(formats[i]);
			} else {
				dateParser.applyPattern(formats[i]);
			}
			try {
				return new Timestamp(dateParser.parse(strValue.toLowerCase()).getTime());
			} catch (ParseException e) {
				logger.error(Arrays.toString(e.getStackTrace()), e);
			}
		}
		
		return null;
	}

	private boolean isQualifyObjectToMorph(Object value) {
		if (value == null) {
			return false;
		}
		if (!supports(value.getClass())) {
			throw new MorphException(value.getClass() + " type not support, only support value which of String type.");
		}
		return true;
	}

	@Override
	@SuppressWarnings("rawtypes")
	public Class morphsTo() {
		return Timestamp.class;
	}

	/**
	 *
	 * @param clazz
	 * @return
     */
	@SuppressWarnings("rawtypes")
	@Override
	public boolean supports(Class clazz) {
		return String.class.isAssignableFrom(clazz);
	}

}