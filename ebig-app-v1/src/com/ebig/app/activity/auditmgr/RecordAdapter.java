package com.ebig.app.activity.auditmgr;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ebig.app.R;

public class RecordAdapter extends BaseAdapter {

	private Context mContext;
	private int resourceId = R.layout.item_audit_fragment_lv;
	private LinkedList mList;
	private int pageSize = 0;
	private int currPage = 1;
	private boolean isHistory = false;
	
	private OnClickListener mOnClickListener;
 
	public RecordAdapter(Context context,OnClickListener listener,boolean b) {
		super();
		mContext = context;
		mOnClickListener = listener;
		pageSize = mContext.getResources().getInteger(
				R.integer.default_pushtorefash_pagesize);
		isHistory = b;
	}
 
	public int getCurrPage() {
		return currPage;
	}

	public void nextPage() {
		++currPage;
	}

	public void prevPage() {
		if (currPage > 0)
			--currPage;
	}

	public LinkedList getData(){
		return mList;
	}
	
	public void setHistory(boolean isHistory) {
		this.isHistory = isHistory;
	}
	
	public boolean isHistory() {
		return isHistory;
	}

	public void addData(List linkedList){
		if(mList == null) mList = new LinkedList();
		if(linkedList != null){
			mList.addAll(linkedList);
		}else {
			//mList.clear();
		}
		notifyDataSetInvalidated();
	}
	
	/**
	 * 重置数据,当前页面将置为1
	 * @param linkedList
	 */
	public void reSetData(List list){
		if(mList == null) mList = new LinkedList();
		mList.clear();
		if(list != null)
			mList.addAll(list);
		
		currPage = 1;
		notifyDataSetInvalidated();
	}
	
	@Override
	public int getCount() {
		if (mList == null)
			return 0;
		return mList.size();
	}

	@Override
	public Map getItem(int position) {
		return (Map) mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder = null;
		if (convertView == null) {// 当为空时
			holder = new ViewHolder();
			LayoutInflater infl = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = infl.inflate(resourceId, null);
			holder.recordView = (TextView) convertView.findViewById(R.id.audit_html_tx);
			
			holder.dtlView = (TextView) convertView.findViewById(R.id.audit_dtl_btn);
			holder.nopassView = (TextView) convertView.findViewById(R.id.audit_nopass_btn);
			holder.passView = (TextView) convertView.findViewById(R.id.audit_pass_btn);
			
			holder.dtlView.setOnClickListener(mOnClickListener);
			holder.nopassView.setOnClickListener(mOnClickListener);
			holder.passView.setOnClickListener(mOnClickListener);
			
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		Map map = (Map) getItem(position);
		if (map != null) {
			String str = (String) map.get("html");
			String checkinfo = (String) map.get("checkinfo");
			str = str + "<font color='#999999'>审批信息:</font>" +checkinfo;
			holder.recordView.setText(Html.fromHtml(str));
			holder.dtlView.setTag(position);
			holder.nopassView.setTag(position);
			holder.passView.setTag(position);
		}
		
		holder.dtlView.setVisibility(View.VISIBLE);
		holder.nopassView.setVisibility(isHistory == true ? View.GONE:View.VISIBLE);
		holder.passView.setVisibility(isHistory == true ? View.GONE:View.VISIBLE);
		
		return convertView;
	}

	public final class ViewHolder {
		private TextView recordView;
		
		private TextView dtlView;
		private TextView nopassView;
		private TextView passView;
	}

//	private OnClickListener mOnClickListener = new OnClickListener() {
//		
//		@Override
//		public void onClick(View v) {
//			int id = v.getId();
//			int position = (Integer) v.getTag();
//			switch (id) {
//			case R.id.audit_dtl_btn:
//
//				break;
//			case R.id.audit_nopass_btn:
//
//				break;
//			case R.id.audit_pass_btn:
//
//				break;
//
//			default:
//				break;
//			}
//		}
//	};
}
