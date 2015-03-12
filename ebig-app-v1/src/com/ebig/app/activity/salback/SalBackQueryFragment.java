package com.ebig.app.activity.salback;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.ebig.app.R;
import com.ebig.app.activity.auditmgr.RecordFragment;
import com.ebig.app.base.BaseFirmFragment;
import com.ebig.app.custom.adapter.CommonAdapter;
import com.ebig.app.custom.ui.EbigSpinner;
import com.ebig.app.dbctrl.SalBackDbCtrl;
import com.ebig.app.utils.JSONUtil;

public class SalBackQueryFragment extends BaseFirmFragment implements OnClickListener{
	
	private View layer;
	private EditText mCompanyNoEd;
	private Button mCompanyNoNextBtn;
	private EbigSpinner mWaridSpin;
	
	private View layer1;
	private EditText mGoodsNoEd;
	private Button mGoodsNoNextBtn;
	private Button mGoodsSalBackSureBtn;
	private Button mGoodsNoPrevBtn;
	
	private View layer2;
	private ListView mGoodsLv;
	private CommonAdapter mAdapter;
	private Button mGoodsLvContinueBtn;
	
	private View layer3;
	private Button mSalBackCancleBtn;
	private Button mSalBackCommitBtn;

	private String pCompanyNo;
	private String pGoodsNo;
	
	protected static SalBackDbCtrl mCtrl;
	
	static public SalBackQueryFragment newIntance(Bundle bundle) {
		SalBackQueryFragment mFragment = new SalBackQueryFragment();
		mFragment.setArguments(bundle);
		return mFragment; 
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_salback, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		findViews();
		super.onActivityCreated(savedInstanceState);
		
		// 获取salbackreasonid  , warid;
		newAsyncTaskExecute(-1, -1);
		
	}

