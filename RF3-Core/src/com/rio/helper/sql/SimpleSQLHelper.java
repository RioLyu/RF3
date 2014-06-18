package com.rio.helper.sql;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;

import com.rio.core.BaseSQLiteOpenHelper;
import com.rio.core.L;
import com.rio.core.S;
import com.rio.core.U;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import android.database.sqlite.SQLiteOpenHelper;

/**
 * 1 _id由1开始； 2 不能是基本类型，必须是封装类； 3 布尔值的真是1，假是2
 * 
 * @author rio
 * 
 * @param <T>
 */
public abstract class SimpleSQLHelper<T extends SimpleTable> extends
		BaseSQLiteOpenHelper {

	private static final String KEY = "_id";

	private String mTableName;

	private Class<T> mClass;

	private LinkedHashMap<String, Integer> mHash;

	private boolean cacheIndex;

	/**
	 * 手机内部闪存数据库
	 * 
	 * @param context
	 * @param dataname
	 *            数据名
	 * @param version
	 *            版本
	 * @param cls
	 *            数据结构
	 */
	protected SimpleSQLHelper(Context context, String dataname, String tablename,int version,
			Class<T> cls) {
		super(context, dataname, null, version);
		mTableName = tablename;
		mClass = cls;
	}
	
	/**
	 * 手机内部闪存数据库
	 * 
	 * @param context
	 * @param talbeName
	 *            表名
	 * @param version
	 *            版本
	 * @param cls
	 *            数据结构
	 */
	protected SimpleSQLHelper(Context context, String talbeName, int version,
			Class<T> cls) {
		super(context, talbeName, null, version);
		mTableName = talbeName;
		mClass = cls;
	}

	/**
	 * 手机外部存储数据库，必须权限android.permission.WRITE_EXTERNAL_STORAGE
	 * 
	 * @param path
	 *            数据库文件路径
	 * @param talbeName
	 * @param version
	 * @param cls
	 */
	protected SimpleSQLHelper(String path, String talbeName, int version,
			Class<T> cls) {
		super(path, talbeName, null, version);
		mTableName = talbeName;
		mClass = cls;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(createDropSql());
		db.execSQL(createTableSql());
	}

	protected String getTableName() {
		return mTableName;
	}

	/**
	 * 生成建表语句
	 * 
	 * @return
	 */
	private String createTableSql() {
		StringBuffer sb = new StringBuffer("CREATE TABLE ");
		sb.append(mTableName)
				.append(" ( ")
				.append(KEY)
				.append(" INTEGER PRIMARY KEY  AUTOINCREMENT  NOT NULL  UNIQUE");
		Field[] child = mClass.getDeclaredFields();
		if (null != child) {
			for (Field f : child) {
				String name = f.getName();
				if (!KEY.equals(name)) {
					sb.append(",").append(f.getName());
					Class<?> value = f.getType();
					if (value.isAssignableFrom(Integer.class)
							|| value.isAssignableFrom(Boolean.class)
							|| value.isAssignableFrom(Float.class)
							|| value.isAssignableFrom(Long.class)
							|| value.isAssignableFrom(Double.class)) {
						sb.append(" INTEGER");
					} else {
						sb.append(" TEXT");
					}
				}
			}
		}
		sb.append(");");
		return sb.toString();
	}

	/**
	 * 生成清空语句
	 * 
	 * @return
	 */
	private String createDropSql() {
		StringBuffer sb = new StringBuffer("DROP TABLE IF EXISTS ");
		sb.append(mTableName).append(";");
		return sb.toString();
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		onCreate(db);
	}

	private ContentValues toContentValues(T obj) {
		ContentValues values = new ContentValues();

		Field[] child = mClass.getDeclaredFields();

		if (null != child) {
			String name = null;
			Object object = null;
			for (Field f : child) {
				try {
					name = f.getName();
					object = f.get(obj);
					if (null != object) {
						Class<?> value = f.getType();
						if (value.isAssignableFrom(String.class)) {
							values.put(name, (String) object);
						} else if (value.isAssignableFrom(Short.class)) {
							values.put(name, (Short) object);
						} else if (value.isAssignableFrom(Integer.class)) {
							values.put(name, (Integer) object);
						} else if (value.isAssignableFrom(Long.class)) {
							values.put(name, (Long) object);
						} else if (value.isAssignableFrom(Float.class)) {
							values.put(name, (Float) object);
						} else if (value.isAssignableFrom(Double.class)) {
							values.put(name, (Double) object);
						} else if (value.isAssignableFrom(Boolean.class)) {
							if ((Boolean) object) {
								values.put(name, 1);
							} else {
								values.put(name, 2);
							}
						}
					}
				} catch (Exception e) {
					L.e(e);
					continue;
				}
			}
		}
		return values;

	}

	/**
	 * 添加
	 * 
	 * @param obj
	 * @return 成功返回有索引的obj
	 */
	public T insert(T obj) {
		try {
			getWritableDatabase().beginTransaction();
			ContentValues values = toContentValues(obj);
			obj.setIndex(getWritableDatabase().insert(mTableName, KEY, values));
			getWritableDatabase().setTransactionSuccessful();
			getWritableDatabase().endTransaction();
			return obj;
		} catch (Exception e) {
			L.e(e);
		}
		return null;
	}

	/**
	 * 查找全部
	 * 
	 * @return
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public List<T> findAll() throws IllegalAccessException,
			InstantiationException {
		StringBuffer sb = new StringBuffer("SELECT * FROM ").append(mTableName)
				.append(S.SEMICOLON);
		Cursor cursor = getWritableDatabase().rawQuery(sb.toString(), null);
		List<T> list = new ArrayList<T>();
		while (cursor.moveToNext()) {
			T t = mClass.newInstance();
			Field[] child = mClass.getFields();
			if (null != child) {
				for (Field f : child) {
					String name = f.getName();
					Class<?> value = f.getType();
					if (value.isAssignableFrom(String.class)) {
						f.set(t, cursor.getString(getColumnIndex(cursor, name)));
					} else if (value.isAssignableFrom(Short.class)) {
						f.set(t, cursor.getShort(getColumnIndex(cursor, name)));
					} else if (value.isAssignableFrom(Integer.class)) {
						f.set(t, cursor.getInt(getColumnIndex(cursor, name)));
					} else if (value.isAssignableFrom(Long.class)) {
						f.set(t, cursor.getLong(getColumnIndex(cursor, name)));
					} else if (value.isAssignableFrom(Double.class)) {
						f.set(t, cursor.getDouble(getColumnIndex(cursor, name)));
					} else if (value.isAssignableFrom(Float.class)) {
						f.set(t, cursor.getFloat(getColumnIndex(cursor, name)));
					} else if (value.isAssignableFrom(Boolean.class)) {
						if (cursor.getInt(getColumnIndex(cursor, name)) == 1) {
							f.set(t, true);
						} else {
							f.set(t, false);
						}
					}
				}
			}
			list.add(t);
		}
		cursor.close();
		return list;
	}

	/**
	 * 缓存列号
	 * 
	 * @param cursor
	 * @param name
	 * @return
	 */
	private int getColumnIndex(Cursor cursor, String name) {
		if (!cacheIndex) {
			String[] colName = cursor.getColumnNames();
			mHash = new LinkedHashMap<String, Integer>();
			for (String s : colName) {
				mHash.put(s, cursor.getColumnIndex(s));
			}
			cacheIndex = true;
		}
		return mHash.get(name);
	}

	/**
	 * 删除
	 * 
	 * @param key
	 */
	public void removeByIndex(long key) {
		StringBuffer sb = new StringBuffer("DELETE FROM ").append(mTableName)
				.append(" WHERE ").append(KEY).append("=?").append(S.SEMICOLON);
		getWritableDatabase().beginTransaction();
		getWritableDatabase().execSQL(sb.toString(),
				new String[] { String.valueOf(key) });
		getWritableDatabase().setTransactionSuccessful();
		getWritableDatabase().endTransaction();
	}

	/**
	 * 删除
	 * 
	 * @param obj
	 */
	public void remove(T obj) {
		removeByIndex(obj.getIndex());
	}
	
	/**
	 * 删除
	 * @param condition
	 */
	public void removeByCondition(String condition){
		StringBuffer sb = new StringBuffer("DELETE FROM ").append(mTableName).append(S.SPACE)
				.append(condition).append(S.SEMICOLON);
		getWritableDatabase().beginTransaction();
		getWritableDatabase().execSQL(sb.toString());
		getWritableDatabase().setTransactionSuccessful();
		getWritableDatabase().endTransaction();		
	}
	
	/**
	 * 删除
	 * @param where
	 */
	public void removeByWhere(String where){
		removeByCondition("WHERE "+ where);
	}

	/**
	 * 索引查询
	 * 
	 * @param index
	 * @return
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public T findByIndex(long index) throws IllegalAccessException,
			InstantiationException {
		StringBuffer sb = new StringBuffer("SELECT * FROM ").append(mTableName)
				.append(" WHERE ").append(KEY).append("=?").append(S.SEMICOLON);
		Cursor cursor = getWritableDatabase().rawQuery(sb.toString(),
				new String[] { String.valueOf(index) });
		T t = null;
		while (cursor.moveToNext()) {
			Field[] child = mClass.getFields();
			if (null != child) {
				t = mClass.newInstance();
				for (Field f : child) {
					String name = f.getName();
					Class<?> value = f.getType();
					if (value.isAssignableFrom(String.class)) {
						f.set(t, cursor.getString(getColumnIndex(cursor, name)));
					} else if (value.isAssignableFrom(Short.class)) {
						f.set(t, cursor.getShort(getColumnIndex(cursor, name)));
					} else if (value.isAssignableFrom(Integer.class)) {
						f.set(t, cursor.getInt(getColumnIndex(cursor, name)));
					} else if (value.isAssignableFrom(Long.class)) {
						f.set(t, cursor.getLong(getColumnIndex(cursor, name)));
					} else if (value.isAssignableFrom(Double.class)) {
						f.set(t, cursor.getDouble(getColumnIndex(cursor, name)));
					} else if (value.isAssignableFrom(Float.class)) {
						f.set(t, cursor.getFloat(getColumnIndex(cursor, name)));
					} else if (value.isAssignableFrom(Boolean.class)) {
						if (cursor.getInt(getColumnIndex(cursor, name)) == 1) {
							f.set(t, true);
						} else {
							f.set(t, false);
						}
					}
				}
			}
		}
		cursor.close();
		return t;
	}

	/**
	 * 查询，返回符合对象特征的全部记录（注意，不能指定索引）
	 * 
	 * @param target
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public List<T> find(T target) throws IllegalArgumentException,
			IllegalAccessException, InstantiationException {
		StringBuffer sb = new StringBuffer("SELECT * FROM ").append(mTableName)
				.append(" WHERE ");
		Field[] child = mClass.getDeclaredFields();
		List<String> values = new ArrayList<String>();
		if (null != child) {
			String key = null;
			Object obj = null;
			for (Field f : child) {
				Class<?> value = f.getType();
				try {
					obj = f.get(target);
				} catch (Exception e) {
					continue;
				}
				if (null != obj) {
					if (value.isAssignableFrom(String.class)) {
						key = ((String) obj);
					} else if (value.isAssignableFrom(Short.class)) {
						key = U.toString(((Short) obj));
					} else if (value.isAssignableFrom(Integer.class)) {
						key = U.toString(((Integer) obj));
					} else if (value.isAssignableFrom(Long.class)) {
						key = U.toString(((Long) obj));
					} else if (value.isAssignableFrom(Double.class)) {
						key = U.toString(((Double) obj));
					} else if (value.isAssignableFrom(Float.class)) {
						key = U.toString(((Float) obj));
					} else if (value.isAssignableFrom(Boolean.class)) {
						if (((Boolean) obj)) {
							key = "1";
						} else {
							key = "2";
						}
					}
					sb.append(f.getName()).append("=? AND ");
					values.add(key);
				}
			}
		}
		List<T> list = null;
		if (!values.isEmpty()) {
			sb.delete(sb.length() - 4, sb.length()).append(S.SEMICOLON);
			String[] array = (String[]) values
					.toArray(new String[values.size()]);
			Cursor cursor = getWritableDatabase()
					.rawQuery(sb.toString(), array);
			list = new ArrayList<T>();
			child = mClass.getFields();
			if (null != child) {
				while (cursor.moveToNext()) {
					T t = mClass.newInstance();
					for (Field f : child) {
						String name = f.getName();
						Class<?> value = f.getType();

						if (value.isAssignableFrom(String.class)) {
							f.set(t, cursor.getString(getColumnIndex(cursor,
									name)));
						} else if (value.isAssignableFrom(Short.class)) {
							f.set(t, cursor.getShort(getColumnIndex(cursor,
									name)));
						} else if (value.isAssignableFrom(Integer.class)) {
							f.set(t,
									cursor.getInt(getColumnIndex(cursor, name)));
						} else if (value.isAssignableFrom(Long.class)) {
							f.set(t, cursor
									.getLong(getColumnIndex(cursor, name)));
						} else if (value.isAssignableFrom(Double.class)) {
							f.set(t, cursor.getDouble(getColumnIndex(cursor,
									name)));
						} else if (value.isAssignableFrom(Float.class)) {
							f.set(t, cursor.getFloat(getColumnIndex(cursor,
									name)));
						} else if (value.isAssignableFrom(Boolean.class)) {
							if (cursor.getInt(getColumnIndex(cursor, name)) == 1) {
								f.set(t, true);
							} else {
								f.set(t, false);
							}
						}// end if isAssignableFrom

					}// end for
					list.add(t);

				}// end while

			}// end if
			cursor.close();
		}// end isEmpty

		return list;
	}

	/**
	 * 查询，返回符合对象特征的全部记录（注意，不要添加“；”）
	 * 
	 * @param target
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public List<T> findByWhere(String where) throws IllegalArgumentException,
			IllegalAccessException, InstantiationException {
		return findByCondition("WHERE "+where);
	}

	
	/**
	 * 查询，返回符合对象特征的全部记录（注意，不要添加“；”）
	 * 
	 * @param target
	 * @return
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public List<T> findByCondition(String condition) throws InstantiationException, IllegalAccessException {
		StringBuffer sb = new StringBuffer("SELECT * FROM ").append(mTableName).append(S.SPACE)
				.append(condition).append(S.SEMICOLON);
		List<T> list = null;
		Cursor cursor = getWritableDatabase().rawQuery(sb.toString(),null);
		list = new ArrayList<T>();
		Field[] child = mClass.getFields();
		if (null != child) {
			while (cursor.moveToNext()) {
				T t = mClass.newInstance();
				for (Field f : child) {
					String name = f.getName();
					Class<?> value = f.getType();

					if (value.isAssignableFrom(String.class)) {
						f.set(t, cursor.getString(getColumnIndex(cursor, name)));
					} else if (value.isAssignableFrom(Short.class)) {
						f.set(t, cursor.getShort(getColumnIndex(cursor, name)));
					} else if (value.isAssignableFrom(Integer.class)) {
						f.set(t, cursor.getInt(getColumnIndex(cursor, name)));
					} else if (value.isAssignableFrom(Long.class)) {
						f.set(t, cursor.getLong(getColumnIndex(cursor, name)));
					} else if (value.isAssignableFrom(Double.class)) {
						f.set(t, cursor.getDouble(getColumnIndex(cursor, name)));
					} else if (value.isAssignableFrom(Float.class)) {
						f.set(t, cursor.getFloat(getColumnIndex(cursor, name)));
					} else if (value.isAssignableFrom(Boolean.class)) {
						if (cursor.getInt(getColumnIndex(cursor, name)) == 1) {
							f.set(t, true);
						} else {
							f.set(t, false);
						}
					}// end if isAssignableFrom

				}// end for
				list.add(t);

			}// end while

		}// end if
		cursor.close();

		return list;
	}
	
	
	/**
	 * 返回最后一行数据
	 * @return
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public T findLast() throws IllegalAccessException, InstantiationException{
		List<T> list = null;
		list = findByCondition("ORDER BY "+KEY+" DESC LIMIT 1");
		if(U.notNull(list) && !list.isEmpty()){
			return list.get(0);
		}
		return null;
	}
	
	/**
	 * 返回第一条数据
	 * @return
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 */
	public T findFirst() throws IllegalAccessException, InstantiationException{
		List<T> list = null;
		list = findByCondition("ORDER BY "+KEY+" ASC LIMIT 1");
		if(U.notNull(list) && !list.isEmpty()){
			return list.get(0);
		}
		return null;
	}
	/**
	 * 更新
	 * 
	 * @param obj
	 * @throws IllegalAccessException
	 * @throws IllegalArgumentException
	 */
	public void update(T obj) throws IllegalArgumentException,
			IllegalAccessException {
		StringBuffer sb = new StringBuffer("UPDATE ").append(mTableName);
		Field[] child = mClass.getDeclaredFields();
		if (null != child) {
			Object[] bindArgs = null;
			int length = child.length;
			bindArgs = new Object[length + 1];
			sb.append(" SET ");
			for (int i = 0; i < length; i++) {
				Field f = child[i];
				sb.append(f.getName()).append("=?").append(S.COMMA);
				bindArgs[i] = f.get(obj);
			}
			sb.deleteCharAt(sb.length() - 1);
			sb.append(" WHERE ").append(KEY).append("=?").append(S.SEMICOLON);
			bindArgs[length] = obj.getIndex();
			getWritableDatabase().beginTransaction();
			getWritableDatabase().execSQL(sb.toString(), bindArgs);
			getWritableDatabase().setTransactionSuccessful();
			getWritableDatabase().endTransaction();

		}

	}

	/**
	 * 获取数据总数
	 * 
	 * @return
	 */
	public long getCount() {
		StringBuffer sb = new StringBuffer("SELECT * FROM ").append(mTableName)
				.append(S.SEMICOLON);
		Cursor cursor = getWritableDatabase().rawQuery(sb.toString(), null);
		long count = (long) (cursor.getCount());
		cursor.close();
		return count;
	}
}
