package com.ebig.app.activity.main;

import java.util.LinkedList;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.ebig.app.NetInfo;
import com.ebig.app.R;
import com.ebig.app.base.BaseFirmFragment;
import com.ebig.app.dbctrl.AppDbCtrl;
import com.ebig.app.model.Functions;

public class FunsFragment extends BaseFirmFragment implements
		LoaderManager.LoaderCallbacks<LinkedList<Functions>>,OnItemClickListener {
	
	private GridView mFunsGridView;
	//protected static LinkedList<Functions> mFunctions;
	protected static FunsAdapter mFunsAdapter;
	 
	public FunsFragment() {
		super();
	}

	static public FunsFragment newIntance(Bundle bundle) {
		FunsFragment mFunsFragment = new FunsFragment();
		mFunsFragment.setArguments(bundle);
		return mFunsFragment; 
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) { 
		return inflater.inflate(R.layout.fragment_funs, null);
	}
	 
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		if(mFunsGridView != null) return ;
		View mRootView = getView();
		mFunsGridView = (GridView) mRootView.findViewById(R.id.funsGridView);
		mFunsGridView.setOnItemClickListener(this);
//		if (mFunctions == null)
//			mFunctions = new LinkedList<Functions>();
		if (mFunsAdapter == null) 
			mFunsAdapter = new FunsAdapter(getActivity());
		mFunsGridView.setAdapter(mFunsAdapter);
		
		View v = getActivity().findViewById(R.id.app_curr_netinfo);
		if (v != null)
			if(!NetInfo.isEnable()){				
				v.setVisibility(View.VISIBLE);
			}else {
				v.setVisibility(View.GONE);
			}

		if (mFunsAdapter.getData().isEmpty())
			getLoaderManager().initLoader(0, null, this);

	}


	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		Functions functions = mFunsAdapter.getItem(position);
		if(functions != null){
			Intent mIntent = FunsIntentAdapter.newIntent(getActivity(),
					functions);
			if (mIntent != null)
				startActivity(mIntent);
//			String clsName = functions.getModuleClass();
//			if(clsName != null && !"".equals(clsName)){
//				//getToast().makeText(getActivity(), clsName+"", null);
//				startActivity(new Intent(getActivity(), AuditMgrActivity.class));
//			}
		}
	} 
	
	
	@Override
	public void onStart() { 
		super.onStart();
		//mFunsAdapter.notifyDataSetChanged();
	}

	
	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onDestroyView() {
		super.onDestroyView();
		mFunsGridView = null;
	}

//	@Override
//	public Loader<LinkedList<Functions>> onCreateLoader(int id, Bundle args) {
//		return new FunsListLoader(getActivity());
//	}
//	
//	@Override
//	public void onLoadFinished(Loader<LinkedList<Functions>> loader,
//			LinkedList<Functions> data) {
//		mFunsAdapter.notifyDataSetChanged();
//	}
//	
//	@Override
//	public void onLoaderReset(Loader<LinkedList<Functions>> loader) {
//		if(mFunctions != null){
//			mFunctions.clear();
//			mFunctions = null;
//		}
//	}
	
	@Override
	public void onLoadFinished(Loader<LinkedList<Functions>> loader,
			LinkedList<Functions> funs) {	
		//mFunsAdapter.notifyDataSetChanged();
		if(funs != null)
		if(funs.isEmpty())
			mFunsGridView.setEmptyView(LayoutInflater.from(getActivity()).inflate(R.layout.activity_fragment, null));

	}

	@Override
	public void onLoaderReset(Loader<LinkedList<Functions>> arg0) {
		mFunsAdapter.setData(null);
	}

	@Override
	public Loader<LinkedList<Functions>> onCreateLoader(
			int arg0, Bundle arg1) {
		return new FunsListLoader(getActivity());
	}
	
	//加载账号功能权限
	private  static class FunsListLoader extends AsyncTaskLoader<LinkedList<Functions>>{

		public FunsListLoader(Context context) {
			super(context);
		}

		@Override
		public LinkedList<Functions> loadInBackground() {
////			 读取账号权限返回的是一个Map
//			LinkedList<Functions> mFunctions = new LinkedList<Functions>();
//			Functions mFuns;
//			for (int i = 0; i < 5; i++) {
//				mFuns = new Functions();
//				mFuns.setModuleId("moduleid");
//				mFuns.setModuleName("moduleid");
//				mFuns.setModulePic("moduleid");
//				mFuns.setModuleClass("moduleid");
//				mFuns.setIsShortcuts("moduleid");
//				mFuns.setAccountId("moduleid");
//				mFunctions.add(mFuns);
//			}
//			// 自动补全空白输入.. 					
//			int size = mFunctions.size() % 3;
//			if (size != 0) {
//				size = 3 - size;
//				for (int i = 0; i < size; i++) {
//					mFunctions.add(null);
//				}
//			}
//			return mFunctions;
			return AppDbCtrl.genAccModules();
		}

		@Override
		protected void onStartLoading() {
			super.onStartLoading();
			if(!mFunsAdapter.getData().isEmpty()){
				deliverResult(mFunsAdapter.getData());			
			}else {
				forceLoad();
			}
		}
		
		@Override
		public void deliverResult(LinkedList<Functions> data) {
			super.deliverResult(data);
			mFunsAdapter.setData(data);
		}
		
	}
 
}
