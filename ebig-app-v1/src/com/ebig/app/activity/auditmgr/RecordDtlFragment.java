package com.ebig.app.activity.auditmgr;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.os.Bundle;
import android.text.Html;
import android.text.method.ScrollingMovementMethod;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView.LayoutParams;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.ebig.app.App;
import com.ebig.app.R;
import com.ebig.app.base.BaseFirmFragment;
import com.ebig.app.custom.adapter.CommonAdapter;
import com.ebig.app.custom.adapter.CommonViewHolder;
import com.ebig.app.dbctrl.AuditMgrDbCtrl;
import com.ebig.app.utils.DensityUtil;
import com.ebig.pub.DateUtil;
import com.ebig.utils.JSONUtils;

public class RecordDtlFragment extends BaseFirmFragment {
	
	private ListView mEditFromLv;
	private List auditDtlList;
	private CommonAdapter mAdapter;
	
	private View mCheckLayer;
	private EditText mChcekText;
	private TextView mEditFromText;
	
	private String attrid  ;
	private String sourceid  ;
	private String pintype ;
	private String checkinfo;
	private String workflowstatus; // 0' 草稿  1' 待审批  2' >审批通过  3' >审批未通过
	
	static public RecordDtlFragment newIntance(Bundle bundle) {
		RecordDtlFragment mDtlFragment = new RecordDtlFragment();
		mDtlFragment.setArguments(bundle);
		return mDtlFragment; 
	}

	public void initPrm(Bundle bundle) {
		attrid = bundle.getString("attrid");
		sourceid = bundle.getString("sourceid");
		pintype = bundle.getString("pintype");
		workflowstatus = bundle.getString("workflowstatus");
		checkinfo = bundle.getString("checkinfo");
	}
	
	public void refashData(Bundle bundle) {
		attrid = bundle.getString("attrid");
		sourceid = bundle.getString("sourceid");
		pintype = bundle.getString("pintype");
		workflowstatus = bundle.getString("workflowstatus");
		checkinfo = bundle.getString("checkinfo");
		
		newAsyncTaskExecute(-1, -1);
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_auditmgr_dtlinfo, container,false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		initPrm(getArguments());
		findViews();
		newAsyncTaskExecute(-1, -1);
		
		mEditFromText.setText(Html.fromHtml(checkinfo));
	}

	private void findViews() {
		 mEditFromLv = (ListView) getView().findViewById(R.id.audit_editfromdtl_lv);
		 mEditFromText = (TextView) getView().findViewById(R.id.audit_editfrom);
		 mEditFromText.setMovementMethod(new ScrollingMovementMethod());
		 
		 mCheckLayer = (View) getView().findViewById(R.id.audit_check_layer);
		 mChcekText = (EditText) getView().findViewById(R.id.audit_check_text);
		 mCheckLayer.setVisibility(View.GONE);
		 
		 ((Button) getView().findViewById(R.id.audit_check_nopass)).setOnClickListener(mClickListener);
		 ((Button) getView().findViewById(R.id.audit_check_pass)).setOnClickListener(mClickListener);
		 if(auditDtlList == null){
			 auditDtlList = new ArrayList();
		 }
		 if(mAdapter == null)
			 mAdapter = new CommonAdapter(getActivity(), ItemView.class, auditDtlList);
		 mEditFromLv.setAdapter(mAdapter);
	}
	
	private OnClickListener mClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			String checktext = mChcekText.getText().toString().trim()
					+ "手机客户端(" + App.getAccount().getPersonname() + ")"
					+ DateUtil.format(new Date());
			switch (v.getId()) {
			case R.id.audit_check_nopass:
				AuditMgrDbCtrl.auditOperSrv(attrid, pintype, checktext, false);
				break;
			case R.id.audit_check_pass:
				AuditMgrDbCtrl.auditOperSrv(attrid, pintype, checktext, true);
				break;

			default:
				break;
			}

			mCheckLayer.setVisibility(View.GONE);
			getToast().makeText(getActivity(), "审批完成!", null);
			
			Timer mTimer = new Timer();
			mTimer.schedule(new TimerTask() {
				
				@Override
				public void run() {
					getActivity().finish();
				}
			}, 1 * 1000);
		}
	};
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		if (!"2".equals(workflowstatus))// 如果未审批侧生成菜单
			inflater.inflate(R.menu.audit_dtl_menu, menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.auditmgr_check:
			mCheckLayer.setVisibility(mCheckLayer.getVisibility() == View.GONE ? View.VISIBLE
							: View.GONE);
			break;

		default:
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}

	@Override
	protected void onPostExecuteTask(int asyncid, Object result) {
		super.onPostExecuteTask(asyncid, result);
		switch (asyncid) {
		case -1:
			Map tmap = JSONUtils.json2Map(result.toString());
			Map totalMap = JSONUtils.json2Map(tmap.get("total").toString());
			String customno = (String) totalMap.get("customno");
			if(customno != null && !"".equals(customno)){
				newAsyncTaskExecute(1, customno);
			}
			auditDtlList.clear();
			auditDtlList.add(0, totalMap);
			if(tmap.get("detail") != null){				
				List detailList = JSONUtils.json2List(tmap.get("detail").toString());
				auditDtlList.addAll(detailList);
			}
			mAdapter.notifyDataSetInvalidated();
			break;
		case 1:
			String msg = "没有维护荣誉值";
			if(!result.toString().contains("android.errormsg")){				
				Map mCusInfMap = JSONUtils.json2Map(result.toString());
				msg = (String) mCusInfMap.get("msg");
			}
			msg = checkinfo +"<br /><font color=red>"+ msg+"</font>";
			mEditFromText.setText(Html.fromHtml(msg));
			break;

		default:
			break;
		}
	}

	@Override
	protected void onPostExecuteTaskError(int asyncid, Exception result) {
		super.onPostExecuteTaskError(asyncid, result);
	}

	@Override
	protected Object doInBackgroundTask(int asyncid, Object[] params) {
		Object obj = null;
		switch (asyncid) {
		case -1:
			obj = AuditMgrDbCtrl.getAuditDtlInfoSrv(1, attrid,sourceid,pintype);
			break;
		case 1:
			obj = AuditMgrDbCtrl.getCusInfoSrv(params[0].toString());
			break;

		default:
			break;
		}
		return obj;
	}
	
//	public class DtlItemView extends CommonViewHolder{
//
//		private TextView mTextView;
//		@Override
//		public View createView() {
//			if(mTextView == null){				
//				mTextView = new TextView(getActivity());
//				int padPd = DensityUtil.dip2px(getActivity(), 5);
//				mTextView.setPadding(padPd, padPd, padPd, padPd);
//				mTextView.setGravity(Gravity.CENTER);
//				mTextView.setBackgroundResource(R.drawable.item_funs_bg_sel);
//				LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
//				mTextView.setLayoutParams(lp);
//			}
//			return mTextView;
//		}
//
//		@Override
//		public void refashViewValue(Object obj) {
//			try { 
//				Map map = (Map) obj; 
//				mTextView.setText(Html.fromHtml(map.get("html").toString()));
//			} catch (Exception e) {
//				mTextView.setText(e.getMessage() == null ? "报错了" : e
//						.getMessage());
//			}
//		}
//		
//	}
	
}
