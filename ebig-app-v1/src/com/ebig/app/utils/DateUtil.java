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

		System.out.println("\n��ǰ���ڣ� ");
		System.out.println(tt.getNowTime()); // ��ȡ��ǰʱ��
		System.out.println(tt.getNowTime2()); // ��ȡ��ǰʱ��
		System.out.println(tt.getNowTime3()); // ��ȡ��ǰʱ��

		System.out.println("\n�����ܣ� ");
		System.out.println(tt.getDaysOfNow2SundayInWeek()); // ��ȡ��ǰ�����뱾���յ��������
		System.out.println(tt.getDateOfMondayInWeek()); // ��ȡ����һ������
		System.out.println(tt.getDateOfSaturdayInWeek()); // ��ȡ������������
		System.out.println(tt.getDateOfSaturdayInNextWeek()); // ��ȡ����һ������
		System.out.println(tt.getDateOfSaturdayInLastWeek()); // ��ȡ����һ������

		System.out.println("\n�����£� ");
		System.out.println(tt.getFirstDayOfMonth()); // ��ȡ���µĵ�һ��
		System.out.println(tt.getLastDayOfMonth()); // ��ȡ�������һ��
		System.out.println(tt.getFirstDayOfPreviousMonth()); // ��ȡ���µ�һ��
		System.out.println(tt.getLastDayOfPreviousMonth()); // ��ȡ�������һ��
		System.out.println(tt.getFirstDayOfNextMonth()); // ��ȡ���µ�һ��
		System.out.println(tt.getLastDayOfNextMonth()); // ��ȡ�������һ��

		System.out.println("\n�����꣺ ");
		System.out.println(tt.getFirstDayOfYear()); // ��ȡ����ĵ�һ��
		System.out.println(tt.getLastDayOfYear()); // ��ȡ�������һ��
		System.out.println(tt.getFirstDayOfPreviousYear()); // ��ȡ����ĵ�һ��
		System.out.println(tt.getLastDayOfPreviousYear()); // ��ȡ�������һ��
		System.out.println(tt.getFirstDayOfNextYear()); // ��ȡ����ĵ�һ��
		System.out.println(tt.getLastDayOfNextYear()); // ��ȡ�������һ��
		System.out.println(tt.getDaysOfYear()); // ��ȡ���������

		System.out.println("\n���㼾�ȣ� ");
		System.out.println(tt.getSeasonOfMonth()); // ��ȡ��ǰ�µļ���
		System.out.println(tt.getLastDayOfNow()); // ��ȡ��ǰ�������·ݵ�����
		System.out.println(tt.isLeapYear(2012)); // �ж��Ƿ�������

		System.out.println("\n���ڸ�ʽת������㣺 ");
		System.out.println(tt.getDateFromStr("2012-06-20")); // ���ַ���ʱ���ʽ
																// yyyy-MM-dd��ת����Date����
		System.out.println(tt.getWeek("2012-06-20")); // ����һ�����ڣ����������ڼ����ַ���
		System.out.print("2012-06-02 -> 2012-06-12�������:"); // �����������ڼ�ļ������
		System.out.println(tt.getDaysFromTwoDate("2012-06-02", "2012-06-12"));
	}

	public static String getDateFormat(String format, Date date) {
		SimpleDateFormat sDateFormat = new SimpleDateFormat(format);
		return sDateFormat.format(date);
	}

	// ��ȡ��ǰʱ��
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

	// ��ȡ��ǰ�����뱾���յ��������
	public static int getDaysOfNow2SundayInWeek() {
		Calendar calendar = Calendar.getInstance();

		int dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK) - 1; // ��һ�죬��һΪ1�������й���ϰ�ߡ�Sunday
																// - 1; Monday -
																// 2; Saturday -
																// 7
		if (dayOfWeek == 0) { // ����
			return 0;
		} else {
			return 0 - dayOfWeek;
		}
	}

	// ��ȡ����һ������ yyyy-MM-dd HH:mm:ss pm/am
	public static Date getDateOfMondayInWeek() {
		int day = getDaysOfNow2SundayInWeek() + 1; // ��1������һ�뱾���յļ������
		GregorianCalendar gCalendar = new GregorianCalendar();

		gCalendar.add(GregorianCalendar.DATE, day); // �����뱾��һ����ʱ����
		Date curDay = gCalendar.getTime();

		// DateFormat dateFormat = DateFormat.getInstance();
		// String dateOfMonday = dateFormat.format(curDay);

		return curDay;
	}

	// ��ȡ����һ������ yyyy-MM-dd
	public static String getDateOfMondayInWeek2() {
		int day = getDaysOfNow2SundayInWeek() + 1; // ��1������һ�뱾���յļ������
		GregorianCalendar gCalendar = new GregorianCalendar();

		gCalendar.add(GregorianCalendar.DATE, day); // �����뱾��һ����ʱ����
		Date curDay = gCalendar.getTime();

		String dateOfMonday = sDateFormat.format(curDay);

		return dateOfMonday;
	}

	// ��ȡ������������ yyyy-MM-dd HH:mm:ss pm/am
	public static Date getDateOfSaturdayInWeek() {
		int day = getDaysOfNow2SundayInWeek() + 6; // ��6���������뱾���յļ������
		GregorianCalendar gCalendar = new GregorianCalendar();

		gCalendar.add(GregorianCalendar.DATE, day); // �����뱾��������ʱ����
		Date curDay = gCalendar.getTime();

		// DateFormat dateFormat = DateFormat.getInstance();
		// String dateOfMonday = dateFormat.format();

		return curDay;
	}

	// ��ȡ������������ yyyy-MM-dd
	public static String getDateOfSaturdayInWeek2() {
		int day = getDaysOfNow2SundayInWeek() + 6; // ��6���������뱾���յļ������
		GregorianCalendar gCalendar = new GregorianCalendar();

		gCalendar.add(GregorianCalendar.DATE, day); // �����뱾��������ʱ����
		Date curDay = gCalendar.getTime();

		String dateOfMonday = sDateFormat.format(curDay);

		return dateOfMonday;
	}

	// ��ȡ����һ������
	public static String getDateOfSaturdayInLastWeek() {
		int day = getDaysOfNow2SundayInWeek() - 1; // ��1��������һ�뱾���յļ������
		GregorianCalendar gCalendar = new GregorianCalendar();

		gCalendar.add(GregorianCalendar.DATE, day); // ����������һ����ʱ����
		Date curDay = gCalendar.getTime();

		DateFormat dateFormat = DateFormat.getInstance();
		String dateOfMonday = dateFormat.format(curDay);

		return dateOfMonday;
	}

	// ��ȡ����һ������
	public static String getDateOfSaturdayInNextWeek() {
		int day = getDaysOfNow2SundayInWeek() + 8; // ��8��������һ�뱾���յļ������
		GregorianCalendar gCalendar = new GregorianCalendar();

		gCalendar.add(GregorianCalendar.DATE, day); // ����������һ����ʱ����
		Date curDay = gCalendar.getTime();

		DateFormat dateFormat = DateFormat.getInstance();
		String dateOfMonday = dateFormat.format(curDay);

		return dateOfMonday;
	}

	// ��ȡ���µ�һ��
	public static Date getFirstDayOfMonth() {
		// String strFirstDay = "";

		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DATE, 1); // ���õ�ǰ�µ�1��

		// strFirstDay = sDateFormat.format();
		return calendar.getTime();
	}

	// ��ȡ�������һ��
	public static Date getLastDayOfMonth() {
		// String strLastDay = "";

		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DATE, 1); // ���õ�ǰ�µ�1��
		calendar.add(Calendar.MONDAY, 1); // ��һ���£���Ϊ���µ�1��
		calendar.add(Calendar.DATE, -1); // ��ȥһ�죬��Ϊ��ǰ�µ����һ��

		// strLastDay = sDateFormat.format();
		return calendar.getTime();
	}

	// ��ȡ���µ�һ��
	public static String getFirstDayOfPreviousMonth() {
		String strFirstDay = "";

		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DATE, 1); // ���õ�ǰ�µ�1��
		calendar.add(Calendar.MONDAY, -1); // ��һ���£���Ϊ���µ�1��

		strFirstDay = sDateFormat.format(calendar.getTime());
		return strFirstDay;
	}

	// ��ȡ�������һ��
	public static String getLastDayOfPreviousMonth() {
		String strLastDay = "";

		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DATE, 1); // ���õ�ǰ�µ�1��
		calendar.add(Calendar.DATE, -1); // ��һ�죬��Ϊ���µ�1��

		strLastDay = sDateFormat.format(calendar.getTime());
		return strLastDay;
	}

	public static String getLastDayOfPreviousMonth2() {
		String strLastDay = "";

		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONDAY, -1); // ��һ����
		calendar.set(Calendar.DATE, 1); // ���õ�ǰ�µ�1��
		calendar.roll(Calendar.DATE, -1); // �����ڻع�һ�죬Ҳ���Ǳ������һ��

		strLastDay = sDateFormat.format(calendar.getTime());
		return strLastDay;
	}

	// ��ȡ���µ�һ��
	public static String getFirstDayOfNextMonth() {
		String strFirstDay = "";

		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, 1); // ��һ����
		calendar.set(Calendar.DATE, 1); // ���õ�ǰ�µ�һ��

		strFirstDay = sDateFormat.format(calendar.getTime());
		return strFirstDay;
	}

	// ��ȡ�������һ��
	public static String getLastDayOfNextMonth() {
		String strLastDay = "";

		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DATE, 1); // ���õ�ǰ�µ�1��
		calendar.add(Calendar.MONDAY, 2); // �������£���Ϊ�����µ�1��
		calendar.add(Calendar.DATE, -1); // ��һ�죬��Ϊ���µ����һ��

		strLastDay = sDateFormat.format(calendar.getTime());
		return strLastDay;
	}

	public static String getLastDayOfNextMonth2() {
		String strLastDay = "";

		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MONTH, 1); // ��1����
		calendar.set(Calendar.DATE, 1); // ����������Ϊ���µ�1��
		calendar.roll(Calendar.DATE, -1); // ���ڻع�1�죬Ҳ���Ǳ������1��

		strLastDay = sDateFormat.format(calendar.getTime());
		return strLastDay;
	}

	// ��ȡ����ĵ�һ��
	public static String getFirstDayOfYear() {
		String strFirstDay = "";

		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_YEAR, 1); // ���õ����һ��

		strFirstDay = sDateFormat.format(calendar.getTime());
		return strFirstDay;
	}

	// ��ȡ��������һ��
	public static String getLastDayOfYear() {
		String strLastDay = "";

		Calendar calendar = Calendar.getInstance();
		strLastDay = calendar.get(Calendar.YEAR) + "-12-31";

		return strLastDay;
	}

	// ��ȡ����ĵ�һ��
	public static String getFirstDayOfPreviousYear() {
		String strFirstDay = "";

		Calendar calendar = Calendar.getInstance();
		strFirstDay = (calendar.get(Calendar.YEAR) - 1) + "-01-01";

		return strFirstDay;
	}

	// ��ȡ��������һ��
	public static String getLastDayOfPreviousYear() {
		String strLastDay = "";

		Calendar calendar = Calendar.getInstance();
		strLastDay = (calendar.get(Calendar.YEAR) - 1) + "-12-31";

		return strLastDay;
	}

	// ��ȡ����ĵ�һ��
	public static String getFirstDayOfNextYear() {
		String strFirstDay = "";

		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.YEAR, 1); // ��һ��
		calendar.set(Calendar.DAY_OF_YEAR, 1); // ���õ����һ��

		strFirstDay = sDateFormat.format(calendar.getTime());
		return strFirstDay;
	}

	// ��ȡ��������һ��
	public static String getLastDayOfNextYear() {
		String strLastDay = "";

		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.YEAR, 1); // ��һ��
		calendar.set(Calendar.DAY_OF_YEAR, 1); // ���õ����һ��
		calendar.roll(Calendar.DAY_OF_YEAR, -1); // �ع����������һ��

		strLastDay = sDateFormat.format(calendar.getTime());
		return strLastDay;
	}

	// ��ȡ���������
	public static String getDaysOfYear() {
		int year = 0;
		int days = 0;

		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_YEAR, 1); // ��������Ϊ�����һ��
		calendar.roll(Calendar.DAY_OF_YEAR, -1); // �����ڻع�һ��

		year = calendar.get(Calendar.YEAR);
		days = calendar.get(Calendar.DAY_OF_YEAR);

		return (days + " in " + year);
	}

	// ��ȡ��ǰ�µļ���,��һ����
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

	// ��ȡ��ǰ�µļ���,���һ����
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
		calendar.add(Calendar.MONDAY, 1); // ��һ���£���Ϊ���µ�1��
		calendar.add(Calendar.DATE, 1); // ��ȥһ�죬��Ϊ��ǰ�µ����һ��
		String endMonths = sDateFormat.format(mCalendar.getTime());
		return endMonths;
	}

	// ��ȡ��ǰ�µļ���
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

	// ��ȡ��ǰ�������·ݵ�����
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

	// �ж��Ƿ�������
	public static boolean isLeapYear(int year) {
		return (year % 4 == 0 && year % 100 != 0) || (year % 400 == 0);
	}

	// ���ַ���ʱ���ʽ yyyy-MM-dd��ת����Date����
	public static Date getDateFromStr(String txtDate) {

		ParsePosition pos = new ParsePosition(0);
		Date date = sDateFormat.parse(txtDate, pos);

		return date;
	}

	// ����һ�����ڣ����������ڼ����ַ���
	public static String getWeek(String txtDate) {
		Date date = getDateFromStr(txtDate);
		Calendar calendar = Calendar.getInstance();

		calendar.setTime(date);
		String week = new SimpleDateFormat("EEEE").format(date);

		return week;
	}

	// �����������ڼ�ļ������
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
			days = (date2.getTime() - date1.getTime()) / (24 * 60 * 60 * 1000); // ͨ��getTime()��������ʱ��Dateת���ɺ����ʽLong���ͣ����м���
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}

		return days;
	}

}
