package com.rio.helper.gl2;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;

public class BufferHelper {

	
	public static FloatBuffer create(float[] vertices){
		ByteBuffer vbb = ByteBuffer.allocateDirect(vertices.length * 4);
		vbb.order(ByteOrder.nativeOrder());// 设置字节顺序
		FloatBuffer buffer = vbb.asFloatBuffer();// 转换为Float型缓冲
		buffer.put(vertices);// 向缓冲区中放入顶点坐标数据
		buffer.position(0);// 设置缓冲区起始位置
		return buffer;
		//特别提示：由于不同平台字节顺序不同数据单元不是字节的一定要经过ByteBuffer
        //转换，关键是要通过ByteOrder设置nativeOrder()，否则有可能会出问题
	}
}
