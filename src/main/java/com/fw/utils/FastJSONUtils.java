package com.fw.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections4.KeyValue;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.alibaba.fastjson.serializer.JSONLibDataFormatSerializer;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;

/**
 * json操作工具类
 * 
 * @author yangtao
 *
 */
public final class FastJSONUtils {
	
	private static final SerializeConfig config;  
    static {  
        config = new SerializeConfig();  
        config.put(java.util.Date.class, new JSONLibDataFormatSerializer()); // 使用和json-lib兼容的日期输出格式  
        config.put(java.sql.Date.class, new JSONLibDataFormatSerializer()); // 使用和json-lib兼容的日期输出格式  
    }  
	
	/**
	 * 默认json格式化方式
	 *
	 */
	private static final SerializerFeature[] features = { SerializerFeature.WriteMapNullValue, // 输出空置字段
			SerializerFeature.WriteNullListAsEmpty, // list字段如果为null，输出为[]，而不是null
			SerializerFeature.WriteNullNumberAsZero, // 数值字段如果为null，输出为0，而不是null
			SerializerFeature.WriteNullBooleanAsFalse, // Boolean字段如果为null，输出为false，而不是null
			SerializerFeature.WriteNullStringAsEmpty // 字符类型字段如果为null，输出为""，而不是null
	};

	private FastJSONUtils() {
	}
	public static String toJSONString(Object object) {  
        return JSON.toJSONString(object, config, features);  
    }  
      
    public static String toJSONNoFeatures(Object object) {  
        return JSON.toJSONString(object, config);  
    }  
      
  
  
    public static Object toBean(String text) {  
        return JSON.parse(text);  
    }  
  
    public static <T> T toBean(String text, Class<T> clazz) {  
        return JSON.parseObject(text, clazz);  
    }  
  
    // 转换为数组  
    public static <T> Object[] toArray(String text) {  
        return toArray(text, null);  
    }  
  
    // 转换为数组  
    public static <T> Object[] toArray(String text, Class<T> clazz) {  
        return JSON.parseArray(text, clazz).toArray();  
    }  
  
    // 转换为List  
    @SuppressWarnings("rawtypes")
	public static <T> List<Map> toList(String text, Class<T> clazz) {  
        return JSON.parseArray(text, Map.class);  
    }  
  
    /**  
     * 将javabean转化为序列化的json字符串  
     * @param keyvalue  
     * @return  
     */  
    @SuppressWarnings("rawtypes")
	public static Object beanToJson(KeyValue keyvalue) {  
        String textJson = JSON.toJSONString(keyvalue);  
        Object objectJson  = JSON.parse(textJson);  
        return objectJson;  
    }  
      
    /**  
     * 将string转化为序列化的json字符串  
     * @param keyvalue  
     * @return  
     */  
    public static Object textToJson(String text) {  
        Object objectJson  = JSON.parse(text);  
        return objectJson;  
    }  
      
    /**  
     * json字符串转化为map  
     * @param s  
     * @return  
     */  
    @SuppressWarnings("rawtypes")
	public static Map stringToMap(String s) {  
        Map m = JSONObject.parseObject(s);  
        return m;  
    }  
      
    /**  
     * 将map转化为string  
     * @param m  
     * @return  
     */  
    @SuppressWarnings("rawtypes")
	public static String collectToString(Map m) {  
        String s = JSONObject.toJSONString(m);  
        return s;  
    }  
	/**
	 * 用自定义模板从json字符串中取值
	 * 
	 * @param json
	 * @param template
	 *            demo:items.items.x_item.0[].open_iid---->0[]：[]表示数组，0表示取第几个
	 * @return
	 * @author yangtao
	 */
	public static String getJSONValueByTemplate(String json, String template) {
		String keys[] = template.split("\\.");
		for (String key : keys) {
			if (json == null) {
				return null;
			}
			if (key.indexOf("[]") > 0) {
				json = JSON.parseObject(json, new TypeReference<List<String>>() {
				}).get(Integer.parseInt(key.replace("[]", "")));
			} else {
				json = JSON.parseObject(json).getString(key);
			}
		}
		return json;
	}

	/**
	 * JSON转Map
	 * 
	 * @param jsonStr
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static Map<String, Object> parseJSON2Map(String jsonStr) {
		Map<String, Object> map = new HashMap<String, Object>();
		// 最外层解析
		JSONObject json = JSONObject.parseObject(jsonStr);
		for (Object k : json.keySet()) {
			Object v = json.get(k);
			// 如果内层还是数组的话，继续解析
			if (v instanceof JSONArray) {
				// 数组对象又分为两种（简单数组或键值对）
				List list = parseJSON2List(json.get(k).toString());
				map.put((String) k, list);
			} else {
				map.put((String) k, v);
			}
		}
		return map;
	}

	/**
	 * JSON转List<Map<String, Object>>
	 * 
	 * @param json
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List<Map<String, Object>> parseJSON2List(String json) {
		JSONArray jsonArray = JSONArray.parseArray(json);
		List list = new ArrayList();
		for (Object object : jsonArray) {
			try {
				JSONObject jsonObject = (JSONObject) object;
				HashMap<String, Object> map = new HashMap<String, Object>();
				for (Map.Entry entry : jsonObject.entrySet()) {
					if (entry.getValue() instanceof JSONArray) {
						map.put((String) entry.getKey(), parseJSON2List(entry.getValue().toString()));
					} else {
						map.put((String) entry.getKey(), entry.getValue());
					}
				}
				list.add(map);
			} catch (Exception e) {
				list.add(object);
			}
		}
		return list;
	}
}