package com.rio.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.rio.core.L;
import com.rio.helper.file.FileHelper;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Bitmap.Config;
import android.graphics.PorterDuff.Mode;
import android.util.Log;

/**
 * 图片的工具类
 * @author rio
 *
 */
public class BitmapHelper {
	
	private static final Config BITMAP_CONFIG  = Config.ARGB_8888;
		
	private Bitmap mBitmap;
	
	private CompressFormat format;

	private BitmapHelper(Bitmap bitmap) {
		mBitmap = bitmap;
	}	

	/**
	 * 打开一个图片
	 * @param fileName 文件名
	 * @return 
	 * @throws IOException 
	 */	
	public static BitmapHelper open(String filename) {
		byte[] data;
		try {
			data = FileHelper.openFile(filename);
		} catch (IOException e) {
			return new BitmapHelper(null);
		}	
		return open(data);
	}
	
	/**
	 * 打开一个图片
	 * @param data 二进制数据
	 * @return
	 */
	public static BitmapHelper open(byte[] data) {
		Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);		
		return new BitmapHelper(bitmap);
	}

	/**
	 * 打开一个图片
	 * @param is 输入流
	 * @return
	 * @throws IOException
	 */
	public static BitmapHelper open(InputStream is) throws IOException {
		Bitmap bitmap = BitmapFactory.decodeStream(is);	
		is.close();
		return new BitmapHelper(bitmap);
	}
	
	/**
	 * 打开一个图片
	 * @param context 上下文
	 * @param resid 资源
	 * @return
	 */
	public static BitmapHelper open(Context context,int resid) {
		Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), resid);		
		return new BitmapHelper(bitmap);
	}
	
	/**
	 * 打开一个图片
	 * @param bitmap 图片
	 * @return
	 */
	public static BitmapHelper open(Bitmap bitmap){	
		return new BitmapHelper(bitmap);
	}
	

	/**
	 * 产生一个圆角图片
	 * @param roundPx
	 * @return
	 */
	public BitmapHelper corner(float roundPx) {
		
		if(isEmpty()) return this; 
		
		int width = (int)(mBitmap.getWidth());
		int height = (int)(mBitmap.getHeight());
		
	    Bitmap output = Bitmap.createBitmap(width,
	    		height, BITMAP_CONFIG);
	    Canvas canvas = new Canvas(output);

	    final int color = 0xff000000;
	    final Paint paint = new Paint();
	    final Rect rect = new Rect(0, 0, width, height);
	    final RectF rectF = new RectF(0, 0, width, height);

	    paint.setAntiAlias(true);
	    canvas.drawARGB(0, 0, 0, 0);
	    paint.setColor(color);
	    canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
	    
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		
	    canvas.drawBitmap(mBitmap, rect, rectF, paint);
	    mBitmap.recycle();
	    
	    mBitmap = output;
	    
	    return this;
	}
	

	/**
	 * 缩放图片
	 * @param scale 比例
	 * @return
	 */
	public BitmapHelper scale(float scale){
		
		if(isEmpty()) return this; 
		
		Matrix matrix=new Matrix();
		matrix.postScale(scale, scale);
		Bitmap output=Bitmap.createBitmap(mBitmap,0,0,mBitmap.getWidth(),
				mBitmap.getHeight(),matrix,true);
		
	    mBitmap = output;		
	    
	    return this;
	}
	
	

	/**
	 * 得到图片Bitmap
	 * @return
	 */
	public Bitmap get(){
		
		return mBitmap;
	}
	
	/**
	 * 是否为空
	 * @return
	 */
	public boolean isEmpty(){
		if(mBitmap==null){
			L.i("BITMAP IS NULL");
			return true;
		}else{
			return false;
		}		
	}
	

	/**
	 * 保存图片
	 * @param filename
	 * @return
	 */
	public boolean save(String filename)
	{
		if(isEmpty()) return false; 
		File file = FileHelper.getSDCardFile(filename);	
	
		if(file != null){
			try {	
				file.getParentFile().mkdirs();
				if(file.exists())file.delete();
				FileOutputStream os = new FileOutputStream(file);
				format = getFormatBySuffix(FileHelper.getSuffix(filename));	
				mBitmap.compress(format, 50, os);
				os.flush();
				os.close();	
				return true;
			} catch (Exception e) {
				L.e(e);
				return false;
			}
		}else{

			return false;
		}
	}
	/**
	 * 保存图片
	 * @param context
	 * @param filename
	 * @return
	 */
	public boolean save(Context context,String filename)
	{
		if(isEmpty()) return false; 
		
		try {			
			FileOutputStream os = context.openFileOutput(filename, Context.MODE_PRIVATE);
			format = getFormatBySuffix(FileHelper.getSuffix(filename));
			mBitmap.compress(format,50, os);
			os.flush();
			os.close();				
			return true;
		} catch (Exception e) {
			return false;
		}
	}
	
	/**
	 * 标志bitmap死了
	 */
	public void recycle(){
		mBitmap.recycle();
	}
	
	/**
	 * 取得图片后缀
	 * @param suffix
	 * @return
	 */
	public CompressFormat getFormatBySuffix(String suffix){
		if(FileHelper.SUFFIX.JPG.equals(suffix)) return Bitmap.CompressFormat.JPEG;		
		return Bitmap.CompressFormat.PNG;
	}
}
