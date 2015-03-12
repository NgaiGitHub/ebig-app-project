package com.ebig.app.utils;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import com.ebig.app.App;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;

/**
 * Internal storage: ���ǿ��õ� ������ļ�Ĭ����ֻ�ܱ����app�����ʵġ�
 *       ���û�ж�����app��ʱ��ϵͳ���internal���������ļ�������ɾ��� Internal��������ȷ�������û�������app�����ʵ���Ѵ洢����
 * External storage: �������ǿ��õģ���Ϊ�û�����ѡ����ⲿ����ΪUSB�洢ģʽ�������Ͳ����Է����ˡ�
 *       �Ǵ�Ҷ����Է��ʵģ���˱��浽������ļ���ʧȥ���ʿ���Ȩ�޵ġ�
 *       ���û�ж�����appʱ��ϵͳ������ɾ��external��Ŀ¼��getExternalFilesDir()���µ�����ļ���
 *       External�����㲻��Ҫ�ϸ�ķ���Ȩ�޲�����ϣ����Щ�ļ��ܹ�������app����������������û�ͨ�����Է���ʱ����Ѵ洢����
 * 
 * @author HungLau.Ngai
 * Ȩ�ޣ�WRITE_EXTERNAL_STORAGE
 */
public class DataUtil {

	public File getFile(String filename) {
		return new File(App.getContext().getFilesDir(), filename);
	}

	public File getTempFile(String url) {
		File file = null;
		if(url != null && !"".equals(url)){			
			try {
				String fileName = Uri.parse(url).getLastPathSegment();
				file = File.createTempFile(fileName, null, App.getContext().getFilesDir());
			}catch (IOException e) {
				// Error while creating file
				file = getFile(url);
			}
		}
	    return file;
	}
	
	/**
	 * Context.MODE_PRIVATE
	 * @param filename
	 * @param datastr
	 */
	public void writeFile(String filename, String datastr) {
		// String filename = "myfile";
		// String string = "Hello world!";
		FileOutputStream outputStream;

		try {
			outputStream = App.getContext().openFileOutput(filename,
					Context.MODE_PRIVATE);
			outputStream.write(datastr.getBytes());
			outputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String readFile(String filename){
		String rs = "";
		FileInputStream inputStream;
		BufferedReader mReader =  null;
		try {
			inputStream = App.getContext().openFileInput(filename);
			if(inputStream != null){
				mReader = new BufferedReader(new InputStreamReader(inputStream));
				if(mReader != null)
					while (mReader.ready()) {
						rs = rs + mReader.readLine();
					}
				
			}
			inputStream.read();
		} catch (FileNotFoundException e) {
	        LogUtil.LogE(DataUtil.class.getName(), e.getMessage());
		} catch (IOException e) {
	        LogUtil.LogE(DataUtil.class.getName(), e.getMessage());
		}
		return rs;
	}
	
	 /* Checks if external storage is available for read and write */
	public boolean isExternalStorageWritable() {
	    String state = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(state)) {
	        return true;
	    }
	    return false;
	}

	/* Checks if external storage is available to at least read */
	public boolean isExternalStorageReadable() {
	    String state = Environment.getExternalStorageState();
	    if (Environment.MEDIA_MOUNTED.equals(state) ||
	        Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
	        return true;
	    }
	    return false;
	}
	
	/**
	 * �����ļ�Ϊpublic��ʽ��
	 * @param albumName
	 * @return
	 * ���ס��getExternalFilesDir() �����ᴴ����Ŀ¼����app��ж��ʱ��ϵͳɾ�����������ļ�����app��ɾ��ʱ��Ȼ��������ʹ��getExternalStoragePublicDirectory().
	 */
	public File getAlbumStorageDir(String albumName) {
	    // Get the directory for the user's public pictures directory.
	    File file = new File(Environment.getExternalStoragePublicDirectory(
	            Environment.DIRECTORY_PICTURES), albumName);
	    if (!file.mkdirs()) {
	        LogUtil.Log(DataUtil.class, "Directory not created");
	    }
	    return file;
	}
	/**
	 * �����ļ�Ϊ˽�еķ�ʽ
	 * @param context
	 * @param albumName
	 * @return
	 * ���ס��getExternalFilesDir() �����ᴴ����Ŀ¼����app��ж��ʱ��ϵͳɾ�����������ļ�����app��ɾ��ʱ��Ȼ��������ʹ��getExternalStoragePublicDirectory().
	 */
	public File getAlbumStorageDir(Context context, String albumName) {
	    // Get the directory for the app's private pictures directory.
	    File file = new File(context.getExternalFilesDir(
	            Environment.DIRECTORY_PICTURES), albumName);
//	    file.getFreeSpace();
//	    file.getTotalSpace();
	    if (!file.mkdirs()) {
	        LogUtil.Log(DataUtil.class, "Directory not created");
	    }
	    return file;
	}
	
}
