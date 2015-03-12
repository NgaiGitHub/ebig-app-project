package com.ebig.net.client;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


import com.ebig.utils.JSONUtils;



/**
 *
 * <p>Title: �ͻ��˵��ô����࣬��װ�˿ͻ��˵��÷������ķ�����ʵ�ֿͻ��˺ͷ�����ֱ�����ݵĽ�������</p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2010</p>
 * <p>Company: ebig</p>
 * @author lhl
 * @version 1.0
 */
public class HttpClient {
    
    private String PARAMS_PRIFIX = "ajaxParams_";
    private String VALUE_PRIFIX = "ajaxParamsValue_";          
    
    private String SERVLET_URL  = "/bacreatorServlet"; //��������̨URL
    
    private String LOGIN_URL  = "/loginactionservlet"; //��������½URL
    
    private String REGISTER_URL  = "/registerServlet"; //Appע��URL
    
    private String VERSION_URL = "/versionInfoServlet"; //app�汾���URL
    
    private String LOGINOUT_URL  = "/logout.jsp"; //��������½URL
    
    private static String WEBROOT_URL = "http://localhost:9999/appsrv";
    
    private static final String RESPONSETYPE = "RESPONSE_TYPE";
    
    private static final String CLTSESSIONKEY = "CLTSESSIONKEY";
    
    private static String RESPONSETYPE_OK = "0";  //�ص����ͣ�û�д���
    private static String RESPONSETYPE_ERR = "1"; //�ص����ͣ��д�����ϸ��Ϣ�ɴ�����Ϣ����    
    
    private static String DEVICECODE  = "mobile" ; 
    private Map<String,String> headerParams ;
    
    private static Integer socketTimeout            = 10 * 1000;
    private static Integer connectTimeout           = 5 * 1000;
    private static Integer connectionRequestTimeout = 3 * 1000;
    
    private static Map CLTSESSION = new HashMap();
    
         
    public HttpClient() {   
    	this.SERVLET_URL   = WEBROOT_URL + SERVLET_URL;
    	this.LOGIN_URL     = WEBROOT_URL + LOGIN_URL;
    	this.LOGINOUT_URL  = WEBROOT_URL + LOGINOUT_URL;
    	this.VERSION_URL   = WEBROOT_URL + VERSION_URL;
    	this.REGISTER_URL     = WEBROOT_URL + REGISTER_URL;
	}
    
    public HttpClient(String webrooturl) {   
    	WEBROOT_URL   = webrooturl;
    	this.SERVLET_URL   = WEBROOT_URL + SERVLET_URL;
    	this.LOGIN_URL     = WEBROOT_URL + LOGIN_URL;
    	this.LOGINOUT_URL  = WEBROOT_URL + LOGINOUT_URL;    
    	this.VERSION_URL   = WEBROOT_URL + VERSION_URL;
    	this.REGISTER_URL     = WEBROOT_URL + REGISTER_URL;
	}
    
    public static void init(String webrootUrl){
    	HttpClient.WEBROOT_URL = webrootUrl;
    }
    
    public synchronized void login(String accountid,String password)throws Exception{
    	try{
    		 Map<String, String> httpParams = new HashMap<String, String>();
	    	 httpParams.put("username", accountid);
	    	 httpParams.put("password", password);
	    	 httpParams.put("devicecode", DEVICECODE);
	    	 ResponseContent responseContent = HttpClient.post(this.LOGIN_URL, httpParams);
	    	  if(responseContent == null )
	    		   throw new RuntimeException("����������Ӧ���أ���");
	    	 Map outresult = JSONUtils.json2Map(responseContent.getContent("GBK"));
	    	   
	    	   String responsetype = (String) outresult.get(RESPONSETYPE);
	    	   if(responsetype == null )
	    		   throw new RuntimeException("����������Ӧ���أ���");
	    	   
	    	   if(responsetype.equals(RESPONSETYPE_OK)){
	    		   //�ͻ��˻�ȡ��¼��Ϣ
	    		   HttpClient.setCLTSESSION((Map) outresult.get(CLTSESSIONKEY));
	    	   }    		   
	    	   else if(responsetype.equals(RESPONSETYPE_ERR))
	    	   {
	    		   StringBuffer buf = new StringBuffer(); 
	    		   String error = (String) outresult.get("error");
	    		   String errorinfo = (String) outresult.get("errorinfo");    		   
	    		   buf.append(error).append("\n").append(errorinfo);
	    		   
	    		   throw new RuntimeException(buf.toString());
	    		   
	    	   }else{
	    		   throw new RuntimeException("δ֪���󣡣�");
	    	   }	    	 	    
    	}catch(RuntimeException ex){    	
    		throw new RuntimeException("������Ϣ��"+ex.getMessage());
    	}
    }
    
