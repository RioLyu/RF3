package com.rio.helper.file;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.logging.FileHandler;

import com.rio.core.L;
import com.rio.core.S;
import com.rio.core.U;


/**
 * 1、保存到SD卡
 * 2、能按大小自动新建另一日志文档，保证不能大于5M
 * 3、格式包括日期与操作
 * 
 * @author rio
 *
 */
public class Logger {
		
	private static final int MAX_SIZE = 1024 * 5;

	private static final String TAG = "log";

	private static final SimpleDateFormat mFormat = new SimpleDateFormat("[yyyy-MM-dd HH:mm:ss]");
	
	private static final FileModifiedComparator mComparator = new FileModifiedComparator();

	/**
	 * 添加信息到日志末尾
	 * @param message 信息
	 */
	public static void append(String message) {
		L.i(message);
		// 是否有SD
		if (U.isSDCardAvaiable()) {
			try {
				// 获取编号最大的文件
				File dir = FileHelper.getSDCardFile(TAG);
				if (!dir.exists()) {
					dir.mkdir();
				}
				File file = null;
				if (dir.isDirectory()) {
					File[] list = dir.listFiles();
					if (list.length > 0) {
						file = Collections.max(Arrays.asList(list),mComparator);
					} else {
						
						file = new File(dir, TAG+"-"+System.currentTimeMillis());
					}

					// 打开一个随机访问文件流，按读写方式
					RandomAccessFile randomFile = new RandomAccessFile(file,"rw");
					// 文件长度，字节数
					long fileLength = randomFile.length();
					if (fileLength > MAX_SIZE) {
						file = new File(dir, TAG +"-"+ System.currentTimeMillis());
						randomFile = new RandomAccessFile(file, "rw");
						fileLength = 0;
					}
					// 将写文件指针移到文件尾。
					randomFile.seek(fileLength);
					// 写入日期
					randomFile.writeBytes(mFormat.format(new Date()));
					// 写入日志
					randomFile.writeBytes(message+S.LINE);
					randomFile.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}


	
	/**
	 * 打印异常信息到日志末尾
	 * @param throwable 信息
	 */
	public static void append(Throwable throwable){
		StringBuffer sb = new StringBuffer(throwable.toString()).append(S.LINE);
		String message = throwable.getMessage();
		if(U.notNull(message)){
			sb.append(message).append(S.LINE);
		}
		StackTraceElement [] messages =throwable.getStackTrace();
		int length=messages.length;
		for(int i=0;i<length;i++){
			sb.append(messages[i].getMethodName()).append(S.DOCUMENT);
			sb.append(messages[i].toString()).append(S.LINE);
		}
		sb.append("...");
		append(sb.toString());						
	}
	


}
