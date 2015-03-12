package com.ebig.app.activity.auditmgr;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.ebig.app.R;
import com.ebig.app.base.BaseFirmFragment;
import com.ebig.app.custom.ui.DateFilter;
import com.ebig.app.dbctrl.AuditMgrDbCtrl;
import com.ebig.app.utils.DensityUtil;

public class FilterFragment extends BaseFirmFragment {

	private DateFilter mDateFilter;
	private ProgressBar mPrBar;
	private GridView mGridView;
	private GroupAdapter mGroupAdapter;
	private ArrayList mGroups;
	private Map curMap;// …Û≈˙∑÷¿‡{pintypename:"",pintype:""}
	private  Button reLoadBtn ;

	static public FilterFragment newIntance(Bundle bundle) {
		FilterFragment mFilterFragment = new FilterFragment();
		mFilterFragment.setArguments(bundle);
		return mFilterFragment;
	}

	public Date getBeginDate(){
		if(mDateFilter == null)
			return new Date();
		return mDateFilter.getBeginDate();
	}
	
	public Date getEndDate(){
		if(mDateFilter == null)
			return new Date();
		return mDateFilter.getEndDate();
	}
	
	public String getPintypeName(){
		if(curMap == null)
			return "";
		return (String) curMap.get("pintypename");
	}
	
	public String getPintype(){
		if(curMap == null)
			return "";
		return (String) curMap.get("pintype");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_auditmgr_filter, null);
	}


	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		findViews();
		
		if (mGroupAdapter == null)
			mGroupAdapter = new GroupAdapter();
		 mGridView.setAdapter(mGroupAdapter);
		 mGridView.setOnItemClickListener(mItemClickListener);
		 
		if(mGroups == null || mGroups.size()<=1){
			newAsyncTaskExecute(-1, -1);			
		}else {
			mPrBar.setVisibility(View.GONE);
			mGroupAdapter.notifyDataSetInvalidated();
		}
	}


	private void findViews() {
//		
//		if(mGridView != null)
//			return ;
		View rootV = getView();
		 mDateFilter =(DateFilter) rootV.findViewById(R.id.date_filter);
		 mPrBar = (ProgressBar) rootV.findViewById(R.id.progressBar);
		 mGridView = (GridView) rootV.findViewById(R.id.audit_group_gv);
		 mPrBar.setVisibility(View.VISIBLE);
		
		 mDateFilter.initDate(DateFilter.MONTH);
		 
		 reLoadBtn = (Button) rootV.findViewById(R.id.audit_group_reloading);
		 reLoadBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				reLoadBtn.setVisibility(View.GONE);
				newAsyncTaskExecute(-1, -1);
			}
		});
		 reLoadBtn.setVisibility(View.GONE);
	}
	
	private OnItemClickListener mItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if(curMap != null)
				curMap.remove("select");
			if (mGroups != null)
				curMap = (Map) mGroups.get(position);

			if(AuditActivity.mSlidingLayer != null)
				if(AuditActivity.mSlidingLayer.isOpened())
					AuditActivity.mSlidingLayer.closeLayer(true);
			
			if(AuditActivity.mRFragment != null)
				AuditActivity.mRFragment.refashFragment(false);
			
			
			if (curMap != null){
				curMap.put("select", true);
				mGroupAdapter.notifyDataSetInvalidated();
			}
		}
	};
	
//	private void refashGridView(){
//		if(mGridView == null ) return ;
//		if(mGroupAdapter == null){
//			mGroupAdapter = new GroupAdapter();
//			mGridView.setAdapter(mGroupAdapter);			
//		}
//		mGroupAdapter.notifyDataSetChanged();
//		mGridView.setVisibility(View.VISIBLE);
//	}
	
	@Override
	protected void onPostExecuteTask(int asyncid, Object result) {
		super.onPostExecuteTask(asyncid, result);
		List list = (List) result;
		if(list != null && list.size() >0){			
			mGroups.clear();
			mGroups.addAll(list);
			mGroupAdapter.notifyDataSetChanged();
		}else {
			mPrBar.setVisibility(View.GONE);
			reLoadBtn.setVisibility(View.VISIBLE);
		}
	}


	@Override
	protected void onPostExecuteTaskError(int asyncid, Exception result) {
		super.onPostExecuteTaskError(asyncid, result);
		
	}


	@Override
	protected Object doInBackgroundTask(int asyncid, Object[] params) {
		return AuditMgrDbCtrl.getAuditGroups();
	}
	
	class GroupAdapter extends BaseAdapter {
		
		public GroupAdapter() {
			super();
			if (mGroups == null)
				mGroups = new ArrayList();
		}

		@Override
		public int getCount() {
			return mGroups.size();
		}

		@Override
		public Object getItem(int position) {
			return mGroups.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public void notifyDataSetChanged() {
			super.notifyDataSetChanged();
			mPrBar.setVisibility(View.GONE);
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView == null){
				TextView mTextView = new TextView(getActivity());
				int padPd = DensityUtil.dip2px(getActivity(), 5);
				mTextView.setPadding(padPd, padPd, padPd, padPd);
				mTextView.setGravity(Gravity.CENTER);
				mTextView.setBackgroundResource(R.drawable.item_funs_bg_sel);
				LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, DensityUtil.dip2px(getActivity(), 66));
				mTextView.setLayoutParams(lp);
				//mTextView.setTextSize(getActivity().getResources().getDimension(R.dimen.app_15sp));
				convertView = mTextView;
			}
			Map map = (Map) getItem(position); 
			if(map != null && map.get("pintypename") != null){				
				((TextView)convertView).setText(map.get("pintypename").toString());
			}else{
				((TextView)convertView).setText("");
			} 
			if(map != null && map.containsKey("select")){
				((TextView)convertView).setTextColor(getActivity().getResources().getColor(R.color.default_custom_actionbar_bg));
			}else {
				((TextView)convertView).setTextColor(Color.BLACK);
			}
			return convertView;
		}
		
	}


}
