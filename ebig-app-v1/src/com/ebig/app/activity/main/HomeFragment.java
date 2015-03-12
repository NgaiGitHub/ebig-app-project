package com.ebig.app.activity.main;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.ebig.app.NetInfo;
import com.ebig.app.R;
import com.ebig.app.base.BaseFirmFragment;
import com.ebig.app.utils.LogUtil;

@SuppressLint("SetJavaScriptEnabled")
public class HomeFragment extends BaseFirmFragment{
 
	static public HomeFragment newIntance(Bundle bundle) {
		HomeFragment mHomeFragment = new HomeFragment();
		mHomeFragment.setArguments(bundle);
		return mHomeFragment; 
	}
	 
	@Override
	public void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState);
		//setHasOptionsMenu(true);//
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {// 直接返回布局视图就好,没必要搞那么多.
		return inflater.inflate(R.layout.fragment_home, container, false);
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) { 
		super.onActivityCreated(savedInstanceState);
		findViews();
	}

	private void findViews() {
		LogUtil.Log(getClass(), "findViews");
		View root = getView();
		WebView mWebView = (WebView) root.findViewById(R.id.homeWebView);
		final ProgressBar progressBar = (ProgressBar) root.findViewById(R.id.progressBar);
		mWebView.loadUrl(NetInfo.getServiceUrlBase()+"ebig/index.html");
		mWebView.getSettings().setJavaScriptEnabled(true);
		
		mWebView.setWebViewClient(new WebViewClient(){

			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				progressBar.setVisibility(View.VISIBLE);
			}

			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);

				progressBar.setVisibility(View.GONE);
			}
			
		});
	}

	@Override
	public void onStart() {
		super.onStart();
	}
	
	@Override
	public void onPause() { 
		super.onPause();
	}

	@Override
	public void onResume() { 
		super.onResume();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
	}

 
 
}
