package com.mobilesecurity.activities;

import android.app.Activity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.mobilesecurity.R;

public abstract class BaseSafeAbsActivity extends Activity {
	
	private GestureDetector dg;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		dg = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener(){

			@Override
			public boolean onFling(MotionEvent e1, MotionEvent e2,
				float velocityX, float velocityY) {
				if(Math.abs(e1.getRawY() - e2.getRawY()) > 100){
					//System.out.println("竖直方向运动");
					return true;
				}
				
				if(e1.getRawX() - e2.getRawX() > 70){
					//System.out.println(" next");
					showNext();
					overridePendingTransition(R.anim.activity_next_in, R.anim.activity_next_out);
					return true;
				}else if (e2.getRawX() - e1.getRawX() > 70){
					//System.out.println("pre ");
					showPre();
					overridePendingTransition(R.anim.activity_pre_in, R.anim.activity_pre_out);
					return true;
				}
				
				return super.onFling(e1, e2, velocityX, velocityY);
			}
			 
		} );
	}
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		dg.onTouchEvent(event);
		return super.onTouchEvent(event);
	}
	

	
	public abstract void showNext();
	public abstract void showPre();
	public void next(View view){
		showNext();
		overridePendingTransition(R.anim.activity_next_in, R.anim.activity_next_out);
	}
	
	public void pre(View view){
		showPre();
		overridePendingTransition(R.anim.activity_pre_in, R.anim.activity_pre_out);
	}
	

}
