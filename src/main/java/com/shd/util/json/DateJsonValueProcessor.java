package com.shd.util.json;

import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;


/**
 * 将Bean中的Timestamp转换为json中的日期字符串
 */
public class DateJsonValueProcessor implements JsonValueProcessor {
	public static final String DEFAULT_DATE_PATTERN = "yyyy-MM-dd";
	private static Logger logger = LoggerFactory.getLogger(DateJsonValueProcessor.class);
	private DateFormat dateFormat;

	/**
	 * @param datePattern
     */
	public DateJsonValueProcessor(String datePattern) {
		try {
			dateFormat = new SimpleDateFormat(datePattern);
		} catch (Exception e) {
			logger.error(Arrays.toString(e.getStackTrace()), e);
			dateFormat = new SimpleDateFormat(DEFAULT_DATE_PATTERN);
		}
	}

	@Override
	public Object processArrayValue(Object value, JsonConfig jsonConfig) {
		return process(value);
	}

	@Override
	public Object processObjectValue(String key, Object value, JsonConfig jsonConfig) {
		return process(value);
	}

	private Object process(Object value) {
		if (null == value) {
			return "";
		}
		return dateFormat.format(value);
	}
}