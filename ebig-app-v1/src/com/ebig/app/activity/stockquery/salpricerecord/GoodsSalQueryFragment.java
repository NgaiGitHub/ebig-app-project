package com.ebig.app.activity.stockquery.salpricerecord;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.sax.RootElement;
import android.text.Html;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RadioGroup;
import android.widget.FrameLayout.LayoutParams;

import com.ebig.app.R;
import com.ebig.app.base.BaseFirmFragment;
import com.ebig.app.custom.ui.ClearEditText;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.ebig.app.custom.adapter.CommonFilterAdapter;
import com.ebig.app.custom.adapter.CommonFilterAdapter.CommonFilter;
import com.ebig.app.dbctrl.StockQueryDbCtrl;
import com.ebig.app.utils.DateUtil;

public class GoodsSalQueryFragment extends BaseFirmFragment 
		implements OnClickListener, OnRefreshListener<ListView> {
	private static GoodsSalQueryFragment queryFragment;
	private final int GOODS_PRICE_SALRECORD_SRV = 1;// 1
	
	public PopupWindow mDateQueryPopWin;
	private Button mBeginDate;
	private Button mEndDate;
	private Button mDateQuery;
	private ClearEditText mCompanyEditText;
	private SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

	private Button mGoodsIdBtn;

	private PullToRefreshListView mGoodsPriceSalRecordListView;

	private LinkedList<Object> mGoodsPriceSalRecordItems;

	private CommonFilterAdapter mGoodsPriceSalRecordAdapter;

	private StockQueryDbCtrl mDbCtrl;
	private Bundle bundle;

	private String cellphone;
	private String channelno;
	private String goodsid; 
	private String html;

	public GoodsSalQueryFragment(Bundle bundle) {
		this.bundle = bundle;
	}

	/**
	 * 单实例
	 * @return
	 */
	static public GoodsSalQueryFragment newIntent(Bundle bundle){
		//this.bundle = bundle;
		if(queryFragment == null){
			queryFragment = new GoodsSalQueryFragment(bundle);
		}
		return queryFragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(
				R.layout.fragment_goods_search_price_sal_record, 
				container, false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		findViews();
		
		initFilterMsg();
	}

	private void findViews() {
		View viewRoot = getView();
		mGoodsIdBtn = (Button) viewRoot.findViewById(R.id.goodsInfo);
		mGoodsIdBtn.setOnClickListener(this);
		
		mGoodsPriceSalRecordListView = (PullToRefreshListView) viewRoot.findViewById(R.id.goodsPriceSalRecordListView);
		mGoodsPriceSalRecordListView.setMode(Mode.PULL_FROM_END);
		mGoodsPriceSalRecordListView.setOnRefreshListener(this);
		
		mGoodsPriceSalRecordItems = new LinkedList<Object>();
		mGoodsPriceSalRecordAdapter = new CommonFilterAdapter(getActivity(),
				ItemViewGoodsPriceSalRecord.class, mGoodsPriceSalRecordItems);
		mGoodsPriceSalRecordAdapter.setCommonFilter(new CommonFilter() {
			
			@Override
			public String filterString(Map map) {
				if (map != null)
					return (String) map.get("html");
				return null;
			}
		});
		mGoodsPriceSalRecordListView.setAdapter(mGoodsPriceSalRecordAdapter);
		
		if (mDateQueryPopWin == null){			
			initDatePopWin();
		}
		
		mDbCtrl = new StockQueryDbCtrl();
	}

	public void initDatePopWin() {
		View v = LayoutInflater.from(getActivity()).inflate(
				R.layout.popwin_good_price_salrecord_view, null);
		mDateQueryPopWin = new PopupWindow(v);
		mDateQueryPopWin.setHeight(LayoutParams.WRAP_CONTENT);
		mDateQueryPopWin.setWidth(LayoutParams.MATCH_PARENT);
		mDateQueryPopWin.setFocusable(true);
		//mDateQueryPopWin.setBackgroundDrawable(new BitmapDrawable());
		mDateQueryPopWin.setOutsideTouchable(false);
		mBeginDate = (Button) v.findViewById(R.id.beginDateBtn);
		mEndDate = (Button) v.findViewById(R.id.endDateBtn);
		mDateQuery = (Button) v.findViewById(R.id.dateBtnQuery);
		//mMonthRadioGroup = (RadioGroup) v.findViewById(R.id.radioGroupMonth);
		//mMonthRadioGroup.setOnCheckedChangeListener(this);
		mCompanyEditText = (ClearEditText) v
				.findViewById(R.id.radioGroupCompany);
		setData(mBeginDate.getId(), DateUtil.getFirstDayOfMonth());
		setData(mEndDate.getId(), DateUtil.getLastDayOfMonth());
		
		mBeginDate.setOnClickListener(this);
		mEndDate.setOnClickListener(this);
		mDateQuery.setOnClickListener(this);
	}
	
	private void setData(int requestCode, Date date) {
		if (requestCode == mBeginDate.getId()) {
			mBeginDate.setTag(date);
			mBeginDate.setText("始:" + mDateFormat.format(date));
		}
		if (requestCode == mEndDate.getId()) {
			mEndDate.setTag(date);
			mEndDate.setText("终:" + mDateFormat.format(date));
		}
	}
	
	public void showDatePopWin() {
		if (mDateQueryPopWin != null){
			if (!mDateQueryPopWin.isShowing()) {
				mDateQueryPopWin.showAsDropDown(getActionBar().getCustomView());
			} else {
				dismissDatePopWin();
			}
		}	
	}
	
	private void dismissDatePopWin() {
		if (mDateQueryPopWin != null){
			if (mDateQueryPopWin.isShowing())
				mDateQueryPopWin.dismiss();			
		}
	}
	
	private void initFilterMsg() {
		channelno = bundle.getString("channelno");
		goodsid = bundle.getString("goodsid");
		html = bundle.getString("html");
		cellphone = bundle.getString("phone");
		if (channelno == null)
			channelno = "";
		if (goodsid == null)
			goodsid = "";
		if (html == null)
			html = "";
		
		
		setGoodsFilterMsg(true);
	}
	
	private void setGoodsFilterMsg(boolean autoQuery) {
//		mGoodsChannelNoBtn.setTag(channelno);
//		mGoodsChannelNoBtn.setVisibility(View.GONE);
		mGoodsIdBtn.setText(Html.fromHtml(html));
		mGoodsIdBtn.setTag(goodsid);
		if (autoQuery){			
			goodsFilterQuery();
		}
	}
	
	// 商品过滤刷新，根据渠道号、商品ID，重新刷新现在有的数据...
	private void goodsFilterQuery() {
		String customer = "";
		if(mCompanyEditText != null){
			customer = mCompanyEditText.getText().toString();			
		}
		if (goodsid == null || "".equals(goodsid)) {
			getToast().makeText(getActivity(), "注意：[ * ]必须输入商品ID ~ !!", null);
			return;
		}
		mGoodsPriceSalRecordItems.clear();
		mGoodsPriceSalRecordListView.setTag(1);
		mGoodsPriceSalRecordListView.setTag(-1, GOODS_PRICE_SALRECORD_SRV);
		newAsyncTaskExecute(GOODS_PRICE_SALRECORD_SRV, 
				1, customer);
	}

	@Override
	public void onClick(View v) {
		Date beginDate;
		Calendar lastDate;
		Date endDate;
		
		int id = v.getId();
		switch (id) {
		case R.id.callBtn:
			if(cellphone != null && !"".equals(cellphone)){  //提示打电话
				AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
				alert.setTitle("消息提示")
						.setMessage("联系采购员：[ "+cellphone+" ] 电话？")
						.setPositiveButton("确 定", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
								Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:"
										+ cellphone));
								startActivity(intent);
							}
						})
						.setNegativeButton("取消", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int which) {
							}
						});
				alert.create().show();
			}else {
				getToast().makeText(getActivity(),
						"未维护改采购员电话信息,请联系管理员确认。", null);
			}
			break;
		case R.id.radioBtnOneMonth:
			setData(mBeginDate.getId(), DateUtil.getFirstDayOfMonth());
			setData(mEndDate.getId(), DateUtil.getLastDayOfMonth());
			break;
		case R.id.radioBtnThreeMonth:
			beginDate = DateUtil.getLastDayOfMonth();
			lastDate = Calendar.getInstance();
			lastDate.set(Calendar.DATE, 1);// 设为当前月的1号
			lastDate.add(Calendar.MONTH, -2);// 
			lastDate.add(Calendar.DATE, +1);// 减去一天，变为当月最后一天 
			endDate = lastDate.getTime();
			setData(mBeginDate.getId(),	endDate);
			setData(mEndDate.getId(), beginDate);
			break;
		case R.id.radioBtnSixMonth:
			beginDate = DateUtil.getLastDayOfMonth();
			lastDate = Calendar.getInstance();
			lastDate.set(Calendar.DATE, 1);// 设为当前月的1号
			lastDate.add(Calendar.MONTH, -5);// 
			lastDate.add(Calendar.DATE, +1);// 减去一天，变为当月最后一天 
			endDate = lastDate.getTime();
			setData(mBeginDate.getId(),	endDate);
			setData(mEndDate.getId(), beginDate);
			break;
		
		default:
			break;
		}
		
		if(mBeginDate != null){
			if(id == mBeginDate.getId()){
				showDataPicker(id); 
				return ;
			}
		}
		
		if(mEndDate != null){
			if(id == mEndDate.getId()){
				showDataPicker(id); 
				return ;
			}
		}
			
		if(mDateQuery != null){
			if(id == mDateQuery.getId()){
				Date dateB = (Date)mBeginDate.getTag();
				Date dateE = (Date)mEndDate.getTag();
				if(!dateB.after(dateE)){
					goodsFilterQuery();
					dismissDatePopWin();				
				}else {
					getToast().makeText(getActivity(), 
							"搞错了,开始日期不能大于结束日期", null);
				}
				return ;
			} 
		}
	}

	private void showDataPicker(final int viewId) {
		int mYear;
		int mMonth;
		int mDay;
		final Calendar c = Calendar.getInstance();
		mYear = c.get(Calendar.YEAR);
		mMonth = c.get(Calendar.MONTH);
		mDay = c.get(Calendar.DAY_OF_MONTH);
		new DatePickerDialog(getActivity(), new OnDateSetListener() {
			
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear,
					int dayOfMonth) {
				Calendar calendar = Calendar.getInstance();
				calendar.set(year, monthOfYear, dayOfMonth);
				setData(viewId, calendar.getTime());
			}
		},
				mYear, mMonth, mDay).show();
	}
	
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		inflater.inflate(R.menu.goodssalquery_menu, menu);
	}
	
	@Override
	public void onRefresh(PullToRefreshBase<ListView> refreshView) {
		setLastUpdatedLabel();
		goodsFilterQueryMore();
	}
	
	@Override
	protected Object doInBackgroundTask(int asyncid, Object[] params) {
		Object resultObj = null;
		switch (asyncid) {
		case GOODS_PRICE_SALRECORD_SRV:
			resultObj = mDbCtrl.goodsPriceSalRecordSrv(Integer.valueOf(params[0].toString()), goodsid,
					channelno, (String) params[1],
					(Date) mBeginDate.getTag(), (Date) mEndDate.getTag());
			break;

		default:
			break;
		}
		return resultObj;
	}
	
	@Override
	protected void onPostExecuteTask(int asyncid, Object result) {
		super.onPostExecuteTask(asyncid, result);
		
		int curPage = Integer.valueOf(mGoodsPriceSalRecordListView.getTag().toString());
		if(curPage<=1)
			mGoodsPriceSalRecordItems.clear();
		List<Object> items = (List<Object>) result;
		if(items != null)
			mGoodsPriceSalRecordItems.addAll(items);
		mGoodsPriceSalRecordAdapter.notifyDataSetChanged(true);
		onRefreshComplete();
	}

	/**
	 * 关闭刷新...
	 */
	private void onRefreshComplete() {
		if (mGoodsPriceSalRecordListView != null){			
			if (mGoodsPriceSalRecordListView.isRefreshing())
				mGoodsPriceSalRecordListView.onRefreshComplete();
		}
	}
	
	private void setLastUpdatedLabel() {
		String label = DateUtils.formatDateTime(getActivity(),
				System.currentTimeMillis(), DateUtils.FORMAT_SHOW_TIME
						| DateUtils.FORMAT_SHOW_DATE
						| DateUtils.FORMAT_ABBREV_ALL);

		if (mGoodsPriceSalRecordListView != null)
			mGoodsPriceSalRecordListView.getLoadingLayoutProxy()
					.setLastUpdatedLabel(label);
	}
	// / 向下刷新ListView.End****
	
	/**
	 * 查询更多
	 */
	private void goodsFilterQueryMore() {
		String customer = "";
		if(mCompanyEditText != null)
			customer = mCompanyEditText.getText().toString();
		if (goodsid == null || "".equals(goodsid)) {
			getToast().makeText(getActivity(), "注意：[ * ]必须输入商品ID ~ !!", null);
			return;
		}
		int curPage = (Integer) mGoodsPriceSalRecordListView.getTag();
		int nextPage = curPage + 1;
		mGoodsPriceSalRecordListView.setTag(nextPage);
		int sycid = (Integer) mGoodsPriceSalRecordListView.getTag(-1);
		
		newAsyncTaskExecute(sycid, nextPage, customer);
	}
}