    public synchronized void loginout()throws Exception{       
    	HttpClient.getUrlRespContent(this.LOGINOUT_URL);   	        
    	HttpClient.setCLTSESSION(new HashMap());    	
    	HttpClient.setJSESSIONID(null);
    }
    
	/**
	 * steven add רҵע��30�� <br />
	 * mobernumber �ֻ��� <br />
	 * mescmaccount Erp�˺� <br />
	 * pswd �û��������� <br />
	 * entryid EppSrv��Ŀ�����Ӧ�ľ�Ӫ��λ <br />
	 * 
	 * @return ע��ɹ�,�����˺���Ϣ
	 * @param httpParams
	 *            ����Map
	 * @return
	 * @throws Exception
	 */
    public synchronized Object register( Map<String, String> httpParams)throws Exception{
    	try{
//    		 Map<String, String> httpParams = new HashMap<String, String>();
//    		 httpParams.put("mobernumber", mobernumber);
//    		 httpParams.put("mailbox", mailbox);
//    		 httpParams.put("mescmaccount", mescmaccount);
//    		 httpParams.put("pswd", pswd);
//    		 httpParams.put("entryid", entryid);
	    	 httpParams.put("devicecode", DEVICECODE);
	    	 ResponseContent responseContent = HttpClient.post(this.REGISTER_URL, httpParams);
	    	  if(responseContent == null )
	    		   throw new RuntimeException("����������Ӧ���أ���");
	    	 Map outresult = JSONUtils.json2Map(responseContent.getContent("GBK"));
	    	   
	    	   String responsetype = (String) outresult.get(RESPONSETYPE);
	    	   if(responsetype == null )
	    		   throw new RuntimeException("����������Ӧ���أ���");
	    	   
	    	   if(responsetype.equals(RESPONSETYPE_OK)){
	    		   return outresult.get("data");
	    	   }    		   
	    	   else if(responsetype.equals(RESPONSETYPE_ERR))
	    	   {
	    		   StringBuffer buf = new StringBuffer(); 
	    		   String error = (String) outresult.get("error");
	    		   String errorinfo = (String) outresult.get("errorinfo");    		   
	    		   buf.append(error).append("\n").append(errorinfo);
	    		   
	    		   throw new RuntimeException(buf.toString());
	    		   
	    	   }else{
	    		   throw new RuntimeException("δ֪���󣡣�");
	    	   }	    	 	    
    	}catch(RuntimeException ex){    	
    		throw new RuntimeException("������Ϣ��"+ex.getMessage());
    	}
    }
    
