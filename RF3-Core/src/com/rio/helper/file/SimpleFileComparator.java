package com.rio.helper.file;

import java.io.File;
import java.util.Comparator;

/**
 * 把目录的排在前面
 * @author rio
 *
 */
public class SimpleFileComparator implements Comparator<File>{

	@Override
	public int compare(File file1, File file2) {
		boolean isDir1 = file1.isDirectory();
		boolean isDir2 = file2.isDirectory();
		if(isDir1 == isDir2){
			return 0;
		}else if(isDir1){
			return -1;
		}else{
			return 1;
		}
	}
	
}
