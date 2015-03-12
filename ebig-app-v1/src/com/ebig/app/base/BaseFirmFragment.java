package com.ebig.app.base;

import android.app.ActionBar;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.ebig.app.App;
import com.ebig.app.base.intf.BaseToastInterface;
import com.ebig.app.custom.ui.DefaultActionBar;
import com.ebig.app.utils.LogUtil;

public abstract class BaseFirmFragment extends Fragment{
	//protected BaseAsyncTask mAsyncTask;
	
//	/**
//	 * 当销毁时Fragment,调用些方法,清空生产的内存信息<br />
//	 * 例如：产生了List数据集合,当Fragment销毁时清除List集合,list.clear();<br />
//	 * 适当使用<br />
//	 */
//	abstract public void clearDbCtrl();
//	
//	@Override
//	public void onDestroy() {
//		super.onDestroy();
//		clearDbCtrl();
//	}
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}
	 
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) { 
		return super.onCreateView(inflater, container, savedInstanceState);
	}
 
	@Override
	public void onActivityCreated(Bundle savedInstanceState) { 
		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) { 
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) { 
		return super.onOptionsItemSelected(item);
	}

	protected BaseToastInterface getToast() {
		return App.getCroutonToast();
	}

	protected ActionBar getActionBar() {
		if(getActivity() == null)
			return null;
		return getActivity().getActionBar();
	}
	
	protected void hideDefaultBarBack(boolean b) {
		if(getBar() != null)
		getBar().hideBack(b);
	}

	protected void setDefaultBarTitle(String title) {
		if(getBar() != null)
		getBar().setDefaultTitle(title);
	}

	protected void showDefaultBarProgressBar(boolean b) {
		if(getBar() != null)
		getBar().showDefaultProgressBar(b);
	}
	
	private DefaultActionBar getBar() {
		final ActionBar mActionBar = getActionBar();
		if (mActionBar == null)
			return null;
		if (mActionBar.getCustomView() == null)
			return null;
		if (mActionBar.getCustomView() instanceof DefaultActionBar)
			return (DefaultActionBar) mActionBar.getCustomView();
		return null;
	}
	 
	/**
	 * 
	 * @param asyncid  同步方法唯一ID，不可缺少
	 * @param params 参数
	 * @return
	 */
	protected BaseAsyncTask newAsyncTaskExecute(final int asyncid, Object... params) {
		 BaseAsyncTask mAsyncTask = null;
//		if (mAsyncTask != null && mAsyncTask.asyncid == asyncid)
//			return mAsyncTask;
		mAsyncTask = new BaseAsyncTask();
		mAsyncTask.setAsyncid(asyncid);
		if (Build.VERSION.SDK_INT >= 11) {
			mAsyncTask.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, params);
		} else {
			mAsyncTask.execute(params);
		}
		return mAsyncTask;
	}

	protected boolean cancelCurrentAsyncTask() {
//		try {
//			if (mAsyncTask != null&& mAsyncTask.getStatus() != AsyncTask.Status.FINISHED)
//				mAsyncTask.cancel(true);
//			mAsyncTask = null;
//		} catch (Exception e) {
//			LogUtil.Log(getClass(), e.getMessage());
//			return false;
//		}
		return true;
	}

	protected class BaseAsyncTask extends AsyncTask<Object, Void, Object> {

		private int asyncid = -1;

		public void setAsyncid(int asyncid) {
			this.asyncid = asyncid;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			showDefaultBarProgressBar(true);
		}

		@Override
		protected Object doInBackground(Object... params) {
			Object obj = null;
			try {
				obj = doInBackgroundTask(asyncid, params);
			} catch (Exception e) {
				return e;
			}
			return obj;
		}

		@Override
		protected void onPostExecute(Object result) {
			super.onPostExecute(result);
			// mAsyncTask = null;
			if (result instanceof Exception) {
				onPostExecuteTaskError(asyncid, (Exception) result);
			} else {
				try {
					onPostExecuteTask(asyncid, result);
				} catch (Exception e) {
					onPostExecuteTaskError(asyncid, e);
				}
			}
			cancelCurrentAsyncTask();
			showDefaultBarProgressBar(false);
		}
	}
	
	protected void onPostExecuteTask(final int asyncid, Object result) {
	}

	protected void onPostExecuteTaskError(final int asyncid, Exception result) {
	}

	protected Object doInBackgroundTask(final int asyncid, Object[] params) {
		return null;
	}
}
