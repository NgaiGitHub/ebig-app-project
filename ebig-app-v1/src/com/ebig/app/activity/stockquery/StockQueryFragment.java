package com.ebig.app.activity.stockquery;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.ebig.app.R;
import com.ebig.app.base.BaseFirmFragment;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.slidinglayer.SlidingLayer;
import com.ebig.app.custom.adapter.CommonAdapter;
import com.ebig.app.custom.adapter.CommonFilterAdapter.CommonFilter;
import com.ebig.app.custom.adapter.CommonFilterAdapter;
import com.ebig.app.dbctrl.StockQueryDbCtrl;
import com.ebig.app.activity.stockquery.ItemViewGoodsInfo;
import com.ebig.app.activity.stockquery.salpricerecord.GoodsSalQueryActivity;

import com.ebig.app.custom.ui.ScanClearEditText;
import com.ebig.app.custom.ui.ScanClearEditText.OnTextChangedListener;
import com.ebig.app.custom.ui.SegmentedGroup;

public class StockQueryFragment extends BaseFirmFragment 
	implements OnRefreshListener, OnClickListener{
	public static final String SEARCHMSG_KEY = "msg";
	/**任务标签：查询商品信息*/
	protected final int GOODS_QUERY_SRV = 1;
	/**任务标签：查询指定商品详细信息*/
	private final int GOODSDTL_QUERY_SRV = 2;
	/**任务标签：查询商品类别列表（一级列表）*/
	private final int QUERY_SORT_FIRST_LEVEL_LIST_SRV = 3;
	/**任务标签：查询商品类别列表（二级列表）*/
	private final int QUERY_SORT_SECOND_LEVEL_LIST_SRV = 4;
	/**实例对象*/
	static public StockQueryFragment queryFragment = null;
	
	/**一般商品信息ListView*/
	private PullToRefreshListView mGoodsListView;
	/**一般商品信息数据列表*/
	private LinkedList<Object> mGoodsItems;
	/**当前商品*/
	private Map goodsCurrentMap;
	/**带关键字过滤的适配器*/
	private CommonFilterAdapter mGoodsFilterAdapter;
	/**商品详细信息列表的适配器*/
	private CommonAdapter mPinnedGoodsDtlAdapter;
	private CommonFilter mCommonAdapterFilter;
	private int page = 1;
	private StockQueryDbCtrl mDbCtrl;
	/**Radio Tab选择器*/
	private SegmentedGroup mSegmentedGroup;
	private SlidingLayer mSlidLayerGoodsDtl, mSlidLayerSortFirstLv, mSlidLayerSortSecondLv;
	/**商品详情数据列表*/
	private LinkedList<Object> mGoodsDtlItems;
	/**商品详情ListView*/
	private ListView mPinnedSectionListView;
	/**标题*/
	private TextView mHeadTextView;
	/**搜索关键字输入框*/
	private ScanClearEditText scanClearEditText;
	
	private ListView sortFirstLevel, sortSecondLevel;
	private LinkedList<Object> sortFirstLevelItems, sortSecondLevelItems;
	private CommonAdapter sortFirstLvAdapter, sortSecondLvAdapter;
	
	private Map currentMapLevelOne;
	
	/**
	 * 单实例
	 * @return
	 */
	static public StockQueryFragment newIntent(){
		if(queryFragment == null){
			queryFragment = new StockQueryFragment();
		}
		return queryFragment;
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_stockquery, container, false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		
		findViews();
		
		page = 1;
		goToGoodsQuery(GOODS_QUERY_SRV);
	}
	
	/**
	 * 初始化视图组件等
	 */
	private void findViews(){
		View rootView = getView();
		if(rootView == null) return ;
		
		//初始化主界面商品信息列表
		mGoodsListView = (PullToRefreshListView) rootView
				.findViewById(R.id.goodsInfoSearchListView);
		mGoodsItems = new LinkedList<Object>();
		mGoodsFilterAdapter = new CommonFilterAdapter(getActivity(), ItemViewGoodsInfo.class, mGoodsItems);
		mCommonAdapterFilter = new CommonFilter() {
			
			@Override
			public String filterString(Map map) {
				String html = (String) map.get("html");
				return html;
			}
		};
		mGoodsFilterAdapter.setCommonFilter(mCommonAdapterFilter);
		mGoodsListView.setAdapter(mGoodsFilterAdapter);
		mGoodsListView.setMode(Mode.PULL_FROM_END);
		mGoodsListView.setOnRefreshListener(this);
		mGoodsListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				if(mSlidLayerGoodsDtl.isOpened()){ //关闭已打开的商品详情页面
					closeGoodsDtlLayout();
				}else if(mSlidLayerSortFirstLv.isOpened()){  //关闭已打开的一级商品类别页面
					closeSortFirstLvLayout();
				}else if(mSlidLayerSortSecondLv.isOpened()){  //关闭已打开的二级商品类别页面
					closeSortSecondLvLayout();
				}else{
					goodsCurrentMap = (Map) mGoodsItems.get(position - 1);
					newAsyncTaskExecute(GOODSDTL_QUERY_SRV);
				}
			}
		});
		
		//初始化主界面Tab切换视图
		mSegmentedGroup = (SegmentedGroup) rootView.findViewById(R.id.segmentedGroup);
		mSegmentedGroup.setTintColor(getResources().getColor(R.color.radio_button_selected_color));

		//初始化商品详情界面商品信息列表（SlidingLayer）
		mSlidLayerGoodsDtl = (SlidingLayer) rootView.findViewById(R.id.slidingLayer_goodsdtl);
		mGoodsDtlItems = new LinkedList<Object>();
		mPinnedSectionListView = (ListView) rootView.findViewById(R.id.stockQueryRightListView);
		mPinnedGoodsDtlAdapter = new CommonAdapter(getActivity(), ItemViewGoodsInfoDtl.class, mGoodsDtlItems);
		mPinnedSectionListView.setAdapter(mPinnedGoodsDtlAdapter);
		mPinnedSectionListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position,
					long id) {
				Map goodsMap = (Map) mGoodsDtlItems.get(position);
				String goodsid = (String) goodsCurrentMap.get("goodsid");
				String goodsname = (String) goodsCurrentMap.get("goodsname");
				String channel = (String) goodsMap.get("ditchno");// 渠道号
				openGoodsPriceSearch(channel, goodsid,goodsname);  
			}
		});
		
		mHeadTextView = (TextView) rootView.findViewById(R.id.headTextView);
		mHeadTextView.setOnClickListener(this);
		
		//初始化关键字输入框
		if(getActivity() instanceof StockQueryActivity){
			scanClearEditText = ((StockQueryActivity)getActivity()).scanClearEditText;
		}
		scanClearEditText.setOnTextChangedListener(new OnTextChangedListener() {
			
			@Override
			public boolean onTextChange(String text) {
				//当输入框文字变化时即时过滤本地数据
				mGoodsFilterAdapter.adapterFilter(text);
				return false;
			}
		});
		
		//初始化商品类别选择（一级）界面（SlidingLayer）
		mSlidLayerSortFirstLv
		 	= (SlidingLayer)rootView.findViewById(R.id.slidingLayer_sort_selector);
		mSlidLayerSortFirstLv.setStickTo(SlidingLayer.STICK_TO_RIGHT);
		sortFirstLevel = (ListView)rootView.findViewById(R.id.sort_select_listView);
		sortFirstLevelItems = new LinkedList<Object>(); 
		sortFirstLvAdapter = new CommonAdapter(getActivity(), 
				ItemViewSortFirstLvList.class, sortFirstLevelItems);
		sortFirstLevel.setAdapter(sortFirstLvAdapter);
		sortFirstLevel.setOnItemClickListener(firstLvListener);
		
		//初始化商品类别选择（二级）界面（SlidingLayer）
		mSlidLayerSortSecondLv
			=(SlidingLayer)rootView.findViewById(R.id.slidingLayer_sort_selector_second_lv);
		mSlidLayerSortSecondLv.setStickTo(SlidingLayer.STICK_TO_RIGHT);
		sortSecondLevel = (ListView)rootView.findViewById(R.id.sort_select_listView_second);
		sortSecondLevelItems = new LinkedList<Object>();
		sortSecondLvAdapter = new CommonAdapter(getActivity(), 
				ItemViewSortSecondLvList.class, sortSecondLevelItems);
		sortSecondLevel.setAdapter(sortSecondLvAdapter);
		sortSecondLevel.setOnItemClickListener(secondLvListener);
		 
		//初始化DbCtrl
		mDbCtrl = new StockQueryDbCtrl();
	}
	
	private AdapterView.OnItemClickListener firstLvListener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view,
				int position, long id) {
			currentMapLevelOne = (Map) sortFirstLevelItems.get(position);
			String groupId = (String) currentMapLevelOne.get("groupid");
			String hasChildFlag = (String) currentMapLevelOne.get("haschildflag");
			if("1".equals(hasChildFlag)){ //有二级类别则异步查询二级类别
				newAsyncTaskExecute(QUERY_SORT_SECOND_LEVEL_LIST_SRV, groupId);
			}else{
				closeSortFirstLvLayout();
				//设置“商品列表”为选择状态
				mSegmentedGroup.check(R.id.goodsSearchComplexTab);
				newAsyncTaskExecute(GOODS_QUERY_SRV, groupId, "1", "", "");
			}
		}
	};
	
	private AdapterView.OnItemClickListener secondLvListener = new AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, 
				int position, long id) {
			String groupId = (String)((Map)sortSecondLevelItems.get(position)).get("groupid");
			closeSortFirstLvLayout();
			closeSortSecondLvLayout();
			//设置“商品列表”为选择状态
			mSegmentedGroup.check(R.id.goodsSearchComplexTab);
			newAsyncTaskExecute(GOODS_QUERY_SRV, groupId, "1", "", "");
		}
	};

	
	/**
	 * 实现OnRefreshListener接口的方法
	 * @param refreshView
	 */
	@Override
	public void onRefresh(PullToRefreshBase refreshView) {
		String label = DateUtils.formatDateTime(getActivity(), System.currentTimeMillis(),
				DateUtils.FORMAT_SHOW_TIME | DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_ABBREV_ALL); 
		refreshView.getLoadingLayoutProxy().setLastUpdatedLabel(label);
		
		if(mGoodsItems != null && mGoodsItems.size()>0){			
			++page;
			goToGoodsQuery(GOODS_QUERY_SRV);
		}else {
			if(mGoodsListView.isRefreshing()) 
				mGoodsListView.onRefreshComplete();
		}
	}
	
	/**
	 * 根据选择的Tab、输入的关键字进行异步查询商品信息
	 * @param sycid
	 */
	protected void goToGoodsQuery(final int sycid) {
		String searchText = getSearchText();
		String salby ="";
		String newflag = "";
		String groupid = "";
		int id = mSegmentedGroup.getCheckedRadioButtonId();
		if(id == R.id.goodsSearchComplexTab){ 
			salby = "1";
		}
		if(id == R.id.goodsSearchRcmdTab){ 
			newflag = "1";
		} 
		if(id == R.id.goodsSearchGroupTab){ 
			String goodsGroupid = (String) mSegmentedGroup.getTag();
			if(goodsGroupid != null)
				groupid = goodsGroupid;
		}
		newAsyncTaskExecute(sycid, groupid, salby, newflag, searchText);
	}

	/**
	 * 获得搜索框输入的关键字
	 * @return
	 */
	protected String getSearchText() {
		return scanClearEditText.getText().toString().trim();
	}
	
	@Override
	protected Object doInBackgroundTask(int asyncid, Object[] params) {
		Object resultObj = null;
		switch (asyncid) {
		case GOODS_QUERY_SRV:
			resultObj = mDbCtrl.goodsQuerySrv(page, (String)params[0], 
					(String)params[1], (String)params[2], (String)params[3]);
			break;
		case GOODSDTL_QUERY_SRV:
			String goodsid = (String) goodsCurrentMap.get("goodsid");
			resultObj = mDbCtrl.goodsDtlQuerySrv(goodsid);
			break;
		case QUERY_SORT_FIRST_LEVEL_LIST_SRV:
			resultObj = mDbCtrl.stockQueryFirstLevelListSrv();
			break;
		case QUERY_SORT_SECOND_LEVEL_LIST_SRV:
			resultObj = mDbCtrl.stockQuerySecondLevelListSrv((String)params[0]);
			break;
		default:
			break;
		}
		return resultObj;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	protected void onPostExecuteTask(int asyncid, Object result) {
		super.onPostExecuteTask(asyncid, result);
		switch (asyncid) {
		case GOODS_QUERY_SRV:
			if (page <= 1){				
				mGoodsItems.clear();
			}
			List<Object> items = (List<Object>) result;
			if(items != null){
				mGoodsItems.addAll(items);
			}
			getToast().makeText(getActivity(), "已加载商品数（件）：" + mGoodsItems.size(), null);
			mGoodsFilterAdapter.notifyDataSetChanged(true);
			if (mGoodsListView.isRefreshing()){
				mGoodsListView.onRefreshComplete();
			}
			break;
		case GOODSDTL_QUERY_SRV:
			mGoodsDtlItems.clear();
			List<Object> dtlItems = (List<Object>) result;
			if(dtlItems != null && dtlItems.size() > 0){
				mGoodsDtlItems.addAll(dtlItems);
				if(mHeadTextView != null){
					mHeadTextView.setText(Html.fromHtml(goodsCurrentMap.get("html")
							.toString()));
				}
				openGoodsDtlLayout();
			}else{
				getToast().makeText(getActivity(), "查无记录", null);
			}
			mPinnedGoodsDtlAdapter.notifyDataSetChanged();
			break;
		case QUERY_SORT_FIRST_LEVEL_LIST_SRV:
			List<Object> firstLvItems = (List<Object>) result;
			sortFirstLevelItems.clear();
			if(firstLvItems != null && firstLvItems.size() > 0){
				sortFirstLevelItems.addAll(firstLvItems);
			}
			openSortFirstLvLayout();
			sortFirstLvAdapter.notifyDataSetChanged();
			break;
		case QUERY_SORT_SECOND_LEVEL_LIST_SRV:
			List<Object> secondLvItems = (List<Object>) result;
			sortSecondLevelItems.clear();
			if(secondLvItems != null && secondLvItems.size() > 0){
				sortSecondLevelItems.addAll(secondLvItems);
			}
			openSortSecondLvLayout();
			sortSecondLvAdapter.notifyDataSetChanged();
			break;

		default:
			break;
		}
	}
	
	@Override
	protected void onPostExecuteTaskError(int asyncid, Exception result) {
		super.onPostExecuteTaskError(asyncid, result);
		if(result instanceof Exception){
			getToast().makeTextError(getActivity(), ((Exception)result).getMessage(), 
					null);
		}
	}
 
	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
		case R.id.goodsSearchComplexTab: //商 品 列 表
			page = 1;
			mSegmentedGroup.setTag("");
			goToGoodsQuery(GOODS_QUERY_SRV);
			break;
		case R.id.goodsSearchRcmdTab:  //TODO 新 品 推 荐 
			page = 1;
			mSegmentedGroup.setTag("");
			goToGoodsQuery(GOODS_QUERY_SRV);
			break;
		case R.id.goodsSearchGroupTab:  //商 品 分 类
			if(sortFirstLevelItems != null && sortFirstLevelItems.size() > 0){
				openSortFirstLvLayout();  //之前已经查过则直接打开
//				if(sortSecondLevelItems != null && sortSecondLevelItems.size() > 0){
//					openSortSecondLvLayout();
//				}
				break;
			}
			newAsyncTaskExecute(QUERY_SORT_FIRST_LEVEL_LIST_SRV);
			break;
		case R.id.headTextView:  //点击了商品详情页面的标题
			String goodsid = (String) goodsCurrentMap.get("goodsid");
			String goodsname = (String) goodsCurrentMap.get("goodsname");
			openGoodsPriceSearch(null, goodsid,goodsname);
			break;
		default:
			break;
		}
	}
	
	/**
	 * 打开商品详情页面(SlidingLayer)
	 */
	private void openGoodsDtlLayout(){
		if (!mSlidLayerGoodsDtl.isOpened()) {
			mSlidLayerGoodsDtl.openLayer(true);
		}
	}
	
	/**
	 * 关闭商品详情页面(SlidingLayer)
	 */
	private void closeGoodsDtlLayout() {
		if (mSlidLayerGoodsDtl.isOpened()) {
			mSlidLayerGoodsDtl.closeLayer(true);
		}
	}
	
	/**
	 * 打开商品类别选择（一级）页面(SlidingLayer)
	 */
	private void openSortFirstLvLayout(){
		if (!mSlidLayerSortFirstLv.isOpened()) {
			mSlidLayerSortFirstLv.openLayer(true);
		}
	}
	
	/**
	 * 关闭商品类别选择（一级）页面(SlidingLayer)
	 */
	private void closeSortFirstLvLayout() {
		if (mSlidLayerSortFirstLv.isOpened()) {
			mSlidLayerSortFirstLv.closeLayer(true);
		}
	}
	
	/**
	 * 打开商品类别选择（二级）页面(SlidingLayer)
	 */
	private void openSortSecondLvLayout() {
		if (!mSlidLayerSortSecondLv.isOpened()) {
			mSlidLayerSortSecondLv.openLayer(true);
		}
	}
	
	/**
	 * 关闭商品类别选择（二级）页面(SlidingLayer)
	 */
	private void closeSortSecondLvLayout() {
		if (mSlidLayerSortSecondLv.isOpened()) {
			mSlidLayerSortSecondLv.closeLayer(true);
		}
	}
	
	/**
	 * 打开商品流向查询页面
	 * @param channel
	 * @param goodsid
	 * @param goodsname
	 */
	private void openGoodsPriceSearch(String channel,String goodsid,String goodsname){
		closeGoodsDtlLayout();
		Intent mGoPriceQueryIntent = new Intent(getActivity(), GoodsSalQueryActivity.class);
		Bundle bundle = new Bundle();
		bundle.putString("channelno", channel);
		bundle.putString("goodsid", goodsid);
		
		String html = (String) goodsCurrentMap.get("html");
		String cellphone = (String) goodsCurrentMap.get("mobile");// 采购员电话号码
		bundle.putString("html", html);
		bundle.putString("phone", cellphone);
		
		mGoPriceQueryIntent.putExtras(bundle);
		startActivity(mGoPriceQueryIntent);
	}
}
