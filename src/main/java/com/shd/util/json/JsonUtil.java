package com.shd.util.json;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.JSONUtils;

import java.sql.Timestamp;
import java.util.Map;

/**
 * JsonUtil: use for json to bean, json to array, bean to json.
 */
public class JsonUtil {

	private static  String dateTime = "yyyy-MM-dd HH:mm:ss";
	private static  String date = "yyyy-MM-dd";
	private static  String[] formats = {dateTime, date};
	private JsonUtil(){}

	/**
	 *
	 * @param json
	 * @param clazz
     * @return
     */
	@SuppressWarnings("rawtypes")
	public static Object jsonToBean(String json, Class clazz) {
        JSONUtils.getMorpherRegistry().registerMorpher(new TimestampMorpher(formats));
        JSONObject jsonObject = JSONObject.fromObject(json);
        return JSONObject.toBean(jsonObject, clazz);
    }

	/**
	 *
	 * @param json
	 * @param clazz
	 * @param map
     * @return
     */
	@SuppressWarnings("rawtypes")
	public static Object jsonToBean(String json, Class clazz, Map<String, Class> map) {
		JSONUtils.getMorpherRegistry().registerMorpher(new TimestampMorpher(formats));
		JSONObject jsonObject = JSONObject.fromObject(json);
		return JSONObject.toBean(jsonObject, clazz, map);
	}

	/**
	 *
	 * @param json
	 * @param clazz
     * @return
     */
	@SuppressWarnings("rawtypes")
	public static Object jsonToArray(String json, Class clazz) {
		JSONUtils.getMorpherRegistry().registerMorpher(new TimestampMorpher(formats));
		return JSONArray.toArray(JSONArray.fromObject(json), clazz);
	}

	/**
	 *
	 * @param json
	 * @param clazz
	 * @param map
     * @return
     */
	@SuppressWarnings("rawtypes")
	public static Object jsonToArray(String json, Class clazz, Map<String, Class> map) {
		JSONUtils.getMorpherRegistry().registerMorpher(new TimestampMorpher(formats));
		return JSONArray.toArray(JSONArray.fromObject(json), clazz, map);
	}

	/**
	 *
	 * @param obj
	 * @param dateFormat
     * @return
     */
    public static String beanToJson(Object obj, String dateFormat) {
        JsonConfig config = new JsonConfig();
        config.registerJsonValueProcessor(Timestamp.class, new DateJsonValueProcessor(dateFormat));
        JSONObject json = JSONObject.fromObject(obj, config);
        return json.toString();
    }

    /**
	 *
	 * @param obj
	 * @return
     */
    public static String beanToJson(Object obj) {
    	return beanToJson(obj, dateTime);
    }
}