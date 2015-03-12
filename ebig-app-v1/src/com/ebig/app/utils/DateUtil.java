package com.ebig.app.utils;

import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateUtil {

	private static SimpleDateFormat sDateFormat = new SimpleDateFormat(
			"yyyy-MM-dd");
	private static SimpleDateFormat sDateFormat_ = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");

	public static void main(String[] args) {
		DateUtil tt = new DateUtil();

		System.out.println("\n当前日期： ");
		System.out.println(tt.getNowTime()); // 获取当前时间
		System.out.println(tt.getNowTime2()); // 获取当前时间
		System.out.println(tt.getNowTime3()); // 获取当前时间

		System.out.println("\n计算周： ");
		System.out.println(tt.getDaysOfNow2SundayInWeek()); // 获取当前日期与本周日的相差天数
		System.out.println(tt.getDateOfMondayInWeek()); // 获取本周一的日期
		System.out.println(tt.getDateOfSaturdayInWeek()); // 获取本周六的日期
		System.out.println(tt.getDateOfSaturdayInNextWeek()); // 获取下周一的日期
		System.out.println(tt.getDateOfSaturdayInLastWeek()); // 获取上周一的日期

		System.out.println("\n计算月： ");
		System.out.println(tt.getFirstDayOfMonth()); // 获取当月的第一天
		System.out.println(tt.getLastDayOfMonth()); // 获取当月最后一天
		System.out.println(tt.getFirstDayOfPreviousMonth()); // 获取上月第一天
		System.out.println(tt.getLastDayOfPreviousMonth()); // 获取上月最后一天
		System.out.println(tt.getFirstDayOfNextMonth()); // 获取下月第一天
		System.out.println(tt.getLastDayOfNextMonth()); // 获取下月最后一天

		System.out.println("\n计算年： ");
		System.out.println(tt.getFirstDayOfYear()); // 获取本年的第一天
		System.out.println(tt.getLastDayOfYear()); // 获取本年最后一天
		System.out.println(tt.getFirstDayOfPreviousYear()); // 获取上年的第一天
		System.out.println(tt.getLastDayOfPreviousYear()); // 获取上年最后一天
		System.out.println(tt.getFirstDayOfNextYear()); // 获取下年的第一天
		System.out.println(tt.getLastDayOfNextYear()); // 获取下年最后一天
		System.out.println(tt.getDaysOfYear()); // 获取本年的天数

		System.out.println("\n计算季度： ");
		System.out.println(tt.getSeasonOfMonth()); // 获取当前月的季度
		System.out.println(tt.getLastDayOfNow()); // 获取当前天所在月份的天数
		System.out.println(tt.isLeapYear(2012)); // 判断是否是润年

		System.out.println("\n日期格式转换与计算： ");
		System.out.println(tt.getDateFromStr("2012-06-20")); // 将字符串时间格式
																// yyyy-MM-dd，转换成Date类型
		System.out.println(tt.getWeek("2012-06-20")); // 根据一个日期，返回是星期几的字符串
		System.out.print("2012-06-02 -> 2012-06-12间隔天数:"); // 计算两个日期间的间隔天数
		System.out.println(tt.getDaysFromTwoDate("2012-06-02", "2012-06-12"));
	}

	public static String getDateFormat(String format, Date date) {
		SimpleDateFormat sDateFormat = new SimpleDateFormat(format);
		return sDateFormat.format(date);
	}

	// 获取当前时间
	public static String getNowTime() {

		Date now = new Date();
		return sDateFormat.format(now);
	}

	public static String getNowTime2() {

		Date now = new Date(System.currentTimeMillis());
		return sDateFormat_.format(now);
	}

	public static String getNowTime3() {
		StringBuilder strBuildNow = new StringBuilder();

		Calendar calendar = Calendar.getInstance();
		int year = calendar.get(Calendar.YEAR);
		int month = calendar.get(Calendar.MONTH) + 1;
		int day = calendar.get(Calendar.DAY_OF_MONTH);
		int hour = calendar.get(Calendar.HOUR_OF_DAY);
		int minute = calendar.get(Calendar.MINUTE);
		int second = calendar.get(Calendar.SECOND);
		int millissecond = calendar.get(Calendar.MILLISECOND);

		strBuildNow.append(year + "-");
		strBuildNow.append(month + "-");
		strBuildNow.append(day + " ");
		strBuildNow.append(hour + ":");
		strBuildNow.append(minute + ":");
		strBuildNow.append(second + ".");
		strBuildNow.append(millissecond);

		return strBuildNow.toString();
	}

	// 获取当前日期与本周日的相差天数
	public static int getDaysOfNow2SundayInWeek() {
		Calendar calendar = Calendar.getInstance();

		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1; // 减一天，周一为1，符合中国人习惯。Sunday
																// - 1; Monday -
																// 2; Saturday -
																// 7
		if (dayOfWeek == 0) { // 周日
			return 0;
		} else {
			return 0 - dayOfWeek;
		}
	}

	// 获取本周一的日期 yyyy-MM-dd HH:mm:ss pm/am
	public static Date getDateOfMondayInWeek() {
		int day = getDaysOfNow2SundayInWeek() + 1; // 加1，即周一离本周日的间隔天数
		GregorianCalendar gCalendar = new GregorianCalendar();

		gCalendar.add(GregorianCalendar.DATE, day); // 计算与本周一相差的时间间隔
		Date curDay = gCalendar.getTime();

		// DateFormat dateFormat = DateFormat.getInstance();
		// String dateOfMonday = dateFormat.format(curDay);

		return curDay;
	}

	// 获取本周一的日期 yyyy-MM-dd
	public static String getDateOfMondayInWeek2() {
		int day = getDaysOfNow2SundayInWeek() + 1; // 加1，即周一离本周日的间隔天数
		GregorianCalendar gCalendar = new GregorianCalendar();

		gCalendar.add(GregorianCalendar.DATE, day); // 计算与本周一相差的时间间隔
		Date curDay = gCalendar.getTime();

		String dateOfMonday = sDateFormat.format(curDay);

		return dateOfMonday;
	}

	// 获取本周六的日期 yyyy-MM-dd HH:mm:ss pm/am
	public static Date getDateOfSaturdayInWeek() {
		int day = getDaysOfNow2SundayInWeek() + 6; // 加6，即周六离本周日的间隔天数
		GregorianCalendar gCalendar = new GregorianCalendar();

		gCalendar.add(GregorianCalendar.DATE, day); // 计算与本周六相差的时间间隔
		Date curDay = gCalendar.getTime();

		// DateFormat dateFormat = DateFormat.getInstance();
		// String dateOfMonday = dateFormat.format();

		return curDay;
	}

	// 获取本周六的日期 yyyy-MM-dd
	public static String getDateOfSaturdayInWeek2() {
		int day = getDaysOfNow2SundayInWeek() + 6; // 加6，即周六离本周日的间隔天数
		GregorianCalendar gCalendar = new GregorianCalendar();

		gCalendar.add(GregorianCalendar.DATE, day); // 计算与本周六相差的时间间隔
		Date curDay = gCalendar.getTime();

		String dateOfMonday = sDateFormat.format(curDay);

		return dateOfMonday;
	}

	// 获取上周一的日期
	public static String getDateOfSaturdayInLastWeek() {
		int day = getDaysOfNow2SundayInWeek() - 1; // 减1，即上周一离本周日的间隔天数
		GregorianCalendar gCalendar = new GregorianCalendar();

		gCalendar.add(GregorianCalendar.DATE, day); // 计算与上周一相差的时间间隔
		Date curDay = gCalendar.getTime();

		DateFormat dateFormat = DateFormat.getInstance();
		String dateOfMonday = dateFormat.format(curDay);

		return dateOfMonday;
	}

	// 获取下周一的日期
	public static String getDateOfSaturdayInNextWeek() {
		int day = getDaysOfNow2SundayInWeek() + 8; // 加8，即下周一离本周日的间隔天数
		GregorianCalendar gCalendar = new GregorianCalendar();

		gCalendar.add(GregorianCalendar.DATE, day); // 计算与下周一相差的时间间隔
		Date curDay = gCalendar.getTime();

		DateFormat dateFormat = DateFormat.getInstance();
		String dateOfMonday = dateFormat.format(curDay);

		return dateOfMonday;
	}

	// 获取当月第一天
	public static Date getFirstDayOfMonth() {
		// String strFirstDay = "";

		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DATE, 1); // 设置当前月的1号

		// strFirstDay = sDateFormat.format();
		return calendar.getTime();
	}

	// 获取当月最后一天
	public static Date getLastDayOfMonth() {
		// String strLastDay = "";

		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DATE, 1); // 设置当前月的1号
		calendar.add(Calendar.MONDAY, 1); // 加一个月，变为下月的1号
		calendar.add(Calendar.DATE, -1); // 减去一天，变为当前月的最后一天

		// strLastDay = sDateFormat.format();
		return calendar.getTime();
	}

	// 获取上月第一天
	public static String getFirstDayOfPreviousMonth() {
		String strFirstDay = "";

		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DATE, 1); // 设置当前月的1号
		calendar.add(Calendar.MONDAY, -1); // 减一个月，变为上月的1号

		strFirstDay = sDateFormat.format(calendar.getTime());
		return strFirstDay;
	}

	// 获取上月最后一天
	public static String getLastDayOfPreviousMonth() {
		String strLastDay = "";

		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DATE, 1); // 设置当前月的1号
		calendar.add(Calendar.DATE, -1); // 减一天，变为上月的1号

		strLastDay = sDateFormat.format(calendar.getTime());
		return strLastDay;
	}

	public static String getLastDayOfPreviousMonth2() {
		String strLastDay = "";

		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONDAY, -1); // 减一个月
		calendar.set(Calendar.DATE, 1); // 设置当前月的1号
		calendar.roll(Calendar.DATE, -1); // 把日期回滚一天，也就是本月最后一天

		strLastDay = sDateFormat.format(calendar.getTime());
		return strLastDay;
	}

	// 获取下月第一天
	public static String getFirstDayOfNextMonth() {
		String strFirstDay = "";

		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, 1); // 加一个月
		calendar.set(Calendar.DATE, 1); // 设置当前月第一天

		strFirstDay = sDateFormat.format(calendar.getTime());
		return strFirstDay;
	}

	// 获取下月最后一天
	public static String getLastDayOfNextMonth() {
		String strLastDay = "";

		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DATE, 1); // 设置当前月的1号
		calendar.add(Calendar.MONDAY, 2); // 加两个月，变为下下月的1号
		calendar.add(Calendar.DATE, -1); // 减一天，变为下月的最后一天

		strLastDay = sDateFormat.format(calendar.getTime());
		return strLastDay;
	}

	public static String getLastDayOfNextMonth2() {
		String strLastDay = "";

		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, 1); // 加1个月
		calendar.set(Calendar.DATE, 1); // 把日期设置为当月第1天
		calendar.roll(Calendar.DATE, -1); // 日期回滚1天，也就是本月最后1天

		strLastDay = sDateFormat.format(calendar.getTime());
		return strLastDay;
	}

	// 获取本年的第一天
	public static String getFirstDayOfYear() {
		String strFirstDay = "";

		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_YEAR, 1); // 设置当年第一天

		strFirstDay = sDateFormat.format(calendar.getTime());
		return strFirstDay;
	}

	// 获取本年的最后一天
	public static String getLastDayOfYear() {
		String strLastDay = "";

		Calendar calendar = Calendar.getInstance();
		strLastDay = calendar.get(Calendar.YEAR) + "-12-31";

		return strLastDay;
	}

	// 获取上年的第一天
	public static String getFirstDayOfPreviousYear() {
		String strFirstDay = "";

		Calendar calendar = Calendar.getInstance();
		strFirstDay = (calendar.get(Calendar.YEAR) - 1) + "-01-01";

		return strFirstDay;
	}

	// 获取上年的最后一天
	public static String getLastDayOfPreviousYear() {
		String strLastDay = "";

		Calendar calendar = Calendar.getInstance();
		strLastDay = (calendar.get(Calendar.YEAR) - 1) + "-12-31";

		return strLastDay;
	}

	// 获取下年的第一天
	public static String getFirstDayOfNextYear() {
		String strFirstDay = "";

		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.YEAR, 1); // 加一年
		calendar.set(Calendar.DAY_OF_YEAR, 1); // 设置当年第一天

		strFirstDay = sDateFormat.format(calendar.getTime());
		return strFirstDay;
	}

	// 获取下年的最后一天
	public static String getLastDayOfNextYear() {
		String strLastDay = "";

		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.YEAR, 1); // 加一年
		calendar.set(Calendar.DAY_OF_YEAR, 1); // 设置当年第一天
		calendar.roll(Calendar.DAY_OF_YEAR, -1); // 回滚到当年最后一天

		strLastDay = sDateFormat.format(calendar.getTime());
		return strLastDay;
	}

	// 获取本年的天数
	public static String getDaysOfYear() {
		int year = 0;
		int days = 0;

		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_YEAR, 1); // 设置日期为当年第一天
		calendar.roll(Calendar.DAY_OF_YEAR, -1); // 把日期回滚一天

		year = calendar.get(Calendar.YEAR);
		days = calendar.get(Calendar.DAY_OF_YEAR);

		return (days + " in " + year);
	}

	// 获取当前月的季度,第一个月
	public static String getSeasonOfFirstMonth() {
		String strSeason = "";
		int year, month, day;
		int season = 1;
		int array[][] = new int[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 },
				{ 10, 11, 12 } };

		Calendar calendar = Calendar.getInstance();
		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONDAY) + 1;
		day = calendar.get(Calendar.DAY_OF_MONTH);
		switch (month) {
		case 1:
		case 2:
		case 3:
			season = 1;
			break;
		case 4:
		case 5:
		case 6:
			season = 2;
			break;
		case 7:
		case 8:
		case 9:
			season = 3;
			break;
		case 10:
		case 11:
		case 12:
			season = 4;
			break;
		default:
			season = 1;
			break;
		}

		int start_month = array[season - 1][0];
		int end_month = array[season - 1][2];
		Calendar mCalendar = Calendar.getInstance();
		// mCalendar.setTime(new Date(mCalendar.YEAR,start_month,1));
		mCalendar.set(mCalendar.get(Calendar.YEAR), start_month, 1);
		String startMonths = sDateFormat.format(mCalendar.getTime());
		return startMonths;
	}

	// 获取当前月的季度,最后一个月
	public static String getSeasonOfLastMonth() {
		String strSeason = "";
		int year, month, day;
		int season = 1;
		int array[][] = new int[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 },
				{ 10, 11, 12 } };

		Calendar calendar = Calendar.getInstance();
		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONDAY) + 1;
		day = calendar.get(Calendar.DAY_OF_MONTH);
		switch (month) {
		case 1:
		case 2:
		case 3:
			season = 1;
			break;
		case 4:
		case 5:
		case 6:
			season = 2;
			break;
		case 7:
		case 8:
		case 9:
			season = 3;
			break;
		case 10:
		case 11:
		case 12:
			season = 4;
			break;
		default:
			season = 1;
			break;
		}

		// int start_month = array[season-1][0];
		int end_month = array[season - 1][2];
		Calendar mCalendar = Calendar.getInstance();
		mCalendar.set(mCalendar.get(Calendar.YEAR), end_month, 1);
		calendar.add(Calendar.MONDAY, 1); // 加一个月，变为下月的1号
		calendar.add(Calendar.DATE, 1); // 减去一天，变为当前月的最后一天
		String endMonths = sDateFormat.format(mCalendar.getTime());
		return endMonths;
	}

	// 获取当前月的季度
	public static String getSeasonOfMonth() {
		String strSeason = "";
		int year, month, day;
		int season = 1;
		int array[][] = new int[][] { { 1, 2, 3 }, { 4, 5, 6 }, { 7, 8, 9 },
				{ 10, 11, 12 } };

		Calendar calendar = Calendar.getInstance();
		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONDAY) + 1;
		day = calendar.get(Calendar.DAY_OF_MONTH);
		switch (month) {
		case 1:
		case 2:
		case 3:
			season = 1;
			break;
		case 4:
		case 5:
		case 6:
			season = 2;
			break;
		case 7:
		case 8:
		case 9:
			season = 3;
			break;
		case 10:
		case 11:
		case 12:
			season = 4;
			break;
		default:
			season = 1;
			break;
		}

		int start_month = array[season - 1][0];
		int end_month = array[season - 1][2];
		strSeason = year + "-" + month + "-" + day;
		strSeason += " in [ " + year + "-" + start_month + " : " + year + "-"
				+ end_month + " ]";

		return strSeason;
	}

	// 获取当前天所在月份的天数
	public static String getLastDayOfNow() {
		String strLastDay = "";
		int year, month, day;
		int days = 0;

		Calendar calendar = Calendar.getInstance();
		year = calendar.get(Calendar.YEAR);
		month = calendar.get(Calendar.MONTH) + 1;
		day = calendar.get(Calendar.DAY_OF_MONTH);

		switch (month) {
		case 1:
		case 3:
		case 5:
		case 7:
		case 8:
		case 10:
		case 12:
			days = 31;
			break;
		case 4:
		case 6:
		case 9:
		case 11:
			days = 30;
			break;
		case 2:
			if (isLeapYear(year)) {
				days = 29;
			} else {
				days = 28;
			}
			break;
		default:
			days = 0;
			break;
		}

		strLastDay = days + " in [ " + year + "-" + month + "-" + day + " ]";
		return strLastDay;
	}

	// 判断是否是润年
	public static boolean isLeapYear(int year) {
		return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
	}

	// 将字符串时间格式 yyyy-MM-dd，转换成Date类型
	public static Date getDateFromStr(String txtDate) {

		ParsePosition pos = new ParsePosition(0);
		Date date = sDateFormat.parse(txtDate, pos);

		return date;
	}

	// 根据一个日期，返回是星期几的字符串
	public static String getWeek(String txtDate) {
		Date date = getDateFromStr(txtDate);
		Calendar calendar = Calendar.getInstance();

		calendar.setTime(date);
		String week = new SimpleDateFormat("EEEE").format(date);

		return week;
	}

	// 计算两个日期间的间隔天数
	public static long getDaysFromTwoDate(String txtDate1, String txtDate2) {
		if (txtDate1 == null || txtDate1.equals("")) {
			return 0;
		}
		if (txtDate2 == null || txtDate2.equals("")) {
			return 0;
		}

		long days = 0;
		try {
			Date date1 = sDateFormat.parse(txtDate1);
			Date date2 = sDateFormat.parse(txtDate2);
			days = (date2.getTime() - date1.getTime()) / (24 * 60 * 60 * 1000); // 通过getTime()方法，把时间Date转换成毫秒格式Long类型，进行计算
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return days;
	}

}
