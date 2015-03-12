package com.ebig.app.utils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException; 

import com.ebig.pub.Encrypter;

 
import android.content.Context; 
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.AssetManager; 
import android.graphics.Bitmap;
import android.graphics.BitmapFactory; 
import android.os.Environment; 
import android.util.Log;

public class FileUtil {

	static public void saveToAssets(Context context, String fileName) {

	}

	static public String readFromAssets(Context context, String fileName) {

		try {
			AssetManager asset = context.getAssets();
			InputStream is = asset.open(fileName);
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			byte[] bit = new byte[1024];
			int len;
			while ((len = is.read(bit)) != -1) {
				os.write(bit, 0, len);
			}
			byte[] data = os.toByteArray();
			return new String(data);

		} catch (IOException e) {
			new RuntimeException("��Assets�ļ��£����ܴ��ļ���Ϊ[" + fileName + "]���ļ�");
			e.printStackTrace();
		}

		return "";
	}
	
	/**
	 * ��������
	 * @param pwd
	 * @return
	 */
	public static String encodeString(String string){
		String resulet ="";
		try {
			resulet = Encrypter.encode(string);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		}
		return resulet;
	}
	
	/**
	 * ��������
	 * @param pwd
	 * @return
	 */
	public static String decodeString(String string){
		String resulet ="";
		try {
			resulet = Encrypter.decode(string);
		} catch (InvalidKeyException e) {
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (BadPaddingException e) {
			e.printStackTrace();
		} catch (IllegalBlockSizeException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return resulet;
	}
	
	/**
	 * �������ݵ��ֻ�
	 * @param context
	 * @param filename
	 * @param data
	 * @return
	 */
	static public boolean saveDataForCP(Context context ,String filename,String data){
 
		boolean result = false;
		//File codepath = context.getFilesDir();
		String encodeName = encodeString(filename);
		File file = new File(context.getFilesDir(), encodeName);
		FileOutputStream os;
		try {
			os = new FileOutputStream(file, false);
			os.write(data.getBytes());
			os.close();
			result = true;

			Log.i("�������ݵ��ֻ�  =", String.valueOf(result));
		} catch (FileNotFoundException e) {
			Log.i("�������ݵ��ֻ�  = [����]", e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			Log.i("�������ݵ��ֻ�  = [����]", e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
	
	static public String readDataFromCP(Context context ,String filename){
 
		String result = "";
		//String codepath = context.getPackageCodePath();
		String encodeName = encodeString(filename);
		File file = new File(context.getFilesDir(), encodeName);
		FileInputStream is;
		BufferedReader reader = null ;
		try {
			is = new FileInputStream(file);
			reader = new BufferedReader(new InputStreamReader(is));
			if(reader != null)
				while (reader.ready()) {
					result = result + reader.readLine();
				}

			Log.i("���ֻ����ȡ����  =", result);
		} catch (FileNotFoundException e) {
			Log.i("���ֻ���ȡ��Ϣ = [����]", e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			Log.i("���ֻ���ȡ��Ϣ = [����]", e.getMessage());
			e.printStackTrace();
		}
		return result;
	}
	
	static public boolean saveDataForSD(Context context ,String filename,String data){

		
		boolean result = false;
		
		if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
			
			String encodeName = encodeString(filename);
			String fileDir = Environment.getExternalStorageDirectory().toString()+"/"+context.getPackageName()+"/data";
			File dirFile = new File(fileDir);
			if(!dirFile.exists()){
				dirFile.mkdirs();
			}
			File file = new File(fileDir,encodeName+".ngai");
			FileOutputStream os;
			try {
				if(!file.exists()){
					file.createNewFile();
				}
				os = new FileOutputStream(file, false);
				os.write(encodeString(data).getBytes());
				os.close();
				result = true; ;
				Log.i("�������ݵ�SD�� = ", String.valueOf(result));
			} catch (FileNotFoundException e) { 
				Log.i("�������ݵ�SD�� = [����]", e.getMessage());
				e.printStackTrace();
			} catch (IOException e) { 
				Log.i("�������ݵ�SD�� = [����]", e.getMessage());
				e.printStackTrace();
			}
			
		}
		
		return result;
	}
	
	static public String readDataFromSD(Context context ,String filename){

		String result = null;
		if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
			result = "";
			String encodeName = encodeString(filename);
			String fileDir = Environment.getExternalStorageDirectory().toString()+"/"+context.getPackageName()+"/data";
			
			File file = new File(fileDir, encodeName+".ngai");
			FileInputStream is;
			BufferedReader reader = null ;
			try {
				is = new FileInputStream(file);
				reader = new BufferedReader(new InputStreamReader(is));
				if(reader != null)
					while (reader.ready()) {
						result = result + reader.readLine();
					}
				Log.i("��SD����ȡ��Ϣ = ", result.toString());
			} catch (FileNotFoundException e) {
				Log.i("��SD����ȡ��Ϣ = [����]", e.getMessage());
				e.printStackTrace();
			} catch (IOException e) {
				Log.i("��SD����ȡ��Ϣ = [����]", e.getMessage());
				e.printStackTrace();
			}
			
		}
		if( !"".equals(result) && result != null )
			return decodeString(result);
		
		return result;
	}
	
	public static boolean saveBitmapForSD(Context context,Bitmap bitmap,String filename){
		 
		boolean result = false;
		if(bitmap == null) return result;
		if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
			String encodeName = encodeString(filename);
			
			String fileDir = Environment.getExternalStorageDirectory().toString()+"/"+context.getPackageName()+"/cache";
			File dirFile = new File(fileDir);
			if(!dirFile.exists()){
				dirFile.mkdirs();
			}
			File file = new File(fileDir,encodeName+".ngai");
			FileOutputStream os;
			try {
				if(!file.exists()){
					file.createNewFile();
				}
				os = new FileOutputStream(file);
				bitmap.compress(Bitmap.CompressFormat.JPEG, 90, os);
				os.flush();
				os.close();
				result = true;

				Log.i("����Bmp��SD�� = ", String.valueOf(result));
			} catch (FileNotFoundException e) {
				Log.i("����Bmp��SD�� = [����]", e.getMessage());
				e.printStackTrace();
			} catch (IOException e) {
				Log.i("����Bmp��SD�� = [����]", e.getMessage());
				e.printStackTrace();
			}
		}
		return result;
	}
	
	public static Bitmap readBitmapFromSD(Context context ,String filename){
 
		Bitmap result = null;
		try {
			if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
				String encodeName = encodeString(filename);
				String fileDir = Environment.getExternalStorageDirectory().toString()+"/"+context.getPackageName()+"/cache";
				File dirFile = new File(fileDir);
				File file = new File(fileDir,encodeName+".ngai");
				result = BitmapFactory.decodeFile(file.getAbsolutePath()); 
			}
			Log.i("��SD����ȡBmp = ", "true");
		} catch (Exception e) {
			Log.i("��SD����ȡBmp = [����]", e.getMessage());
			e.printStackTrace();
		}
		return result;
	
	}
	
	/**
	 * ����Map����,��������ǰ������...
	 */
	static public boolean saveMapDb(Context context,Map map,String name){
		if(context !=null)
		if(map != null && map.size()>0)
		if(!"".equals(name) && name != null){
			SharedPreferences preference = context.getSharedPreferences(name, Context.MODE_PRIVATE);
			Editor editor = preference.edit();
			Iterator ite = map.entrySet().iterator();
			while (ite.hasNext()) {
				Entry entry = (Entry) ite.next(); 
				editor.putString(entry.getKey()+"", entry.getValue()+"");
			}
			editor.commit();
			Log.i("����Map�����ļ���Ϊ"+name+"", "�ɹ�!!");
			return true;
		}
		Log.i("����Map�����ļ���Ϊ"+name+"", "ʧ��!!");
		return false;
	}
	
	/**
	 * ����Map����
	 */
	static public boolean saveMapDb(Context context,Map map,String name,int mode){
		if(context !=null)
		if(map != null && map.size()>0)
		if(!"".equals(name) && name != null){
			SharedPreferences preference = context.getSharedPreferences(name,mode);
			Editor editor = preference.edit();
			Iterator ite = map.entrySet().iterator();
			while (ite.hasNext()) {
				Entry entry = (Entry) ite.next();
				editor.putString(entry.getKey()+"", entry.getValue()+"");
			}
			editor.commit();
			Log.i("����Map�����ļ���Ϊ"+name+"", "�ɹ�!!");
			return true;
		}
		Log.i("����Map�����ļ���Ϊ"+name+"", "ʧ��!!");
		return false;
	}
	
	/**
	 * ��ȡSharedPreferences �ļ�...
	 * @param context
	 * @param name
	 * @return
	 */
	static public Map<String, ?> readMapDb(Context context,String name){
		if(context !=null)
		if(!"".equals(name) && name != null){
			SharedPreferences preference = context.getSharedPreferences(name, Context.MODE_PRIVATE);
			return preference.getAll();
		}
		return null;
	}
	
	/**
	 * ������������ֽ�����
	 * @param inStream
	 * @return
	 * @throws IOException
	 */
	static public byte[] uncodeToByte(InputStream inStream) throws IOException{
		ByteArrayOutputStream outStream = new ByteArrayOutputStream();
		byte[] buf = new byte[1024];
		int len = 0;
		while ((len = inStream.read(buf))!=-1) {
			outStream.write(buf, 0, len);
		}
		return outStream.toByteArray();
	}
	
	/**
	 * ��鴫���Ĳ����Ƿ�Ϊnull,Ϊ���ַ���
	 * 
	 * @return Ϊnull,��Ϊ���ַ���,������
	 */
	static public boolean isEmpty(String... str) {
		for (int i = 0; i < str.length; i++) {
			if ("".equals(str[i]) || str[i] == null)
				return true;
		}
		return false;
	}
	
	/**
	 * �ж��Ƿ�������
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str) {
		for (int i = str.length(); --i >= 0;) {
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}
	
}






