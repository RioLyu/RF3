package com.rio.helper.sql;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.rio.core.L;
import com.rio.helper.file.FileHelper;



import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

/**
 * 使用已经存在的数据库，数据库放在asset目录里
 * 
 * @author rio
 *
 * @param <T>
 */
public abstract class AssetSQLHelper<T extends SimpleTable> extends SimpleSQLHelper<T> {
	
	private Context mContext;
	
	private String mPath;

	/**
	 * @param context
	 * @param talbeName
	 * @param version
	 * @param cls
	 * @param asset	asset目录的文件名
	 */
	protected AssetSQLHelper(Context context, String talbeName, int version,
			Class cls,String asset) {
		super(context, talbeName, version, cls);
		mContext = context;
		mPath = asset;
		build(false);		
	}

	/**
	 * 拷贝数据库
	 * @param force
	 */
	protected void build(boolean force) {
		try {
			File database = mContext.getDatabasePath(getTableName());
			if(force || !database.exists()){
				InputStream is = mContext.getAssets().open(mPath);
				FileHelper.mkdirs(database);
				OutputStream os = new FileOutputStream(database);
				FileHelper.saveFile(is, os);				
			}
		} catch (IOException e) {
			L.e(e);
		}
	}

	@Override
	public void onCreate(SQLiteDatabase db) {

	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		build(true);
	}
}
