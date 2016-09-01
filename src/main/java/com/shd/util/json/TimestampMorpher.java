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
 * 将json串中的日期字符串转换为bean中的Timestamp
 */
public class TimestampMorpher extends AbstractObjectMorpher {

	private static Logger logger = LoggerFactory.getLogger(TimestampMorpher.class);
	/*** 日期字符串格式 */
	private String[] formats;

	/**
	 *
	 * @param formats
     */
	public TimestampMorpher(String[] formats) {
		this.formats = formats;
	}

	/**
	 *
	 * @param value
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
			throw new MorphException(value.getClass() + " 是不支持的类型");
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