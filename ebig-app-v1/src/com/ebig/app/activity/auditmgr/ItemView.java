package com.ebig.app.activity.auditmgr;

import java.util.Map;

import android.content.Context;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.widget.AbsListView.LayoutParams;
import android.widget.TextView;

import com.ebig.app.R;
import com.ebig.app.custom.adapter.CommonViewHolder;
import com.ebig.app.utils.DensityUtil;

public class ItemView extends CommonViewHolder {

	private TextView mTextView;
	private Context getActivity() {
		return mContext;
	}
	@Override
	public View createView() {
		if(mTextView == null){
			mTextView = new TextView(getActivity());
			int padPd = DensityUtil.dip2px(getActivity(), 5);
			mTextView.setPadding(padPd, padPd, padPd, padPd);
			mTextView.setGravity(Gravity.LEFT);
			mTextView.setBackgroundResource(R.drawable.item_funs_bg_sel);
			LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
			mTextView.setLayoutParams(lp);
		}
		return mTextView;
	}


	@Override
	public void refashViewValue(Object obj) {
		try { 
			Map map = (Map) obj; 
			mTextView.setText(Html.fromHtml(map.get("html").toString()));
		} catch (Exception e) {
			mTextView.setText(e.getMessage() == null ? "±®¥Ì¡À" : e
					.getMessage());
		}
	}

}
