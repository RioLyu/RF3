package com.rio.helper;

/**
 * 基本类型转换为字节数组（大端方式编码）
 * 
 * @author 陈金荣
 * 
 */
public class BitConverter {

	public static byte[] shortToByteArray(short s) {
		byte[] shortBuf = new byte[2];
		for (int i = 0; i < 2; i++) {
			int offset = (shortBuf.length - 1 - i) * 8;
			shortBuf[i] = (byte) ((s >>> offset) & 0xff);
		}
		return shortBuf;
	}

	public static final short byteArrayToShort(byte[] b) {
		return byteArrayToShort(b, 0);
	}

	public static final short byteArrayToShort(byte[] b, int startIndex) {
		return (short) ((b[startIndex] << 8) | (b[startIndex + 1] & 0xFF));
	}

	public static short byteArrayToShort(Byte[] b, int startIndex) {
		return (short) ((b[startIndex].byteValue() << 8) | (b[startIndex + 1]
				.byteValue() & 0xFF));

	}

	/**
	 * 双字节转换为short
	 * 
	 * @param b1
	 *            低位字节
	 * @param b2
	 *            高位字节
	 * @return short类型数值
	 */
	public static final short byteToShort(byte b1, byte b2) {
		return (short) ((b2 << 8) | (b1 & 0xFF));
	}

	public static byte[] intToByteArray(int value) {
		byte[] b = new byte[4];
		for (int i = 0; i < 4; i++) {
			int offset = (b.length - 1 - i) * 8;
			b[i] = (byte) ((value >>> offset) & 0xFF);
		}
		return b;
	}

	public static final int byteArrayToInt(byte[] b) {
		return (b[0] << 24) + ((b[1] & 0xFF) << 16) + ((b[2] & 0xFF) << 8)
				+ (b[3] & 0xFF);
	}

	public static int byteArrayToInt(byte[] b, int startIndex) {
		return (b[startIndex] << 24) + ((b[startIndex + 1] & 0xFF) << 16)
				+ ((b[startIndex + 2] & 0xFF) << 8)
				+ (b[startIndex + 3] & 0xFF);
	}

	public static byte[] longToByteArray(long value) {
		byte[] b = new byte[8];
		for (int i = 0; i < 8; i++) {
			int offset = (b.length - 1 - i) * 8;
			b[i] = (byte) ((value >>> offset) & 0xFF);
		}
		return b;
	}

	public static byte[] singleToByteArray(float value) {
		byte[] b = new byte[4];
		int n = Float.floatToIntBits(value);
		for (int i = 0; i < 4; i++) {
			b[i] = (byte) (n >> (24 - i * 8));
		}
		return b;
	}

	public static float byteArrayToSingle(byte[] b, int startIndex) {
		int value = byteArrayToInt(b, startIndex);
		return Float.intBitsToFloat(value);
	}

	public static byte[] doubleToByteArray(double value) {
		byte[] b = new byte[8];
		long l = Double.doubleToLongBits(value);
		for (int i = 0; i < 8; i++) {
			b[i] = (byte) (l >> (56 - i * 8));
		}
		return b;
	}

	public static double byteArrayToDouble(byte[] b) {
		long l;
		l = b[7];
		l &= 0xFF;
		l |= (long) b[6] << 8;
		l &= 0xFFFF;
		l |= ((long) b[5] << 16);
		l &= 0xFFFFFF;
		l |= ((long) b[4] << 24);
		l &= 0xFFFFFFFFl;
		l |= ((long) b[3] << 32);
		l &= 0xFFFFFFFFFFl;

		l |= ((long) b[2] << 40);
		l &= 0xFFFFFFFFFFFFl;
		l |= ((long) b[1] << 48);
		l &= 0xFFFFFFFFFFFFFFl;
		l |= ((long) b[0] << 56);
		return Double.longBitsToDouble(l);
	}

	public static double byteArrayToDouble(byte[] b, int startIndex) {
		long l;
		l = b[startIndex + 7];
		l &= 0xFF;
		l |= (long) b[startIndex + 6] << 8;
		l &= 0xFFFF;
		l |= ((long) b[startIndex + 5] << 16);
		l &= 0xFFFFFF;
		l |= ((long) b[startIndex + 4] << 24);
		l &= 0xFFFFFFFFl;
		l |= ((long) b[startIndex + 3] << 32);
		l &= 0xFFFFFFFFFFl;

		l |= ((long) b[startIndex + 2] << 40);
		l &= 0xFFFFFFFFFFFFl;
		l |= ((long) b[startIndex + 1] << 48);
		l &= 0xFFFFFFFFFFFFFFl;
		l |= ((long) b[startIndex] << 56);
		return Double.longBitsToDouble(l);
	}

	/**
	 * 字节数组转字符串
	 * 
	 * @param b
	 * @return
	 */
	public static String byteArrayToString(byte[] b) {

		if (b == null)
			return null;
		int length = b.length;
		char[] ch = new char[length];
		for (int i = 0; i < length; i++) {
			ch[i] = (char) b[i];
		}
		return String.valueOf(ch);
	}

	/**
	 * 字节数组转字符串
	 * 
	 * @param b
	 * @param length
	 *            长度
	 * @return
	 */
	public static String byteArrayToString(byte[] b, int length) {
		if (b == null || b.length == 0 || b.length < length)
			return null;
		char[] ch = new char[length];
		for (int i = 0; i < length; i++) {
			ch[i] = (char) b[i];
		}
		return String.valueOf(ch);
	}

	/**
	 * 字节数组转字符串
	 * 
	 * @param b
	 * @param index
	 *            起点索引
	 * @param length
	 *            长度
	 * @return
	 */
	public static String byteArrayToString(byte[] b, int index, int length) {

		if (b == null || b.length < index + 1 || b.length < index + length)
			return null;
		char[] ch = new char[length];
		for (int i = index; i < length + index; i++) {
			ch[i - index] = (char) b[i];
		}
		return String.valueOf(ch);
	}
}
