package com.rio.helper.gl2;

import java.io.IOException;
import java.io.InputStream;

import com.rio.core.U;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.opengl.GLES20;
import android.opengl.GLUtils;


public class TextureUtil {

	public static int loadImage2D(InputStream is, boolean isRepeat,boolean isMipmap){
		//生成纹理ID
		int[] textures = new int[1];
		GLES20.glGenTextures
		(
				1,          //产生的纹理id的数量
				textures,   //纹理id的数组
				0           //偏移量
		);    
		int textureId = textures[0];    
		GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
		if(isMipmap)
		{
			GLES20.glTexParameteri ( GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR_MIPMAP_LINEAR);   
			GLES20.glTexParameteri ( GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_LINEAR_MIPMAP_NEAREST);
		}
		else
		{
			GLES20.glTexParameteri ( GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);   
			GLES20.glTexParameteri ( GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
		}
        if(isRepeat)
        {
        	GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, 
        			GLES20.GL_TEXTURE_WRAP_S,GLES20.GL_REPEAT);
    		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, 
    				GLES20.GL_TEXTURE_WRAP_T,GLES20.GL_REPEAT);
        }
        else
        {
        	GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, 
        			GLES20.GL_TEXTURE_WRAP_S,GLES20.GL_CLAMP_TO_EDGE);
    		GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D, 
    				GLES20.GL_TEXTURE_WRAP_T,GLES20.GL_CLAMP_TO_EDGE);
        }  
        //通过输入流加载图片===============begin===================
        Bitmap bitmapTmp;
        try 
        {
        	bitmapTmp = BitmapFactory.decodeStream(is);
        } 
        finally 
        {
            try 
            {
                is.close();
            } 
            catch(IOException e) 
            {
                e.printStackTrace();
            }
        }        
        //实际加载纹理
        GLUtils.texImage2D
        (
        		GLES20.GL_TEXTURE_2D,   //纹理类型，在OpenGL ES中必须为GL10.GL_TEXTURE_2D
        		0, 					  //纹理的层次，0表示基本图像层，可以理解为直接贴图
        		bitmapTmp, 			  //纹理图像
        		0					  //纹理边框尺寸
        );
        //自动生成Mipmap纹理
        if(isMipmap)
        {
        	GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);
        }
        bitmapTmp.recycle(); 		  //纹理加载成功后释放图片		
		return textureId;		
	}
	
	public static int loadImage2D(Bitmap bitmap, boolean isRepeat,
			boolean isMipmap) {
		if (U.notNull(bitmap)) {
			// 生成纹理ID
			int[] textures = new int[1];
			GLES20.glGenTextures(
					1, // 产生的纹理id的数量
					textures, // 纹理id的数组
					0 // 偏移量
			);
			int textureId = textures[0];
			GLES20.glBindTexture(GLES20.GL_TEXTURE_2D, textureId);
			if (isMipmap) {
				GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,
						GLES20.GL_TEXTURE_MAG_FILTER,
						GLES20.GL_LINEAR_MIPMAP_LINEAR);
				GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,
						GLES20.GL_TEXTURE_MIN_FILTER,
						GLES20.GL_LINEAR_MIPMAP_NEAREST);
			} else {
				GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,
						GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR);
				GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D,
						GLES20.GL_TEXTURE_MIN_FILTER, GLES20.GL_NEAREST);
			}
			if (isRepeat) {
				GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
						GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT);
				GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
						GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT);
			} else {
				GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
						GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_CLAMP_TO_EDGE);
				GLES20.glTexParameterf(GLES20.GL_TEXTURE_2D,
						GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_CLAMP_TO_EDGE);
			}

			// 实际加载纹理
			GLUtils.texImage2D(
					GLES20.GL_TEXTURE_2D, // 纹理类型，在OpenGLES中必须为GL10.GL_TEXTURE_2D
					0, // 纹理的层次，0表示基本图像层，可以理解为直接贴图
					bitmap, // 纹理图像
					0 // 纹理边框尺寸
			);
			// 自动生成Mipmap纹理
			if (isMipmap) {
				GLES20.glGenerateMipmap(GLES20.GL_TEXTURE_2D);
			}
			bitmap.recycle(); // 纹理加载成功后释放图片
			return textureId;
		}
		return 0;
	};
}
