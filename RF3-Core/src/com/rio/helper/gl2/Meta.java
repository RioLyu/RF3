package com.rio.helper.gl2;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;



import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;

public abstract class Meta {
	
	
	private int mProgramId;//自定义渲染管线程序id
	

	private boolean isLoadBuffer;
	
	public Meta(String vertexShell, String fragmentShell) {
		mProgramId = ShaderUtil.createProgram(vertexShell, fragmentShell);		
		initShader(mProgramId);
	}
	
	public Meta(Context context,String vertexFile, String fragmentFile) {
		mProgramId = ShaderUtil.createProgram(context, vertexFile, fragmentFile);		
		initShader(mProgramId);
	}
	
	public int getProgramId() {
		return mProgramId;
	}
	
	public abstract void initShader(int program);
	
	
	public void load(Object...parame){
		
		if(!isLoadBuffer){
			
			isLoadBuffer = onLoadBuffer();
		}
				
	}	
	
	public abstract boolean onLoadBuffer(Object...parame);

	public void draw(Texture texture,Object...parame){
		
		if(isLoadBuffer){
			
			onDraw(texture,parame);
		}
				
	}

	public abstract void onDraw(Texture texture, Object[] parame);



}