    /**
     * ��ȡEppSrv�������Ѿ���Ȩ����ҵ��Ϣ<br />
     * @return
     * @throws Exception
     */
    public synchronized Object genPojects()throws Exception{
    	try{
    		 Map<String, String> httpParams = new HashMap<String, String>();
    		 httpParams.put("genprojects", "true");
	    	 ResponseContent responseContent = HttpClient.post(this.REGISTER_URL, httpParams);
	    	  if(responseContent == null )
	    		   throw new RuntimeException("����������Ӧ���أ���");
	    	 Map outresult = JSONUtils.json2Map(responseContent.getContent("GBK"));
	    	   
	    	   String responsetype = (String) outresult.get(RESPONSETYPE);
	    	   if(responsetype == null )
	    		   throw new RuntimeException("����������Ӧ���أ���");
	    	   
	    	   if(responsetype.equals(RESPONSETYPE_OK)){
	    		   return outresult.get("data");
	    	   }    		   
	    	   else if(responsetype.equals(RESPONSETYPE_ERR))
	    	   {
	    		   StringBuffer buf = new StringBuffer(); 
	    		   String error = (String) outresult.get("error");
	    		   String errorinfo = (String) outresult.get("errorinfo");    		   
	    		   buf.append(error).append("\n").append(errorinfo);
	    		   
	    		   throw new RuntimeException(buf.toString());
	    		   
	    	   }else{
	    		   throw new RuntimeException("δ֪���󣡣�");
	    	   }	    	 	    
    	}catch(RuntimeException ex){    	
    		throw new RuntimeException("������Ϣ��"+ex.getMessage());
    	}
    }
    
    public synchronized Object genVersionInfo()throws Exception{
    	try{

	    	 ResponseContent responseContent = HttpClient.getUrlRespContent(this.VERSION_URL);
	    	  if(responseContent == null )
	    		   throw new RuntimeException("����������Ӧ���أ���");
	    	  Map outresult = JSONUtils.json2Map(responseContent.getContent("GBK"));
	    	   
	    	   String responsetype = (String) outresult.get(RESPONSETYPE);
	    	   if(responsetype == null )
	    		   throw new RuntimeException("����������Ӧ���أ���");
	    	   
	    	   if(responsetype.equals(RESPONSETYPE_OK)){
	    		   return outresult.get("data");
	    	   }    		   
	    	   else if(responsetype.equals(RESPONSETYPE_ERR))
	    	   {
	    		   StringBuffer buf = new StringBuffer(); 
	    		   String error = (String) outresult.get("error");
	    		   String errorinfo = (String) outresult.get("errorinfo");    		   
	    		   buf.append(error).append("\n").append(errorinfo);
	    		   
	    		   throw new RuntimeException(buf.toString());
	    		   
	    	   }else{
	    		   throw new RuntimeException("δ֪���󣡣�");
	    	   }	    	 	    
    	}catch(RuntimeException ex){    	
    		throw new RuntimeException("������Ϣ��"+ex.getMessage());
    	}
    }
    
