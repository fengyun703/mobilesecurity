package com.mobilesecurity.ui;

import com.mobilesecurity.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SettingChangeView extends LinearLayout {
	private TextView tv_result;

	public SettingChangeView(Context context) {
		super(context);
		init(context);
	}

	public SettingChangeView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public SettingChangeView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
		//String ;
	}
	
	private void init(Context context){
		View view = View.inflate(context, R.layout.item_change, this);
		tv_result = (TextView) view.findViewById(R.id.tv_result_change);
		
	}
	
	public void setResult(String result){
		tv_result.setText(result);
	}

}
