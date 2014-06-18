package com.rio.helper.file;

import java.io.File;
import java.io.FileFilter;

import com.rio.core.U;


/**
 * 后缀文件过滤器
 * @author rio
 *
 */
public class SuffixFileFilter implements FileFilter {

	String mSuffix;
	
	public SuffixFileFilter(String suffix) {
		mSuffix = suffix;
	}
	
	@Override
	public boolean accept(File pathname) {
		if(pathname.isFile()){
			String suffix = FileHelper.getSuffix(pathname.getAbsolutePath());
			if(U.notNull(suffix)&& suffix.equals(mSuffix)){
				return true;
			}
			return false;
		}
		return true;
	}
	

}
