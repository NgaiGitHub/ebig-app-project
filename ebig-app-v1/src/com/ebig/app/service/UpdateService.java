package com.ebig.app.service;

import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.widget.RemoteViews;

import com.ebig.app.App;
import com.ebig.app.R;
import com.ebig.app.utils.CacheUtil;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.litesuits.common.utils.VibrateUtil;

/**
 * ������³���
 * @author HungLau.Ngai
 *
 */
public class UpdateService extends IntentService {
	
	private String update_url = "";
	public static String UPDATE_URL_KEY = "update_url";
	
	private NotificationManager mNotificationManager;
	private Notification mNotification;
	
	private static File mLoadingFile;

	public UpdateService(String name) {
		super(name);
	}
	public UpdateService() {
		super(UpdateService.class.getName());
	}

	public static boolean isLoading(){
		if(mLoadingFile != null)
			return true;
		return false;
	}
	
	@Override
	protected void onHandleIntent(Intent intent) {
		// TODO Auto-generated method stub
		if(intent == null) return ;
		update_url = intent.getStringExtra(UPDATE_URL_KEY);
		downApp();
	}

	@Override
	public void onCreate() {
		super.onCreate();
		newNotification();
	}

	private void newNotification() {
		// TODO Auto-generated method stub
		mNotification = new Notification(R.drawable.app_update_icon,"",System.currentTimeMillis());
		mNotification.flags |= Notification.FLAG_ONGOING_EVENT;
		mNotification.contentView = newRemoteViews();
		// ������֪ͨ��
		mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
		mNotificationManager.notify(R.layout.notification_update_app,
				mNotification);
	}

	private RemoteViews newRemoteViews() {
		RemoteViews mRemoteViews = new RemoteViews(App.getContext()
				.getPackageName(), R.layout.notification_update_app);
		//mRemoteViews.setTextViewText(R.id.appUpdateTitle,App.getContext().getPackageName()+" Downloading...");
		mRemoteViews.setTextViewText(R.id.appUpdateTitle, "�Դ�Ƽ�(��Ѷ) Downloading...");
		mRemoteViews.setProgressBar(R.id.appUpdateProgressBar, 100, 0, true);
		return mRemoteViews;
	}
	
	private File newFile() {
		if(mLoadingFile != null)
			return mLoadingFile;
		File mFileDir = CacheUtil.getExternalCacheDir(App.getContext());
		if(!mFileDir.exists())// ���Ŀ¼������,�½�һ��
			mFileDir.mkdirs();
		
//		String fileName = App.getContext().getPackageName() + "_"
//				+ new SimpleDateFormat("yyyy-MM-dd").format(new Date())+".apk";
		String fileName = App.getContext().getPackageName() + "_"
				+ System.currentTimeMillis()
				+ ".apk";
		File mFile = new File(mFileDir.toString()+"/"+fileName);
		if(!mFile.exists()){//���������,�½�һ��
			try {
				mFile.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		mLoadingFile = mFile;
		return mLoadingFile;
	}

	/**
	 * ��ȡ�ٷֱ�
	 * 
	 * @param p1
	 * @param p2
	 * @return
	 */
	private String percent(double current, double total) {
		String str;
		double p3 = current / total;
		NumberFormat nf = NumberFormat.getPercentInstance();
		nf.setMinimumFractionDigits(2);
		str = nf.format(p3);
		return str;
	}
	
	private void downApp() {
		//final Message message = new Message();
		
		VibrateUtil.vibrate(App.getContext(), 300);
		
		HttpUtils http = new HttpUtils();
		HttpHandler handler = http.download(update_url,
		    newFile().getPath(),
		    true, // ���Ŀ���ļ����ڣ�����δ��ɵĲ��ּ������ء���������֧��RANGEʱ���������ء�
		    true, // ��������󷵻���Ϣ�л�ȡ���ļ�����������ɺ��Զ���������
		    new RequestCallBack<File>() {

		        @Override
		        public void onStart() {}

					@Override
					public void onLoading(long total, long current,
							boolean isUploading) {
						mNotification.contentView.setTextViewText(
								R.id.appUpdatePercentage,
								percent(current, total) + "");
						mNotification.contentView.setProgressBar(
								R.id.appUpdateProgressBar, (int) total,
								(int) current, true);
						mNotificationManager.notify(R.layout.notification_update_app,
										mNotification);
					}

		        @Override
		        public void onSuccess(ResponseInfo<File> responseInfo) {
		        	mLoadingFile = null;
					// ������ɣ������װ
					Uri uri = Uri.fromFile(newFile());
					Intent intent = new Intent(Intent.ACTION_VIEW);
					intent.setDataAndType(uri,
							"application/vnd.android.package-archive");

					mNotification.flags = Notification.FLAG_AUTO_CANCEL;
					PendingIntent mPendingIntent = PendingIntent.getActivity(UpdateService.this, 0, intent, 0); 
					mNotification.setLatestEventInfo(UpdateService.this,
							App.getContext().getPackageName(), "���سɹ��������װ", mPendingIntent);
					mNotificationManager.notify(R.layout.notification_update_app, mNotification);
		        }


		        @Override
		        public void onFailure(HttpException error, String msg) {
		        	Intent intent = new Intent(App.getContext(),UpdateService.class);
					PendingIntent mPendingIntent = PendingIntent.getActivity(
							UpdateService.this, 0, intent, 0);  
		        	mNotification.setLatestEventInfo(UpdateService.this,
							App.getContext().getPackageName(), "����ʧ��!", mPendingIntent);
		        }
		});
	}
	
}
