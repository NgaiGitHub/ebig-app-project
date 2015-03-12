package com.ebig.app.activity.stockquery;

import java.util.Map;

import android.graphics.Bitmap;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ebig.app.R;
import com.ebig.app.custom.adapter.CommonViewHolder;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

/**
 * 商品信息ViewHolder
 * @author Billow
 *
 */
public class ItemViewGoodsInfo extends CommonViewHolder {
 
	private View mView;
	private DisplayImageOptions options;
	private ImageLoader imageLoader;
	private TextView mTextView; 
	
	@Override
	public View createView() {
		if(mView == null){
			mView = LayoutInflater.from(mContext).inflate(
					R.layout.item_goodsinfo_view, null);
			mTextView = (TextView) mView.findViewById(R.id.item2);
		//	mHtmlTextView = (HtmlTextView) mView.findViewById(R.id.item1);
		}
		if(imageLoader == null)
			imageLoader = ImageLoader.getInstance();
		if(options == null)
		    	 options = new DisplayImageOptions.Builder()
				.showImageOnLoading(R.drawable.chatoption_item_add_pressed)
				.showImageForEmptyUri(R.drawable.chatoption_item_add_pressed)
				.showImageOnFail(R.drawable.chatoption_item_add_pressed)
				.cacheInMemory(true)
				.cacheOnDisc(true)
				.considerExifParams(true)
				.displayer(new RoundedBitmapDisplayer(70))
				.bitmapConfig(Bitmap.Config.RGB_565)
				.build();
		return mView;
	}

	@Override
	public void refashViewValue(Object obj) {
		try { 
			Map map = (Map) obj;
			//mHtmlTextView.setHtmlFromString(map.get("html").toString(),false);
			mTextView.setText(Html.fromHtml(map.get("html").toString()));
			//((TextView)mView.findViewById()).setText(Html.fromHtml(map.get("html").toString()));
			//((HtmlTextView)mView.findViewById(R.id.item2)).setHtmlFromString(map.get("html").toString(), false);

			// 向服务器索取图片
//			String url = map.get("goodspic").toString();
//			if(url != null && !"".equals(url)){
//				imageLoader.displayImage(url, ((ImageView)mView.findViewById(R.id.imageView)), options, new ImageLoadingListener() {
//					@Override
//					public void onLoadingStarted(String arg0, View arg1) {}
//					@Override
//					public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {}
//					@Override
//					public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {}
//					@Override
//					public void onLoadingCancelled(String arg0, View arg1) {}
//				});
//			}
		} catch (Exception e) {
			((TextView)mView.findViewById(R.id.item2)).setText(e.getMessage());
		}
	}

}
