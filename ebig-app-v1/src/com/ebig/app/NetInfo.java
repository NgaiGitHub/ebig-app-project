package com.ebig.app;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Properties;

import com.ebig.app.utils.FileUtil;
import com.ebig.net.client.HttpClient;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log; 
import android.view.View;
import android.widget.Toast;


/**
 * ����������Ϣ
 * @author HungLau.Ngai
 *
 */
public class NetInfo {

	static public String SERVER_IP = "183.63.146.102";
	static public String SERVER_PORT = "2015";
	static public String SERVER_NAME = "appsrv";// ��������
	
    public static String SERVER_IP_KEY = "SERVER_IP";
    public static String SERVER_PORT_KEY = "SERVER_PORT";
    public static String SERVER_NAME_KEY = "SERVER_NAME";
	
	static private  String ADCONFIG = "adconfig";
	static private  String TAG = NetInfo.class.toString();
	static private  boolean isEnable = false;
	
	static private HttpClient mHttpClient;
	/**
	 * ��ǰ�����Ƿ����
	 * @return
	 */
	public static boolean isEnable() {
		return isEnable;
	} 
	public static void setEnable(boolean isEnable) {
		NetInfo.isEnable = isEnable;
	} 
	 
	public static HttpClient getHttpClient() {
		if(mHttpClient == null){
			HttpClient.init(getServiceUrlBase().toString());			
			mHttpClient = new HttpClient();
		}
		return mHttpClient;
	}
	static public boolean initLocalConfig(Context context){
		
		
		if(context == null) return false;
		
		Properties props = new Properties();
		 
		// ��ȡ�û��޸�����,�����,�Ͳ���raw����
		try {
			Map<String, ?> value = FileUtil.readMapDb(context, ADCONFIG); 
			props.putAll(value);
		} catch (Exception e) {
			props.clear();
			Log.e(TAG, "Could not read sharedpreferences message file.so ,try to read raw config(adconfig)", e);
		} 
		
		if(props.isEmpty()){// ��ȡ����raw����			
			try {
				Resources mResources = context.getResources();
				int id = mResources.getIdentifier(ADCONFIG, "raw",
						context.getPackageName());
				props.load(mResources.openRawResource(id));
			} catch (Exception e) {
				Log.e(TAG, "Could not find the properties file. file.name = "+ADCONFIG, e);
			}
		}  

		if (props.isEmpty())
			return false;
		
		NetInfo.SERVER_IP = props.getProperty(SERVER_IP_KEY);
		NetInfo.SERVER_PORT = props.getProperty(SERVER_PORT_KEY);
		NetInfo.SERVER_NAME = props.getProperty(SERVER_NAME_KEY); 
		
		Log.i(TAG, "����������Ϣ:Ip=" + NetInfo.SERVER_IP + ",Port="
				+ NetInfo.SERVER_PORT + ",Service=" + NetInfo.SERVER_NAME);
		//isEnable = true;
		getHttpClient();
		
		return true;
	}  
	
	/**
	 * ���û����õ��������ñ�����SharedPreferences
	 * @param context
	 * @return
	 */
	static public boolean saveNetConfig(Context context){
		try {
			SharedPreferences sharedPrefs = context.getSharedPreferences(
					ADCONFIG, Context.MODE_PRIVATE);
			Editor editor = sharedPrefs.edit();
			editor.putString(SERVER_IP_KEY,SERVER_IP);
			editor.putString(SERVER_PORT_KEY,SERVER_PORT);
			editor.putString(SERVER_NAME_KEY,SERVER_NAME);
			editor.commit();  
		} catch (Exception e) {
			Log.i(TAG, e.getMessage());
			return false;
		}
		return true;
	}
	
	/**
	 * ���ط��ʷ������Ļ���Url,�� http://127.0.0.0:8080/
	 * @return
	 */
	static public URL getServiceUrlBase() {
		String urlStr = "http://" + NetInfo.SERVER_IP + ":"
				+ NetInfo.SERVER_PORT + "/" + NetInfo.SERVER_NAME + "/";
		URL url = null;
		try {
			url = new URL(urlStr);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return url;
	}
	
	/**
	 * ���WIFI / Gprs ���粻ͨ,�򷵻�false
	 * 
	 * @return
	 */
	static public boolean isConnected() {
		ConnectivityManager connectivityManager = (ConnectivityManager) App
				.getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
		NetworkInfo mobNetInfo = connectivityManager
				.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		NetworkInfo wifiNetInfo = connectivityManager
				.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

		if (mobNetInfo != null && wifiNetInfo != null)
			if (mobNetInfo.isConnected() || wifiNetInfo.isConnected()) {
				return true;
			}
		return false;
	}
	
}