	private void findViews() {
		layer = getView().findViewById(R.id.layer);
		mCompanyNoEd = (EditText) getView().findViewById(R.id.salback_companyno_ed);
		mCompanyNoNextBtn = (Button) getView().findViewById(R.id.salback_companyno_nextbtn);
		mCompanyNoNextBtn.setOnClickListener(this);
		
		layer1 = getView().findViewById(R.id.layer1);
		mGoodsNoEd = (EditText) getView().findViewById(R.id.salback_goodsno_ed);
		mGoodsNoNextBtn = (Button) getView().findViewById(R.id.salback_goodsno_nextbtn);
		mGoodsSalBackSureBtn = (Button) getView().findViewById(R.id.salback_surebtn);
		mGoodsNoPrevBtn = (Button) getView().findViewById(R.id.salback_goodsno_prevbtn);
		mGoodsSalBackSureBtn.setVisibility(View.GONE);
		mGoodsNoPrevBtn.setOnClickListener(this);
		mGoodsSalBackSureBtn.setOnClickListener(this);
		mGoodsNoNextBtn.setOnClickListener(this);
		
		layer2 = getView().findViewById(R.id.layer2);
		mGoodsLv = (ListView) getView().findViewById(R.id.salback_lv);
		mGoodsLvContinueBtn = (Button) getView().findViewById(R.id.salback_lv_continue);
		mGoodsLvContinueBtn.setOnClickListener(this);
		
		layer3 = getView().findViewById(R.id.layer3);
		layer3.setVisibility(View.GONE);
		mSalBackCancleBtn = (Button) getView().findViewById(R.id.salback_lv_check_cancel);
		mSalBackCommitBtn = (Button) getView().findViewById(R.id.salback_lv_check_commit);
		mSalBackCancleBtn.setOnClickListener(this);
		mSalBackCommitBtn.setOnClickListener(this);
		
		if(mAdapter == null)
			mAdapter = new CommonAdapter(getActivity(), Item.class, new ArrayList());
		mGoodsLv.setAdapter(mAdapter);
		
		mWaridSpin = (EbigSpinner) getView().findViewById(R.id.salback_warid_spin);
//		List waridList = new ArrayList();
//		Map waridMap = new HashMap();
//		waridMap.put("key", "100");
//		waridMap.put("value", "A仓");
//		waridList.add(waridMap);
//		ArrayAdapter<String> adapterQuality = new ArrayAdapter<String>(getActivity(),
//				android.R.layout.simple_spinner_item, list);  //初始化Spinner样式适配器
//		adapterQuality.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // 使用系统内置下拉菜单样式
//		mWaridSpin.setAdapter(getActivity(),waridList);
		
		mCtrl = new SalBackDbCtrl();
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		if(id == mCompanyNoNextBtn.getId()){
			// 录入客户编码  下一步按钮
			pCompanyNo = mCompanyNoEd.getText().toString().trim();
			if(pCompanyNo == null || "".equals(pCompanyNo)){
				getToast().makeText(getActivity(), "客户编码输入有错!", null);
			}else {
				layer.setVisibility(View.GONE);
				layer1.setVisibility(View.VISIBLE);
			} 
		}
		if(id == mGoodsNoNextBtn.getId()){
			// 录入商品编码  下一步按钮
			pGoodsNo = mGoodsNoEd.getText().toString().trim();
			if(pGoodsNo == null || "".equals(pGoodsNo)){
				getToast().makeText(getActivity(), "商品编码输入有错!", null);
			}else {
				// 同步获取商品信息 并显示 TODO
				newAsyncTaskExecute(1, -1);
				layer2.setVisibility(View.VISIBLE);
				mGoodsLvContinueBtn.setVisibility(View.VISIBLE);
				layer3.setVisibility(View.GONE);
			} 
		}
		if(id == mGoodsLvContinueBtn.getId()){
			// 查看商品信息列表,或勾选商品后,或录入商品信息后   继续添加其他商品按钮
			// TODO 将勾选的商品添加到列表内
			if(Item.isSelected != null){
				List goodsList = mAdapter.getList();
				for (int i = 0; i < Item.isSelected.size(); i++) {
					if(Item.isSelected.get(i)){
						// 选择的一项
						mCtrl.addGoodsForSalBack((Map)goodsList.get(i));
					}
				} 
				Item.isSelected.clear();// 清空选中状态
				mAdapter.notifyDataSetInvalidated();
				
				if(mCtrl.getGoodsForSalBack()!= null && mCtrl.getGoodsForSalBack().size() > 0){
					mGoodsSalBackSureBtn.setText("已录入 [ "+mCtrl.getGoodsForSalBack().size()+" ]个销退商品,点击查看");
					mGoodsSalBackSureBtn.setVisibility(View.VISIBLE);				
				}else {
					mGoodsSalBackSureBtn.setVisibility(View.GONE);
				}
			}
			layer2.setVisibility(View.GONE);
		}
		if(id == mGoodsSalBackSureBtn.getId()){
			// 查看商品信息列表,或勾选商品后,或录入商品信息后   继续添加其他商品按钮
			// TODO 将勾选的商品添加到列表内
			mAdapter.setList(mCtrl.getGoodsForSabBack2List());
			
			layer2.setVisibility(View.VISIBLE);
			layer3.setVisibility(View.VISIBLE);
			mGoodsLvContinueBtn.setVisibility(View.GONE);
			}
		if(id == mGoodsNoPrevBtn.getId()){
			// 上一步
			layer1.setVisibility(View.GONE);
			layer.setVisibility(View.VISIBLE);
		}
		if(id == mSalBackCancleBtn.getId()){
			// 上一步
			layer2.setVisibility(View.GONE);
			layer1.setVisibility(View.VISIBLE);
		}
        if(id == mSalBackCommitBtn.getId()){
			// 提交
        	// getToast().makeText(getActivity(), mCtrl.creatGoodsForSalBack().toString(), null);
        	if(mCtrl.commitGoodsForSalBack(mCtrl.creatGoodsForSalBack())){
        		layer2.setVisibility(View.GONE);
    			layer1.setVisibility(View.VISIBLE);
    			
    			mCtrl.getGoodsForSalBack().clear();
    			mGoodsSalBackSureBtn.setVisibility(View.GONE);
        	}else {
				
			}
		}
	}

	@Override
	protected void onPostExecuteTask(int asyncid, Object result) {
		super.onPostExecuteTask(asyncid, result);
		switch (asyncid) {
		case -1:
			mWaridSpin.setAdapter(getActivity(),mCtrl.getDisptypeItem("warid".toUpperCase()));
			mCompanyNoNextBtn.setVisibility(View.VISIBLE);
			break;
		case 1:
			mAdapter.setList(JSONUtil.json2List(result.toString()));
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
		Object object = null;
		switch (asyncid) {
		case -1:// 获取下拉框信息  salbackreasonid  , warid;
			mCtrl.queryDisptypeItem("salbackreasonid".toUpperCase());
			mCtrl.queryDisptypeItem("warid".toUpperCase());
			break;
		case 1:
			object = mCtrl.querySalBackGoods(1, mWaridSpin.getItemKey(), pCompanyNo, pGoodsNo);
			break;
		default:
			break;
		}
		return object;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		setDefaultBarTitle("销售退货申请");
		inflater.inflate(R.menu.qrcode_menu, menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		return super.onOptionsItemSelected(item);
	}

	
	
}
