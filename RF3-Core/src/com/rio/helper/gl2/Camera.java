package com.rio.helper.gl2;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.util.Stack;


import android.opengl.Matrix;
import android.widget.Button;

public class Camera {
	private static float[] mProjMatrix = new float[16];//4x4矩阵 投影用
    private static float[] mVMatrix = new float[16];//摄像机位置朝向9参数矩阵   
    private static float[] mCurMatrix;//当前变换矩阵
    private static float[] lightLocation=new float[]{0,0,0};//定位光光源位置
    private static FloatBuffer lightFB;
    private static float[] lightDirection=new float[]{0,0,1};//定向光光源方向
    private static FloatBuffer cameraFB;   
    private static Stack<float[]> mStack=new Stack<float[]>();//保护变换矩阵的栈
    
    public static void setInitStack()//获取不变换初始矩阵
    {
    	mCurMatrix=new float[16];
    	Matrix.setRotateM(mCurMatrix, 0, 0, 1, 0, 0);
    }
    
    public static void freeze()//保护变换矩阵
    {
    	mStack.push(mCurMatrix.clone());
    }
    
    public static void unfreeze()//恢复变换矩阵
    {
    	mCurMatrix=mStack.pop();
    }
    
    public static void translate(float x,float y,float z)//设置沿xyz轴移动
    {
    	Matrix.translateM(mCurMatrix, 0, x, y, z);
    }
    
    public static void rotate(float angle,float x,float y,float z)//设置绕xyz轴移动
    {
    	Matrix.rotateM(mCurMatrix,0,angle,x,y,z);
    }

    public static void scale(float x,float y,float z)//设置绕xyz轴缩放
    {
    	Matrix.scaleM(mCurMatrix,0,x,y,z);
    }
    
    //设置摄像机
    /**
     * @param cx		摄像机位置x
     * @param cy		摄像机位置y
     * @param cz		摄像机位置z
     * @param tx		摄像机目标点x
     * @param ty		摄像机目标点y
     * @param tz		摄像机目标点z
     * @param upx		摄像机UP向量X分量,世界坐标正方向
     * @param upy		摄像机UP向量Y分量,世界坐标正方向
     * @param upz		摄像机UP向量Z分量,世界坐标正方向	
     */
    public static void setLookAt
    (
    		float cx,	
    		float cy,   
    		float cz,   
    		float tx,   
    		float ty,   
    		float tz,   
    		float upx,  
    		float upy,  
    		float upz   	
    )
    {
    	Matrix.setLookAtM
        (
        		mVMatrix, 
        		0, 
        		cx,
        		cy,
        		cz,
        		tx,
        		ty,
        		tz,
        		upx,
        		upy,
        		upz
        );
    	
    	float[] cameraLocation=new float[3];//摄像机位置
    	cameraLocation[0]=cx;
    	cameraLocation[1]=cy;
    	cameraLocation[2]=cz;
    	//设置字节顺序
        cameraFB=BufferHelper.create(cameraLocation);

    }
    

    /**
     * 设置透视投影参数
     * @param left		near面的left
     * @param right		near面的right
     * @param bottom	near面的bottom
     * @param top		near面的top
     * @param near		near面距离
     * @param far		far面距离
     */
    public static void setProjectFrustum
    (
    	float left,		
    	float right,    
    	float bottom,  
    	float top,      
    	float near,		
    	float far       
    )
    {
    	Matrix.frustumM(mProjMatrix, 0, left, right, bottom, top, near, far);    	
    }
    
    //设置正交投影参数
    public static void setProjectOrtho
    (
    	float left,		//near面的left
    	float right,    //near面的right
    	float bottom,   //near面的bottom
    	float top,      //near面的top
    	float near,		//near面距离
    	float far       //far面距离
    )
    {    	
    	Matrix.orthoM(mProjMatrix, 0, left, right, bottom, top, near, far);
    }   
   
    //获取具体物体的总变换矩阵
    public static float[] getCamenaMatrix()
    {
    	float[] mMVPMatrix=new float[16];
    	Matrix.multiplyMM(mMVPMatrix, 0, mVMatrix, 0, mCurMatrix, 0);
        Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mMVPMatrix, 0);        
        return mMVPMatrix;
    }
    //获取投影、摄像机组合矩阵
    public static float[] getViewProjMatrix()
    {
    	float[] mMVPMatrix=new float[16];
    	Matrix.multiplyMM(mMVPMatrix, 0, mProjMatrix, 0, mVMatrix, 0);      
        return mMVPMatrix;
    }
    
    //获取具体物体的变换矩阵
    public static float[] getMatrix()
    {       
        return mCurMatrix;
    }
    
    //设置灯光位置的方法

    public static void setLightLocation(float x,float y,float z)
    {
   	
    	lightLocation[0]=x;
    	lightLocation[1]=y;
    	lightLocation[2]=z;
        lightFB = BufferHelper.create(lightLocation);

    }
    //设置灯光方向的方法
    public static void setLightDirection(float x,float y,float z)
    {
   	
    	lightDirection[0]=x;
    	lightDirection[1]=y;
    	lightDirection[2]=z;
    	lightFB = BufferHelper.create(lightDirection);
    }

    public static FloatBuffer getLightFB() {
		return lightFB;
	}


    public static FloatBuffer getCameraFB() {
		return cameraFB;
	}
    
    
}
