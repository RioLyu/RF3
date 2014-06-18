package com.rio.helper;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;

public class SubHandler extends Handler {

	public SubHandler() {

		super();

	}

	public SubHandler(Looper looper) {
		super(looper);
	}

	public static SubHandler newInstance(){
		//生成一个HandlerThread对象，实现了使用Looper来处理消息队列的功能，这个类由Android应用程序框架提供
		HandlerThread handlerThread = new HandlerThread("Sub_Thread");
		//在使用HandlerThread的getLooper()方法之前，必须先调用该类的start();
		handlerThread.start();
		return new SubHandler(handlerThread.getLooper());
	}
}