    /**
     * ���ͱ���
     * @param packet RmiPacket
     * @return RmiPacket
     * @throws Exception
     */
    public synchronized HttpPacket send(HttpPacket packet) throws Exception {
            	 
    	try{
    		    	
    	  String baservleturl = packet.getBaservleturl();
    	  String srvtype  = packet.getSrvtype();
    	     	 
    	  if(baservleturl != null && !"".equals(baservleturl)){
    		  this.SERVLET_URL = baservleturl;
    	  }
    	   
    	   Map<String, String> httpParams = new HashMap<String, String>();    	    	  
    	   //����srvtype�ҵ���Ӧ�ķ���
      	   httpParams.put("srvtype", srvtype);    	   
    	   
    	   List params = packet.getInparams();
    	   int paramsLength = 0;    	   
    	   if(params != null && params.size() > 0){   
    		   paramsLength = params.size();    		   
    		   int ei = 0 ;
    		   for (int i = 0 ; i < params.size() ; i++ ) {
    			   Object paramstype = params.get(i);
    			   if( paramstype instanceof String 
    					   || paramstype instanceof Short 
    					   || paramstype instanceof Integer
    					   || paramstype instanceof Long){
    				   
    				   httpParams.put(VALUE_PRIFIX + ei, String.valueOf(paramstype));
    				   httpParams.put(PARAMS_PRIFIX + i, "String:"+VALUE_PRIFIX + ei);
    		           ei++;
    		           
    			   }else if(paramstype == null || paramstype instanceof Map ){
    				   Map map = (Map) paramstype;
    				   String paramstr = "";
    				   if(paramstype != null){
    	                for(Object skey : map.keySet() ){
    	                	 httpParams.put(VALUE_PRIFIX + ei,String.valueOf(map.get(skey)));    	                	 
    	                     paramstr += skey+","+ VALUE_PRIFIX + ei+";";
    	                     ei++;
    	                }
    				   }
    	                 if("".equals(paramstr))
    	                    paramstr = null;//��Ҫ������ת��SessionContext
    	                 
    	                 httpParams.put(PARAMS_PRIFIX + i, "Object:"+paramstr);
    	                
    			   }else if( paramstype instanceof String[]){
    				   
    				   String[] p = (String[])paramstype;
    				   int plength = p.length;
    				   String paramStr = "";
                       for(int m = 0; m < plength; m ++){
                    	   httpParams.put(VALUE_PRIFIX + ei, p[m]);
                    	   httpParams.put(PARAMS_PRIFIX + i+"_"+m, VALUE_PRIFIX + ei);
                           paramStr +=  PARAMS_PRIFIX + i+"_"+m+",";
                           ei++;
                       }
                       httpParams.put(PARAMS_PRIFIX + i, "StringArray:"+paramStr);  				   
    				   
    			   }else if( paramstype instanceof Map[]){
    				   
    				   Map[] p = (Map[]) paramstype;
    				   int plength = p.length;
    				   String paramStr = "";
                       for(int m = 0; m < plength; m ++){
                             Map map = p[m];
                             String mapParamstr = "";
                             for(Object skey : map.keySet()){
                               httpParams.put(VALUE_PRIFIX + ei, String.valueOf(map.get(skey)));
                               mapParamstr+=skey+","+ VALUE_PRIFIX + ei+";";
                               ei++;
                             }
                             httpParams.put(PARAMS_PRIFIX + i+"_"+m, mapParamstr);
                           paramStr +=  PARAMS_PRIFIX + i+"_"+m+",";

                       }
                       httpParams.put(PARAMS_PRIFIX + i , "ObjectArray:"+paramStr);    				   
    				   
    			   }else if( paramstype instanceof Boolean ){
    				   httpParams.put(VALUE_PRIFIX + ei, String.valueOf(paramstype));
    				   httpParams.put(PARAMS_PRIFIX + i, "Boolean:"+VALUE_PRIFIX + ei);    				       			
    		           ei++;    				   
    			   }
    			       			       		
    		   }
    		       		  
    	   }
    	   
    	   httpParams.put("paramsLength", String.valueOf(paramsLength));
    	  
    	   Map<String,String> header = new HashMap<String,String>();  
    	   header.put("Cookie", "JSESSIONID="+HttpClient.getJSESSIONID());  
    	   
    	   Map<String,String> addHeader = this.getHeaderParams();
    	   if(addHeader != null && !addHeader.isEmpty()){
    		   for (Object key : addHeader.keySet()) {
    			   header.put((String) key, addHeader.get(key));
    		   }
    	   }
    	   ResponseContent responseContent = HttpClient.post(this.SERVLET_URL, httpParams, header);
    	      	   
    	   Map outresult = JSONUtils.json2Map(responseContent.getContent("GBK"));
    	   
    	   String responsetype = (String) outresult.get(RESPONSETYPE);
    	   if(responsetype == null )
    		   throw new RuntimeException("����������Ӧ���أ���");
    	   
    	   if(responsetype.equals(RESPONSETYPE_OK)){
    		   
    		   packet.setOutresult(outresult.get("data"));
    		   
    	   }    		   
    	   else if(responsetype.equals(RESPONSETYPE_ERR))
    	   {
    		   StringBuffer buf = new StringBuffer(); 
    		   String error = (String) outresult.get("error");
    		   String errorinfo = (String) outresult.get("errorinfo");    		   
    		   buf.append(error).append("\n").append(errorinfo);
    		   
    		   throw new RuntimeException(buf.toString());
    		   
    	   }else{
    		   throw new RuntimeException("δ֪���󣡣�");
    	   }
    	}catch(RuntimeException ex){    	
    		throw new RuntimeException("������Ϣ��"+ex.getMessage());
    	}
    	
    	   return packet;																																																																																																																																																																												
    }

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
        	  throw new RuntimeException(e.getMessage());
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
        	  throw new RuntimeException(e.getMessage());
        }
        return response;
    }
    
    public static ResponseContent post(String url, Map<String, String> urlParams, Map<String,String> headerParams ) {
        return postEntity(url, null, null, urlParams, headerParams);
    }
    
    public static ResponseContent post(String url, Map<String, String> urlParams) {
        return postEntity(url, null, null, urlParams, null);
    }
            
    private static ResponseContent postEntity(String url, String body, String contentType, Map<String, String> urlParams, Map<String,String> headerParams ) {      
    	HttpClientWrapper hw = new HttpClientWrapper(connectionRequestTimeout, connectTimeout, socketTimeout);
    	ResponseContent ret = null;
        try {  
            hw.addNV("body", body);
            hw.buildHeader(headerParams);
            hw.buildGetUrl(urlParams);            
            ret = hw.postNV(url, contentType);
        } catch (Exception e) {
           throw new RuntimeException(e.getMessage());
        }
        return ret;
    }    
    
	public static Map getCLTSESSION() {
		return CLTSESSION;
	}

	public static void setCLTSESSION(Map cLTSESSION) {
		CLTSESSION = cLTSESSION;
	}

		
	public static Integer getSocketTimeout() {
		return socketTimeout;
	}

	public static void setSocketTimeout(Integer socketTimeout) {
		HttpClient.socketTimeout = socketTimeout;
	}

	public static Integer getConnectTimeout() {
		return connectTimeout;
	}

	public static void setConnectTimeout(Integer connectTimeout) {
		HttpClient.connectTimeout = connectTimeout;
	}

	public static Integer getConnectionRequestTimeout() {
		return connectionRequestTimeout;
	}

	public static void setConnectionRequestTimeout(Integer connectionRequestTimeout) {
		HttpClient.connectionRequestTimeout = connectionRequestTimeout;
	}

	public static String getJSESSIONID() {	
		return  HttpClientWrapper.getJSESSIONID();
	}

	public static void setJSESSIONID(String jSESSIONID) {		
		HttpClientWrapper.setJSESSIONID(jSESSIONID);
	}

	public void clearHeaderParams(){
		headerParams = new HashMap<String, String>();
	}
	
	public Map<String, String> getHeaderParams() {
		return headerParams;
	}

	public void setHeaderParams(Map<String, String> headerParams) {
		this.headerParams = headerParams;
	}

	public static void main(String[] args)throws Exception{
		HttpClient http = new HttpClient();
		Object version = http.genVersionInfo();
		System.out.println(version);
		
		for (int i = 0; i < 5; i++) {
					
    	
    	String usrname = "10086";
    	String pwd = "aaa";
  
    	http.login(usrname, pwd); 
    	   	
		HttpPacket packet = new HttpPacket();    	
		packet.setSrvtype("ModuleSetSrv");    				
		packet.setParameter(null);
		packet.setParameter("10086");
		packet.setParameter("MobileSrvMsgModuleSetSrv");
		packet.setParameter(false);
		
		packet = http.send(packet);    	
		Object obj111 = packet.getOutresult();
		System.out.println("���:"+obj111);
		
		
		HttpPacket packet1 = new HttpPacket();    	
		packet1.setSrvtype("ModuleListSrv");    				
		packet1.setParameter(null);
		packet1.setParameter("10086");

		packet1 = http.send(packet1);    	
		Object obj222 = packet1.getOutresult();
		System.out.println("���:"+obj222);
		
		http.loginout();
		}
    }
}
