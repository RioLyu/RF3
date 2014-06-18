package com.rio.helper.file;

import java.io.File;
import java.util.Comparator;

public class FileModifiedComparator implements Comparator<File> {

	@Override
	public int compare(File file1, File file2) {
		long last1 = file1.lastModified();
		long last2 = file2.lastModified();
		if (last1 == last2) {
			return 0;
		} else if (last1 < last2) {
			return -1;
		} else {
			return 1;
		}
	}

}