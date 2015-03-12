package com.ebig.app.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;

/**
 * 
 * 根据中文名自动计算出对应的拼音出来
 * 
 */
public class Cn2Spell {
	/**
	 * Default Format 默认输出格式
	 * 
	 * @return
	 */
	public static HanyuPinyinOutputFormat defaultOutputFormat() {
		HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
		format.setCaseType(HanyuPinyinCaseType.UPPERCASE); // 大写
		format.setToneType(HanyuPinyinToneType.WITHOUT_TONE); // 没有音调数字
		format.setVCharType(HanyuPinyinVCharType.WITH_U_AND_COLON); // u显示

		return format;
	}

	/**
	 * 返回字符串的全拼,是汉字转化为全拼,其它字符不进行转换
	 * 
	 * @param cnStr
	 *            String 字符串
	 * @return String 转换成全拼后的字符串
	 */
	public static String getFullSpell(String chinese) throws Exception {

		StringBuffer buf = new StringBuffer();
		char[] chars = chinese.toCharArray();

		for (int i = 0; i < chars.length; i++) {
			String[] spells = PinyinHelper.toHanyuPinyinStringArray(chars[i],
					defaultOutputFormat());
			// 当转换不是中文字符时,返回null
			if (spells != null) {
				buf.append(spells[0]);
			} else {
				buf.append(chars[i]);
			}
		}

		return buf.toString().toUpperCase();
	}

	public static String getFirstSpell(String chinese) throws Exception {
		StringBuffer buf = new StringBuffer();
		char[] chars = chinese.toCharArray();

		for (int i = 0; i < chars.length; i++) {
			String[] spells = PinyinHelper.toHanyuPinyinStringArray(chars[i],
					defaultOutputFormat());
			// 当转换不是中文字符时,返回null
			if (spells != null) {
				buf.append(spells[0].substring(0, 1));
			} else {
				buf.append(chars[i]);
			}
		}

		return buf.toString().toUpperCase();

	}

	public static void main(String[] args) throws Exception {
		String str = null;
		str = "谢海101普降喜雨";
		System.out.println("Spell=" + Cn2Spell.getFirstSpell(str));

		str = "头孢氨苄胶囊（板）";
		System.out.println("Spell=" + Cn2Spell.getFirstSpell(str));

		str = "李鹏，可耻下场。";
		System.out.println("Spell=" + Cn2Spell.getFirstSpell(str));

		str = "猪油，猪八戒。";
		System.out.println("Spell=" + Cn2Spell.getFirstSpell(str));
	}
}
