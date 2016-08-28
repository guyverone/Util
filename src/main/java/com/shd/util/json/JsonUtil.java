package com.shd.util.json;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.JSONUtils;

import java.sql.Timestamp;
import java.util.Map;

public class JsonUtil {

	@SuppressWarnings("rawtypes")
	public static Object jsonToBean(String json, Class clazz) {
        String[] formats = {"yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd"};
        JSONUtils.getMorpherRegistry().registerMorpher(new TimestampMorpher(formats));
        JSONObject jsonObject = JSONObject.fromObject(json);
        return JSONObject.toBean(jsonObject, clazz);
    }
	
	@SuppressWarnings("rawtypes")
	public static Object jsonToBean(String json, Class clazz, Map<String, Class> map) {
		String[] formats = {"yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd"};
		JSONUtils.getMorpherRegistry().registerMorpher(new TimestampMorpher(formats));
		JSONObject jsonObject = JSONObject.fromObject(json);
		return JSONObject.toBean(jsonObject, clazz, map);
	}
	
	@SuppressWarnings("rawtypes")
	public static Object jsonToArray(String json, Class clazz) {
		String[] formats = {"yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd"};
		JSONUtils.getMorpherRegistry().registerMorpher(new TimestampMorpher(formats));
		return JSONArray.toArray(JSONArray.fromObject(json), clazz);
	}
	
	@SuppressWarnings("rawtypes")
	public static Object jsonToArray(String json, Class clazz, Map<String, Class> map) {
		String[] formats = {"yyyy-MM-dd HH:mm:ss", "yyyy-MM-dd"};
		JSONUtils.getMorpherRegistry().registerMorpher(new TimestampMorpher(formats));
		return JSONArray.toArray(JSONArray.fromObject(json), clazz, map);
	}

    public static String beanToJson(Object obj, String dateFormat) {
        JsonConfig config = new JsonConfig();
        config.registerJsonValueProcessor(Timestamp.class, new DateJsonValueProcessor(dateFormat));
        JSONObject json = JSONObject.fromObject(obj, config);
        return json.toString();
    }
    
    public static String beanToJson(Object obj) {
    	return beanToJson(obj, "yyyy-MM-dd HH:mm:ss");
    }
}