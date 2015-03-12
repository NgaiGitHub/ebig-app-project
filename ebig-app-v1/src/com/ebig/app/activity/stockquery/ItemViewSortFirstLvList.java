package com.ebig.app.activity.stockquery;

import java.util.Map;

import android.graphics.Bitmap;
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
 * 一级商品分类ViewHolder
 * @author Billow
 *
 */
public class ItemViewSortFirstLvList extends CommonViewHolder {
	private View mView;
	private DisplayImageOptions options;
	private ImageLoader imageLoader;

	@Override
	public View createView() {
		if(mView == null){
			mView = LayoutInflater.from(mContext).inflate(
					R.layout.item_goodsinfo_group_view, null); 
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
			((TextView)mView.findViewById(R.id.goods_sort_item)).setText((String)map.get("groupname"));
			
			String url = (String)map.get("groupurl");
			if(url != null && !"".equals(url)){
				imageLoader.displayImage(url, ((ImageView)mView.findViewById(R.id.imageView)), options, new ImageLoadingListener() {
					@Override
					public void onLoadingStarted(String arg0, View arg1) {}
					@Override
					public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {}
					@Override
					public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {}
					@Override
					public void onLoadingCancelled(String arg0, View arg1) {}
				});
			}
		} catch (Exception e) {
			((TextView)mView.findViewById(R.id.goods_sort_item)).setText(e.getMessage());
		}
	}

}
