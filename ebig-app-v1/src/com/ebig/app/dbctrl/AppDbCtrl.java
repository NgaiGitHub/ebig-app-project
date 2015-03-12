package com.ebig.app.dbctrl;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import android.content.res.Resources;
import android.util.Log;

import com.activeandroid.ActiveAndroid;
import com.activeandroid.query.Select;
import com.activeandroid.query.Update;
import com.ebig.app.App;
import com.ebig.app.NetInfo;
import com.ebig.app.activity.lockpattern.LockScreenFragment;
import com.ebig.app.model.Account;
import com.ebig.app.model.Functions;
import com.ebig.app.model.LockPattern;
import com.ebig.app.utils.JSONUtil;
import com.ebig.app.utils.LogUtil;
import com.ebig.net.client.HttpPacket;
import com.ebig.utils.JSONUtils;

public class AppDbCtrl extends SrvDbCtrl{

	private static String TAG = AppDbCtrl.class.getName();
	private final static String ADACCOUNT="adaccount";
	
	// ���˺���Ϣ
	public static Account readAccount(){
		Account mAccount = null;
		try {
			ActiveAndroid.beginTransaction();
			mAccount = new Select().from(Account.class)
			// .where("Category = ?", category.getId())
					.orderBy("lastLoginDate desc").executeSingle();
			ActiveAndroid.setTransactionSuccessful();
		} catch (Exception e) {
			Log.i(TAG, e.getMessage());
			return mAccount;
		} finally {
			ActiveAndroid.endTransaction();
		}
		 
		if(mAccount == null){// ��ȡ����raw����		
		   mAccount = new Account();
			try {
				Properties props = new Properties();
				Resources mResources = App.getContext().getResources();
				int id = mResources.getIdentifier(ADACCOUNT, "raw",
						App.getContext().getPackageName());
				props.load(mResources.openRawResource(id));
				if (props.isEmpty())
					return null;
				mAccount.setAccountId(props.getProperty("accountid", "1"));
				mAccount.setAccountName(props.getProperty("accountname","��������Ա"));
				mAccount.setAccountPassword(props.getProperty("accountpswd","aaa"));
				mAccount.setPersonId(Long.valueOf(props.getProperty("personid", "1")));
				mAccount.setLastLoginIp(props.getProperty("lastloginip","127.0.0.1"));
				mAccount.setLastLoginArea(props.getProperty("lastloginarea","����")); 
				mAccount.setLastLoginDate(new Date());
			} catch (Exception e) {
				Log.e(TAG, "Could not find the properties file. file.name = "+ADACCOUNT, e);
			}
		} 
		return mAccount;
	}
	
	public static void saveAccount(){
		try {
			ActiveAndroid.beginTransaction();
			App.getAccount().save();
			ActiveAndroid.setTransactionSuccessful();
		} catch (Exception e) {
			Log.i(TAG,e.getMessage());
		} finally {
			ActiveAndroid.endTransaction();
		}
	}
	

	
	static public LockPattern readLockPattern(){
		LockPattern mLockPattern = null;
		try {
			ActiveAndroid.beginTransaction();
			mLockPattern = new Select()
					.from(LockPattern.class)
					.where("accountId = ?", App.getAccount().getAccountId())
					.and("accountPassword = ?",
							App.getAccount().getAccountPassword())
					.executeSingle();
			ActiveAndroid.setTransactionSuccessful();
		} catch (Exception e) {
			LogUtil.Log(LockScreenFragment.class, e.getMessage());
		} finally {
			ActiveAndroid.endTransaction();
		}
		return mLockPattern;
	}
	
