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
 * Internal storage: 总是可用的 这里的文件默认是只能被你的app所访问的。
 *       当用户卸载你的app的时候，系统会把internal里面的相关文件都清除干净。 Internal是在你想确保不被用户与其他app所访问的最佳存储区域。
 * External storage: 并不总是可用的，因为用户可以选择把这部分作为USB存储模式，这样就不可以访问了。
 *       是大家都可以访问的，因此保存到这里的文件是失去访问控制权限的。
 *       当用户卸载你的app时，系统仅仅会删除external根目录（getExternalFilesDir()）下的相关文件。
 *       External是在你不需要严格的访问权限并且你希望这些文件能够被其他app所共享或者是允许用户通过电脑访问时的最佳存储区域。
 * 
 * @author HungLau.Ngai
 * 权限：WRITE_EXTERNAL_STORAGE
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
	 * 保存文件为public形式的
	 * @param albumName
	 * @return
	 * 请记住，getExternalFilesDir() 方法会创建的目录会在app被卸载时被系统删除。如果你的文件想在app被删除时仍然保留，请使用getExternalStoragePublicDirectory().
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
	 * 保存文件为私有的方式
	 * @param context
	 * @param albumName
	 * @return
	 * 请记住，getExternalFilesDir() 方法会创建的目录会在app被卸载时被系统删除。如果你的文件想在app被删除时仍然保留，请使用getExternalStoragePublicDirectory().
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
