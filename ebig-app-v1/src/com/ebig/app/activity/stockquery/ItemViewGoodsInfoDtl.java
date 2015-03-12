package com.ebig.app.activity.stockquery;

import java.util.Map;

import android.annotation.SuppressLint;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.ebig.app.R;
import com.ebig.app.custom.adapter.CommonViewHolder;

/**
 * 商品详情信息ViewHolder
 * @author Billow
 *
 */
public class ItemViewGoodsInfoDtl extends CommonViewHolder {
 
	private View mView;
	private TextView mHtmlTextView;

	@SuppressLint("InflateParams") @Override
	public View createView() {
		if(mView == null){
			mView = LayoutInflater.from(mContext).inflate(
					R.layout.item_goodsinfo_view_dtl, null);
			mHtmlTextView = (TextView) mView.findViewById(R.id.item2);
		}
		return mView;
	}

	@Override
	public void refashViewValue(Object obj) {
		try { 
			Map map = (Map) obj;   
			String html = (String) map.get("html");
			mHtmlTextView.setText(Html.fromHtml(html));
		} catch (Exception e) {
			mHtmlTextView.setText("加载失败...:"+e.getMessage());
		}
	}

}