	static private boolean saveLockPattern(LockPattern lockPattern){
		try {
			ActiveAndroid.beginTransaction();
			lockPattern.save();
			ActiveAndroid.setTransactionSuccessful();
		} catch (Exception e) {
			LogUtil.Log(AppDbCtrl.class, e.getMessage());
			return false;
		} finally {
			ActiveAndroid.endTransaction();
		}
		return true;
	}
	static private boolean updateLockPattern(String encryptionStr){
		try {
			ActiveAndroid.beginTransaction();
			new Update(LockPattern.class).set("encryptionStr = ? ",
					encryptionStr).where(
					"accountId = " + App.getAccount().getAccountId()
							+ " and accountPassword = "
							+ App.getAccount().getAccountPassword() + " ");
			ActiveAndroid.setTransactionSuccessful();
		} catch (Exception e) {
			LogUtil.Log(AppDbCtrl.class, e.getMessage());
			return false;
		} finally {
			ActiveAndroid.endTransaction();
		}
		return true;
	}
 
//	/**
//	 * �����˺���Ϣ��ȡ��������
//	 */
//	static public String readEncryptionStrByAcc() {
//		LockPattern mLockPattern = readLockPattern();
//		if (mLockPattern == null) return null;
//		return mLockPattern.getEncryptionStr();
//	}
	
	/**
	 * ������������,�����ھ��������������
	 * @return
	 */
	static public void saveLockPattern() {
		try {
			ActiveAndroid.beginTransaction();
			App.getLockPattern().save();
			ActiveAndroid.setTransactionSuccessful();
		} catch (Exception e) {
			Log.i(TAG,e.getMessage());
		} finally {
			ActiveAndroid.endTransaction();
		}
	}


	// ****************��������****************************************************
	
	/**
	 * ����˺���Ϣ��Ȩ�޹���
	 */
	public static void appExit(){
		App.appExit();
	}
	/**
	 * �˺ŵ�¼
	 * @return 
	 * @throws Exception 
	 */
	static public boolean appLogin(String account,String pswd) throws Exception{
		Log.i(TAG, "��¼�ɹ�.....");
	    NetInfo.getHttpClient().setJSESSIONID(null);
		NetInfo.getHttpClient().login(account, pswd);
		Map accMap= NetInfo.getHttpClient().getCLTSESSION();
		// ���Account��
		
		App.getAccount().setAccountId((String)accMap.get("account_id"));
		App.getAccount().setAccountPassword((String)accMap.get("account_pwd"));
		App.getAccount().setAccountName((String)accMap.get("account_name"));
		App.getAccount().setPersonId(Long.valueOf(accMap.get("personid")+""));
		App.getAccount().setPersonname((String)accMap.get("personname"));
		App.getAccount().setEntryId((String)accMap.get("entryid"));
		App.getAccount().setAccountPic((String)accMap.get("account_pic"));
		App.getAccount().setGesturePassword((String)accMap.get("account_gesturepwd"));
		
//		// curpoints,totalpoints,levelname,levelpoints}
//		NgaiApp.getParamMap().put("memocount", accMap.get("memocount"));
//		NgaiApp.getParamMap().put("onlinecount", accMap.get("onlinecount"));
//		NgaiApp.getParamMap().put("sysmsgcount", accMap.get("sysmsgcount")); 
//		Map levelMap = (Map) accMap.get("level");
//		NgaiApp.getParamMap().put("curpoints",levelMap.get("curpoints"));
//		NgaiApp.getParamMap().put("totalpoints",levelMap.get("totalpoints"));
//		NgaiApp.getParamMap().put("levelname",levelMap.get("levelname"));
//		NgaiApp.getParamMap().put("levelpoints",levelMap.get("levelpoints"));
		
//		// update ��������
//		App.getLockPattern().setAccountId(App.getAccount().getAccountId());
//		App.getLockPattern().setAccountPassword(App.getAccount().getAccountPassword());
//		App.getLockPattern().setEncryptionStr(App.getAccount().getGesturePassword());
//		App.getLockPattern().setIslock(false);
//		if(App.getAccount().getGesturePassword() != null && !"".equals(App.getAccount().getGesturePassword()))
//			App.getLockPattern().setIslock(true);
//		saveLockPattern();
		
		NetInfo.setEnable(true);//��¼�ɹ����õ�ǰ�������
		return true;
	}
	 

