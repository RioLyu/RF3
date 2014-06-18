package com.rio.helper.gl2;

import java.io.IOException;
import java.io.InputStream;

import com.rio.core.L;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;

public class Texture {

	private int id;
		
	private boolean isUpdate;
	
	private Bitmap bitmap;
	
	private boolean isRepeat;
	
	private boolean isMipmap;
	
	public Texture(InputStream is,boolean isRepeat,boolean isMipmap) {		
		this.isRepeat = isRepeat;
		this.isMipmap = isMipmap;					
		//BitmapFactory.Options options = new BitmapFactory.Options();
		//options.inSampleSize = 2;//图片宽高都为原来的二分之一，即图片为原来的四分之一
		//bitmap = BitmapFactory.decodeStream(is,null,options);
		try {
			bitmap = BitmapFactory.decodeStream(is);
			is.close();
			isUpdate = true;
		} catch (IOException e) {
			L.e(e);
		}	
	}
	
	public Texture(Context context,int drawable,boolean isRepeat,boolean isMipmap) {
		InputStream is = context.getResources().openRawResource(drawable);		
		this.isRepeat = isRepeat;
		this.isMipmap = isMipmap;							
		try {
			bitmap = BitmapFactory.decodeStream(is);
			is.close();
			isUpdate = true;
		} catch (IOException e) {
			L.e(e);
		}			
	}	
	
	public void bindImage2D(){
		if(isUpdate && bitmap != null){
			if(id != 0){
				GLES20.glDeleteTextures(1, new int[]{id}, 0);
			}
			id = TextureUtil.loadImage2D(bitmap, isRepeat, isMipmap);
			bitmap = null;
			isUpdate = false;	
		}
		
	}

	public int getId() {
		return id;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
		isUpdate = true;
	}
	
}
