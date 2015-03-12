package com.ebig.net.client;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.FileBody;
 
 
/**
 * HTTP�����࣬��װHttpClient4.3.x�������ṩ�򻯵�HTTP����
 * @author   lhl
 * @Date     Aug 12, 2014     
 */
public class HttpHelper {
 
    private static Integer socketTimeout            = 30000;
    private static Integer connectTimeout           = 6000;
    private static Integer connectionRequestTimeout = 50;
 
    /**
     * ʹ��Get��ʽ ����URL��ַ����ȡResponseContent����
     * 
     * @param url
     *            ������URL��ַ
     * @return ResponseContent ��������쳣�򷵻�null�����򷵻�ResponseContent����
     */
    public static ResponseContent getUrlRespContent(String url) {
        HttpClientWrapper hw = new HttpClientWrapper(connectionRequestTimeout, connectTimeout, socketTimeout);
        ResponseContent response = null;
        try {
            response = hw.getResponse(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
 
    /**
     * ʹ��Get��ʽ ����URL��ַ����ȡResponseContent����
     * 
     * @param url
     *            ������URL��ַ
     * @param urlEncoding
     *            ���룬����Ϊnull
     * @return ResponseContent ��������쳣�򷵻�null�����򷵻�ResponseContent����
     */
    public static ResponseContent getUrlRespContent(String url, String urlEncoding) {
        HttpClientWrapper hw = new HttpClientWrapper(connectionRequestTimeout, connectTimeout, socketTimeout);
        ResponseContent response = null;
        try {
            response = hw.getResponse(url, urlEncoding);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
 
    /**
     * ������ƴװ��url�У�����post����
     * 
     * @param url
     * @return
     */
    public static ResponseContent postUrl(String url) {
        HttpClientWrapper hw = new HttpClientWrapper();
        ResponseContent ret = null;
        try {
            setParams(url, hw);
            ret = hw.postNV(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }
 
    private static void setParams(String url, HttpClientWrapper hw) {
        String[] paramStr = url.split("[?]", 2);
        if (paramStr == null || paramStr.length != 2) {
            return;
        }
        String[] paramArray = paramStr[1].split("[&]");
        if (paramArray == null) {
            return;
        }
        for (String param : paramArray) {
            if (param == null || "".equals(param.trim())) {
                continue;
            }
            String[] keyValue = param.split("[=]", 2);
            if (keyValue == null || keyValue.length != 2) {
                continue;
            }
            hw.addNV(keyValue[0], keyValue[1]);
        }
    }
 
    /**
     * �ϴ��ļ�������ͼƬ��
     * 
     * @param url
     *            ����URL
     * @param paramsMap
     *            ������ֵ
     * @return
     */
    public static ResponseContent postEntity(String url, Map<String, Object> paramsMap) {
        HttpClientWrapper hw = new HttpClientWrapper();
        ResponseContent ret = null;
        try {
            setParams(url, hw);
            Iterator<String> iterator = paramsMap.keySet().iterator();
            while (iterator.hasNext()) {
                String key = iterator.next();
                Object value = paramsMap.get(key);
                if (value instanceof File) {
                    FileBody fileBody = new FileBody((File) value);
                    hw.getContentBodies().add(fileBody);
                } else if (value instanceof byte[]) {
                    byte[] byteVlue = (byte[]) value;
                    ByteArrayBody byteArrayBody = new ByteArrayBody(byteVlue, key);
                    hw.getContentBodies().add(byteArrayBody);
                } else {
                    if (value != null && !"".equals(value)) {
                        hw.addNV(key, String.valueOf(value));
                    } else {
                        hw.addNV(key, "");
                    }
                }
            }
            ret = hw.postEntity(url);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }
 
    /**
     * ʹ��post��ʽ����������ת�ɵ�json��Rest����
     * 
     * @param url
     * @param jsonBody
     * @return
     */
    public static ResponseContent postJsonEntity(String url, String jsonBody, Map<String, String> urlParams, Map<String,String> headerParams) {
        return postEntity(url, jsonBody, "application/json", urlParams, headerParams);
    }
 
    
    public static ResponseContent postJsonEntity(String url, Map<String, String> urlParams, Map<String,String> headerParams) {

        return postJsonEntity(url, null, urlParams, headerParams);
    } 
 
    public static ResponseContent postJsonEntity(String url, Map<String, String> urlParams) {

        return postJsonEntity(url, null, urlParams, null);
    } 
    
    /**
     * ʹ��post��ʽ����������ת�ɵ�xml��Rest����
     * 
     * @param url
     *            URL��ַ
     * @param xmlBody
     *            xml�ı��ַ���
     * @return ResponseContent ��������쳣�򷵻ؿգ����򷵻�ResponseContent����
     */
    public static ResponseContent postXmlEntity(String url, String xmlBody, Map<String, String> urlParams, Map<String,String> headerParams) {
        return postEntity(url, xmlBody, "application/xml", urlParams, headerParams);
    }
 
    public static ResponseContent postXmlEntity(String url, Map<String, String> urlParams, Map<String,String> headerParams) {
        return postXmlEntity(url, null, urlParams, headerParams);
    }
    
    public static ResponseContent postXmlEntity(String url, Map<String, String> urlParams) {
        return postXmlEntity(url, null, urlParams, null);
    }    
    
    public static ResponseContent post(String url, Map<String, String> urlParams, Map<String,String> headerParams ) {
        return postEntity(url, null, null, urlParams, headerParams);
    }
    
    public static ResponseContent post(String url, Map<String, String> urlParams) {
        return postEntity(url, null, null, urlParams, null);
    }
    
    private static ResponseContent postEntity(String url, String body, String contentType, Map<String, String> urlParams, Map<String,String> headerParams ) {
        HttpClientWrapper hw = new HttpClientWrapper();
        ResponseContent ret = null;
        try {
            hw.addNV("body", body);
            hw.buildHeader(headerParams);
            hw.buildGetUrl(urlParams);            
            ret = hw.postNV(url, contentType);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ret;
    }
   
 
    public static void main(String[] args) {
        testGet();
        //testUploadFile();
    }
 
    //test
    public static void testGet() {
        String url = "http://localhost:9999/appsrv/verifyimag.jsp";
        ResponseContent responseContent = getUrlRespContent(url);
        try {
            System.out.println(responseContent.getContent());
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }
 
    //test
    public static void testUploadFile() {
        try {
            String url = "http://localhost:8280/jfly/action/admin/user/upload.do";
            Map<String, Object> paramsMap = new HashMap<String, Object>();
            paramsMap.put("userName", "jj");
            paramsMap.put("password", "jj");
            paramsMap.put("filePath", new File("C:\\Pictures\\default (1).jpeg"));
            ResponseContent ret = postEntity(url, paramsMap);
            System.out.println(ret.getContent());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}