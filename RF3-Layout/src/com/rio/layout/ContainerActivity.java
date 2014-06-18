package com.rio.layout;

import com.rio.core.L;
import com.rio.core.U;

import android.app.Activity;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.content.res.Resources.Theme;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ViewFlipper;

/**
 * 容器类，装载所有图层
 * 
 * @author rio
 *
 */
public abstract class ContainerActivity extends Activity {
	
	


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		FrameLayout mMainFrame = new FrameLayout(this);
		LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		ViewFlipper mViewFlipper = new ViewFlipper(this);
		mMainFrame.addView(mViewFlipper,0,params);
		LayoutInflater inflater = LayoutInflater.from(this);
		PopupManager.getInstance().setFrame(this,mMainFrame,inflater);
		PlusManager.getInstance(this).init(super.getResources(), super.getTheme());
		LayoutManager.getInstance().setRootChild(onAttach());
		LayoutManager.getInstance().init(this,inflater,mMainFrame,mViewFlipper);	
		setContentView(mMainFrame,params);	
	}
	
	
	public abstract IBackground onAttach();
		

	/**
	 * 设置换屏动画
	 * 
	 * @param addin
	 * @param addout
	 * @param delin
	 * @param delout
	 */
	protected void setFlipperAnimation(int addin, int addout, int delin,
			int delout) {		
		Animation mAnimatAddIn = AnimationUtils.loadAnimation(this, addin);
		Animation mAnimatAddOut = AnimationUtils.loadAnimation(this, addout);
		Animation mAnimatDelIn = AnimationUtils.loadAnimation(this, delin);
		Animation mAnimatDelOut = AnimationUtils.loadAnimation(this, delout);
		LayoutManager.getInstance().setAnimation(mAnimatAddIn, mAnimatAddOut, mAnimatDelIn, mAnimatDelOut);
		
	}
	
	/**
	 * 设置全局的Toast样式
	 * 
	 * @param layout
	 */
	protected void setToastConfig(int layout) {
		PopupManager.getInstance().setToastConfig(layout);

	}



	
	
	/**
	 * 设置全局的Progress样式
	 * 
	 * @param gravity
	 * @param x
	 * @param y
	 * @param animation
	 */
	protected void setProgressConfig(int layout) {
		PopupManager.getInstance().setProgressConfig(layout);
	}

	
	@Override
	protected void onResume() {		
		super.onResume();
		if(!PopupManager.getInstance().onResume()){
			LayoutManager.getInstance().onResume();
		}
		
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		if(!PopupManager.getInstance().onPause()){
			LayoutManager.getInstance().onPause();
		}
	}
	@Override
	protected void onDestroy() {		
		destroyAllManager();		
		super.onDestroy();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return LayoutManager.getInstance().onKeyDown(keyCode, event);
	}
	
	/**
	 * 将四大管理器清空
	 */
	private void destroyAllManager() {
		PlusManager.getInstance(this).destroy();
		TaskManager.getInstance().destroy();
		PopupManager.getInstance().destroy();		
		FragManager.getInstance().destroy();
		LayoutManager.getInstance().destroy();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(!PopupManager.getInstance().onActivityResult(requestCode, resultCode, data)){
			LayoutManager.getInstance().onActivityResult( requestCode,  resultCode,  data);
		}
		
	}
	
    @Override
    public AssetManager getAssets() {
        return PlusManager.getInstance(this).isActive() ? 
        		PlusManager.getInstance(this).getAssetManager():super.getAssets();
    }

    @Override
    public Resources getResources() {
        return PlusManager.getInstance(this).isActive()? 
        		PlusManager.getInstance(this).getResources():super.getResources();
    }

    @Override
    public Theme getTheme() {
        return PlusManager.getInstance(this).isActive()? 
        		PlusManager.getInstance(this).getTheme():super.getTheme();
    }

	
}
