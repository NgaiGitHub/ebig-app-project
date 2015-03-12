package com.ebig.pub;

import java.io.*;
import java.security.*;
import javax.crypto.*;
import javax.crypto.spec.*;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.BadPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.NoSuchPaddingException;

public class Encrypter {
  static private byte b[] = {
      79, 62, 93, 38, -125, -45, -70, -104, -99, -85, -33, -70, -122, -71, 109,
      -51, -23, 121, -29, 73, -42, 118, 42, -23};
  static private SecretKey KEY = new SecretKeySpec(b, "DESede");
  static private String CHAR_SET = "GBK";

  public Encrypter() {
  }

  /**
   * �������ݣ�����һ���ַ��������ؼ��ܺ������
   * @param str
   * @return
   */
  public static String encode(String str) throws NoSuchPaddingException,
      NoSuchAlgorithmException, InvalidKeyException,
      UnsupportedEncodingException, BadPaddingException,
      IllegalBlockSizeException, IllegalStateException {
    if (str == null) {
      return null;
    }

    Cipher cp = Cipher.getInstance("DESede");
    cp.init(Cipher.ENCRYPT_MODE, KEY);

    byte ptext[] = str.getBytes(CHAR_SET);
    byte[] ctext = cp.doFinal(ptext);
    String r = getString(ctext);
    return r;
  }

  /**
   * �������ݣ�����һ���ַ��������ؽ��ܺ������
   * @param str
   * @return
   */
  public static String decode(String str) throws NoSuchPaddingException,
      NoSuchAlgorithmException, InvalidKeyException, BadPaddingException,
      IllegalBlockSizeException, IllegalStateException,
      UnsupportedEncodingException {
    if (str == null) {
      return null;
    }
    Cipher cp = Cipher.getInstance("DESede");
    cp.init(Cipher.DECRYPT_MODE, KEY);
    byte[] ctext = getByte(str);
    byte strbyte[] = cp.doFinal(ctext);
    String r = new String(strbyte, CHAR_SET);
    return r;

  }

  /**
   * str����','�ָ�����֣�ת��Ϊ�ַ�����
   * @param str
   * @return
   */
  static private byte[] getByte(String str) {
    String strs[] = str.split(",");
    byte[] bytes = new byte[strs.length];
    for (int i = 0; i < strs.length; i++) {
      bytes[i] = (new Byte(strs[i])).byteValue();
    }
    return bytes;
  }

  /**
   * ��bytes����ת��Ϊ�ַ�������һϵ�е���������ʾ
   * @param bytes
   * @return
   */
  static private String getString(byte[] bytes) {
    StringBuffer sb = new StringBuffer();
    for (int i = 0; i < bytes.length; i++) {
      sb.append(Byte.toString(bytes[i]) + ",");
    }
    return sb.toString();
  }

  public static void main(String[] args) throws IllegalStateException,
      IllegalBlockSizeException, BadPaddingException,
      UnsupportedEncodingException, InvalidKeyException,
      NoSuchAlgorithmException, NoSuchPaddingException {
   String aa = "29,-116,-125,57,96,20,-75,3,66,-46,93,126,-93,-48,-106,43,";
   String bb = decode(aa);
   System.err.println(bb);
  }

}