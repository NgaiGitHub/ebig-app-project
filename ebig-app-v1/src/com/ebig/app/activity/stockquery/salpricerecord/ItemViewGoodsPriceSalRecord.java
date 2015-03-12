package com.ebig.app.activity.stockquery.salpricerecord;

import java.util.Map;

import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.ebig.app.R;
import com.ebig.app.custom.adapter.CommonViewHolder;

public class ItemViewGoodsPriceSalRecord extends CommonViewHolder {
 
	private View mView;
	private TextView mTextView;

	@Override
	public View createView() {
		if(mView == null){
			mView = LayoutInflater.from(mContext).inflate(
					R.layout.item_good_price_salrecord_view, null);
			mTextView = (TextView) mView.findViewById(R.id.item_salrecord);
			// android:focusable="false"
		}
		return mView;
	}

	@Override
	public void refashViewValue(Object obj) {
		try { 
			Map map = (Map) obj;   
			String html = (String) map.get("html");
			//String html =  "<h2>Hello wold</h2><ul><li>cats</li><li>dogs</li></ul>";
			mTextView.setText(Html.fromHtml(html));
		} catch (Exception e) {
			mTextView.setText("º”‘ÿ ß∞‹...:"+e.getMessage());
		}
	}

}
