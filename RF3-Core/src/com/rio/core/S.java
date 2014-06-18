package com.rio.core;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StrikethroughSpan;
import android.widget.SlidingDrawer;

/**
 * 文字处理
 * 
 * @author rio
 * 
 */
public class S {

	/**
	 * \n
	 */
	public static final String LINE = "\n";

	/**
	 * null
	 */
	public static final String EMPTY = "";

	/**
	 * /
	 */
	public static final String DOCUMENT = "/";

	/**
	 * 空格
	 */
	public static final String SPACE = " ";

	/**
	 * .
	 */
	public static final String DOT = ".";

	/**
	 * '
	 */
	public static final String SINGLE_QUOTES = "'";

	/**
	 * ;
	 */
	public static final String SEMICOLON = ";";

	/**
	 * :
	 */
	public static final String COLON = ":";

	/**
	 * ,
	 */
	public static final String COMMA = ",";

	/**
	 * 0
	 */
	public static final String ZERO = "0";

	/**
	 * false
	 */
	public static final String FALSE = "false";

	/**
	 * true
	 */
	public static final String TRUE = "true";

	/**
	 * UTF8
	 */
	public static final String UTF8 = "UTF8";

	/**
	 * 格式化 "3"->"03"
	 * 
	 * @param t
	 * @return
	 */
	public static String formatTime(int t) {
		if (t < 10)
			return "0" + t;
		return "" + t;
	}

	/**
	 * 有划线的字
	 * 
	 * @param string
	 * @return
	 */
	public static Spannable strikeThrough(String string) {
		Spannable word = new SpannableString(string);
		word.setSpan(new StrikethroughSpan(), 0, word.length(), 0);
		return word;
	}

	/**
	 * 有底色的字
	 * 
	 * @param string
	 * @param color
	 * @return
	 */
	public static Spannable backgroundColor(String string, int color) {
		Spannable word = new SpannableString(string);
		word.setSpan(new BackgroundColorSpan(color), 0, word.length(), 0);
		return word;
	}

	/**
	 * 有前景色的字
	 * 
	 * @param string
	 * @param start
	 * @param end
	 * @param color
	 * @return
	 */
	public static Spannable foregroundColor(String string, int start, int end,
			int color) {
		Spannable word = new SpannableString(string);
		word.setSpan(new ForegroundColorSpan(color), start, end, 0);
		return word;
	}

	/**
	 * HTML转字符串
	 * 
	 * @param sourceStr
	 * @return
	 */
	public static String htmlToText(String sourceStr) {
		if (U.notNull(sourceStr)) {
			sourceStr = sourceStr.replaceAll("&#039;", "'")
					.replaceAll("&amp;", "&").replaceAll("&nbsp;", " ")
					.replaceAll("</(?i)p>", LINE)
					.replaceAll("<(?i)br\\s?/?>", LINE)
					.replaceAll("</(?i)h\\d>", LINE)
					.replaceAll("</(?i)tr>", LINE)
					.replaceAll("<!--.*?-->", EMPTY)
					.replaceAll("<[^>]+>", EMPTY);
		}
		return sourceStr;
	}

	/**
	 * 去除所有HTML标签
	 * 
	 * @param sourceStr
	 * @return
	 */
	public static String removeHtmlTag(String sourceStr) {
		if (U.notNull(sourceStr)) {
			sourceStr = sourceStr.replaceAll("<[^>]+>", "");
		}
		return sourceStr;
	}

	/**
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str) {
		String strPattern = "[0-9]*";
		Pattern p = Pattern.compile(strPattern);
		Matcher m = p.matcher(str.trim());
		if (m.matches()) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNumOrLetter(String str) {
		String strPattern = "^[A-Za-z0-9]+$";
		Pattern p = Pattern.compile(strPattern);
		Matcher m = p.matcher(str.trim());
		if (m.matches()) {
			return true;
		}
		return false;
	}

	/**
	 * @param str
	 * @param maxLen
	 * @return
	 */
	public static boolean isLength(String str, int maxLen) {
		char[] cs = str.toCharArray();
		int count = 0;
		int last = cs.length;
		for (int i = 0; i < last; i++) {
			if (cs[i] > 255)
				count += 2;
			else
				count++;
		}
		if (count >= maxLen)
			return true;
		else
			return false;
	}

