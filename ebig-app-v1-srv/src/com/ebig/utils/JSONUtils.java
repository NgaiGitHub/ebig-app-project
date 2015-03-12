package com.ebig.utils;

import java.util.List;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.JSONLibDataFormatSerializer;
import com.alibaba.fastjson.serializer.SerializeConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class JSONUtils {
	private static final SerializeConfig config;
	static {
		config = new SerializeConfig();
		config.put(java.util.Date.class, new JSONLibDataFormatSerializer()); // 使用和json-lib兼容的日期输出格式
		config.put(java.sql.Date.class, new JSONLibDataFormatSerializer()); // 使用和json-lib兼容的日期输出格式
	}

	private static final SerializerFeature[] features = {
			SerializerFeature.WriteMapNullValue, // 输出空置字段
			SerializerFeature.WriteNullListAsEmpty, // list字段如果为null，输出为[]，而不是null
			SerializerFeature.WriteNullNumberAsZero, // 数值字段如果为null，输出为0，而不是null
			SerializerFeature.WriteNullBooleanAsFalse, // Boolean字段如果为null，输出为false，而不是null
			SerializerFeature.WriteNullStringAsEmpty // 字符类型字段如果为null，输出为""，而不是null
	};
	
	private static final SerializerFeature[] prettyfeatures = {
		SerializerFeature.PrettyFormat, // 输出空置字段
		SerializerFeature.WriteMapNullValue, // 输出空置字段
		SerializerFeature.WriteNullListAsEmpty, // list字段如果为null，输出为[]，而不是null
		SerializerFeature.WriteNullNumberAsZero, // 数值字段如果为null，输出为0，而不是null
		SerializerFeature.WriteNullBooleanAsFalse, // Boolean字段如果为null，输出为false，而不是null
		SerializerFeature.WriteNullStringAsEmpty // 字符类型字段如果为null，输出为""，而不是null
	};
	
	public final static int JSONFORMAT_ERROR = -1;//错误json格式 
	public final static int JSONFORMAT_OBJECT = 1;//对象json格式 
	public final static int JSONFORMAT_ARRAY = 2;//数组json格式 
	public final static int JSONFORMAT_OBJECT_WITHOUTBRACE = 3;//对象json格式缺少大括号 
	public final static int JSONFORMAT_ARRAY_WITHOUTBRACKET = 4;//数组json格式缺少小括号

	public static String toJSONString(Map map){
		return JSON.toJSONString(map, config, features); 
	}
	
	public static String toJSONString(List list){
		return JSON.toJSONString(list, config, features); 
	}
	
	public static String toJSONString(Object obj){
		return JSON.toJSONString(obj, config, features); 
	}
	
	public static String toJSONPrettyFormat(Map map){
		return JSON.toJSONString(map, config, prettyfeatures); 
	}
	
	public static String toJSONPrettyFormat(List list){
		return JSON.toJSONString(list, config, prettyfeatures); 
	}
	
	public static String toJSONPrettyFormat(Object obj){
		return JSON.toJSONString(obj, config, prettyfeatures); 
	}
	
	/**
	 * json字符串转化为Map
	 * @param json json字符串
	 * @return
	 */
	public static Map json2Map(String json){
		StringBuffer sb = new StringBuffer();
		if(json.indexOf("{") != 0){
			sb.append("{");
		}
		sb.append(json);
		if(json.lastIndexOf("}") < json.length() - 1){
			sb.append("}");
		}
		return (Map) JSON.parse(sb.toString());
	}
	
	/**
	 * json字符串转化为List
	 * @param json json字符串
	 * @return
	 */
	public static List json2List(String json){
		StringBuffer sb = new StringBuffer();
		if( json.indexOf("[") != 0){
			sb.append("[");
		}
		sb.append(json);
		if( json.lastIndexOf("]") < json.length() - 1){
			sb.append("]");
		}
		return (List) JSON.parse(sb.toString());
	}
	
	/**
	 * 检查json格式
	 * @param json	json字符串
	 * @return 参考常量JSONFORMAT
	 */
	public static int checkJSONFormat(String json){
		boolean hasBrace = true;
		boolean hasBracket = true;
		boolean arrayFlag = true;
		boolean objectFlag = true;
		
		StringBuffer mapBuffer = new StringBuffer();
		StringBuffer listBuffer = new StringBuffer();
		if( json.indexOf("{") != 0){
			mapBuffer.append("{");
			hasBrace = false;
		}
		if( json.indexOf("[") != 0){
			listBuffer.append("[");
			hasBracket = false;
		}
		
		mapBuffer.append(json);
		listBuffer.append(json);
		
		if( json.indexOf("}") != 0){
			mapBuffer.append("}");
			hasBrace = false;
		}
		if( json.lastIndexOf("]") < json.length() - 1){
			listBuffer.append("]");
			hasBracket = false;
		}
		
		try {
			JSON.parse(mapBuffer.toString());
		} catch (Exception e) {
			objectFlag = false;
		}
		
		try {
			JSON.parse(listBuffer.toString());
		} catch (Exception e) {
			arrayFlag = false;
		}
		
		if(!objectFlag && !arrayFlag){
			
			return JSONFORMAT_ERROR;
			
		} else if(objectFlag && !hasBrace){
			
			return JSONFORMAT_OBJECT_WITHOUTBRACE;
			
		} else if(objectFlag && hasBrace){
			
			return JSONFORMAT_OBJECT;
			
		} else if(arrayFlag && !hasBracket){
			
			return JSONFORMAT_ARRAY_WITHOUTBRACKET;
			
		} else if(arrayFlag && hasBracket){
			
			return JSONFORMAT_ARRAY;
			
		}
		
		return JSONFORMAT_ERROR;
	}
	
	public static void main(String[] args){
		
		String json = "\"name,\":\"yangtze,\",\"age\":26,\"birthday\":\"1987-07-17 00:00:00\",\"school\":[{\"name\":\"SCAU\"},{\"name\":\"GZ5ZX\"},{\"name\":\"GZ108ZX\"}]";
		System.out.println(new java.util.Date()+json);
		Map map = json2Map(json);
		System.out.println(new java.util.Date()+toJSONString(map));
		List list = json2List("{name:\"SCAU\"},{\"name\":\"GZ5ZX\"},{\"name\":\"GZ108ZX\"}");
		System.out.println(new java.util.Date()+toJSONString(list));
	}
}
