package com.ebig.app.custom.ui;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.ebig.app.R;
import com.ebig.app.utils.DateUtil;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * 时间过滤器,针对Mescm项目而特别设计的。
 * @author HungLau.Ngai
 *
 */
@SuppressLint("SimpleDateFormat")
public class DateFilter extends FrameLayout{
	
	private TextView mBeginTime;
	private TextView mEndTime;
	private TextView mTip;
	
	private Button mBeginBtn;
	private Button mEndBtn;
	private Button mTodayBtn;
	private Button mWeekBtn;
	private Button mMonthBtn;
	
	private SimpleDateFormat mDateFormat = new SimpleDateFormat("yyyy - MM - dd");//  HH:mm:ss
	private DatePickerDialog mDateDialog;
	
	static public final int DAY = 1;
	static public final int WEEK = 1;
	static public final int MONTH = 1;
	
	public DateFilter(Context context, AttributeSet attrs) {
		super(context, attrs);
		View mView = LayoutInflater.from(context).inflate(R.layout.ui_date_filter, this);
		
		if(isInEditMode())
			return ;
		findViews(mView);
	}

	private OnClickListener mClickListener = new OnClickListener() {

		@Override
		public void onClick(View v) {
			int id = v.getId();
			if (id == mBeginBtn.getId()) {
				newDateDialog(id).show();
			}
			if (id == mEndBtn.getId()) {
				newDateDialog(id).show();
			}
			
			if (id == mTodayBtn.getId()) {setTodayDate();}
			if (id == mWeekBtn.getId()) {setWeekDate();}
			if (id == mMonthBtn.getId()) {setMonthDate();}
		}
	};
	
	private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

		public void onDateSet(DatePicker view, int year, int monthOfYear,
				int dayOfMonth) {
			int id = (Integer) mTip.getTag();
			final Calendar c = Calendar.getInstance();
			c.set(year, monthOfYear, dayOfMonth);
			if (id == mBeginBtn.getId()) {
				setDate(c.getTime(), null);
			}else {
				setDate((Date)mBeginTime.getTag(), c.getTime());
			}
		}
	};
	
	private void findViews(View rootView) {
		mBeginTime = (TextView) rootView.findViewById(R.id.date_filter_begintime_tx);
		mEndTime = (TextView) rootView.findViewById(R.id.date_filter_endtime_tx);
		mTip = (TextView) rootView.findViewById(R.id.date_filter_tip);
		
		mBeginBtn = (Button) rootView.findViewById(R.id.date_filter_begintime);
		mEndBtn = (Button) rootView.findViewById(R.id.date_filter_endtime);
		mTodayBtn = (Button) rootView.findViewById(R.id.date_filter_today);
		mWeekBtn = (Button) rootView.findViewById(R.id.date_filter_week);
		mMonthBtn = (Button) rootView.findViewById(R.id.date_filter_moth);
		
		mBeginBtn.setOnClickListener(mClickListener);
		mEndBtn.setOnClickListener(mClickListener);
		mTodayBtn.setOnClickListener(mClickListener);
		mWeekBtn.setOnClickListener(mClickListener);
		mMonthBtn.setOnClickListener(mClickListener);
		
		setTodayDate();
	}

	public void initDate(int mode) {
		if (mode == DAY) {
			setTodayDate();
		}
		if (mode == WEEK) {
			setWeekDate();
		}
		if (mode == MONTH) {
			setMonthDate();
		}

	}

	public Date getBeginDate() {
		return (Date) mBeginTime.getTag();
	}

	public Date getEndDate() {
		return (Date) mEndTime.getTag();
	}
	
	private void setDate(Date beginDate, Date endDate) {
		if (beginDate != null){
			mBeginTime.setTag(beginDate);			
			mBeginTime.setText(mDateFormat.format(beginDate));
		}

		if (endDate != null){
			mEndTime.setTag(endDate);			
			mEndTime.setText(mDateFormat.format(endDate));
		}

		checkBeginEndDate(beginDate, endDate);
	}
	
	// 检查是否结束时间早于开始时间,如果是,则清空结束时间,并显示提示
	private void checkBeginEndDate(Date beginDate, Date endDate) {
		showTip(false);
		if (endDate != null && beginDate != null)
			if (beginDate.after(endDate)) {
				mEndTime.setText("?? - ?? - ??");
				showTip(true);
				return ;
			}
	}
	
	private void setWeekDate() {
		Date weekFirstDay = DateUtil.getDateOfMondayInWeek();
		Date weekLastDay = DateUtil.getDateOfSaturdayInWeek();
		setDate(weekFirstDay, weekLastDay);
	}

	private void setMonthDate() {
		Date monthFirstDay = DateUtil.getFirstDayOfMonth();
		Date monthLastDay = DateUtil.getLastDayOfMonth();
		setDate(monthFirstDay, monthLastDay);
	}

	private void setTodayDate() {
		setDate(new Date(), new Date());
	}

	private void showTip(boolean b){
		if(b){
			mTip.setVisibility(View.VISIBLE);
		}else {
			mTip.setVisibility(View.GONE);
		}
	}

	protected DatePickerDialog newDateDialog(int viewId) {
		if (mDateDialog == null) {
			// date and time
			int mYear;
			int mMonth;
			int mDay;
			final Calendar c = Calendar.getInstance();
			mYear = c.get(Calendar.YEAR);
			mMonth = c.get(Calendar.MONTH);
			mDay = c.get(Calendar.DAY_OF_MONTH);
			mDateDialog = new DatePickerDialog(getContext(), mDateSetListener,
					mYear, mMonth, mDay);
		}
		mTip.setTag(viewId);
		return mDateDialog;
	}
}
