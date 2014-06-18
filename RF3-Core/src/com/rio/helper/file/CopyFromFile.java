package com.rio.helper.file;

import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * 复制例子
 * @author rio
 *
 */
public class CopyFromFile {


	public void test(String SrcFile, String DesFile) throws IOException{
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