	public static LinkedList<Functions> genAccModules() {
		HttpPacket mPacket = getPacket(SrvType.MODULE_LIST_SRV); 
		mPacket.setParameter(App.getAccount().getAccountId());
		Object obj = null;
		try {
			obj = requestSrv(mPacket);
		} catch (Exception ex) {
			LogUtil.Log(AppDbCtrl.class.getName(), ex.getMessage());
		}
		LinkedList<Functions> mFunctions = null; 
		if(obj != null && !(obj instanceof Exception)){
			try {
				mFunctions = new LinkedList<Functions>();
				Functions mFuns ;
				Map tMap;
				List mList = JSONUtil.json2List(obj.toString());
				for (int i = 0; i < mList.size(); i++) {
					mFuns = new Functions();
					tMap = (Map) mList.get(i);
					mFuns.setModuleId((String)tMap.get("moduleid"));
					mFuns.setModuleName((String)tMap.get("modulename"));
					mFuns.setModulePic((String)tMap.get("modulepic"));
					mFuns.setModuleClass((String)tMap.get("moduleclass"));
					mFuns.setIsShortcuts((String)tMap.get("isshortcuts"));
					mFuns.setAccountId((String)tMap.get("account_id"));
					mFunctions.add(mFuns);
				}
				// �Զ���ȫ�հ�����..
				int size = mFunctions.size() % 3;
				if (size != 0) {
					size = 3 - size;
					for (int i = 0; i < size; i++) {
						mFunctions.add(null);
					}
				}
			} catch (Exception e) {
				LogUtil.Log(AppDbCtrl.class.getName(), e.getMessage());
			}
		}
		
		
		return mFunctions;
	}
	/**
	 * ��ȡ�°汾<br />
	 * versionnumber  �汾��<br />
	 * versionurl  �汾����<br />
	 * @return
	 */
	public static Map genAppNewVersion() {
		
		try {
			Object object = genVersionInfo();
			return JSONUtils.json2Map(object.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static Object saveAccSuggest(String suggest){
		HttpPacket mPacker = getPacket("SuggestSendSrv");
		mPacker.setParameter(App.getAccount().getAccountId());
		mPacker.setParameter(suggest);
		try {
			return requestSrv(mPacker);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * ע���˺�,������Mescm�˺�
	 * 
	 * @param mobernumber
	 * @param mailbox
	 * @param mescmaccount
	 *            ���Ϊ��,��ô��Ϊ��ͨ�˺�ע�ᡣ
	 * @param pswd
	 * @param entryid
	 *            EPPsrv ��Ŀ������ά���ľ�Ӫ��λ
	 * @param entryid2 
	 * @return
	 */
	public static Object registerAccount(String mobernumber, String mailbox, String accountname,
			String mescmaccount, String pswd, String entryid) {
		Map<String, String> httpParams = new HashMap<String, String>();
		httpParams.put("mobilenumber", mobernumber);
		httpParams.put("mailbox", mailbox);
		httpParams.put("mescmaccountid", mescmaccount);
		httpParams.put("pswd", pswd);
		httpParams.put("entryid", entryid);
		httpParams.put("accountname", accountname);
		try {
			return registerAccount(httpParams);
		} catch (Exception e) {
			e.printStackTrace();
			return e;
		}
		// HttpPacket mPacket = getPacket("RegisterSrv");
		// mPacket.setParameter(mobernumber);
		// mPacket.setParameter(mescmaccount);
		// mPacket.setParameter(pswd);
		// mPacket.setParameter(entryid);
		//
		// try {
		// NetInfo.setEnable(true);
		// return requestSrv(mPacket);
		// } catch (Exception e) {
		// e.printStackTrace();
		// }
	}

	public static List genAppProjectList() {
		try {
			Object object = genProjectList();
			//Map tMap = JSONUtil.json2Map(object.toString());
			List list = JSONUtils.json2List(object.toString());
			return list;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
