package com.ebig.app.activity.salback;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;

import com.ebig.app.R;
import com.ebig.app.custom.adapter.CommonViewHolder;
import com.ebig.app.custom.ui.EbigSpinner;

public class Item extends CommonViewHolder {

	private View v;
	private TextView mCompanyTx;
	private TextView mGoodsTx;
	private CheckBox mCheckBox;
	
	private View layer;
	private EditText mSalbackQty;
	private EbigSpinner mSalbackRsid;
	private Button mBtn;
	
	// 用来控制CheckBox的选中状况 
    protected static HashMap<Integer, Boolean> isSelected;
    private Map map;
	
	@Override
	public View createView() {
		if(isSelected == null)
		   isSelected = new HashMap<Integer, Boolean>(); 
		
		v = LayoutInflater.from(mContext).inflate(R.layout.item_salback, null);
		mCompanyTx = (TextView) v.findViewById(R.id.item_salback_company);
		mGoodsTx = (TextView) v.findViewById(R.id.item_salback_goodsinfo);
		mCheckBox = (CheckBox) v.findViewById(R.id.item_salback_cb);
//		mCheckBox.setOnCheckedChangeListener(mCheckedChangeListener);
		mCheckBox.setOnClickListener(mCheckClickListener);
		
		layer = v.findViewById(R.id.layer);
		layer.setVisibility(View.GONE);
		mSalbackQty = (EditText) v.findViewById(R.id.item_salback_qty);
		mSalbackRsid = (EbigSpinner) v.findViewById(R.id.item_salback_spin);
		
//		// TODO  初始销售原因列表
//		List<String> list = new ArrayList<String>();
//		list.add("3");
//		list.add("5");
//		list.add("10");
//		ArrayAdapter<String> adapterQuality = new ArrayAdapter<String>(mContext,
//				android.R.layout.simple_spinner_item, list);  //初始化Spinner样式适配器
//		adapterQuality.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item); // 使用系统内置下拉菜单样式
		mSalbackRsid.setAdapter(mContext,SalBackQueryFragment.mCtrl.getDisptypeItem("salbackreasonid".toUpperCase()));
		
		mBtn = (Button) v.findViewById(R.id.item_salback_btn);
		mBtn.setOnClickListener(mClickListener);
		return v;
	}

	@Override
	public void refashViewValue(Object obj) {
		mBtn.setTag(position);
		map = (Map) obj;
		String companyHtml = "销售总单ID:"+map.get("salid")+"<br />(编码)客户名称:("+map.get("customno")+")"+map.get("customname");
		mCompanyTx.setText(Html.fromHtml(companyHtml)); 
		String goodsHtml = (String) map.get("html");
		
		String salbackqty_v = (String) map.get("salbackqty");
		String salbackreasonid_v = (String) map.get("salbackreasonid");
		String salbackreasonidvalue_v = (String) map.get("salbackreasonidvalue");
		
		if(salbackqty_v == null) salbackqty_v = "";
		if(salbackreasonid_v == null) salbackreasonid_v = "";
		if(salbackreasonidvalue_v == null) salbackreasonidvalue_v = "";
		
		mGoodsTx.setText(Html.fromHtml(goodsHtml+ getSalBackInfo(salbackqty_v,salbackreasonidvalue_v)));
		
		Object cbSel = map.get("checkbox");
		if(cbSel != null){
			if(cbSel instanceof Boolean && ((Boolean)cbSel) == true)
			mCheckBox.setChecked(true);
		}else {mCheckBox.setChecked(getIsSelected(position));}
		
		changeBackgroud(mCheckBox);
		layer.setVisibility(View.GONE);
	}
	
	
	private String getSalBackInfo(String salbackqty, String salbackreasonid) {
		String s = "<br /><font color='#999999'>销退数量:</font>"+salbackqty+"<font color='#999999'>  销退原因:</font><font >"+salbackreasonid+"</font><br />";
		return s;
	}

	private void changeBackgroud(CheckBox cb) {
		layer.setVisibility(cb.isChecked() == true ? View.VISIBLE : View.GONE);
		layer.setBackgroundColor(cb.isChecked() == true ? mContext.getResources().getColor(android.R.color.holo_orange_light) : mContext.getResources().getColor(R.color.default_white));
		mCheckBox.setBackgroundColor(cb.isChecked() == true ? mContext.getResources().getColor(android.R.color.holo_orange_light) : mContext.getResources().getColor(R.color.default_white));
		mGoodsTx.setBackgroundColor(cb.isChecked() == true ? mContext.getResources().getColor(android.R.color.holo_orange_light) : mContext.getResources().getColor(R.color.default_white));
		
		if(layer.getVisibility() == View.VISIBLE){
			// 显示编辑框
			String salbackqty_v = (String) map.get("salbackqty");
			String salbackreasonid_v = (String) map.get("salbackreasonid");
			String salbackreasonidvalue_v = (String) map.get("salbackreasonidvalue");
			
			if(salbackqty_v == null) salbackqty_v = "";
			if(salbackreasonid_v == null) salbackreasonid_v = "";
			if(salbackreasonidvalue_v == null) salbackreasonidvalue_v = "";
			
			mSalbackQty.setText(salbackqty_v);
			mSalbackRsid.setSelection(0);
		}
	}

	private OnClickListener mCheckClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if(getIsSelected(position)){
				isSelected.put(position, false);
			}else {
				isSelected.put(position, true);
		    }
			changeBackgroud(mCheckBox);
		}
	};

//	private OnCheckedChangeListener mCheckedChangeListener = new OnCheckedChangeListener() {
//		
//		@Override
//		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//			isSelected.put(position, isChecked);
//			layer.setVisibility(getIsSelected(position) == true ? View.VISIBLE : View.GONE);
//		}
//	};
	
	private OnClickListener mClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// 获取当前修改的商品信息
			String salBackQty = mSalbackQty.getText().toString().trim();
			if(salBackQty == null || "".equals(salBackQty)){
				// 消退数量不能为空
				mSalbackQty.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.shake));
			}else {
				String goodsQty_v = (String) map.get("goodsqty");
				if( new BigDecimal(goodsQty_v).compareTo(new BigDecimal(salBackQty)) < 0){
					mSalbackQty.startAnimation(AnimationUtils.loadAnimation(mContext, R.anim.shake));
				}else {					
					map.put("salbackqty", salBackQty);
					map.put("salbackreasonid", mSalbackRsid.getItemKey());
					map.put("salbackreasonidvalue", mSalbackRsid.getItemValue());
					layer.setVisibility(View.GONE);
					String goodsHtml = (String) map.get("html");
					mGoodsTx.setText(Html.fromHtml(goodsHtml+ getSalBackInfo(salBackQty,mSalbackRsid.getItemValue())));
				}
			}
		}
	};
	
	public static boolean getIsSelected(int p) { 
		if(isSelected.get(p) == null)
			isSelected.put(p, false);
        return  isSelected.get(p) ; 
    }  
}
