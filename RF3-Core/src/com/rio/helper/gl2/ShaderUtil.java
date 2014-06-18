package com.rio.helper.gl2;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import com.rio.core.L;

import android.content.Context;
import android.opengl.GLES20;

//加载顶点Shader与片元Shader的工具类
public class ShaderUtil {

	/**
	 * 加载制定shader的方法
	 * 
	 * @param shaderType
	 *            shader的类型 GLES20.GL_VERTEX_SHADER GLES20.GL_FRAGMENT_SHADER
	 * @param source
	 *            shader的脚本字符串
	 * @return
	 */
	public static int loadShader(int shaderType, String source) {
		// 创建一个新shader
		int shader = GLES20.glCreateShader(shaderType);
		// 若创建成功则加载shader
		if (shader != 0) {
			// 加载shader的源代码
			GLES20.glShaderSource(shader, source);
			// 编译shader
			GLES20.glCompileShader(shader);
			// 存放编译成功shader数量的数组
			int[] compiled = new int[1];
			// 获取Shader的编译情况
			GLES20.glGetShaderiv(shader, GLES20.GL_COMPILE_STATUS, compiled, 0);
			if (compiled[0] == 0) {// 若编译失败则显示错误日志并删除此shader
				L.e("Could not compile shader " + shaderType + ":");
				L.e(GLES20.glGetShaderInfoLog(shader));
				GLES20.glDeleteShader(shader);
				shader = 0;
			}
		}
		return shader;
	}

	// 创建shader程序的方法
	public static int createProgram(String vertexSource, String fragmentSource) {
		// 加载顶点着色器
		int vertexShader = loadShader(GLES20.GL_VERTEX_SHADER, vertexSource);
		if (vertexShader == 0) {
			return 0;
		}

		// 加载片元着色器
		int pixelShader = loadShader(GLES20.GL_FRAGMENT_SHADER, fragmentSource);
		if (pixelShader == 0) {
			return 0;
		}

		// 创建程序
		int program = GLES20.glCreateProgram();
		// 若程序创建成功则向程序中加入顶点着色器与片元着色器
		if (program != 0) {
			// 向程序中加入顶点着色器
			GLES20.glAttachShader(program, vertexShader);
			checkGlError();
			// 向程序中加入片元着色器
			GLES20.glAttachShader(program, pixelShader);
			checkGlError();
			// 链接程序
			GLES20.glLinkProgram(program);
			// 存放链接成功program数量的数组
			int[] linkStatus = new int[1];
			// 获取program的链接情况
			GLES20.glGetProgramiv(program, GLES20.GL_LINK_STATUS, linkStatus, 0);
			// 若链接失败则报错并删除程序
			if (linkStatus[0] != GLES20.GL_TRUE) {
				L.e("Could not link program: ");
				L.e(GLES20.glGetProgramInfoLog(program));
				GLES20.glDeleteProgram(program);
				program = 0;
			}
		}
		return program;
	}
	// 创建shader程序的方法
	public static int createProgram(Context context,String vertexFile, String fragmentFile) {
		
		return createProgram(loadFromAssetsFile(vertexFile,context),loadFromAssetsFile(fragmentFile,context));
		
	}
	
	// 检查每一步操作是否有错误的方法
	public static void checkGlError() {
		int error;
		while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
			switch (error) {
			case GLES20.GL_INVALID_ENUM:
				L.e("GLES20.GL_INVALID_ENUM");
				break;
			case GLES20.GL_INVALID_VALUE:
				L.e("GLES20.GL_INVALID_VALUE");
				break;
			case GLES20.GL_INVALID_OPERATION:
				L.e("GLES20.GL_INVALID_OPERATION");
				break;
			case GLES20.GL_OUT_OF_MEMORY:
				L.e("GLES20.GL_OUT_OF_MEMORY");
				break;				
			default:
				break;
			}
			throw new RuntimeException("glError " + error);
		}
	}

	// 从sh脚本中加载shader内容的方法
	public static String loadFromFile(String fname, InputStream in) {
		String result = null;
		try {
			int ch = 0;
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			while ((ch = in.read()) != -1) {
				baos.write(ch);
			}
			byte[] buff = baos.toByteArray();
			baos.close();
			in.close();
			result = new String(buff, "UTF-8");
			result = result.replaceAll("\\r\\n", "\n");
		} catch (Exception e) {
			L.e(e);
		}
		return result;
	}

	// 从sh脚本中加载shader内容的方法
	public static String loadFromAssetsFile(String fname, Context context) {
		String result = null;
		InputStream is = null;
		try {
			is = context.getAssets().open(fname);
			result = loadFromFile(fname, is);
		} catch (Exception e) {
			L.e(e);
		}finally{
			if(is != null){
				try {
					is.close();
				} catch (IOException e) {
					L.e(e);
				}
			}			
		}
		return result;
	}
	
}