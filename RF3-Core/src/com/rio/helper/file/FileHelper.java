package com.rio.helper.file;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import com.rio.core.S;
import com.rio.core.U;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;

/**
 * 文件工具类
 * 
 * @author rio
 * @version 1.0
 */
public class FileHelper {

	/**
	 * 在SD卡对应的目录 /sdcard/{@value}
	 */
	private static String LOCAL_DUCUMENT_NAME = "porject";

	public static final class SUFFIX {
		public static final String JPG = ".jpg";

		public static final String PNG = ".png";

	}

	/**
	 * 设置项目目录，默认是porject
	 * 
	 * @param tag
	 */
	public static void setDirectory(String doc) {
		LOCAL_DUCUMENT_NAME = doc;
	}

	private static final int MIN_STORAGE_AVAILABLE_SIZE = 5;
	public static final long BT_SIZE = 1024L;
	public static final long KB_SIZE = BT_SIZE * 1024L;
	public static final long MB_SIZE = KB_SIZE * 1024L;
	public static final long GB_SIZE = MB_SIZE * 1024L;

	/**
	 * 得到应用对应的目录 {@link LOCAL_DUCUMENT_NAME}
	 * 
	 * @return
	 */
	public static File getLocalDirectory() {
		if (U.isSDCardAvaiable()) {
			File file = new File(Environment.getExternalStorageDirectory(),
					LOCAL_DUCUMENT_NAME);
			if (file.exists() || file.mkdirs()) {
				return file;
			}
		}
		return null;
	}

	/**
	 * 得到SD的根目录
	 * 
	 * @return
	 */
	public static File getSDCardDirectory() {
		if (U.isSDCardAvaiable()) {
			return Environment.getExternalStorageDirectory();
		}
		return null;
	}

	/**
	 * 得到应用目录的文件
	 * 
	 * @param filename
	 * @return
	 */
	public static File getSDCardFile(String filename) {
		File dir = getLocalDirectory();
		if (U.notNull(dir)) {
			if (U.notNull(filename)) {
				String path = S.joint(dir.getPath(), File.separator, filename);
				return new File(path);
			} else {
				return dir;
			}
		}
		return null;
	}

	/**
	 * 组成一条文件的URI
	 * 
	 * @param file
	 * @return
	 */
	public static String getFileUri(File file) {
		return S.joint("file://", file.getAbsolutePath());
	}

	/**
	 * 列出目录的文件列表
	 * 
	 * @param file
	 * @return
	 */
	public static List<File> listFiles(File file) {

		if (U.notNull(file) && file.exists() && file.isDirectory()) {
			List<File> list = Arrays.asList(file.listFiles());
			Collections.sort(list, new SimpleFileComparator());
			return list;
		}
		return null;
	}

	/**
	 * 获取SD卡的容量
	 * 
	 * @return
	 */
	public static long getSDCardAvailableStorageSize() {
		long availableSize = 0L;
		if (U.isSDCardAvaiable()) {
			File path = Environment.getExternalStorageDirectory();
			StatFs stat = new StatFs(path.getPath());
			availableSize = stat.getBlockSize() * stat.getAvailableBlocks();
		}
		return availableSize;
	}

	/**
	 * 检验SD卡的容量差不某个范围
	 * 
	 * @return
	 */
	public static boolean isSDCardStorageEnough() {
		long minSize = MIN_STORAGE_AVAILABLE_SIZE * BT_SIZE;
		long availableSize = getSDCardAvailableStorageSize();
		return availableSize > minSize;
	}

	/**
	 * 获取文件的大小
	 * 
	 * @param file
	 * @return
	 */
	public static String getFileSize(File file) {
		String str = "";
		long size = file.length();
		if (size >= 0 && size < KB_SIZE) {
			str = size / BT_SIZE + "B";
		} else if (size >= KB_SIZE && size < MB_SIZE) {
			str = size / KB_SIZE + "KB";
		} else if (size >= MB_SIZE && size < GB_SIZE) {
			BigDecimal longs = new BigDecimal(Double.valueOf(size + "")
					.toString());
			BigDecimal sizeMB = new BigDecimal(Double.valueOf(MB_SIZE + "")
					.toString());
			str = longs.divide(sizeMB, 2, BigDecimal.ROUND_HALF_UP) + "GB";
		}
		return str;
	}

	/**
	 * 检验目录是否存在
	 * 
	 * @param filename
	 * @return
	 */
	public static boolean isExists(String filename) {
		if (U.isSDCardAvaiable()) {
			String path = new File(Environment.getExternalStorageDirectory(),
					LOCAL_DUCUMENT_NAME).getPath() + "/" + filename;
			File file = new File(path);
			if (!file.exists() || file.isDirectory() || file.length() == 0) {
				return false;
			} else {
				return true;
			}
		} else {
			return false;
		}
	}

