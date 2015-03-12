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
		config.put(java.util.Date.class, new JSONLibDataFormatSerializer()); // ʹ�ú�json-lib���ݵ����������ʽ
		config.put(java.sql.Date.class, new JSONLibDataFormatSerializer()); // ʹ�ú�json-lib���ݵ����������ʽ
	}

	private static final SerializerFeature[] features = {
			SerializerFeature.WriteMapNullValue, // ��������ֶ�
			SerializerFeature.WriteNullListAsEmpty, // list�ֶ����Ϊnull�����Ϊ[]��������null
			SerializerFeature.WriteNullNumberAsZero, // ��ֵ�ֶ����Ϊnull�����Ϊ0��������null
			SerializerFeature.WriteNullBooleanAsFalse, // Boolean�ֶ����Ϊnull�����Ϊfalse��������null
			SerializerFeature.WriteNullStringAsEmpty // �ַ������ֶ����Ϊnull�����Ϊ""��������null
	};
	
	private static final SerializerFeature[] prettyfeatures = {
		SerializerFeature.PrettyFormat, // ��������ֶ�
		SerializerFeature.WriteMapNullValue, // ��������ֶ�
		SerializerFeature.WriteNullListAsEmpty, // list�ֶ����Ϊnull�����Ϊ[]��������null
		SerializerFeature.WriteNullNumberAsZero, // ��ֵ�ֶ����Ϊnull�����Ϊ0��������null
		SerializerFeature.WriteNullBooleanAsFalse, // Boolean�ֶ����Ϊnull�����Ϊfalse��������null
		SerializerFeature.WriteNullStringAsEmpty // �ַ������ֶ����Ϊnull�����Ϊ""��������null
	};
	
	public final static int JSONFORMAT_ERROR = -1;//����json��ʽ 
	public final static int JSONFORMAT_OBJECT = 1;//����json��ʽ 
	public final static int JSONFORMAT_ARRAY = 2;//����json��ʽ 
	public final static int JSONFORMAT_OBJECT_WITHOUTBRACE = 3;//����json��ʽȱ�ٴ����� 
	public final static int JSONFORMAT_ARRAY_WITHOUTBRACKET = 4;//����json��ʽȱ��С����

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
	 * json�ַ���ת��ΪMap
	 * @param json json�ַ���
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
	 * json�ַ���ת��ΪList
	 * @param json json�ַ���
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
	 * ���json��ʽ
	 * @param json	json�ַ���
	 * @return �ο�����JSONFORMAT
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