	/**
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isChinese(String str) {
		String strPattern = "[\u0391-\uFFE5]*";
		Pattern p = Pattern.compile(strPattern);
		Matcher m = p.matcher(str.trim());
		if (m.matches()) {
			return true;
		}
		return false;
	}

	/**
	 * 
	 * @param phone
	 * @return
	 */
	public static boolean isPhone(String phone) {
		Pattern pattern = Pattern.compile("([1]{1})([0-9]{10})");
		Matcher m = pattern.matcher(phone.trim());
		if (m.matches()) {
			return true;
		}
		return false;
	}

	/**
	 * 如果大于某个长度，就截取
	 * 
	 * @param s
	 * @param max
	 * @return
	 */
	public static String substirng(String s, int max) {

		if (s.length() > max) {

			s = new String(s.substring(0, max));

		}

		return s;
	}

	/**
	 * 截取带中文的字符串
	 * 
	 * @param s
	 *            要截取的字符串
	 * @param l
	 *            要截取字数，1 = 1个汉字 = 1个字母
	 */
	public static String subChinese(String s, int l) {
		if (U.notNull(s)) {
			int length = 2 * l;
			byte[] bytes;
			try {
				bytes = s.getBytes("Unicode");
			} catch (UnsupportedEncodingException e) {
				return S.EMPTY;
			}
			int n = 0; // 表示当前的字节数
			int i = 2; // 要截取的字节数，从第3个字节开始
			for (; i < bytes.length && n < length; i++) {
				// 奇数位置，如3、5、7等，为UCS2编码中两个字节的第二个字节
				if (i % 2 == 1) {
					n++; // 在UCS2第二个字节时n加1
				} else {
					// 当UCS2编码的第一个字节不等于0时，该UCS2字符为汉字，一个汉字算两个字节
					if (bytes[i] != 0) {
						n++;
					}
				}

			}
			// 如果i为奇数时，处理成偶数
			/*
			 * if (i % 2 == 1){ // 该UCS2字符是汉字时，去掉这个截一半的汉字 if (bytes[i - 1] != 0)
			 * i = i - 1; // 该UCS2字符是字母或数字，则保留该字符 else i = i + 1; }
			 */
			// 将截一半的汉字要保留
			if (i % 2 == 1) {
				i = i + 1;
			}
			try {
				return new String(bytes, 0, i, "Unicode");
			} catch (UnsupportedEncodingException e) {
				return S.EMPTY;
			}
		} else {
			return S.EMPTY;
		}
	}

	/**
	 * 截取小数点后几位
	 * 
	 * @param i
	 *            小数位数
	 * @param d
	 *            需要格式化的实数
	 * @return 截取小数点后几位的实数字符串
	 */
	public static String subDouble(double d, int i) {
		StringBuffer sb = new StringBuffer("0.");
		for (int j = 0; j < i; j++) {
			sb.append("0");
		}
		return new DecimalFormat(sb.toString()).format(d);
	}

	/**
	 * 连接字符串
	 * 
	 * @param strings
	 * @return
	 */
	public static String joint(String... strings) {
		StringBuffer sb = new StringBuffer();
		for (String str : strings) {
			sb.append(str);
		}
		return sb.toString();
	}


	/**
	 * 切割字符串
	 * 
	 * @param line
	 * @param split
	 * @param limit
	 * @return
	 */
	public static String[] split(String line, String split) {
		if (TextUtils.isEmpty(line))
			return null;
		return line.trim().split(split);
	}

	/**
	 * 切割字符串
	 * 
	 * @param line
	 * @param split
	 * @param size
	 * @return
	 */
	public static String[] split(String line, String split, int size) {
		if (TextUtils.isEmpty(line))
			return null;
		String[] result = new String[size];
		String tmp = line;
		int j = 0;
		int s = 0;
		while(s < size){
			j = tmp.indexOf(split);
			if (j < 0)
				break;
			if(j > 1){
				result[s] = new String(tmp.substring(0, j));				
				s++;				
			}
			tmp = tmp.substring(j + 1);
		}
		if(s < size){
			result[s] = tmp;
		}
		return result;

	}

	/**
	 * 替换
	 * 
	 * @param s
	 * @param m
	 * @return
	 */
	public static String replace(String target, String old, String replacement) {
		if (U.isNull(target)) {
			return S.EMPTY;
		}
		return target.replaceAll(old, replacement);
	}
	
	
	public static String inputStream2String(InputStream is) throws IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		int i = -1;
		while ((i = is.read()) != -1) {
			baos.write(i);
		}
		is.close();
		return baos.toString();
	}

}
