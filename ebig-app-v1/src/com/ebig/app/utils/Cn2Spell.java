package com.ebig.app.utils;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;

/**
 * 
 * �����������Զ��������Ӧ��ƴ������
 * 
 */
public class Cn2Spell {
	/**
	 * Default Format Ĭ�������ʽ
	 * 
	 * @return
	 */
	public static HanyuPinyinOutputFormat defaultOutputFormat() {
		HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();
		format.setCaseType(HanyuPinyinCaseType.UPPERCASE); // ��д
		format.setToneType(HanyuPinyinToneType.WITHOUT_TONE); // û����������
		format.setVCharType(HanyuPinyinVCharType.WITH_U_AND_COLON); // u��ʾ

		return format;
	}

	/**
	 * �����ַ�����ȫƴ,�Ǻ���ת��Ϊȫƴ,�����ַ�������ת��
	 * 
	 * @param cnStr
	 *            String �ַ���
	 * @return String ת����ȫƴ����ַ���
	 */
	public static String getFullSpell(String chinese) throws Exception {

		StringBuffer buf = new StringBuffer();
		char[] chars = chinese.toCharArray();

		for (int i = 0; i < chars.length; i++) {
			String[] spells = PinyinHelper.toHanyuPinyinStringArray(chars[i],
					defaultOutputFormat());
			// ��ת�����������ַ�ʱ,����null
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
			// ��ת�����������ַ�ʱ,����null
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
		str = "л��101�ս�ϲ��";
		System.out.println("Spell=" + Cn2Spell.getFirstSpell(str));

		str = "ͷ�߰��н��ң��壩";
		System.out.println("Spell=" + Cn2Spell.getFirstSpell(str));

		str = "�������ɳ��³���";
		System.out.println("Spell=" + Cn2Spell.getFirstSpell(str));

		str = "���ͣ���˽䡣";
		System.out.println("Spell=" + Cn2Spell.getFirstSpell(str));
	}
}
