package com.ebig.pub;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * <p>Title: </p>
 * <p>Description: </p>
 * <p>Copyright: Copyright (c) 2005</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class DateUtil { 

  private static DateFormat simpleFormat = new SimpleDateFormat("yyyy-MM-dd");

  private static DateFormat dateTimeFormat = new SimpleDateFormat(
      "yyyy-MM-dd HH:mm:ss");

  public DateUtil() {
  }

  public static Calendar getCalendar() {
    return GregorianCalendar.getInstance();
  }

  /**
   * ȡ��ָ�����ڼӼ�һ����ݺ������
   * @param date ָ������
   * @param years ����
   * @return ָ�����ڼӼ���ݺ�õ�������
   */
  public static Date addYears(Date date, int years) {
    //ָ�����ڵ���ݼ�һ�����
    GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
    gc.setTime(date);
    gc.add(Calendar.YEAR, years);
    //  return gc.getTime();
    return new Date(gc.get(Calendar.YEAR) - 1900, gc.get(Calendar.MONTH),
                    gc.get(Calendar.DATE));
  }

  /**
   * ȡ��ָ�����ڼӼ�һ���·ݺ������
   * @param date ָ������
   * @param months ����
   * @return ָ�����ڼӼ��·ݺ�õ�������
   */
  public static Date addMonths(Date date, int months) {
    //ָ�����ڵ��·ݼ�һ���·�
    GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
    gc.setTime(date);
    gc.add(Calendar.MONTH, months);
    //  return gc.getTime();
    return new Date(gc.get(Calendar.YEAR) - 1900, gc.get(Calendar.MONTH),
                    gc.get(Calendar.DATE));
  }

  /**
   * ȡ��ָ�����ڼӼ�һ�������������
   * @param date ָ������
   * @param days ����
   * @return ָ�����ڼӼ�������õ�������
   */
  public static Date addDays(Date date, int days) {
    //ָ�����ڼ�һ������
    GregorianCalendar gc = (GregorianCalendar) Calendar.getInstance();
    gc.setTime(date);
    gc.add(Calendar.DATE, days);
    //  return gc.getTime();
    return new Date(gc.get(Calendar.YEAR) - 1900, gc.get(Calendar.MONTH),
                    gc.get(Calendar.DATE));
  }

  /**
   * ��ȡ�ַ�����ʽ�ĵ�ǰ����
   * @return yyyy-MM-dd��ʽ������
   */
  public static String getStringNow() {
    return simpleFormat.format(new Date());
  }

  /**
   * ��ʽ������,���ڸ�ʽyyyy-MM-dd
   * @param date ����
   * @return �����ַ���
   */
  public static String format(Date date) {
    return simpleFormat.format(date);
  }
  public static String formatDTime(Date date) {
    return dateTimeFormat.format(date);
  }

  /**
   * ��������
   * @param stringDate �����ַ���
   * @return ����,���޷������򷵻�null
   */
  public static Date parse(String stringDate) {
    try {
      if (stringDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
        return simpleFormat.parse(stringDate);
      }
      else if (stringDate.matches("\\d{4}-\\d{2}-\\d{2}\\s\\d{2}:\\d{2}:\\d{2}")) {
        return dateTimeFormat.parse(stringDate);
      }
      else
        return dateTimeFormat.parse(stringDate);
    }
    catch (ParseException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * ʱ������ת��
   * @param date ��ʽ��ǰ������
   * @return ��ʽ���������
   */
  public static Date changeDate(Date date) {
    SimpleDateFormat dateformat = new SimpleDateFormat(
        "yyyy-MM-dd HH:mm:ss");
    Date formatdate = null;
    try {
      formatdate = dateformat.parse(dateformat.format(date));
    }
    catch (ParseException ex) {
      throw new RuntimeException(ex);
    }
    return formatdate;
  }

  public static Date calcMonthFirstDay() {
    Calendar calendar = Calendar.getInstance();
    Calendar cpcalendar = (Calendar) calendar.clone();
    cpcalendar.set(Calendar.DAY_OF_MONTH, 1);
    return new Date(cpcalendar.getTimeInMillis());
  }

  public static Date calcMonthEndDay() {
    Calendar calendar = Calendar.getInstance();
    Calendar cpcalendar = (Calendar) calendar.clone();
    cpcalendar.set(Calendar.DAY_OF_MONTH, 1);
    cpcalendar.add(Calendar.MONTH, 1);
    cpcalendar.add(Calendar.DATE, -1);
    return new Date(cpcalendar.getTimeInMillis());
  }
}

