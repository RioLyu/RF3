package com.rio.layout.view;


import android.content.Intent;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;

import com.rio.layout.IBackground;
import com.rio.layout.ILayout;

public abstract class SimpleLayout implements ILayout {

	@Override
	public void onDisplay(String callerName, View view, int frag,
			Object... params) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}


	@Override
	public void onResume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void onHide(String nextName) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void onDetach() {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public boolean onGoBack() {
		// TODO Auto-generated method stub
		return false;
	}


	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public Animation getAnimatAddIn() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Animation getAnimatAddOut() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Animation getAnimatDelIn() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Animation getAnimatDelOut() {
		// TODO Auto-generated method stub
		return null;
	}
}
