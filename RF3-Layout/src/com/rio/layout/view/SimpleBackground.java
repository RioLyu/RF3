package com.rio.layout.view;


import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;

import com.rio.layout.IBackground;
import com.rio.layout.ILayout;

/**
 * 
 * 
 * @author rio
 *
 */
public abstract class SimpleBackground implements IBackground {


	@Override
	public void onDetach() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onDisplayChild(String childName, int index) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onKeyBackHome() {
		return false;

	}

	@Override
	public void onKeyMenu() {
		// TODO Auto-generated method stub

	}
	


	@Override
	public boolean onResume() {
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean onPause() {
		// TODO Auto-generated method stub
		return false;
	}

}
