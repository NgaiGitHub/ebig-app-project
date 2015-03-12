package com.ebig.app.activity.auditmgr;

import java.util.Date;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.ebig.app.App;
import com.ebig.app.R;
import com.ebig.app.base.BaseFirmFragment;
import com.ebig.app.dbctrl.AuditMgrDbCtrl;
import com.ebig.pub.DateUtil;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

public class RecordFragment extends BaseFirmFragment {

	private ProgressBar mProgressBar;
	private PullToRefreshListView mListView;
	private RecordAdapter mRecordAdapter;
	
	static public RecordFragment newIntance(Bundle bundle) {
		RecordFragment mAuditGroupFragment = new RecordFragment();
		mAuditGroupFragment.setArguments(bundle);
		return mAuditGroupFragment; 
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_auditmgr_record, container,false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		findViews();
		if(mRecordAdapter == null)
			mRecordAdapter = new RecordAdapter(getActivity(), mClickListener,false);
		mListView.setAdapter(mRecordAdapter);
		mListView.setMode(Mode.PULL_FROM_END);
		mListView.setOnRefreshListener(mRefreshListener);
		refashFragment(false);
	}

	
	private void findViews() {
		//if(mListView != null) return ;
		mListView = (PullToRefreshListView) getView().findViewById(R.id.pull_refresh_list);
		mProgressBar =  (ProgressBar) getView().findViewById(R.id.progressBar);
		mProgressBar.setVisibility(View.VISIBLE);
		
		mListView.setOnItemClickListener(mItemClickListener);
	}
	
	private OnItemClickListener mItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			if((RecordFragment.this.getActivity() instanceof AuditActivity) && 
					((AuditActivity)RecordFragment.this.getActivity()).mSlidingLayer.isOpened()){
				((AuditActivity)RecordFragment.this.getActivity()).mSlidingLayer.closeLayer(true);
				return;
			}
			// TODO 详细
//			getToast().makeText(getActivity(), "audit_dtl_btn = "+position, null);
			intentDtlScreen(position-1);
		}
	};

	private OnRefreshListener<ListView> mRefreshListener = new OnRefreshListener<ListView>() {

		@Override
		public void onRefresh(PullToRefreshBase<ListView> refreshView) {
			String label = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(),
					DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL); 
			refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
			
			mRecordAdapter.nextPage();
			newAsyncTaskExecute(1, -1);
		}
	};
	
	private OnClickListener mClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			int id = v.getId();
			int position = (Integer) v.getTag();
			switch (id) {
			case R.id.audit_dtl_btn:
				//getToast().makeText(getActivity(), "audit_dtl_btn = "+position, null);
				intentDtlScreen(position);
				break;
			case R.id.audit_nopass_btn:
				//getToast().makeText(getActivity(), "audit_nopass_btn = "+position, null);
				auditCheck(position,false);
				break;
			case R.id.audit_pass_btn:
				auditCheck(position,true);
				break;

			default:
				break;
			}
		}

		

	};
	
	private void intentDtlScreen(int position) {
		Map auditItem = mRecordAdapter.getItem(position);
		String attrid = (String) auditItem.get("attrid");
		String sourceid = (String) auditItem.get("sourceid");
		String pintype = (String) auditItem.get("pintype");
		String checkinfo = (String) auditItem.get("checkinfo");
		
		Intent mIntent = new Intent(getActivity(), AuditDtlActivity.class);
		Bundle mBundle = new Bundle();
		mBundle.putString("attrid", attrid);
		mBundle.putString("sourceid", sourceid);
		mBundle.putString("pintype", pintype);
		mBundle.putString("checkinfo", checkinfo);
		mBundle.putString("workflowstatus", "1");
		if (mRecordAdapter.isHistory())
			mBundle.putString("workflowstatus", "2");
		mIntent.putExtras(mBundle);
			
		startActivityForResult(mIntent, 100);
	}
	
	private void auditCheck(int position, boolean b) {
		Map map = mRecordAdapter.getItem(position);
		String attrid = (String) map.get("attrid");
		String pintype = (String) map.get("pintype");
		// 简单审批
		AuditMgrDbCtrl.auditOperSrv(attrid, pintype,
				"手机客户端(" + App.getAccount().getPersonname() + ")"
				+ DateUtil.format(new Date()), b);
		
		refashFragment();
	}

	public void refashFragment() {
		if(mRecordAdapter != null)
		refashFragment(mRecordAdapter.isHistory());
	}
	
	/**
	 * 重新刷新数据
	 * @param b  数据是否加载历史数据
	 */
	public void refashFragment(boolean b) {
		
		if(mRecordAdapter == null)
			mRecordAdapter = new RecordAdapter(getActivity(), mClickListener,false);
		mRecordAdapter.setHistory(b);
		
		mRecordAdapter.reSetData(null);
		newAsyncTaskExecute(-1, -1);
	}

	protected void showDefaultBarProgressBar(boolean b) {
		//super.showDefaultBarProgressBar(b);
		if (mProgressBar != null)
			mProgressBar.setVisibility(b == true ? View.VISIBLE : View.GONE);
	}

	@Override
	protected void onPostExecuteTask(int asyncid, Object result) {
		super.onPostExecuteTask(asyncid, result);
		// 没有数据UI
		View notDateLayer = getView().findViewById(R.id.not_date_layer);
		if(notDateLayer != null)
		  notDateLayer.setVisibility(View.GONE);
		
		if(mListView != null)
			if(mListView.isRefreshing())
				mListView.onRefreshComplete();
		
		List tList = null;
		if(result instanceof List){
			tList = ((List)result);
			if(tList != null && tList.size()>0){
				if(asyncid == 1){
					mRecordAdapter.addData((List)result);
				}else {			
					mRecordAdapter.reSetData((List)result);
				}
				mRecordAdapter.notifyDataSetInvalidated();
			}else {
				if (mRecordAdapter.getData() == null
						|| mRecordAdapter.getData().size() <= 0)
					if (notDateLayer != null)
						notDateLayer.setVisibility(View.VISIBLE);
			}
		}
		
		
	}

	@Override
	protected void onPostExecuteTaskError(int asyncid, Exception result) {
		super.onPostExecuteTaskError(asyncid, result);
	}

	@Override
	protected Object doInBackgroundTask(int asyncid, Object[] params) {
		
		 String pintype = AuditActivity.mFilterFragment.getPintype();
		 Date begindate = AuditActivity.mFilterFragment.getBeginDate();
		 Date enddate = AuditActivity.mFilterFragment.getEndDate() ;
		
		return AuditMgrDbCtrl.getAuditRecord(mRecordAdapter.getCurrPage(),
				pintype, begindate, enddate, mRecordAdapter.isHistory() == true ? "2" : "1");
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.audit_menu, menu);
	}
	
	
}