	/**
	 * 保存文件
	 * 
	 * @param is
	 * @param os
	 * @return
	 * @throws IOException
	 */
	public static void saveFile(InputStream is, OutputStream os)
			throws IOException {
		byte[] buff = new byte[1024];

		int rc = 0;

		while ((rc = is.read(buff, 0, buff.length)) > 0) {

			os.write(buff, 0, rc);

		}
		is.close();
		os.close();
	}

	/**
	 * 打开输入流
	 * 
	 * @param is
	 * @return
	 * @throws IOException
	 */
	public static byte[] openFile(InputStream is) throws IOException {
		int size = is.available();
		byte[] buffer = new byte[size];
		is.read(buffer);
		is.close();
		return buffer;
	}

	/**
	 * 打开一个文件
	 * 
	 * @param context
	 * @param filename
	 * @return
	 * @throws IOException
	 */
	public static byte[] openFile(Context context, String filename)
			throws IOException {
		FileInputStream is = context.openFileInput(filename);
		return openFile(is);
	}

	/**
	 * 保存一个文件
	 * 
	 * @param context
	 * @param filename
	 * @param is
	 * @return
	 * @throws IOException
	 */
	public static void saveFile(Context context, String filename, InputStream is)
			throws IOException {
		FileOutputStream fos = context.openFileOutput(filename,
				Context.MODE_PRIVATE);
		saveFile(is, fos);
	}

	/**
	 * 打开一个文件
	 * 
	 * @param filename
	 * @return
	 * @throws IOException
	 */
	public static byte[] openFile(String filename) throws IOException {
		File file = getSDCardFile(filename);
		FileInputStream is = new FileInputStream(file);
		return openFile(is);
	}

	/**
	 * 保存一个文件
	 * 
	 * @param filename
	 * @param is
	 * @return
	 * @throws IOException
	 */
	public static void saveFile(String filename, InputStream is)
			throws IOException {
		File file = getSDCardFile(filename);
		FileOutputStream os = new FileOutputStream(file);
		saveFile(is, os);
	}

	/**
	 * 删除一个文件
	 * 
	 * @param filename
	 * @return
	 */
	public static boolean removeFile(String filename) {
		File file = getSDCardFile(filename);
		if (file != null) {
			return file.delete();
		} else {
			return false;
		}
	}

	/**
	 * 删除一组文件
	 * 
	 * @param dir
	 * @param filter
	 */
	public static void removeFiles(String dir, FileFilter filter) {
		File file = getSDCardFile(dir);
		if (file != null) {
			File[] files = file.listFiles(filter);
			if (files != null && files.length != 0) {
				for (File f : files) {
					f.delete();
				}
			}
		}
	}

	/**
	 * 获得文件后缀
	 * 
	 * @param filename
	 * @return
	 */
	public static String getSuffix(String filename) {
		try {
			int beginIndex = filename.lastIndexOf(S.DOT);
			String result = filename.substring(beginIndex).trim();
			int length = result.length();
			if (length > -1 && length < filename.length() - 1) {
				return result;
			} else {
				return S.EMPTY;
			}
		} catch (Exception e) {
			return S.EMPTY;
		}
	}

	/**
	 * 当父目录不存在时，创建目录！
	 * 
	 * @param dirFile
	 */
	public static void mkdirs(File dirFile) {

		File parentFile = dirFile.getParentFile();
		if (!parentFile.exists()) {

			// 递归寻找上级目录
			mkdirs(parentFile);

			parentFile.mkdir();
		}

	}

	/**
	 * 输出给定目录下的文件，包括子目录中的文件
	 * 
	 * @param dirPath
	 *            给定的目录
	 */
	public static List<File> listAllFile(File file, FilenameFilter filter) {

		List<File> resutl = null;

		// 取得代表目录中所有文件的File对象数组
		File[] list = file.listFiles();
		if (list.length > 0) {
			resutl = new ArrayList<File>();
			// 遍历file数组
			for (int i = 0; i < list.length; i++) {
				File f = list[i];
				if (f.isDirectory()) {
					// 递归子目录
					resutl.addAll(listAllFile(f, filter));
				}else{
					if(filter.accept(file, f.getName())){
						resutl.add(f);
					}
				}
				
			}
			return resutl;
		}
		return resutl;
	}
	
	public static void copy(String SrcFile, String DesFile) throws IOException{
		BufferedRandomAccessFile rafi = new BufferedRandomAccessFile(SrcFile, "r");
		BufferedRandomAccessFile rafo = new BufferedRandomAccessFile(DesFile, "rw");
			FileChannel fci = rafi.getChannel();
		FileChannel fco = rafo.getChannel();
			long size = fci.size();
			MappedByteBuffer mbbi = fci.map(FileChannel.MapMode.READ_ONLY, 0, size);
		MappedByteBuffer mbbo = fco.map(FileChannel.MapMode.READ_WRITE, 0, size);
		for (int i = 0; i < size; i++) {
		            byte b = mbbi.get(i);
		            mbbo.put(i, b);
		}
		rafi.close();
		rafo.close();

	}
	
	
}
