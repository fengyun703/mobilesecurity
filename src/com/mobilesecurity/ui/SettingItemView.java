package com.mobilesecurity.ui;

import com.mobilesecurity.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SettingItemView extends LinearLayout {
	
	private TextView tv;
	private CheckBox cb;

	public SettingItemView(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
		String text = attrs.getAttributeValue("http://schemas.android.com/apk/res/com.mobilesecurity", "mytext");
		tv.setText(text);
	}

	public SettingItemView(Context context) {
		super(context);
		initView(context);
	}

	private void initView(Context context) {
		this.setOrientation(LinearLayout.VERTICAL);
		View view = View.inflate(context, R.layout.item_setting, null);
		tv = (TextView) view.findViewById(R.id.tv_setting_item);
		cb= (CheckBox) view.findViewById(R.id.cb_setting_item);
		this.addView(view);

	}
	
	public  boolean isChecked(){
		return cb.isChecked();
	}
	
	public void setChecked(boolean checked){
		//System.out.println(checked+"    "+",  cb ="+cb.toString()+",  SettingItemView = "+this.toString());
		cb.setChecked(checked);
	}

}
