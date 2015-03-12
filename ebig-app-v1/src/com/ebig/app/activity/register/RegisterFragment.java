package com.ebig.app.activity.register;

import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TextView;

import com.andreabaccega.widget.FormEditText;
import com.ebig.app.App;
import com.ebig.app.R;
import com.ebig.app.base.BaseFirmFragment;
import com.ebig.app.dbctrl.AppDbCtrl;

public class RegisterFragment extends BaseFirmFragment implements OnClickListener{
	
	private TextView mBindMescmTxt;
	private FormEditText mAccountName;
	private FormEditText mMobileNumber;
	private FormEditText mMescmAccountId;
	private FormEditText mPswd;
	private CheckBox mGrantBox;
	
	private View mEntryLay;
	private ListView mEntryLv;
	private ProjectsAdapter mAdapter;
	private View mMescmLay;
	private Button mRuleButton;
	private Button mRegisterBtn;
	
	
	static public RegisterFragment newIntance(Bundle bundle) {
		RegisterFragment mFragment = new RegisterFragment();
		mFragment.setArguments(bundle);
		return mFragment;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState);
		setHasOptionsMenu(true);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) { 
		return inflater.inflate(R.layout.fragment_register, container, false);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) { 
		super.onActivityCreated(savedInstanceState);
		findViews();
		Bundle mBundle = getArguments();
		
	}

	private void findViews() {
		if(mAccountName != null)
			return ;
		
		View rootView = getView();
		mAccountName = (FormEditText) rootView.findViewById(R.id.register_accountname);
		mMobileNumber = (FormEditText) rootView.findViewById(R.id.register_mobilenumber);
		mMescmAccountId = (FormEditText) rootView.findViewById(R.id.register_accountid);
		mPswd = (FormEditText) rootView.findViewById(R.id.register_pswd);
		
		mGrantBox = (CheckBox) rootView.findViewById(R.id.register_bindmescm);
		mGrantBox.setOnCheckedChangeListener(mChangeListener);
		
		mMescmLay = rootView.findViewById(R.id.register_mescmaccountid_lay);
		mRuleButton = (Button) rootView.findViewById(R.id.register_rule);
		String ruleTxt = "点击上面的<font color=blue> \"注册\" </font>按钮,即表示你同意" +
				"<u><font color=blue>《以大以讯软件许可证及服务协议》</font></u>";
		mRuleButton.setText(Html.fromHtml(ruleTxt));
		
		mAccountName.setOnFocusChangeListener(mFocusChangeListener);
		mMobileNumber.setOnFocusChangeListener(mFocusChangeListener);
		
		mEntryLay = rootView.findViewById(R.id.register_entry_layr);
		mEntryLv = (ListView) rootView.findViewById(R.id.register_entrylv);
		mAdapter = new ProjectsAdapter(getActivity(), App.getProjectList());
		mEntryLv.setAdapter(mAdapter);
		mEntryLv.setOnItemClickListener(mItemClickListener);
		
		mBindMescmTxt = (TextView) rootView.findViewById(R.id.register_bindmescm_txt);
		mRegisterBtn = (Button) rootView.findViewById(R.id.register_btn);
		mRegisterBtn.setOnClickListener(this);
		mRegisterBtn.setEnabled(false);
		mRegisterBtn.setBackgroundResource(R.drawable.login_button_press);
		
		mPswd.addTextChangedListener(textWatcher);
	}
	
	private TextWatcher textWatcher = new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			
			if (mMobileNumber.testValidity() && mAccountName.testValidity()
					&& mPswd.testValidity()) {
				if(mGrantBox.isChecked()){
					if(mMescmAccountId.testValidity()){
						mRegisterBtn.setEnabled(true);
					    mRegisterBtn.setBackgroundResource(R.drawable.login_btn_switch_bg);
					}
				}else {
					mRegisterBtn.setEnabled(true);
			    mRegisterBtn.setBackgroundResource(R.drawable.login_btn_switch_bg);
				}
				
			}
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {}
		@Override
		public void afterTextChanged(Editable s) {}
	};
	
	private OnItemClickListener mItemClickListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			Map tMap = (Map) mAdapter.getItem(position);
			if("-1".equals(tMap.get("entryid")))
				return ;
			mBindMescmTxt.setText("授权["+tMap.get("projectname")+"]");
			mBindMescmTxt.setTag(tMap.get("entryid"));
			mEntryLay.setVisibility(View.GONE);
		}
	};
	
	private OnFocusChangeListener mFocusChangeListener = new OnFocusChangeListener() {
		
		@Override
		public void onFocusChange(View v, boolean hasFocus) {
			int id = v.getId();
			// 失去焦点
			if(!hasFocus){
				if (mMobileNumber.getId() == id)
					mMobileNumber.testValidity();
				if (mAccountName.getId() == id)
					mAccountName.testValidity();
				if (mMescmAccountId.getId() == id)
					mMescmAccountId.testValidity();
				if (mPswd.getId() == id)
					mPswd.testValidity();
				    
			}
			// 得到焦点
		
		}
	};
	
	private OnCheckedChangeListener mChangeListener = new OnCheckedChangeListener() {
		
		@Override
		public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
			if(isChecked){
				mMescmLay.setVisibility(View.VISIBLE);
				mEntryLay.setVisibility(View.VISIBLE);
			}else {
				mMescmLay.setVisibility(View.GONE);
				mBindMescmTxt.setText("授权[一般用户]");
				mBindMescmTxt.setTag("0");
			}
		}
	};

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		setDefaultBarTitle("注册新账号");
//		setActionBarCustomView(R.layout.default_actionbar_fragment);
//		((Button) getBarCustomView().findViewById(R.id.default_bar_left_btn)).setOnClickListener(this);
//		((TextView) getBarCustomView().findViewById(R.id.default_bar_left_title)).setText("注册新账号");
//		((Button) getBarCustomView().findViewById(R.id.default_bar_right_btn)).setVisibility(View.GONE);//setBackgroundResource(R.drawable.login_rg);
	}

	@Override
	public void onClick(View v) {
		int id = v.getId();
		switch (id) {
//		case R.id.default_bar_left_btn:
//			getActivity().finish();
//			break;
//			
		case R.id.register_btn:
			newAsyncTaskExecute(-1);
			break;

		default:
			break;
		}
	}

	@Override
	protected void onPostExecuteTask(int asyncid, Object result) {
		super.onPostExecuteTask(asyncid, result);
		getToast().makeTextError(
				getActivity(),
				"注册成功!请牢记您的账号[" + mMobileNumber.getText().toString().trim()
						+ "]", null);

		Timer mTimer = new Timer();
		mTimer.schedule(new TimerTask() {

			@Override
			public void run() {
				getActivity().finish();
			}
		}, 1 * 1000);
	}

	@Override
	protected void onPostExecuteTaskError(int asyncid, Exception result) {
		super.onPostExecuteTaskError(asyncid, result);
		getToast().makeTextError(getActivity(), result.getMessage(), null);
	}

	@Override
	protected Object doInBackgroundTask(int asyncid, Object[] params) {
		
		String mobernumber = mMobileNumber.getText().toString().trim();
		String mailbox = "";
		String pswd = mPswd.getText().toString().trim();
		String entryid = mBindMescmTxt.getTag().toString();
		String mescmaccount = "";
		if(mGrantBox.isChecked())
			mescmaccount = mMescmAccountId.getText().toString().trim();
		String accountname = mAccountName.getText().toString().trim();
		//AppDbCtrl.genAppNewVersion();
		return AppDbCtrl.registerAccount(mobernumber,mailbox,accountname,mescmaccount,pswd,entryid);
		
	}

	
	
	
}
