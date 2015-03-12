package com.ebig.app.activity.main;

import java.util.LinkedList;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.ebig.app.App;
import com.ebig.app.R;
import com.ebig.app.activity.suggest.SuggestActivity;
import com.ebig.app.base.BaseFirmFragment;
import com.ebig.app.model.Category;
import com.ebig.app.model.RowItem;
import com.ebig.app.service.UpdateService;
import com.ebig.app.utils.DialogUtil;

public class IsAccFragment extends BaseFirmFragment {
	
	private ListView isAccListView ;
	private View mIsAccProgress; 
	private LinkedList<Object> mIsAccs;
	private IsAccAdapter mIsAccAdapter;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_isacc, null);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		View mRootView = getView();
		isAccListView = (ListView) mRootView.findViewById(R.id.isacc_lv);
		mIsAccProgress = mRootView.findViewById(R.id.isacc_progress);
		
		if (mIsAccs == null) {
			mIsAccs = new LinkedList<Object>();
			// ABC;
			mIsAccs.add(new Category(""));
			mIsAccs.add(App.getAccount());
			mIsAccs.add(new Category(""));
			mIsAccs.add(new RowItem(App.getContext().getResources().getDrawable(R.drawable.amp), "隐私与安全",1));
			mIsAccs.add(new Category(""));
			mIsAccs.add(new RowItem(App.getContext().getResources().getDrawable(R.drawable.agn), "意见反馈",2));
			mIsAccs.add(new RowItem(App.getContext().getResources().getDrawable(R.drawable.agg), newVersionDra(),"检查更新",3));
			mIsAccs.add(new Category(""));
			mIsAccs.add(new RowItem(App.getContext().getResources().getDrawable(R.drawable.aoj), "关于Epp",4));
			mIsAccs.add(new RowItem(App.getContext().getResources().getDrawable(R.drawable.eyf), "退出",5));
		}
		
		if (mIsAccAdapter == null)
			mIsAccAdapter = new IsAccAdapter(getActivity(), mIsAccs);
		if (mIsAccs.size() > 0)
			mIsAccProgress.setVisibility(View.GONE);
		isAccListView.setAdapter(mIsAccAdapter);
		isAccListView.setOnItemClickListener(mItemClickListener);
//		if (mIsAccs.size()<=0) {
//			newAsyncTaskExecute(100, 1);
//		}
		
	}
	
	private Drawable newVersionDra() {
		if (App.hasNewVersion()) {
			return getActivity().getResources().getDrawable(R.drawable.a9k);
		}
		return null;
	}

	

	private OnItemClickListener mItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Object tObject = mIsAccAdapter.getItem(position);
			if(tObject instanceof RowItem){
				RowItem tItem = (RowItem) tObject;
				switch (tItem.getmId()) {
				case 3:// 检查更新
					if(App.hasNewVersion()){
						final DialogUtil tDialogUtil = new DialogUtil();
						tDialogUtil.openDefaultDialog(getActivity(), "应用更新", "当前版本号V1.0,最新版本为V1.1", new View.OnClickListener() {
							
							@Override
							public void onClick(View v) {
								Intent updateIntent = new Intent(getActivity(),UpdateService.class);
								updateIntent.putExtra(UpdateService.UPDATE_URL_KEY, "http://183.63.146.102:2015/appsrv/imgupload/apk/ebig-app-v1.apk");
								//updateIntent.putExtra(UpdateService.UPDATE_URL_KEY, "http://183.63.146.102:2015/appsrv/imgupload/apk/ebig-app-v1-2.apk");
								getActivity().startService(updateIntent);
								tDialogUtil.closeDefaultDialog();
							}
						},new View.OnClickListener() {
							
							@Override
							public void onClick(View v) {
								tDialogUtil.closeDefaultDialog();}});
						}else{
							App.getCroutonToast().makeText(getActivity(), "已经是最新版本了!", null);
					}
					
					break;
				case 2:
					Intent mSugIntent = new Intent(getActivity(), SuggestActivity.class);
					getActivity().startActivity(mSugIntent);
					break;

				default:
					break;
				}
			}
		}
	};
	 
}
