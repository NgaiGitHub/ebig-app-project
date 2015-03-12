package com.ebig.app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;

import com.activeandroid.ActiveAndroid;
import com.ebig.app.activity.lockpattern.LockScreenFragment;
import com.ebig.app.base.intf.BaseToastInterface;
import com.ebig.app.base.intf.BaseToastInterfaceImpl;
import com.ebig.app.dbctrl.AppDbCtrl;
import com.ebig.app.model.Account;
import com.ebig.app.model.LockPattern;
import com.litesuits.common.utils.PackageUtil;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

/**
 * 架构师,Steven.Ngai. QQ:943334068 欢迎加Q讨论<br />
 * 
 * @author HungLau.Ngai <br />
 *         AUF(Always Use Fragments)原则,总是使用 Fragment 。<br />
 *         不要问我为什么不用 Activity !!。
 */
public class App extends Application {

	static private Map mAppMap ;
	static private Context mContext;
	static private Account mAccount;
	static private LockPattern mLockPattern;
	static private BaseToastInterface mToastInterface;
	static private LockScreenFragment mLockPatternFragment;
	  
    private static DisplayImageOptions options;
	private static ImageLoader imageLoader; 
	
	
	@Override
	public void onCreate() { 
		super.onCreate();
		mAppMap = new HashMap();
		ActiveAndroid.initialize(this);
		mContext = getApplicationContext();
		initImageLoader(mContext);
		
//		mAccount = AppDbCtrl.readAccount();
//		NetInfo.initLocalConfig(mContext); // 初始化网络信息
		
	   // JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
       // JPushInterface.init(mContext);     		// 初始化 JPush 
	}
	
	/**
	 * 清空账号信息、权限功能
	 */
	public static void appExit(){
		mAccount = null;
		mAppMap.clear();
		mLockPatternFragment = null;
		mLockPattern = null;
	}

	/**
	 * 清空账号信息、权限功能,用当前登录人信息进行登录
	 */
	public static boolean appLogin() {
		try {
			return AppDbCtrl.appLogin(getAccount().getAccountId(), getAccount()
					.getAccountPassword());
		} catch (Exception e) {
			return false;
		}
	}
	
	public static BaseToastInterface getCroutonToast(){
		if(mToastInterface == null)
			mToastInterface = new BaseToastInterfaceImpl();
		return mToastInterface;
	}

	public static Account getAccount(){
		if(mAccount == null)
			mAccount = AppDbCtrl.readAccount();// 读取用户账号信息
		return mAccount;
	}
	
	public static Context getContext(){
		return mContext;
	}

//	public static Map getAppParamMap() {
//		return mAppMap;
//	}
	public static Object getAppParamValue(Object key) {
		return App.mAppMap.get(key);
	}
	public static void putAppParamMap(Map map) {
		if(map != null)
		App.mAppMap.putAll(map);
	}
	public static void putAppParamMap(Object key,Object value) {
		App.mAppMap.put(key, value);
	}
	
	public static void putProjectList(List projectlist) {
		if(projectlist == null){
			projectlist = new ArrayList();
			Map tMap = new HashMap();
			tMap.put("entryid", "-1");
			tMap.put("projectname", "网络出现问题!请完全退出应用,再进入注册界面.");
			projectlist.add(tMap);
		}
		App.mAppMap.put("projectlist", projectlist);
	}
	public static List getProjectList() {
		return (List) App.mAppMap.get("projectlist");
	}

	public static LockScreenFragment getLockPatternFragment() {
		if(mLockPatternFragment == null){
			mLockPatternFragment = new LockScreenFragment();
		}
		return mLockPatternFragment;
	}

	public static LockPattern getLockPattern() {
		if(mLockPattern == null){
			mLockPattern = AppDbCtrl.readLockPattern();
			if(mLockPattern == null){
				mLockPattern = new LockPattern();
				mLockPattern.setAccountId(getAccount().getAccountId());
				mLockPattern.setAccountPassword(getAccount().getAccountPassword());
				mLockPattern.setEncryptionStr(getAccount().getGesturePassword());
				mLockPattern.setIslock(false);
				if(getAccount().getGesturePassword() != null && !"".equals(getAccount().getGesturePassword()))
					App.getLockPattern().setIslock(true);
				AppDbCtrl.saveLockPattern();
			}
		} 
		return mLockPattern;
	} 
	
	// 初始化ImageLoader
    private void initImageLoader(Context context) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024)).discCacheSize(10 * 1024 * 1024)
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .build();
        ImageLoader.getInstance().init(config);
    }
    
    public static DisplayImageOptions getDisplayImageOptions() {
		if (options == null)
			options = new DisplayImageOptions.Builder()
					.showImageOnLoading(R.drawable.space)
					.showImageForEmptyUri(R.drawable.space)
					.showImageOnFail(R.drawable.space)
					.cacheInMemory(true).cacheOnDisc(true)
					.considerExifParams(true)
					//.displayer(new RoundedBitmapDisplayer(70))
					.bitmapConfig(Bitmap.Config.RGB_565).build();
		return options;
	}
    
	public static ImageLoader getImageLoader() {
		if (imageLoader == null)
			imageLoader = ImageLoader.getInstance();
		return imageLoader;
	}
	
	public static boolean hasNewVersion() {
		if(App.getAppParamValue("versionnumber") == null)
			return false;
		
		return PackageUtil.getAppVersionName(getContext()).compareTo(
				(String) App.getAppParamValue("versionnumber")) < 0;
	}
}
