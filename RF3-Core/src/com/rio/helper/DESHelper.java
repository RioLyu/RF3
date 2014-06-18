package com.rio.helper;

import java.security.Key;
import java.security.SecureRandom;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;

import com.rio.core.S;
import com.rio.core.U;

import android.util.Base64;

/**
 * <pre>
 * DES算法为密码体制中的对称密码体制，又被成为美国数据加密标准 ，是1972年美国IBM公司研制的对称密码体制加密算法。
 * 其密钥长度为56位，明文按64位进行分组，将分组后的明文组和56位的密钥按位替代或交换的方法形成密文组的加密方法。
 * DES加密算法特点：分组比较短、密钥太短、密码生命周期短、运算速度较慢。
 * DES工作的基本原理是，其入口参数有三个:key、data、mode。 key为加密解密使用的密钥 ，data为加密解密的数据，
 * mode为其工作模式。
 * 利用android2.2自带的SDK中的BASE64编码
 * 
 * </pre>
 * 
 *
 * @author rio
 * 
 */
public class DESHelper {

	private static final String DES = "DES";
	private Key mKey;

	/**
	 * @param key
	 */
	public DESHelper(String key) {
		setKey(key);
	}

	/**
	 * 根据参数生成KEY
	 * 
	 * @param strKey
	 */
	public void setKey(String strKey) {
		try {
			KeyGenerator _generator = KeyGenerator.getInstance(DES);
			_generator.init(new SecureRandom(strKey.getBytes()));
			mKey = _generator.generateKey();
			_generator = null;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 加密: String明文输入,String密文输出
	 * 
	 * @param strMing
	 * @return
	 */
	public String getEncString(String strMing) {
		byte[] byteMi = null;
		byte[] byteMing = null;// 明文字节
		byte[] encode = null;
		try {
			byteMing = strMing.getBytes(S.UTF8);
			byteMi = encode(byteMing); // 加密
			encode = Base64.encode(byteMi, Base64.DEFAULT);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			byteMing = null;
			byteMi = null;
		}
		return new String(encode);
	}

	/**
	 * 解密 : String密文输入,String明文输出
	 * 
	 * @param strMi
	 * @return
	 */
	public String getDesString(String strMi) {
		byte[] byteMing = null;
		byte[] byteMi = null;
		try {
			byteMi = Base64.decode(strMi, Base64.DEFAULT);
			byteMing = decode(byteMi);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			byteMi = null;
		}
		return new String(byteMing);
	}
	/**
	 * DES加密：以byte[]明文输入,byte[]密文输出
	 * 
	 * @param byteS
	 * @return
	 */
	private byte[] encode(byte[] byteS) {

		byte[] byteFina = null;
		Cipher cipher;

		try {
			cipher = Cipher.getInstance(DES);
			cipher.init(Cipher.ENCRYPT_MODE, mKey);
			byteFina = cipher.doFinal(byteS);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cipher = null;
		}
		return byteFina;
	}

	/**
	 * 解密以byte[]密文输入,以byte[]明文输出
	 * 
	 * @param byteD
	 * @return
	 */
	private byte[] decode(byte[] byteD) {
		Cipher cipher;
		byte[] byteFina = null;

		try {
			cipher = Cipher.getInstance(DES);
			cipher.init(Cipher.DECRYPT_MODE, mKey);
			byteFina = cipher.doFinal(byteD);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			cipher = null;
		}
		return byteFina;
	}

}