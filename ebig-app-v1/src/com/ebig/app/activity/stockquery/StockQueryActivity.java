package com.ebig.app.activity.stockquery;

import java.util.LinkedList;

import android.app.ActionBar;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup.LayoutParams;

import com.ebig.app.R;
import com.ebig.app.base.BaseFirmActivity;
import com.ebig.app.custom.ui.DefaultActionBar;
import com.ebig.app.custom.ui.ScanClearEditText;
import com.ebig.app.dbctrl.StockQueryDbCtrl;

/**
 * 库存查询主界面
 * @author Billow
 *
 */
public class StockQueryActivity extends BaseFirmActivity {
	// 详细商品信息 某个商品的信息(批号等)
	private LinkedList<Object> mGoodsDtlItems;
	private ActionBar actionBar;
	private Menu menu;
	private StockQueryFragment mFragment;
	private View defaultCustomView;
	private View userCustomView;
	private boolean isUserCustom = false;
	protected ScanClearEditText scanClearEditText;
	 
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_goods_search);
		iniCustomView();
		
		FragmentManager fm = getSupportFragmentManager();
		mFragment = (StockQueryFragment) fm.findFragmentById(R.id.fragmentContainer);
		if (mFragment == null) {
			mFragment = StockQueryFragment.newIntent();
			fm.beginTransaction().add(R.id.fragmentContainer, mFragment).commit();
		}
		
		findViews();
	}
	
	private void findViews(){
		actionBar = getActionBar();
	}
	
	private void iniCustomView(){
		defaultCustomView = new DefaultActionBar(this);
		
		userCustomView = getLayoutInflater()
				.inflate(R.layout.actionbar_search_view, null);
		ActionBar.LayoutParams lp = new ActionBar.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		userCustomView.setLayoutParams(lp);
		scanClearEditText = (ScanClearEditText)userCustomView.findViewById(R.id.actionBarCustomCenter);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		this.menu = menu;
		getMenuInflater().inflate(R.menu.stockquery_menu, menu);
		setDefaultTitle("库存查询");
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();
		switch (id) {
		case android.R.id.home:  //只有ActionBar是在搜索框输入状态时才会显示home按钮，因此，点返回home时应该关闭搜索框
			isUserCustom = false;
			setActionBarCustomView();
			break;
		case R.id.menu_search:
			if(isUserCustom){  //如果ActionBar是在搜索框输入状态时点击搜索按钮，则执行异步查询
				if(mFragment.getSearchText().trim().length()==0){
					getToast().makeText(this, "请输入关键字", null);
					break;
				}
				mFragment.goToGoodsQuery(mFragment.GOODS_QUERY_SRV);
			}else{  //否则，跳转至ActionBar搜索框输入状态
				isUserCustom = true;
				setActionBarCustomView();
				scanClearEditText.requestFocus();
			}
			break;

		default:
			break;
		}
		return true;
	}

	public void onClick(View v) {
		mFragment.onClick(v);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		StockQueryDbCtrl.clear();
	}
	
	@Override
	protected void onScanCodeResult(String code) {
		super.onScanCodeResult(code);
		scanClearEditText.setText(code);
		mFragment.goToGoodsQuery(mFragment.GOODS_QUERY_SRV);
	}
	
	private void setActionBarCustomView(){
		if(isUserCustom ){
			actionBar.setDisplayOptions(
					ActionBar.DISPLAY_HOME_AS_UP
					| ActionBar.DISPLAY_SHOW_HOME
					/* | ActionBar.DISPLAY_SHOW_TITLE  */
					 | ActionBar.DISPLAY_SHOW_CUSTOM);
			actionBar.setCustomView(userCustomView);
		}else{
			actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
			ActionBar.LayoutParams lp = new ActionBar.LayoutParams(Gravity.START);//(ActionBar.LayoutParams) bar.getLayoutParams();
			lp.gravity = lp.gravity & ~Gravity.RELATIVE_HORIZONTAL_GRAVITY_MASK
					| Gravity.START;
			actionBar.setCustomView(defaultCustomView, lp);
		}
	}
}
